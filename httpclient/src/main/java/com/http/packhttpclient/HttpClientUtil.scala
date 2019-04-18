package com.http.packhttpclient

import java.io.IOException
import java.util

import com.http.builder.HCB
import com.http.common.HttpConfig
import com.http.exception.HttpProcessException
import com.http.util.{HttpMethods, Utils}
import org.apache.http.client.HttpClient
import org.apache.http.client.methods._
import org.apache.http.protocol.HttpContext
import org.apache.http.util.EntityUtils
import org.apache.http.{Header, HttpResponse, NameValuePair}

import scala.collection.mutable.Map
/**
  * @author: shudj
  * @time: 2019/4/18 11:19
  * @description:   使用HttpClient模拟发送(http/https)请求
  */
class HttpClientUtil {


}

object HttpClientUtil {
    // 默认采用的http协议的HttpClient对象
    var client4HTTP: HttpClient = _
    // 默认采用的https协议的HttpClient对象
    var client4HTTPS: HttpClient = _

    {
        try {
            client4HTTP = HCB.custom().build()
            client4HTTPS = HCB.custom().ssl().build()
        } catch {
            case e: HttpProcessException => e.printStackTrace()
        }
    }

    /**
      * 判断是否开启连接池、及url是http还是https
      *     如果已开启连接池，则自动调用build方法，从连接池中获取client对象
      *     否则，直接返回相应的默认client对象
      * @param config   请求参数配置
      */
    @throws[HttpProcessException]
    def create(config: HttpConfig): Unit = {
        // 如果未空，设为默认client对象
        if (null != config.client) {
            if (config.url().toLowerCase.startsWith("https://")) {
                config.client(client4HTTPS)
            } else {
                config.client(client4HTTP)
            }
        }
    }

    /**
      * 以Get方式，请求资源或服务
      * @param client       client对象
      * @param url          资源地址
      * @param headers      请求头信息
      * @param context      http上下文，用于cookie操作
      * @param encodind     编码
      * @throws
      * @return
      */
    @throws[HttpProcessException]
    def get(client: HttpClient, url: String, headers: Array[Header], context: HttpContext, encoding: String): String = get(HttpConfig.custom().client(client).url(url).headers(headers).context(context).encoding(encoding))

    /**
      * 以get方式，请求资源或服务
      * @param config   请求参数配置
      * @throws
      * @return
      */
    @throws[HttpProcessException]
    def get(config: HttpConfig): String = send(config.method(HttpMethods.GET))

    @throws[HttpProcessException]
    def post(client: HttpClient, url: String, headers: Array[Header], parasMap: Map[String, Any],context: HttpContext, encoding: String): String
        = post(HttpConfig.custom().client(client).url(url).headers(headers).map(parasMap).context(context).encoding(encoding))

    @throws[HttpProcessException]
    def post(config: HttpConfig): String = send(config)
    /**
      *
      * @param config
      * @throws
      * @return
      */
    @throws[HttpProcessException]
    def send(config: HttpConfig): String = fmt2String(execute(config), if(null == config.outenc) config.encoding; else config.outenc)

    /**
      * 请求资源或服务
      *
      * @param config   请求配置参数
      * @throws
      * @return
      */
    @throws[HttpProcessException]
    private[this] def execute(config: HttpConfig): HttpResponse = {
        create(config)
        var resp: HttpResponse = null

        try {
            // 创建请求对象
            val requestBase = getRequest(config.url(), config.method)
            // 设置超时
            requestBase.setConfig(config.requestConfig)
            // 设置header信息
            requestBase.setHeaders(config.headers)
            // 判断是否只支持设置entity(仅HTTPPOSt, HTTPPUT, HTTPPATCH支持)
            if (classOf[HttpEntityEnclosingRequestBase].isAssignableFrom(requestBase.getClass)) {
                val nvps: java.util.List[NameValuePair] = new util.ArrayList[NameValuePair]()
                // 检测url中是否存在参数
                // 注：只有get请求，才自动截取url中的参数，post等其他方式不再截取
                if (requestBase.getClass == classOf[HttpGet]) config.url(Utils.checkHasParas(config.url(), nvps, if (config.inenc == null) config.encoding;else config.inenc))

                // 装填参数
                val entity = Utils.map2httpEntity(nvps, config.map(), if (config.inenc == null) config.encoding;else config.inenc)

                // 这是参数到请求对象中
                requestBase.asInstanceOf[HttpEntityEnclosingRequestBase].setEntity(entity)

                if (nvps.size() > 0) println("请求参数：" + config.json)
                if (config.json != null) println("请求参数：" + config.json)
            } else {
                val idx: Int = config.url().indexOf("?")
                println("请求地址：" + config.url().substring(0, if (idx > 0) idx;else config.url().length))
                if (idx > 0) println("请求参数：" + config.url().substring(idx + 1))
            }

            // 执行请求操作，并拿到结果（同步阻塞）
            resp = if (null == config.context) {
                config.client.execute(requestBase)
            }else {
                config.client.execute(requestBase, config.context)
            }
            // 获取结果实体
            if (config.isReturnResponseHeaders) config.headers(resp.getAllHeaders)

            resp
        } catch {
            case e: IOException => throw new HttpProcessException(e)
        }

    }

    /**
      * 根据请求方法名，获取reques对象
      * @param url          资源地址
      * @param methods      请求方式
      * @return
      */
    private[this] def getRequest(url: String, methods: HttpMethods.HttpMethods): HttpRequestBase = {
        val request: HttpRequestBase = methods.id match {
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

        request
    }
    /**
      * 转化为字符串
      * @param resp         响应对象
      * @param encoding     编码
      * @throws
      * @return
      */
    @throws[HttpProcessException]
    private[this] def fmt2String(resp: HttpResponse, encoding: String): String = {
        var body: String = ""
        try {
            if (null != resp.getEntity) {
                // 按指定编码转换结果实体为String类型
                body = EntityUtils.toString(resp.getEntity, encoding)
            } else {
                // 有可能是head请求
                body = resp.getStatusLine.toString
            }

            EntityUtils.consume(resp.getEntity)
        } catch {
            case e: IOException => throw new HttpProcessException(e)
        } finally {
            close(resp)
        }
        body
    }

    /**
      * 尝试关闭response
      * @param response
      */
    private[this] def close(response: HttpResponse): Unit = {
        try {
            if (response == null) return
            if (classOf[CloseableHttpResponse].isAssignableFrom(response.getClass)) response.asInstanceOf[CloseableHttpResponse].close()
        } catch {
            case e: IOException => e.printStackTrace()
        }
    }
}
