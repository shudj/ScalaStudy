package cn.jl.test.util

/**
  * 枚举返回code
  */
object Code extends Enumeration {
    type Code = Value
    val OK = Value(0, "200")
    val REQUEST_PARAM_ERROR = Value(1, "454")
}
