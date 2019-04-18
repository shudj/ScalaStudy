package com.http.common

import org.apache.http.Header

import scala.collection.mutable.Map
/**
  * @author: shudj
  * @time: 2019/4/18 15:08
  * @description:
  */

object HttpHeader {

    def custom = new HttpHeader
}
class HttpHeader {
    import org.apache.http.Consts
    import org.apache.http.message.BasicHeader



    //记录head头信息
    private var headerMaps = Map[String, Header]()

    /**
      * 自定义header头信息
      *
      * @param key   header-key
      * @param value header-value
      * @return 返回当前对象
      */
    def other(key: String, value: String): HttpHeader = {
        headerMaps.put(key, new BasicHeader(key, value))
        this
    }

    /**
      * 指定客户端能够接收的内容类型
      * 例如：Accept: text/plain, text/html
      *
      * @param accept accept
      * @return 返回当前对象
      */
    def accept(accept: String): HttpHeader = {
        headerMaps.put(HttpReqHead.ACCEPT, new BasicHeader(HttpReqHead.ACCEPT, accept))
        this
    }

    /**
      * 浏览器可以接受的字符编码集
      * 例如：Accept-Charset: iso-8859-5
      *
      * @param acceptCharset accept-charset
      * @return 返回当前对象
      */
    def acceptCharset(acceptCharset: String): HttpHeader = {
        headerMaps.put(HttpReqHead.ACCEPT_CHARSET, new BasicHeader(HttpReqHead.ACCEPT_CHARSET, acceptCharset))
        this
    }

    /**
      * 指定浏览器可以支持的web服务器返回内容压缩编码类型
      * 例如：Accept-Encoding: compress, gzip
      *
      * @param acceptEncoding accept-encoding
      * @return 返回当前对象
      */
    def acceptEncoding(acceptEncoding: String): HttpHeader = {
        headerMaps.put(HttpReqHead.ACCEPT_ENCODING, new BasicHeader(HttpReqHead.ACCEPT_ENCODING, acceptEncoding))
        this
    }

    /**
      * 浏览器可接受的语言
      * 例如：Accept-Language: en,zh
      *
      * @param acceptLanguage accept-language
      * @return 返回当前对象
      */
    def acceptLanguage(acceptLanguage: String): HttpHeader = {
        headerMaps.put(HttpReqHead.ACCEPT_LANGUAGE, new BasicHeader(HttpReqHead.ACCEPT_LANGUAGE, acceptLanguage))
        this
    }

    /**
      * 可以请求网页实体的一个或者多个子范围字段
      * 例如：Accept-Ranges: bytes
      *
      * @param acceptRanges accept-ranges
      * @return 返回当前对象
      */
    def acceptRanges(acceptRanges: String): HttpHeader = {
        headerMaps.put(HttpReqHead.ACCEPT_RANGES, new BasicHeader(HttpReqHead.ACCEPT_RANGES, acceptRanges))
        this
    }

    /**
      * HTTP授权的授权证书
      * 例如：Authorization: Basic QWxhZGRpbjpvcGVuIHNlc2FtZQ==
      *
      * @param authorization authorization
      * @return 返回当前对象
      */
    def authorization(authorization: String): HttpHeader = {
        headerMaps.put(HttpReqHead.AUTHORIZATION, new BasicHeader(HttpReqHead.AUTHORIZATION, authorization))
        this
    }

    /**
      * 指定请求和响应遵循的缓存机制
      * 例如：Cache-Control: no-cache
      *
      * @param cacheControl cache-control
      * @return 返回当前对象
      */
    def cacheControl(cacheControl: String): HttpHeader = {
        headerMaps.put(HttpReqHead.CACHE_CONTROL, new BasicHeader(HttpReqHead.CACHE_CONTROL, cacheControl))
        this
    }

    /**
      * 表示是否需要持久连接（HTTP 1.1默认进行持久连接）
      * 例如：Connection: close 短链接； Connection: keep-alive 长连接
      *
      * @param connection connection
      * @return 返回当前对象
      */
    def connection(connection: String): HttpHeader = {
        headerMaps.put(HttpReqHead.CONNECTION, new BasicHeader(HttpReqHead.CONNECTION, connection))
        this
    }

    /**
      * HTTP请求发送时，会把保存在该请求域名下的所有cookie值一起发送给web服务器
      * 例如：Cookie: $Version=1; Skin=new;
      *
      * @param cookie cookie
      * @return 返回当前对象
      */
    def cookie(cookie: String): HttpHeader = {
        headerMaps.put(HttpReqHead.COOKIE, new BasicHeader(HttpReqHead.COOKIE, cookie))
        this
    }

    /**
      * 请求内容长度
      * 例如：Content-Length: 348
      *
      * @param contentLength content-length
      * @return 返回当前对象
      */
    def contentLength(contentLength: String): HttpHeader = {
        headerMaps.put(HttpReqHead.CONTENT_LENGTH, new BasicHeader(HttpReqHead.CONTENT_LENGTH, contentLength))
        this
    }

    /**
      * 请求的与实体对应的MIME信息
      * 例如：Content-Type: application/x-www-form-urlencoded
      *
      * @param contentType content-type
      * @return 返回当前对象
      */
    def contentType(contentType: String): HttpHeader = {
        headerMaps.put(HttpReqHead.CONTENT_TYPE, new BasicHeader(HttpReqHead.CONTENT_TYPE, contentType))
        this
    }

    /**
      * 请求发送的日期和时间
      * 例如：Date: Tue, 15 Nov 2010 08:12:31 GMT
      *
      * @param date date
      * @return 返回当前对象
      */
    def date(date: String): HttpHeader = {
        headerMaps.put(HttpReqHead.DATE, new BasicHeader(HttpReqHead.DATE, date))
        this
    }

    /**
      * 请求的特定的服务器行为
      * 例如：Expect: 100-continue
      *
      * @param expect expect
      * @return 返回当前对象
      */
    def expect(expect: String): HttpHeader = {
        headerMaps.put(HttpReqHead.EXPECT, new BasicHeader(HttpReqHead.EXPECT, expect))
        this
    }

    /**
      * 发出请求的用户的Email
      * 例如：From: user@email.com
      *
      * @param from from
      * @return 返回当前对象
      */
    def from(from: String): HttpHeader = {
        headerMaps.put(HttpReqHead.FROM, new BasicHeader(HttpReqHead.FROM, from))
        this
    }

    /**
      * 指定请求的服务器的域名和端口号
      * 例如：Host: blog.csdn.net
      *
      * @param host host
      * @return 返回当前对象
      */
    def host(host: String): HttpHeader = {
        headerMaps.put(HttpReqHead.HOST, new BasicHeader(HttpReqHead.HOST, host))
        this
    }

    /**
      * 只有请求内容与实体相匹配才有效
      * 例如：If-Match: “737060cd8c284d8af7ad3082f209582d”
      *
      * @param ifMatch if-match
      * @return 返回当前对象
      */
    def ifMatch(ifMatch: String): HttpHeader = {
        headerMaps.put(HttpReqHead.IF_MATCH, new BasicHeader(HttpReqHead.IF_MATCH, ifMatch))
        this
    }

    /**
      * 如果请求的部分在指定时间之后被修改则请求成功，未被修改则返回304代码
      * 例如：If-Modified-Since: Sat, 29 Oct 2010 19:43:31 GMT
      *
      * @param ifModifiedSince if-modified-Since
      * @return 返回当前对象
      */
    def ifModifiedSince(ifModifiedSince: String): HttpHeader = {
        headerMaps.put(HttpReqHead.IF_MODIFIED_SINCE, new BasicHeader(HttpReqHead.IF_MODIFIED_SINCE, ifModifiedSince))
        this
    }

    /**
      * 如果内容未改变返回304代码，参数为服务器先前发送的Etag，与服务器回应的Etag比较判断是否改变
      * 例如：If-None-Match: “737060cd8c284d8af7ad3082f209582d”
      *
      * @param ifNoneMatch if-none-match
      * @return 返回当前对象
      */
    def ifNoneMatch(ifNoneMatch: String): HttpHeader = {
        headerMaps.put(HttpReqHead.IF_NONE_MATCH, new BasicHeader(HttpReqHead.IF_NONE_MATCH, ifNoneMatch))
        this
    }

    /**
      * 如果实体未改变，服务器发送客户端丢失的部分，否则发送整个实体。参数也为Etag
      * 例如：If-Range: “737060cd8c284d8af7ad3082f209582d”
      *
      * @param ifRange if-range
      * @return 返回当前对象
      */
    def ifRange(ifRange: String): HttpHeader = {
        headerMaps.put(HttpReqHead.IF_RANGE, new BasicHeader(HttpReqHead.IF_RANGE, ifRange))
        this
    }

    /**
      * 只在实体在指定时间之后未被修改才请求成功
      * 例如：If-Unmodified-Since: Sat, 29 Oct 2010 19:43:31 GMT
      *
      * @param ifUnmodifiedSince if-unmodified-since
      * @return 返回当前对象
      */
    def ifUnmodifiedSince(ifUnmodifiedSince: String): HttpHeader = {
        headerMaps.put(HttpReqHead.IF_UNMODIFIED_SINCE, new BasicHeader(HttpReqHead.IF_UNMODIFIED_SINCE, ifUnmodifiedSince))
        this
    }

    /**
      * 限制信息通过代理和网关传送的时间
      * 例如：Max-Forwards: 10
      *
      * @param maxForwards max-forwards
      * @return 返回当前对象
      */
    def maxForwards(maxForwards: String): HttpHeader = {
        headerMaps.put(HttpReqHead.MAX_FORWARDS, new BasicHeader(HttpReqHead.MAX_FORWARDS, maxForwards))
        this
    }

    /**
      * 用来包含实现特定的指令
      * 例如：Pragma: no-cache
      *
      * @param pragma pragma
      * @return 返回当前对象
      */
    def pragma(pragma: String): HttpHeader = {
        headerMaps.put(HttpReqHead.PRAGMA, new BasicHeader(HttpReqHead.PRAGMA, pragma))
        this
    }

    /**
      * 连接到代理的授权证书
      * 例如：Proxy-Authorization: Basic QWxhZGRpbjpvcGVuIHNlc2FtZQ==
      *
      * @param proxyAuthorization proxy-authorization
      * @return 返回当前对象
      */
    def proxyAuthorization(proxyAuthorization: String): HttpHeader = {
        headerMaps.put(HttpReqHead.PROXY_AUTHORIZATION, new BasicHeader(HttpReqHead.PROXY_AUTHORIZATION, proxyAuthorization))
        this
    }

    /**
      * 只请求实体的一部分，指定范围
      * 例如：Range: bytes=500-999
      *
      * @param range range
      * @return 返回当前对象
      */
    def range(range: String): HttpHeader = {
        headerMaps.put(HttpReqHead.RANGE, new BasicHeader(HttpReqHead.RANGE, range))
        this
    }

    /**
      * 先前网页的地址，当前请求网页紧随其后,即来路
      * 例如：Referer: http://www.zcmhi.com/archives/71.html
      *
      * @param referer referer
      * @return 返回当前对象
      */
    def referer(referer: String): HttpHeader = {
        headerMaps.put(HttpReqHead.REFERER, new BasicHeader(HttpReqHead.REFERER, referer))
        this
    }

    /**
      * 客户端愿意接受的传输编码，并通知服务器接受接受尾加头信息
      * 例如：TE: trailers,deflate;q=0.5
      *
      * @param te te
      * @return 返回当前对象
      */
    def te(te: String): HttpHeader = {
        headerMaps.put(HttpReqHead.TE, new BasicHeader(HttpReqHead.TE, te))
        this
    }

    /**
      * 向服务器指定某种传输协议以便服务器进行转换（如果支持）
      * 例如：Upgrade: HTTP/2.0, SHTTP/1.3, IRC/6.9, RTA/x11
      *
      * @param upgrade upgrade
      * @return 返回当前对象
      */
    def upgrade(upgrade: String): HttpHeader = {
        headerMaps.put(HttpReqHead.UPGRADE, new BasicHeader(HttpReqHead.UPGRADE, upgrade))
        this
    }

    /**
      * User-Agent的内容包含发出请求的用户信息
      *
      * @param userAgent user-agent
      * @return 返回当前对象
      */
    def userAgent(userAgent: String): HttpHeader = {
        headerMaps.put(HttpReqHead.USER_AGENT, new BasicHeader(HttpReqHead.USER_AGENT, userAgent))
        this
    }

    /**
      * 关于消息实体的警告信息
      * 例如：Warn: 199 Miscellaneous warning
      *
      * @param warning warning
      * @return 返回当前对象
      */
    def warning(warning: String): HttpHeader = {
        headerMaps.put(HttpReqHead.WARNING, new BasicHeader(HttpReqHead.WARNING, warning))
        this
    }

    /**
      * 通知中间网关或代理服务器地址，通信协议
      * 例如：Via: 1.0 fred, 1.1 nowhere.com (Apache/1.1)
      *
      * @param via via
      * @return 返回当前对象
      */
    def via(via: String): HttpHeader = {
        headerMaps.put(HttpReqHead.VIA, new BasicHeader(HttpReqHead.VIA, via))
        this
    }

    /**
      * 设置此HTTP连接的持续时间（超时时间）
      * 例如：Keep-Alive: 300
      *
      * @param keepAlive keep-alive
      * @return 返回当前对象
      *
      */
    def keepAlive(keepAlive: String): HttpHeader = {
        headerMaps.put(HttpReqHead.KEEP_ALIVE, new BasicHeader(HttpReqHead.KEEP_ALIVE, keepAlive))
        this
    }

    def accept: String = get(HttpReqHead.ACCEPT)

    def acceptCharset: String = get(HttpReqHead.ACCEPT_CHARSET)

    def acceptEncoding: String = get(HttpReqHead.ACCEPT_ENCODING)

    def acceptLanguage: String = get(HttpReqHead.ACCEPT_LANGUAGE)

    def acceptRanges: String = get(HttpReqHead.ACCEPT_RANGES)

    def authorization: String = get(HttpReqHead.AUTHORIZATION)

    def cacheControl: String = get(HttpReqHead.CACHE_CONTROL)

    def connection: String = get(HttpReqHead.CONNECTION)

    def cookie: String = get(HttpReqHead.COOKIE)

    def contentLength: String = get(HttpReqHead.CONTENT_LENGTH)

    def contentType: String = get(HttpReqHead.CONTENT_TYPE)

    def date: String = get(HttpReqHead.DATE)

    def expect: String = get(HttpReqHead.EXPECT)

    def from: String = get(HttpReqHead.FROM)

    def host: String = get(HttpReqHead.HOST)

    def ifMatch: String = get(HttpReqHead.IF_MATCH)

    def ifModifiedSince: String = get(HttpReqHead.IF_MODIFIED_SINCE)

    def ifNoneMatch: String = get(HttpReqHead.IF_NONE_MATCH)

    def ifRange: String = get(HttpReqHead.IF_RANGE)

    def ifUnmodifiedSince: String = get(HttpReqHead.IF_UNMODIFIED_SINCE)

    def maxForwards: String = get(HttpReqHead.MAX_FORWARDS)

    def pragma: String = get(HttpReqHead.PRAGMA)

    def proxyAuthorization: String = get(HttpReqHead.PROXY_AUTHORIZATION)

    def referer: String = get(HttpReqHead.REFERER)

    def te: String = get(HttpReqHead.TE)

    def upgrade: String = get(HttpReqHead.UPGRADE)

    def userAgent: String = get(HttpReqHead.USER_AGENT)

    def via: String = get(HttpReqHead.VIA)

    def warning: String = get(HttpReqHead.WARNING)

    def keepAlive: String = get(HttpReqHead.KEEP_ALIVE)


    /**
      * 获取head信息
      *
      * @return
      */
    private def get(headName: String): String = {
        if (headerMaps.contains(headName)) return headerMaps.get(headName).toString
        null
    }

    /**
      * 返回header头信息
      *
      * @return 返回构建的header头信息数组
      */
    def build: Array[Header] = {
        val headers = new Array[Header](headerMaps.size)
        var i = 0
        for (header <- headerMaps.values) {
            headers(i) = header
            i += 1
        }
        headerMaps.clear
        headerMaps = null
        headers
    }

    /**
      * Http头信息
      *
      * @author arron
      * @date 2015年11月9日 上午11:29:04
      * @version 1.0
      */
    private object HttpReqHead {
        val ACCEPT = "Accept"
        val ACCEPT_CHARSET = "Accept-Charset"
        val ACCEPT_ENCODING = "Accept-Encoding"
        val ACCEPT_LANGUAGE = "Accept-Language"
        val ACCEPT_RANGES = "Accept-Ranges"
        val AUTHORIZATION = "Authorization"
        val CACHE_CONTROL = "Cache-Control"
        val CONNECTION = "Connection"
        val COOKIE = "Cookie"
        val CONTENT_LENGTH = "Content-Length"
        val CONTENT_TYPE = "Content-Type"
        val DATE = "Date"
        val EXPECT = "Expect"
        val FROM = "From"
        val HOST = "Host"
        val IF_MATCH = "If-Match "
        val IF_MODIFIED_SINCE = "If-Modified-Since"
        val IF_NONE_MATCH = "If-None-Match"
        val IF_RANGE = "If-Range"
        val IF_UNMODIFIED_SINCE = "If-Unmodified-Since"
        val KEEP_ALIVE = "Keep-Alive"
        val MAX_FORWARDS = "Max-Forwards"
        val PRAGMA = "Pragma"
        val PROXY_AUTHORIZATION = "Proxy-Authorization"
        val RANGE = "Range"
        val REFERER = "Referer"
        val TE = "TE"
        val UPGRADE = "Upgrade"
        val USER_AGENT = "User-Agent"
        val VIA = "Via"
        val WARNING = "Warning"
    }

    /**
      * 常用头信息配置
      *
      * @author arron
      * @version 1.0
      */
    object Headers {
        val APP_FORM_URLENCODED = "application/x-www-form-urlencoded"
        val TEXT_PLAIN = "text/plain"
        val TEXT_HTML = "text/html"
        val TEXT_XML = "text/xml"
        val TEXT_JSON = "text/json"
        val CONTENT_CHARSET_ISO_8859_1: String = Consts.ISO_8859_1.name
        val CONTENT_CHARSET_UTF8: String = Consts.UTF_8.name
        val DEF_PROTOCOL_CHARSET: String = Consts.ASCII.name
        val CONN_CLOSE = "close"
        val KEEP_ALIVE = "keep-alive"
        val EXPECT_CONTINUE = "100-continue"
    }

}
