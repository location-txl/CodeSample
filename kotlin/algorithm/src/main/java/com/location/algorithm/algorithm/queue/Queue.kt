package algorithm.queue

interface Queue<T> {
    val size: Int
    fun enqueue(value: T): Boolean
    fun dequeue(): T?
    fun peek(): T?
}