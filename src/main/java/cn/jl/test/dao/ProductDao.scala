package cn.jl.test.dao


import org.apache.ibatis.annotations.Param
import org.springframework.stereotype.Repository

@Repository
trait ProductDao {

    def list(@Param("name") name: String): Array[Any]
}
