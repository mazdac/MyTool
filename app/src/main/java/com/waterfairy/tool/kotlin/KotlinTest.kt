package com.waterfairy.tool.kotlin

import android.util.Log

/**
 * Created by water_fairy on 2017/6/14.
 * 995637517@qq.com
 */

object KotlinTest {
    private val TAG = "kotlinTest"

    fun doH(name: String, age: Int, dou: Double?): String {
        val content = name + age
        val builder = StringBuilder(content)
        builder.append(dou!!.toString() + "")
        Log.i(TAG, "doH: " + builder.toString())
        return builder.toString()
    }

    fun doH(age: Int, name: String): String {
        val content = name + age
        val builder = StringBuilder(content)
        Log.i(TAG, "doH: " + builder.toString())
        return builder.toString()
    }

}
