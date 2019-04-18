/*
package com.http.packhttpclient

import java.io.{IOException, UnsupportedEncodingException}
import java.util

import com.http.exception.HttpProcessException
import com.http.util.HttpMethods.HttpMethods
import com.http.util.Utils
import org.apache.http.client.HttpClient
import org.apache.http.client.entity.UrlEncodedFormEntity
import org.apache.http.client.methods._
import org.apache.http.util.EntityUtils
import org.apache.http.{Header, HttpResponse, NameValuePair}
import org.slf4j.{Logger, LoggerFactory}

import scala.collection.mutable.Map
import scala.reflect.macros.ParseException

/**
  * @author: shudj
  * @time: 2019/4/16 17:29
  * @description:
  */
object HttpClient1 {

    private val logger: Logger = LoggerFactory.getLogger(HttpClient1.getClass)

    import com.http.util.HttpMethods
    import java.nio.charset.Charset

    @throws[HttpProcessException]
    def send(url: String): String = send(url, Charset.defaultCharset.name)

    @throws[HttpProcessException]
    def send(url: String, encoding: String): String = send(url, Array[Header](), encoding)

    @throws[HttpProcessException]
    def send(url: String, headers: Array[Header]): String = send(url, headers, Charset.defaultCharset.name)

    @throws[HttpProcessException]
    def send(url: String, headers: Array[Header], encoding: String): String = send(url, Map[String, String](), headers, encoding)

    @throws[HttpProcessException]
    def send(url: String, parasMap: Map[String, String]): String = send(url, parasMap, Charset.defaultCharset.name)

    @throws[HttpProcessException]
    def send(url: String, parasMap: Map[String, String], encoding: String): String = send(url, parasMap, Array[Header](), encoding)

    @throws[HttpProcessException]
    def send(url: String, parasMap: Map[String, String], headers: Array[Header]): String = send(url, parasMap, headers, Charset.defaultCharset.name)

    @throws[HttpProcessException]
    def send(url: String, parasMap: Map[String, String], headers: Array[Header], encoding: String): String = send(url, HttpMethods.POST, parasMap, headers, encoding)

    @throws[HttpProcessException]
    def send(url: String, httpMethod: HttpMethods): String = send(url, httpMethod, Charset.defaultCharset.name)

    @throws[HttpProcessException]
    def send(url: String, httpMethod: HttpMethods, encoding: String): String = send(url, httpMethod, Array[Header](), encoding)

    @throws[HttpProcessException]
    def send(url: String, httpMethod: HttpMethods, headers: Array[Header]): String = send(url, httpMethod, headers, Charset.defaultCharset.name)

    @throws[HttpProcessException]
    def send(url: String, httpMethod: HttpMethods, headers: Array[Header], encoding: String): String = send(url, httpMethod, Map[String, String](), headers, encoding)

    @throws[HttpProcessException]
    def send(url: String, httpMethod: HttpMethods, parasMap: Map[String, String]): String = send(url, httpMethod, parasMap, Charset.defaultCharset.name)

    @throws[HttpProcessException]
    def send(url: String, httpMethod: HttpMethods, parasMap: Map[String, String], encoding: String): String = send(url, httpMethod, parasMap, Array[Header](), encoding)

    @throws[HttpProcessException]
    def send(url: String, httpMethod: HttpMethods, parasMap: Map[String, String], headers: Array[Header]): String = send(url, httpMethod, parasMap, headers, Charset.defaultCharset.name)

    @throws[HttpProcessException]
    def send(url: String, httpMethod: HttpMethods, parasMap: Map[String, String], headers: Array[Header], encoding: String): String = send(create(url), url, httpMethod, parasMap, headers, encoding)

    @throws[HttpProcessException]
    def send(client: HttpClient, url: String): String = send(client, url, Charset.defaultCharset.name)

    @throws[HttpProcessException]
    def send(client: HttpClient, url: String, encoding: String): String = send(client, url, Array[Header](), encoding)

    @throws[HttpProcessException]
    def send(client: HttpClient, url: String, headers: Array[Header]): String = send(client, url, headers, Charset.defaultCharset.name)

    @throws[HttpProcessException]
    def send(client: HttpClient, url: String, headers: Array[Header], encoding: String): String = send(client, url, Map[String, String](), headers, encoding)

    @throws[HttpProcessException]
    def send(client: HttpClient, url: String, parasMap: Map[String, String]): String = send(client, url, parasMap, Charset.defaultCharset.name)

    @throws[HttpProcessException]
    def send(client: HttpClient, url: String, parasMap: Map[String, String], encoding: String): String = send(client, url, parasMap, Array[Header](), encoding)

    @throws[HttpProcessException]
    def send(client: HttpClient, url: String, parasMap: Map[String, String], headers: Array[Header]): String = send(client, url, parasMap, headers, Charset.defaultCharset.name)

    @throws[HttpProcessException]
    def send(client: HttpClient, url: String, parasMap: Map[String, String], headers: Array[Header], encoding: String): String = send(client, url, HttpMethods.POST, parasMap, headers, encoding)

    @throws[HttpProcessException]
    def send(client: HttpClient, url: String, httpMethod: HttpMethods): String = send(client, url, httpMethod, Charset.defaultCharset.name)

    @throws[HttpProcessException]
    def send(client: HttpClient, url: String, httpMethod: HttpMethods, encoding: String): String = send(client, url, httpMethod, Array[Header](), encoding)

    @throws[HttpProcessException]
    def send(client: HttpClient, url: String, httpMethod: HttpMethods, headers: Array[Header]): String = send(client, url, httpMethod, headers, Charset.defaultCharset.name)

    @throws[HttpProcessException]
    def send(client: HttpClient, url: String, httpMethod: HttpMethods, headers: Array[Header], encoding: String): String = send(client, url, httpMethod, Map[String, String](), headers, encoding)

    @throws[HttpProcessException]
    def send(client: HttpClient, url: String, httpMethod: HttpMethods, parasMap: Map[String, String]): String = send(client, url, httpMethod, parasMap, Charset.defaultCharset.name)

    @throws[HttpProcessException]
    def send(client: HttpClient, url: String, httpMethod: HttpMethods, parasMap: Map[String, String], encoding: String): String = send(client, url, httpMethod, parasMap, Array[Header](), encoding)

    @throws[HttpProcessException]
    def send(client: HttpClient, url: String, httpMethod: HttpMethods, parasMap: Map[String, String], headers: Array[Header]): String = send(client, url, httpMethod, parasMap, headers, Charset.defaultCharset.name)

    /**
      * 请求资源或服务，自定义client对象，传入请求参数，设置内容类型，并指定参数和返回数据的编码
 *
      * @param client       client对象
      * @param url          资源地址
      * @param httpMethod   请求方法
      * @param parasMap     请求参数
      * @param headers      请求头信息
      * @param encoding     编码
      * @return              返回处理结果
      *                      
      */
    @throws[HttpProcessException]
    def send(client: HttpClient,
             url: String,
             httpMethod: HttpMethods,
             parasMap: Map[String, String],
             headers: Array[Header],
             encoding: String
            ): String = {
        var body = ""
        var tempUrl = url

        try {
            // 创建请求对象
            val request = getRequest(url, httpMethod)
            // 设置header信息
            request.setHeaders(headers)

            // 判断是否支持设置entity(仅HttpPost、HttpPut、HttpPatch支持)
            if (classOf[HttpEntityEnclosingRequestBase].isAssignableFrom(request.getClass)) {

                val nvps = new util.ArrayList[NameValuePair]()
                // 检测url中是否存在参数
                tempUrl = Utils.checkHasParas(url, nvps)

                // 装填参数
                Utils.map2List(nvps, parasMap)

                // 设置参数到请求对象中
                request.asInstanceOf[HttpEntityEnclosingRequestBase].setEntity(new UrlEncodedFormEntity(nvps, encoding))

                logger.debug("请求地址：" + url)

                if (!nvps.isEmpty) {
                    logger.debug("请求参数：" + nvps.toString)
                }
            } else {
                val i = url.indexOf("?")
                logger.debug("请求地址：" + url.substring(0, (if (i > 0) i;else url.length-1)) )

                if (i > 0) {
                    logger.debug("请求参数：" + url.substring(i + 1))
                }
            }

            // 调用发送请求
            body = excute(client, request, tempUrl, encoding)
        } catch {
            case e: UnsupportedEncodingException => throw new HttpProcessException(e)
        }

        body
    }


    /**
      * 根据方法名，获取request对象
      * @param url          资源地址
      * @param httpMethod   请求方式
      * @return
      */
    @throws[HttpProcessException]
    def getRequest(url: String, httpMethod: HttpMethods): HttpRequestBase = {

        httpMethod.id match {
            case 0 => new HttpGet(url)
            case 1 => new HttpPost(url)
            case 2 => new HttpHead(url)
            case 3 => new HttpPut(url)
            case 4 => new HttpDelete(url)
            case 5 => new HttpTrace(url)
            case 6 => new HttpPatch(url)
            case 7 => new HttpOptions(url)
            case _ => new HttpPost(url)
        }

    }

    def close(response: HttpResponse): Unit = {
        try {
            if (null == response) return
            if (classOf[CloseableHttpResponse].isAssignableFrom(response.getClass))
                response.asInstanceOf[CloseableHttpResponse].close()
        } catch {
            case e: Exception => throw new HttpProcessException(e)
        }
    }

    /**
      * 请求资源或服务
      *
      * @param client       client对象
      * @param request      请求对象
      * @param tempUrl      资源地址
      * @param encoding     编码
      * @return             返回结果
      */
    @throws[HttpProcessException]
    def excute(client: HttpClient, request: HttpRequestBase, tempUrl: String, encoding: String): String = {

        var body = ""
        var response: HttpResponse = null

        try {

            // 执行请求操作，并拿到结果(同步阻塞)
            response = client.execute(request)
            // 获取结果实体
            val entity = response.getEntity

            if(null != entity) {
                // 按指定编码转换结果实体为String类型
                body = EntityUtils.toString(entity, encoding)
                logger.debug(body)
            }

            EntityUtils.consume(entity)
        } catch {
            case e: ParseException => throw new HttpProcessException(e)
            case e: IOException => throw new HttpProcessException(e)
        } finally {
            close(response)
        }

        body
    }

}
*/
