package com.location.algorithm.design.zerenlian.sample

import com.location.algorithm.design.zerenlian.Client
import com.location.algorithm.design.zerenlian.Request

fun main() {
    val client = Client()
    client.addHandler(AHandler())
    client.addHandler(BHandler())
    client.addHandler(CHandler())
    val request = Request("apple",266)
    val handlerEvent = client.getHandlerEvent(request)
    handlerEvent?.let {
        println("response=$it")
    }

}