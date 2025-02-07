package com.location.algorithm.code.handler

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.runBlocking

class Looper private constructor() {
    val thread = Thread.currentThread()

    companion object {
        private val looperStore = ThreadLocal<Looper>()
        fun prepare() {
            check(looperStore.get() == null) {
                "looper has already been prepared"
            }
            looperStore.set(Looper())
        }

        fun myLooper(): Looper? {
            return looperStore.get()
        }
    }

     val messageQueue = MessageQueue()


    fun loop() {
        try {
            runBlocking(CoroutineName("Looper")) {
                coroutineScope {
                    while (true) {
                        val message = messageQueue.next()
                        message.target!!.handleMessage(message)
                        message.callback?.invoke()
                    }
                }

            }
        } catch (ignore: CancellationException) {
        }

    }

}