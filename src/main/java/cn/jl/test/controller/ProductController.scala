package cn.jl.test.controller

import cn.jl.test.service.ProductsService
import org.springframework.beans.factory.annotation.{Autowired, Qualifier}
import org.springframework.web.bind.annotation.{GetMapping, RestController}

@RestController
class ProductController {

    @Autowired
    @Qualifier("productServiceImpl")
    private[this] val productServiceImpl: ProductsService = null

    @GetMapping(Array("/index"))
    def index(): String = "index"

    @GetMapping(Array("/product"))
    def list(name: String): Array[Any] = productServiceImpl.list(name)
}
