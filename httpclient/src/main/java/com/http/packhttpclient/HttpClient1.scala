package com.http.packhttpclient

import java.util

import com.http.util.HttpMethods.HttpMethods
import org.apache.http.client.HttpClient
import org.apache.http.client.methods.{HttpEntityEnclosingRequestBase, HttpRequestBase}
import org.apache.http.{Header, NameValuePair}

/**
  * @author: shudj
  * @time: 2019/4/16 17:29
  * @description:
  */
object HttpClient1 {

    def getRequest(url: String, httpMethod: HttpMethods): HttpRequestBase = ???

    def send(client: HttpClient,
             url: String,
             httpMethod: HttpMethods,
             parasMap: Map[String, String],
             headers: Array[Header],
             encoding: String
            ): String = {
        var body = ""

        try {
            // 创建请求对象
            val request = getRequest(url, httpMethod)
            // 设置header信息
            request.setHeaders(headers)

            // 判断是否支持设置entity(仅HttpPost、HttpPut、HttpPatch支持)
            if (classOf[HttpEntityEnclosingRequestBase].isAssignableFrom(request.getClass)) {

                val nvps = new util.ArrayList[NameValuePair]()
                // 检测url中是否存在参数
                //url =
            }
        }

        body
    }

}
