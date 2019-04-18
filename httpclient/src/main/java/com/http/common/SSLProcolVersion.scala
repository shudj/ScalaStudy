package com.http.common

/**
  * @author: shudj
  * @time: 2019/4/17 14:40
  * @description:
  */
object SSLProcolVersion extends Enumeration {

    type SSLProcolVersion = Value

    val SSL = Value("SSL")
    val SSLv3 = Value("SSLv3")
    val TLSv1 = Value("TLSv1")
    val TLSv1_1 = Value("TLSv1.1")
    val TLSv1_2 = Value("TLSv1.2")

    def find(name: String): SSLProcolVersion = {
        for (pv <- SSLProcolVersion.values) {
            if (pv.toString.toUpperCase().equals(name.toUpperCase))
                return pv
        }
        throw new RuntimeException("未支持当前ssl版本号：" + name)
    }
}
