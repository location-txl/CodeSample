package com.location.algorithm.code.handler

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.internal.synchronized
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext

class MessageQueue {


    private var mHead: Message? = null
    private val lock = Any()


    @OptIn(InternalCoroutinesApi::class)
    fun enqueueMessage(message: Message, runTime: Long) {
        message.runTime = runTime
        synchronized(lock) {
            val head = mHead
            var needReLoop = false
            if (head == null) {
                mHead = message
                needReLoop = true
            } else {
                if (head.runTime > runTime) {
                    message.next = head
                    mHead = message
                    needReLoop = true
                } else {
                    var pre = head
                    var cur = head.next
                    while (cur != null) {
                        if (cur.runTime > runTime) {
                            pre?.next = message
                            message.next = cur
                            needReLoop = true
                            break
                        }
                        pre = cur
                        cur = cur.next
                    }
                    if (cur == null) {
                        pre.next = message
                    }
                }
            }
            if (needReLoop) {
                waitJob?.cancel()
                runBlocking {
                    sync.signal()
                }
            }
        }

    }

    private val sync = Sync()
    private var waitJob: Job? = null


    @OptIn(InternalCoroutinesApi::class)
    suspend fun next(): Message {
        var head = mHead
        if (head == null) {
            sync.await()
            return next()
        }
        val remainTime = head.runTime - System.currentTimeMillis()
        if (remainTime > 0) {
            var result: Message? = null
            coroutineScope {
                waitJob?.cancel()
                waitJob = launch {
                    try {
                        delay(remainTime)
                    } catch (e: CancellationException) {
                        result = withContext(NonCancellable) {
                            next()
                        }
                    }
                }
            }
            if (result != null) {
                return result
            }
            synchronized(lock) {
                mHead = head.next
                return head
            }
        } else {
            mHead = head.next
            return head
        }
    }


}

private class Sync {
    private val mutex = Mutex()

    private var deferred: CompletableDeferred<Unit>? = null
    suspend fun await() {
        val wait = mutex.withLock {
            if (deferred == null) {
                deferred = CompletableDeferred<Unit>()
            }
            deferred!!
        }
        wait.await()
    }

    suspend fun signal() {
        mutex.withLock {
            deferred?.complete(Unit)
            deferred = null
        }
    }
}


class Message(
    val what: Int, val obj: Any? = null, val callback: (suspend () -> Unit)? = null
) {
    var next: Message? = null
    var target: Handler? = null
    var runTime = 0L
}