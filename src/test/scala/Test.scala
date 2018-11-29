import cn.jl.test.util.{Code, ResponseMeg}

object Test {

    def main(args: Array[String]): Unit = {

        println(Code.OK)
        println(Code.values)
        println(Code.maxId)

        val res = new ResponseMeg[Any](1, "a", 9)
        print(res.msg)

    }
}
