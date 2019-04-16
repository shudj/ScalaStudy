package com.http.study

import java.util

import org.apache.http.NameValuePair
import org.apache.http.client.entity.UrlEncodedFormEntity
import org.apache.http.client.methods.HttpPost
import org.apache.http.impl.client.HttpClients
import org.apache.http.message.BasicNameValuePair
import org.apache.http.util.EntityUtils

import scala.collection.mutable.Map

/**
  * @author: shudj
  * @time: 2019/4/16 15:39
  * @description:
  */
class HttpClientDemo1 {

    def send(url: String, map: Map[String, String], encoding: String): String = {
        var body: String = ""

        // 创建httpClient对象
        val client = HttpClients.createDefault()
        // 创建post方式请求对象
        val httpPost = new HttpPost(url)

        // 装填参数
        val nvps = new  util.ArrayList[NameValuePair]()
        if (null != map) {
            for((k, v) <- map) {
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

object HttpClientDemo1 {
    def main(args: Array[String]): Unit = {
        val url = "http://php.weather.sina.com.cn/iframe/index/w_cl.php"
        val map = Map[String, String]()
        map += ("code" -> "js")
        map += ("day" -> "0")
        map += ("dfc" -> "1")
        map += ("city" -> "上海")
        map += ("charset" -> "utf-8")

        var body = new HttpClientDemo1().send(url, map, "utf-8")
        println("交易响应结果：")
        println(body)
        println("----------------------------")

        map += ("city" -> "北京")
        body = new HttpClientDemo1().send(url, map, "utf-8")
        println("交易响应结果：")
        println(body)
    }
}
