package com.location.algorithm.code.custom_okhttp

import com.location.algorithm.code.custom_okhttp.Headers


fun Headers() = Headers(mutableMapOf<String, String>())


data class Headers  constructor(
    private val headerMap: MutableMap<String, String>
){
    operator fun get(key: String): String? {
        return headerMap[key]
    }

    operator fun set(key: String, value: String) {
        headerMap[key] = value
    }


}
