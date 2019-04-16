package com.http.study

import java.io.{FileInputStream, IOException}
import java.security.KeyStore

import javax.net.ssl.SSLContext
import java.{io, util}

import org.apache.http.NameValuePair
import org.apache.http.client.entity.UrlEncodedFormEntity
import org.apache.http.client.methods.HttpPost
import org.apache.http.config.{Registry, RegistryBuilder}
import org.apache.http.conn.socket.{ConnectionSocketFactory, PlainConnectionSocketFactory}
import org.apache.http.conn.ssl.{SSLConnectionSocketFactory, TrustSelfSignedStrategy}
import org.apache.http.impl.client.HttpClients
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager
import org.apache.http.message.BasicNameValuePair
import org.apache.http.ssl.SSLContexts
import org.apache.http.util.EntityUtils

/**
  * @author: shudj
  * @time: 2019/4/16 17:02
  * @description:
  */
class HttpClient_SSL_KEY {

    def custom(keyStorePath: String, keyStorepass: String): SSLContext = {
        var sc: SSLContext = null
        var instream: FileInputStream = null
        var trustStore: KeyStore = null

        try {

            trustStore = KeyStore.getInstance(KeyStore.getDefaultType)
            instream = new FileInputStream(new io.File(keyStorePath))
            trustStore.load(instream, keyStorepass.toCharArray)

            // 相信自己的CA和所有自签名的证书
            sc = SSLContexts.custom().loadTrustMaterial(trustStore, new TrustSelfSignedStrategy).build()
        } catch {
            case e: Exception => e.printStackTrace()
        } finally {

            try {

                if (null != instream) {
                    instream.close()
                }
            } catch {
                case e: IOException => e.printStackTrace()
            }
        }

        sc
    }

    def send(url: String, map: Map[String, String], encoding: String): String = {
        var body: String = ""

        // tomcat是我自己的秘钥库的密码，你可以替换成自己的
        // 如果密码为空，则用“"nopassword"代替
        val sslContext = custom("","nopassword")

        // 设置协议http和https对应处理socket链接工厂的对象
        val socketFactoryFactory: Registry[ConnectionSocketFactory] = RegistryBuilder.create[ConnectionSocketFactory]()
            .register("http", PlainConnectionSocketFactory.INSTANCE)
            .register("https", new SSLConnectionSocketFactory(sslContext))
            .build()

        val connectionManager = new PoolingHttpClientConnectionManager(socketFactoryFactory)
        HttpClients.custom().setConnectionManager(connectionManager)
        // 创建自定义的httpClient对象
        val client = HttpClients.custom().setConnectionManager(connectionManager).build()
        //val client = HttpClients.createDefault()

        // 创建post方式请求对象
        val httpPost = new HttpPost(url)

        // 装填参数
        val nvps = new util.ArrayList[NameValuePair]()
        if (null != map) {
            for ((k, v) <- map) {
                nvps.add(new BasicNameValuePair(k, v))
            }
        }

        // 设置参数到请求对象中
        httpPost.setEntity(new UrlEncodedFormEntity(nvps, encoding))
        println("请求地址：" + url)
        println("请求参数：" + nvps.toString)

        // 设置header信息
        // 指定报文头[Content-type]、[User-Agent]
        httpPost.setHeader("Content-type", "application/x-www-form-urlencoded")
        httpPost.setHeader("User-Agent", "Mozilla/4.0(compatible; MSIE 5.0; Windows NT; DigExt)")

        // 执行请求操作，并拿到结果(同步阻塞)
        val response = client.execute(httpPost)
        // 获取结果实体
        val entity = response.getEntity

        if (null != entity) {
            // 按指定编码转换结果实体为String类型
            body = EntityUtils.toString(entity, encoding)
        }

        EntityUtils.consume(entity)

        // 释放连接
        response.close()
        body
    }
}
