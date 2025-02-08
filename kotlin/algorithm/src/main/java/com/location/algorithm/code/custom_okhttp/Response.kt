package com.location.algorithm.code.custom_okhttp

data class Response(
    val code: Int,
    val message: String,
    val headers: Headers,
    val body: ByteArray
)

data class ResponseBody(
    val bytes: ByteArray,//简单写法，实际应该用流
) {
    fun bytes(): ByteArray {
        return bytes
    }

    val length: Int
        get() = bytes.size

    val string: String
        get() = bytes.toString(Charsets.UTF_8)

}