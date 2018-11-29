package cn.jl.test.util

import org.springframework.core.MethodParameter
import org.springframework.http.MediaType
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.http.server.{ServerHttpRequest, ServerHttpResponse}
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice

@ControllerAdvice
class ReturnCodeHandler extends ResponseBodyAdvice[Any] {

    override def supports(methodParameter: MethodParameter,
                          aClass: Class[_ <: HttpMessageConverter[_]]): Boolean =
        classOf[MappingJackson2HttpMessageConverter].isAssignableFrom(aClass)

    override def beforeBodyWrite(t: Any, methodParameter: MethodParameter,
                                 mediaType: MediaType, aClass: Class[_ <: HttpMessageConverter[_]],
                                 serverHttpRequest: ServerHttpRequest,
                                 serverHttpResponse: ServerHttpResponse): Any =
        new Resp[Any](String.valueOf(Code.OK).toInt, "Ok", t)
}
