package com.http.study

import java.security.cert.X509Certificate
import java.util

import javax.net.ssl.{SSLContext, TrustManager, X509TrustManager}
import org.apache.http.NameValuePair
import org.apache.http.client.entity.UrlEncodedFormEntity
import org.apache.http.client.methods.HttpPost
import org.apache.http.config.{Registry, RegistryBuilder}
import org.apache.http.conn.socket.{ConnectionSocketFactory, PlainConnectionSocketFactory}
import org.apache.http.conn.ssl.SSLConnectionSocketFactory
import org.apache.http.impl.client.HttpClients
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager
import org.apache.http.message.BasicNameValuePair
import org.apache.http.util.EntityUtils

/**
  * @author: shudj
  * @time: 2019/4/16 16:22
  * @description:
  */
class HttpClient_SSL {

    /**
      * 绕过验证
      * @return
      */
    def createTgnoreVerifySSL(): SSLContext = {
        val sc = SSLContext.getInstance("SSLv3")

        // 实现一个X509TrustManager接口，用于绕过验证，不用修改里面的方法
        val trustManager = new X509TrustManager {
            override def checkClientTrusted(x509Certificates: Array[X509Certificate], paramString: String): Unit = {

            }

            override def checkServerTrusted(x509Certificates: Array[X509Certificate], paramString: String): Unit = {

            }

            override def getAcceptedIssuers: Array[X509Certificate] = {

                null
            }
        }

        sc.init(null, Array[TrustManager]{trustManager}, null)

        sc
    }

    def send(url: String, map: Map[String, String], encoding: String): String = {
        var body: String = ""

        // 采用绕过验证的方式处理https请求
        val sslContext = createTgnoreVerifySSL()

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

object HttpClient_SSL {
    def main(args: Array[String]): Unit = {

        val url = "https://kyfw.12306.cn/otn/";
        var body = new HttpClient_SSL().send(url, null, "utf-8");
        println("交易响应结果：");
        println(body);
        System.out.println("-----------------------------------");
    }
}
