package com.zzzl.guolinlivekotlinhightorderfunction

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.edit
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // OnClickListener 是 Java 单抽象方法
        // 只有这种 java 单抽象方法，才可以这么写。
        // 如果换成 kotlin 的 Button 类和接口，就不能这么写了。
        // java 必须要使用单抽象方法才能这种 lambda 表达式，kotlin 有更方便的方法，那就是高阶。
        button.setOnClickListener {

        }

        // ================== 高阶应用 ==================
        // 原来的java写法（作对比用）
        val editor = getPreferences(Context.MODE_PRIVATE).edit()
        editor.putString("aaa", "hello")
        editor.putBoolean("vv", true)
        editor.putFloat("ccc", 10f)
        editor.apply()

        // 依赖 core-ktx 库(这个就是高阶函数)
        getPreferences(Context.MODE_PRIVATE).edit {
            editor.putString("aaa", "hello")
            editor.putBoolean("vv", true)
            editor.putFloat("ccc", 10f)
            // 而且不需要单独调用 apply
        }
        /* 对上面的代码的解释
        因为 edit 方法内部已经调用了 commit 和 apply（commit:Boolean = false  是默认参数，这时候这个参数就不是必须要传的。）
         @SuppressLint("ApplySharedPref")
        inline fun SharedPreferences.edit(
            commit: Boolean = false,
            action: SharedPreferences.Editor.() -> Unit
        ) {
            val editor = edit()
            action(editor)
            if (commit) {
                editor.commit()
            } else {
                editor.apply()
            }
        }*/

        // 自己利用高阶函数写一个这样的功能（modify 就是我们自己写的高阶）
        getPreferences(Context.MODE_PRIVATE).modify {
            putString("aad", "hello 高阶")
            putFloat("ccccc", 100f)
        }

        // 自己利用高阶函数写一个这样的功能（modify2 就是我们自己写的高阶）
        getPreferences(Context.MODE_PRIVATE).modify2(true) {
            putString("aad", "hello 高阶")
            putFloat("ccccc", 100f)
        }
    }
}


// 对应 ktx 库的 SharedPreferences 的扩展函数 edit 方法
fun SharedPreferences.modify(block: SharedPreferences.Editor.() -> Unit) {
    // 获取 editor
    val editor = edit()  // 当前上下文是 SharedPreferences，所以可以直接调用 editor() 方法
    // 调用函数
    editor.block()// 上下文是 SharedPreferences.Editor，所以要用 editor 实例来调用。
    // 调用 apply 进行提交
    editor.apply()

    /*
    block: SharedPreferences.Editor.() -> Unit
    意思是  这个 block 函数，是属于 SharedPreferences.Editor 这个类下的 扩展函数
    所以调用的时候需要有 Editor 的实例
     */
}

// 错误的写法
fun SharedPreferences.modifyError(block: () -> Unit) {
    // 获取 editor
    val editor = edit()
    // 调用函数
    block()
    // 调用 apply 进行提交
    editor.apply()

    /*
    这样写的话调用的时候就不能直接使用 editor 中的 putString("aaa","hello") 方法了
    比如这样就会报错，因为上下文是 SharedPreferences ，而不是 SharedPreferences.Editor。
    getPreferences(Context.MODE_PRIVATE).modify {
            putString("aad","hello 高阶")
            putFloat("ccccc",100f)
        }
     */
}

// 对应 ktx 库的 SharedPreferences 的扩展函数 edit 方法
// 跟 ktx 一样的写法是这样的，添加了一个 Boolean 来控制 commit 还是 apply
fun SharedPreferences.modify2(isCommit: Boolean = false, block: SharedPreferences.Editor.() -> Unit) {
    // 获取 editor
    val editor = edit()  // 当前上下文是 SharedPreferences，所以可以直接调用 editor() 方法
    // 调用函数
    editor.block()// 上下文是 SharedPreferences.Editor，所以要用 editor 实例来调用。
    if (isCommit) {
        // 调用 commit 进行提交
        editor.commit()
    } else {
        // 调用 apply 进行提交
        editor.apply()
    }
    /*
    block: SharedPreferences.Editor.() -> Unit
    意思是  这个 block 函数，是属于 SharedPreferences.Editor 这个类下的 扩展函数
    所以调用的时候需要有 Editor 的实例
     */
}