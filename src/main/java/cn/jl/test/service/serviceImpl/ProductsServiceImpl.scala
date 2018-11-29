package cn.jl.test.service.serviceImpl

import cn.jl.test.dao.ProductDao
import cn.jl.test.service.ProductsService
import org.springframework.stereotype.Service
import org.springframework.beans.factory.annotation.{Autowired, Qualifier}

@Service
@Qualifier("productServiceImpl")
class ProductsServiceImpl extends ProductsService{


    @Autowired private[this] val productDao: ProductDao = null
    override def list(name: String): Array[Any] = {

        val chars = name.toCharArray
        var tempName: String = "'"
        chars.foreach(char => tempName += char + ".{0,}")
        tempName += "'"
        productDao.list(tempName)
    }
}
