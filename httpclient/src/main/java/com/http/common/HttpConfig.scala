package com.http.common

import java.io.OutputStream
import java.nio.charset.Charset

import com.http.util.HttpMethods.HttpMethods
import com.http.util.{HttpMethods, Utils}
import org.apache.http.Header
import org.apache.http.client.HttpClient
import org.apache.http.client.config.RequestConfig
import org.apache.http.protocol.HttpContext

import scala.collection.mutable.Map

/**
  * @author: shudj
  * @time: 2019/4/17 16:31
  * @description: 请求配置类
  */
class HttpConfig {

    /**
      * HttpClient对象
      */
    var client: HttpClient = _

    /**
      * Header头信息
      */
    var headers: Array[Header] = _

    /**
      * 是否返回response的请求头
      */
    var isReturnResponseHeaders: Boolean = _

    /**
      * 请求方法名称
      */
    var method: HttpMethods = HttpMethods.GET

    /**
      * 请求方法名称
      */
    var methodName: String = _

    /**
      * 用于cookie操作
      */
    var context: HttpContext = _

    /**
      * 以json格式作为输入参数
      */
    var json: String = _

    /**
      * 输入输出编码
      */
    var encoding: String = Charset.defaultCharset().displayName()

    /**
      * 输入编码
      */
    var inenc: String = _

    /**
      * 输出编码
      */
    var outenc: String = _

    /**
      * 设置RequestConfig
      */
    var requestConfig: RequestConfig = _

    /**
      *
      * @param client    HttpClient对象
      * @return
      */
    def client(client: HttpClient): HttpConfig = {
        this.client = client
        this
    }

    def url(url: String): HttpConfig = {
        HttpConfig.urls.set(url)
        this
    }

    def headers(headers: Array[Header]): HttpConfig = {
        this.headers = headers
        this
    }

    def headers(headers: Array[Header], isReturnRespHeaders: Boolean): HttpConfig = {
        this.headers = headers
        this.isReturnResponseHeaders = isReturnRespHeaders
        this
    }

    def method(method: HttpMethods): HttpConfig = {
        this.method = method
        this
    }

    def methodName(methodName: String): HttpConfig = {
        this.methodName = methodName
        this
    }

    def context(context: HttpContext): HttpConfig = {
        this.context = context
        this
    }

    def map(map1: Map[String, Any]): HttpConfig = {
        var m = HttpConfig.maps.get()
        if (null == m || null == map1) {
            m = map1
        } else {
            m = m ++ map1
        }
        HttpConfig.maps.set(m)

        this
    }

    def json(json: String): HttpConfig = {
        this.json = json
        var map = Map[String, Any]()
        map += (Utils.ENTITY_JSON -> json)
        HttpConfig.maps.set(map)
        this
    }

    def files(filePaths: Array[String]): HttpConfig = {
        files(filePaths, "file")
    }

    def files(filePaths: Array[String], inputName: String): HttpConfig = {
        files(filePaths, inputName, false)
    }

    def files(filePaths: Array[String], inputName: String, forceRemoveContentTypeChraset: Boolean): HttpConfig = {

        var m = HttpConfig.maps.get()
        if (null == m) {
            m = Map[String, Any]()
        }

        m += (Utils.ENTITY_MULTIPART -> filePaths)
        m += (Utils.ENTITY_MULTIPART + ".name" -> inputName)
        m += (Utils.ENTITY_MULTIPART + ".rmCharset" -> forceRemoveContentTypeChraset)

        HttpConfig.maps.set(m)

        this
    }

    def encoding(encoding: String): HttpConfig = {
        inenc(encoding)
        outenc(encoding)
        this.encoding = encoding
        this
    }

    def inenc(inenc: String): HttpConfig = {
        this.inenc = inenc
        this
    }

    def outenc(outenc: String): HttpConfig = {
        this.outenc = outenc
        this
    }

    def out(out: OutputStream): HttpConfig = {
        HttpConfig.outs.set(out)
        this
    }

    def timeout(time: Int): HttpConfig = {
        timeout(time, true)
    }

    def timeout(time: Int, redirectEnable: Boolean): HttpConfig = {
        val config = RequestConfig.custom()
            .setConnectionRequestTimeout(time)
            .setConnectTimeout(time)
            .setSocketTimeout(time)
            .setRedirectsEnabled(redirectEnable)
            .build()

        timeout(config)
    }

    def timeout(config: RequestConfig): HttpConfig = {
        this.requestConfig = config
        this
    }

    def url(): String = HttpConfig.urls.get()
    def map(): Map[String, Any] = HttpConfig.maps.get()
    def out(): OutputStream = HttpConfig.outs.get()
}

object HttpConfig {
    def custom(): HttpConfig = new HttpConfig

    /**
      * 解决多线程下载时，stream被close的问题
      */
    private val outs = new ThreadLocal[OutputStream]()

    /**
      * 解决多线程处理时，url被覆盖问题
      */
    private val urls = new ThreadLocal[String]()

    /**
      * 解决多线程处理时，url被覆盖问题
      */
    private val maps = new ThreadLocal[Map[String, Any]]()
}