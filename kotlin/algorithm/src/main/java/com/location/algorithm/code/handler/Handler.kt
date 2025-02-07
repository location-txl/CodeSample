package com.location.algorithm.code.handler

import kotlinx.coroutines.delay
import java.lang.invoke.MethodHandles.loop
import kotlin.concurrent.thread

open class Handler(val looper: Looper = Looper.myLooper() ?: error(" looper is null")) {

    open suspend fun handleMessage(message: Message) {
    }

    fun post(message: Message){
        message.target = this
        looper.messageQueue.enqueueMessage(message, System.currentTimeMillis())
    }

    fun postDelayed(message: Message, delayMillis: Long){
        message.target = this
        looper.messageQueue.enqueueMessage(message, System.currentTimeMillis() + delayMillis)
    }

    fun postDelayed(runnable: suspend () -> Unit, delayMillis: Long){
        postDelayed(Message(0, callback = runnable).apply {
            target = this@Handler
        }, delayMillis)
    }


}

fun main() {
    Looper.prepare()
    val looper = Looper.myLooper()!!
    var handler: Handler = object : Handler(looper){
        override suspend fun handleMessage(message: Message) {
            println("handleMessage what:${message.what}:${System.currentTimeMillis()}")
            delay(1000)
        }
    }
    handler.post(Message(1))
    handler.postDelayed({
        println("postDelayed invoke")
    }, 3000)
    handler.postDelayed(Message(2), 2000)
    looper.loop()
}