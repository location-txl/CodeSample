@file:Suppress("UNCHECKED_CAST")

package com.location.algorithm.algorithm.queue

import algorithm.queue.Queue

/**
 * 使用数组实现的自动扩容的无限队列
 */
class ArrayQueue<T>(initCapacity:Int) : Queue<T> {
    private var count = 0
    private var capacity = initCapacity
    private var array = arrayOfNulls<Any>(initCapacity)
    private var head:Int = -1
    private var tail:Int = -1
    override val size: Int
        get() = count

    override fun dequeue(): T? {
        if(count <= 0){
            return null
        }
        return (array[head++] as T).also {
            count--
        }
    }

    override fun peek(): T?{
        if(count <= 0){
            return null
        }
        return array[head] as T
    }

    override fun enqueue(value: T): Boolean {
        checkCapacity()
        if(head == -1){
            head = 0
            tail = 0
            array[head] = value
        }else{
            array[++tail] = value
        }
        count++
        return true
    }

    private fun checkCapacity(){
        //tail指针已经到达数组末尾
        if(tail == capacity - 1){
              // 容量不足
            if(count == capacity){
                //扩容
                val newCapacity = (capacity * 1.5f).toInt()
                val newArray = arrayOfNulls<Any>(newCapacity)
                System.arraycopy(array,0,newArray, 0, capacity)
                head = 0
                tail = capacity - 1
                capacity = newCapacity
                array = newArray
            }else{
                //容量充足 重置head指针
                var tempIndex = 0
                for(i in head..tail){
                    array[tempIndex] = array[i]
                    tempIndex++
                }
                head = 0
                tail = count - 1
            }
        }
    }
}