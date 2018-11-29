package cn.jl.test

import org.mybatis.spring.annotation.MapperScan
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer

@SpringBootApplication
@MapperScan(Array("cn.jl.test"))
class ProdcutApplication {}
object ProdcutsApplication extends App{

    SpringApplication.run(classOf[ProdcutApplication])
}