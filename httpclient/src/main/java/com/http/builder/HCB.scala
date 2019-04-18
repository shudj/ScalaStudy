package com.http.builder

import java.io.IOException
import java.net.UnknownHostException

import com.http.common.{SSLProcolVersion, SSLs}
import com.http.exception.HttpProcessException
import com.http.common.SSLProcolVersion.SSLProcolVersion
import javax.net.ssl.{SSLException, SSLHandshakeException}
import org.apache.http._
import org.apache.http.client.HttpRequestRetryHandler
import org.apache.http.client.config.RequestConfig
import org.apache.http.client.protocol.HttpClientContext
import org.apache.http.config.RegistryBuilder
import org.apache.http.conn.ConnectTimeoutException
import org.apache.http.conn.socket.{ConnectionSocketFactory, PlainConnectionSocketFactory}
import org.apache.http.impl.client.HttpClientBuilder
import org.apache.http.impl.conn.{DefaultProxyRoutePlanner, PoolingHttpClientConnectionManager}
import org.apache.http.protocol.HttpContext

/**
  * @author: shudj
  * @time: 2019/4/17 11:42
  * @description:
  */
class HCB extends HttpClientBuilder {

    private[this] var isSetPool = false
    private[this] var sslpv: SSLProcolVersion = SSLProcolVersion.SSLv3
    private[this] var ssls = SSLs.getInstance()

    def timeout(timeout1: Int): HCB = {
        timeout(timeout1, true)
    }

    /**
      * 设置超时时间以及是否允许网页重定向
      * @param timeout          超时时间，单位-毫秒
      * @param redirectEnable   自动调转
      * @return
      */
    def timeout(timeout: Int, redirectEnable: Boolean): HCB = {
        val builder = RequestConfig.custom()
            .setConnectionRequestTimeout(timeout)
            .setConnectTimeout(timeout)
            .setSocketTimeout(timeout)
            .setRedirectsEnabled(redirectEnable)
            .build()
        this.setDefaultRequestConfig(builder).asInstanceOf[HCB]
    }

    /**
      * 设置安全连接
      * @return
      */
    @throws[HttpProcessException]
    def ssl(): HCB = {
        this.setSSLSocketFactory(ssls.getSSLCONNSF(sslpv)).asInstanceOf[HCB]
    }

    /**
      * 设置自定义sslcontext
      * @param keyStorePath     秘钥库路径
      * @throws
      * @return
      */
    @throws[HttpProcessException]
    def ssl(keyStorePath: String): HCB = {
        ssl(keyStorePath, "nopassword")
    }

    /**
      * 设置自定义sslContex
      * @param str      秘钥库路径
      * @param str1     秘钥库密码
      * @throws
      * @return
      */
    @throws[HttpProcessException]
    def ssl(keyStorePath: String, keyStorepass: String): HCB = {
        ssls = SSLs.custom.customSSL(keyStorePath, keyStorepass)
        ssl()
    }

    /**
      * 设置连接池(默认开启https)
      * @param maxTotal                 最大连接数
      * @param defaultMaxPerRoute      每个路由默认连接数
      * @throws
      * @return
      */
    @throws[HttpProcessException]
    def pool(maxTotal: Int, defaultMaxPerRoute: Int): HCB = {
        val socketFactoryRegistry = RegistryBuilder.create[ConnectionSocketFactory]()
            .register("http", PlainConnectionSocketFactory.INSTANCE)
            .register("https",ssls.getSSLCONNSF(sslpv))
            .build()

        // 这是连接池大小
        val connectionManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry)
        connectionManager.setMaxTotal(maxTotal)
        connectionManager.setDefaultMaxPerRoute(defaultMaxPerRoute)
        isSetPool = true

        this.setConnectionManager(connectionManager).asInstanceOf[HCB]
    }

    /**
      * 设置代理
      * @param hostOrIp     代理host或者ip
      * @param port         代理端口
      * @return
      */
    def proxy(hostOrIp: String, port: Int): HCB = {
        val host = new HttpHost(hostOrIp, port, "http")
        val routePlanner = new DefaultProxyRoutePlanner(host)
        this.setRoutePlanner(routePlanner).asInstanceOf[HCB]
    }

    /**
      * 重试（如果请求是幂等的，就再次尝试）
      * @param tryTimes     重试次数
      * @return
      */
    def retry(tryTimes: Int): HCB = {
        retry(tryTimes, false)
    }

    /**
      * 重试（如果请求是幂等的，就在此尝试）
      * @param tryTimes                 重试次数
      * @param retryWhenInterruptedIO  连接拒绝时，是否重试
      * @return
      */
    def retry(tryTimes: Int, retryWhenInterruptedIO: Boolean): HCB = {
        // 请求重试处理
        val httpRequestHandler = new HttpRequestRetryHandler {
            override def retryRequest(exception: IOException, executionCount: Int, context: HttpContext): Boolean = {
                // 如果已经重试了n次了，就放弃
                if (executionCount >= tryTimes) return false
                // 如果服务器丢掉了连接，那么就重试
                if (exception.isInstanceOf[NoHttpResponseException]) return true
                // 不要重试SSL握手异常
                if (exception.isInstanceOf[SSLHandshakeException]) return false
                // 超时
                if (exception.isInstanceOf[java.lang.InterruptedException]) return retryWhenInterruptedIO
                // 目标服务器不可达
                if (exception.isInstanceOf[UnknownHostException]) return true
                // 连接被拒绝
                if (exception.isInstanceOf[ConnectTimeoutException]) return false
                // SSL握手异常
                if (exception.isInstanceOf[SSLException]) return false

                val clientContext = HttpClientContext.adapt(context)
                val request = clientContext.getRequest
                // 如果请求是幂等，就在此尝试
                if (!(request.isInstanceOf[HttpEntityEnclosingRequest])) return true

                false
            }
        }

        this.setRetryHandler(httpRequestHandler)
        this
    }

    /**
      * 设置ssl版本
      * 如果你想要设置ssl版本，必须先调用此方法，在调用ssl方法
      * 仅支持 SSLv3, TSLv1, TSLv1.1, TSLv1.2
      * @param sslpv    版本号
      * @return
      */
    def sslpv(sslpvNew: String): HCB = {
        sslpv(SSLProcolVersion.find(sslpvNew))
    }

    def sslpv(pv: SSLProcolVersion): HCB = {
        this.sslpv = pv
        this
    }
}


object HCB {
    def custom(): HCB = {

        new HCB
    }
}
