package com.location.algorithm.code.custom_okhttp

import com.location.algorithm.code.custom_okhttp.CustomOkhttp.Builder


fun CustomOkhttp.toBuilder(): Builder {
    return Builder().apply {
        connectTimeout = this@toBuilder.connectTimeout
        readTimeout = this@toBuilder.readTimeout
        writeTimeout = this@toBuilder.writeTimeout
    }
}

class CustomOkhttp private constructor(
    val connectTimeout: Long,
    val readTimeout: Long,
    val writeTimeout: Long
){

    fun request(request: Request): Call {
        return Call(request, this)
    }


    class Builder {
        fun build(): CustomOkhttp {
            return CustomOkhttp(
                connectTimeout,
                readTimeout,
                writeTimeout
            )
        }

        var connectTimeout: Long = 0
        var readTimeout: Long = 0
        var writeTimeout: Long = 0
    }
}

/**
* TODO 写了个壳子 重要不紧急 后面实现责任链 重试 等逻辑
 */
fun main(){
    val client = CustomOkhttp.Builder().apply {
        connectTimeout = 1000
        readTimeout = 2000
    }.build()
    var response = client.request(
        Request(
            url = "https://www.baidu.com",
            method = "GET",
            headers = Headers(),
            body = null
        )
    ).execute()

    println(response.body.toString(Charsets.UTF_8))
}

