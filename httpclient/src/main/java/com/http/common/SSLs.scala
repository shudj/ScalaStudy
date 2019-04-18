package com.http.common

import java.io.{FileInputStream, IOException}
import java.security.cert.X509Certificate
import java.security.{KeyManagementException, KeyStore, NoSuchAlgorithmException, SecureRandom}

import com.http.common.SSLProcolVersion.SSLProcolVersion
import com.http.exception.HttpProcessException
import javax.net.ssl._
import org.apache.http.conn.ssl.{SSLConnectionSocketFactory, TrustSelfSignedStrategy}
import org.apache.http.nio.conn.ssl.SSLIOSessionStrategy
import org.apache.http.ssl.SSLContexts

/**
  * @author: shudj
  * @time: 2019/4/17 13:39
  * @description:
  */
class SSLs private {


    @throws[HttpProcessException]
    def getSSLContext(sslpv: SSLProcolVersion): SSLContext = {
        try {
            if (null == SSLs.sc ) SSLs.sc = SSLContext.getInstance(sslpv.toString)

            SSLs.sc
        } catch {
            case e: NoSuchAlgorithmException => throw new HttpProcessException(e)
        }
    }

    @throws[HttpProcessException]
    def  getSSlSF(sslpv: SSLProcolVersion): SSLSocketFactory = {
        this.synchronized {
            if (null == SSLs.sslFactory) {
                try {
                    val sc = getSSLContext(sslpv)
                    sc.init(null, Array[TrustManager]{SSLs.simpleVerifier}, null)

                    SSLs.sslFactory = sc.getSocketFactory
                } catch {
                    case e: KeyManagementException => throw new HttpProcessException(e)
                }
            }
            SSLs.sslFactory
        }
    }

    @throws[HttpProcessException]
    def getSSLCONNSF(sslpv: SSLProcolVersion): SSLConnectionSocketFactory = {
        this.synchronized {
            if (null == SSLs.sslConnFactory) {

                try {
                    val sc = getSSLContext(sslpv)
                    sc.init(null, Array[TrustManager]{SSLs.simpleVerifier}, new SecureRandom())
                    SSLs.sslConnFactory = new SSLConnectionSocketFactory(sc, SSLs.simpleVerifier)
                } catch {
                    case e: KeyManagementException => throw new HttpProcessException(e)
                }
            }
            SSLs.sslConnFactory
        }
    }

    @throws[HttpProcessException]
    def getSSLIOSS(sslpv: SSLProcolVersion): SSLIOSessionStrategy = {

        this.synchronized{
            if (null == SSLs.sslIOSessionStrategy) {
                try {
                    val sc = getSSLContext(sslpv)
                    sc.init(null, Array[TrustManager]{SSLs.simpleVerifier}, new SecureRandom())
                    SSLs.sslIOSessionStrategy = new SSLIOSessionStrategy(sc, SSLs.simpleVerifier)
                } catch {
                    case e: KeyManagementException => throw new HttpProcessException(e)
                }

            }
            SSLs.sslIOSessionStrategy
        }
    }

    @throws[HttpProcessException]
    def customSSL(keyStorePath: String, keyStorepass: String): SSLs = {
        var instream: FileInputStream = null;
        var trustStore: KeyStore = null
        try {
            trustStore = KeyStore.getInstance(KeyStore.getDefaultType)
            instream = new FileInputStream(new java.io.File(keyStorePath))
            trustStore.load(instream, keyStorepass.toCharArray)
            SSLs.sc = SSLContexts.custom().loadTrustMaterial(trustStore, new TrustSelfSignedStrategy()).build()
        } catch {
            case e: Exception => throw new HttpProcessException(e)
        } finally {
            try {
                instream.close()
            } catch {
                case e: IOException => e.printStackTrace()
            }
        }

        this
    }
}

object SSLs {

    val simpleVerifier: SSLs.SSLHandler = new SSLs.SSLHandler

    var sslFactory: SSLSocketFactory = null
    var sslConnFactory: SSLConnectionSocketFactory = null
    var sslIOSessionStrategy: SSLIOSessionStrategy= null
    var sc: SSLContext = null
    val sslutil: SSLs = new SSLs

    def getInstance() = sslutil
    def custom: SSLs = new SSLs

    class SSLHandler extends X509TrustManager with HostnameVerifier {
        override def checkClientTrusted(x509Certificates: Array[X509Certificate], s: String): Unit = {}

        override def checkServerTrusted(x509Certificates: Array[X509Certificate], s: String): Unit = {}

        override def getAcceptedIssuers: Array[X509Certificate] = Array[X509Certificate]()

        override def verify(s: String, sslSession: SSLSession) = true
    }

    def getVerifier(): HostnameVerifier = {
        simpleVerifier
    }
}


