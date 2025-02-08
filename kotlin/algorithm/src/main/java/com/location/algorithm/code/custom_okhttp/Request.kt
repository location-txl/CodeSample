package com.location.algorithm.code.custom_okhttp

class Request(
    val url: String,
    val method: String,
    val headers: Headers,
    val body: ByteArray?
)