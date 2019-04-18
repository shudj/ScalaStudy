package com.http.util

import java.io.{File, UnsupportedEncodingException}
import java.lang.reflect.{Field, Modifier}
import java.nio.charset.Charset

import org.apache.http.client.entity.UrlEncodedFormEntity
import org.apache.http.entity.mime.{HttpMultipartMode, MultipartEntityBuilder}
import org.apache.http.entity.{ByteArrayEntity, ContentType, FileEntity, StringEntity}
import org.apache.http.message.{BasicHeader, BasicNameValuePair}
import org.apache.http.protocol.HTTP
import org.apache.http.{HttpEntity, NameValuePair}

import scala.collection.mutable
import scala.collection.mutable.Map
import scala.util.control.Breaks._

/**
  * @author: shudj
  * @time: 2019/4/17 9:13
  * @description:   工具类
  *                     用于设定参数特定类型
  *                     启用debug模式，打印消息
  */
object Utils {

    import java.util
    //传入参数特定类型//传入参数特定类型

    val ENTITY_STRING = "$ENTITY_STRING$"
    val ENTITY_JSON = "$ENTITY_JSON$"
    val ENTITY_FILE = "$ENTITY_FILEE$"
    val ENTITY_BYTES = "$ENTITY_BYTES$"
    val ENTITY_INPUTSTREAM = "$ENTITY_INPUTSTREAM$"
    val ENTITY_SERIALIZABLE = "$ENTITY_SERIALIZABLE$"
    val ENTITY_MULTIPART = "$ENTITY_MULTIPART$"
    private val SPECIAL_ENTITIY = util.Arrays.asList(ENTITY_STRING, ENTITY_JSON, ENTITY_BYTES, ENTITY_FILE, ENTITY_INPUTSTREAM, ENTITY_SERIALIZABLE, ENTITY_MULTIPART)


    /**
      * 检测url是否含参数，如果有，则把参数加到参数列表中
      * @param url      资源地址
      * @param nvps     参数列表
      * @return         返回去掉参数的url
      */
    @throws[UnsupportedEncodingException]
    def checkHasParas(url: String, nvps: util.List[NameValuePair], encoding: String): String = {

        var temp = url
        // 检测url中是否存在参数
        if (url.contains("?") && url.indexOf("?") < url.indexOf("=")) {

            val map = buildParas(url.substring(url.indexOf("?") + 1))
            map2httpEntity(nvps, map, encoding)
            temp = url.substring(0, url.indexOf("?"))
        }

        temp
    }

    /**
      * 参数转换，将map中的参数，转到参数列表中
      * @param nvps
      * @param map
      * @param encoding
      * @throws
      * @return
      */
    @throws[UnsupportedEncodingException]
    def map2httpEntity(nvps: util.List[NameValuePair], map: mutable.Map[String, Any], encoding: String): HttpEntity = {
        var entity: HttpEntity = null
        // 拼接参数
        breakable(
            for ((k, v) <- map) {
                var isSpecial: Boolean = false
                if (SPECIAL_ENTITIY.contains(k)) {
                    isSpecial = true
                    if (ENTITY_STRING.equals(k)) {
                        entity = new StringEntity(String.valueOf(v), encoding)
                        break
                    } else if (ENTITY_JSON.equals(k)) {
                        var contentType: String = "application/json"
                        if (null != encoding) {
                            contentType += ";charset=" + encoding
                        }
                        entity.asInstanceOf[StringEntity].setContentType(contentType)
                        break
                    } else if (ENTITY_BYTES.equals(k)) {
                        entity = new ByteArrayEntity(k.getBytes())
                        break
                    } else if (ENTITY_FILE.equals(k)) {
                        if (classOf[java.io.File].isAssignableFrom(k.getClass)) {
                            entity = new FileEntity(v.asInstanceOf[java.io.File], ContentType.APPLICATION_OCTET_STREAM)
                        } else if (v.getClass == classOf[String]) {
                            entity = new FileEntity(new java.io.File(String.valueOf(v)), ContentType.create("text/plain", "UTF-8"))
                        }
                        break()
                    } else if (ENTITY_INPUTSTREAM.equals(k)) {
                        break()
                    } else if (ENTITY_SERIALIZABLE.equals(k)) {
                        break()
                    } else if (ENTITY_MULTIPART.equals(k)) {
                        var files: Array[File] = null
                        if (classOf[File].isAssignableFrom(k.getClass.getComponentType)) {
                            files = v.asInstanceOf[Array[File]]
                        } else if (v.getClass.getComponentType == classOf[String]) {
                            val names: Array[String] = v.asInstanceOf[Array[String]]
                            files = new Array[File](names.length)
                            for (i <- 0 until names.length) {
                                files(i) = new File(names(i))
                            }
                        }

                        val builder = MultipartEntityBuilder.create()
                        // 设置请求的编码格式
                        builder.setCharset(Charset.forName(encoding))
                        // 设置浏览器兼容模式
                        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE)
                        var count: Int = 0
                        for (file <- files) {
                            builder.addBinaryBody(String.valueOf(map.get(ENTITY_MULTIPART + ".name")) + count, file)
                            count += 1
                        }

                        val forceRemoveContentTypeCharset: Boolean = map.get(ENTITY_MULTIPART + ".rmCharset").asInstanceOf[Boolean]
                        var m = Map[String, Any]()
                        m = m ++ map
                        m -= (ENTITY_MULTIPART)
                        m -= (ENTITY_MULTIPART + ".name")
                        m -= (ENTITY_MULTIPART + ".rmCharset")
                        // 发送数据
                        for ((k1, v1) <- m) builder.addTextBody(k1, String.valueOf(v1), ContentType.create("text/plain", encoding))

                        // 生成HTTP POST实体
                        entity = builder.build()
                        // 强制去除contentType中的编码设置，否则咋子某些情况下会导致上传失败
                        if (forceRemoveContentTypeCharset){
                            removeContentTypeCharset(encoding, entity)
                            break()
                        } else {
                            nvps.add(new BasicNameValuePair(k, String.valueOf(v)))
                        }
                    } else {
                        nvps.add(new BasicNameValuePair(k, String.valueOf(v)))
                    }
                }
                if (!isSpecial) {
                    entity = new UrlEncodedFormEntity(nvps, encoding)
                }
            }
        )

        entity
    }

    /**
      * 移除content-type中charSet
      * @param encoding 编码
      * @param entity   请求参数及数据信息
      * @return
      */
    def removeContentTypeCharset(encoding: String, entity: HttpEntity) = {
        try {
            val clazz = entity.getClass
            val field: Field = clazz.getDeclaredField("contentType")
            // 将字段的访问权限设为true：即去除private修饰符的影响
            field.setAccessible(true)
            if (Modifier.isFinal(field.getModifiers)) {
                //去除final修饰符的影响，将字段设为可修改的
                val modifiers = classOf[Field].getDeclaredField("modifiers")
                modifiers.setAccessible(true)
                modifiers.setInt(field, field.getModifiers & ~Modifier.FINAL)
            }

            val header = field.get(entity).asInstanceOf[BasicHeader]
            field.set(entity, new BasicHeader(HTTP.CONTENT_TYPE, header.getValue.replace(";charset=" + encoding, "")))
        } catch {
            case e: Exception => e.printStackTrace()
        }
    }

    /**
      * 参数转换，将map中的参数，转到参数列表中
      * @param nvps     参数列表
      * @param map      参数列表(map)
      */
    def map2List(nvps: util.List[NameValuePair], map: Map[String, String]): Unit = {
        if (null == map) {
            return
        }

        // 拼接参数
        for ((k, v) <- map) {
            nvps.add(new BasicNameValuePair(k, v))
        }
    }

    /**
      * 生成参数
      * 参数格式 “"k1=v1&k2=v2"
      * @param paras        参数列表
      * @return             返回参数列表(map)
      */
    def buildParas(paras: String): Map[String, Any] = {
        val par = paras.split("&")

        val map = Map[String, Any]()
        for (p <- par) {
            val str = p.split("=")
            map += (str(0) -> str(1))
        }

        map
    }
}
