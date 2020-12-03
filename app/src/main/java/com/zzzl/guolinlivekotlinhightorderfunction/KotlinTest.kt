package com.zzzl.guolinlivekotlinhightorderfunction

import com.zzzl.guolinlivekotlinhightorderfunction.java.Button

fun main() {

    // String 的扩展函数
    stringTest()

    // lambda 是什么
    lambdaTest()

    // lambda 演化过程（用来理解 lambda）
    lambdaEvolution()


    println("============= 高阶函数 ===============")
    // 演示调用 java 单抽象方法
    val button = Button()
    // OnClickListener 必须是 java 方法，接口中必须是单个方法。
    button.setOnClickListener {
        println("button is clicked")
    }

    // kotlin 函数类型
    // kotlin 函数类型和 String Int 一样都是类型
    // (String, Int) -> Unit 就是一个函数类型
    // d 就是这个函数类型的变量，跟 d: String = "1" 一样理解
    val d: (String, Int) -> Unit

    // kotlin 高阶
    val num1 = 10
    val num2 = 20
    val result1 = num1AndNum2(num1,num2,::plus)
    val result2 = num1AndNum2(num1,num2,::minus)
    println("result1:$result1\nresult2:$result2")

    // 匿名函数写法，就是没有函数名
    val result3 = num1AndNum2(num1,num2,fun (num1: Int, num2: Int): Int{
        return num1 + num2
    })
    val result4 = num1AndNum2(num1,num2,fun (num1: Int, num2: Int): Int{
        return num1 - num2
    })

    // lambda 表达式写法
    val result5 = num1AndNum2(num1,num2){a: Int, b: Int ->
        a + b
    }
    println("result5:$result5")

    // lambda 表达式写法（类型推导）
    val result6 = num1AndNum2(num1,num2){a, b ->
        a - b
    }
    println("result6:$result6")

    // ============以上基本用法已经讲完了=================
    // 具体应用看 MainActivity（SharePreference扩展）


    // 高级应用
    val list = listOf("apple","zoo","pear","banana","watermelon")
    val resultMaxByLetterTable = list.findMax { it }
    val resultMaxByLength = list.findMax { it.length }
    println("resultMaxByLetterTable:$resultMaxByLetterTable")
    println("resultMaxByLength:$resultMaxByLength")


}

// num1 参数1，num2 参数2
// operation 参数3，参数3 的类型，(Int, Int) -> Int 函数类型。
fun num1AndNum2(num1: Int, num2: Int, operation: (Int, Int) -> Int): Int{
    // 内部调用 operation 函数
    return operation(num1,num2);
}

// 加法
fun plus(num1: Int, num2: Int): Int{
    return num1 + num2
}
// 减法
fun minus(num1: Int, num2: Int): Int{
    return num1 - num2
}

// lambda 演化
fun lambdaEvolution() {
    println("============= lambdaEvolution ==================")

    val list = listOf("apple","pear","banana","watermelon")
    // lambda 表达式
    val lambda = {fruit: String ->
        fruit.length
    }
    val result = list.maxBy(lambda)
    println(result)

    // 演化过程
    val result2 = list.maxBy(
        {fruit: String -> fruit.length}
    ) // 提示你删除小括号
    // 省略括号（一个方法，参数只是一个 lambda 表达式，小括号可以省略）
    val result3 = list.maxBy{fruit: String -> fruit.length}
    // 省略参数类型（kotlin有自动的类型推导机制）
    val result4 = list.maxBy{fruit -> fruit.length}
    // 如果只有一个参数，可以省略参数名，用 it 来标识
    val result5 = list.maxBy{ it.length }
    println("result:$result \n")
    println("result2:$result2 \n")
    println("result3:$result3 \n")
    println("result4:$result4 \n")
    println("result5:$result5 \n")

}

fun lambdaTest() {
    println("============= lambdaTest ==================")

    // 找到水果集中名称最长的元素
    val list = listOf("apple","pear","banana","watermelon")
    // java 写法
//    var result = ""
//    for(fruit in list){
//        if(fruit.length > result.length){
//            result = fruit
//        }
//    }
    // kotlin 写法
//    val result = list.maxBy { it.length } // maxBy() 废弃，建议使用 maxByOrNull()
    val result = list.maxByOrNull { it.length } // it 就是 lambda 表达式 表达的参数
    // 打印结果
    println(result)

}

// String 的扩展函数
fun stringTest(){
    println("============= stringTest ==================")
    // String 的扩展函数
    println("hello kotlin")
    "".hello()

    println("hello".toUpperCase())
    println("hello".capitalize())
    println("hello".decapitalize())

    println("code".capitalEnd())
}

fun String.hello(){
    println("hello String")
}

// 尾字母大写
fun String.capitalEnd(): String{
    if(this.isEmpty()) return ""
    val charArray = this.toCharArray()
    charArray[length -1] = charArray[length - 1].toUpperCase()
    return String(charArray)
}

/**
T 是 List 的 泛型，就是 List 集合元素

高阶 block: (T) 入参要用这个 T
高阶 block 返回值要是一种可比较的类型，因为Comparable 是可比较的，所以选用 Comparable。 比如 Int Long 都实现了 Comparable 接口
R 是 Comparable 的上界，就是必须要是 Comparable 的子类。
R 也是 Comparable 的泛型

因为我们要找集合中的最大值，所以返回值也是 T
T? 是代表了可为空
 */
fun <T, R: Comparable<R>> List<T>.findMax(block: (T) -> R): T?{
    if(isEmpty()){ return null}
    var maxElement = get(0) // 获取第一个元素
    var maxValue = block(maxElement) // 遍历 list
    for(element in this){
        val value = block(element) // 获取当前元素可比较的值
        if(value > maxValue){
            maxElement = element  // 对最大值元素进行赋值
            maxValue = value // 对最大值进行赋值
        }
    }
    return maxElement
}