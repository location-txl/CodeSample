@file:Suppress("UNCHECKED_CAST")

package algorithm.queue

/**
 * 使用数组实现的循环队列 有限容量
 */
class ArrayRingQueue<T>(private val capacity: Int) : Queue<T> {
    private var count = 0
    private var head = -1
    private var tail = -1
    private val array = arrayOfNulls<Any>(capacity)

    override val size: Int
        get() = count

    override fun dequeue(): T? {
        if (count == 0) {
            return null
        }
        return (array[head] as T).also {
            count--
            head = (head + 1) % capacity
        }
    }

    override fun peek(): T? {
        if (count == 0) {
            return null
        }
        return array[head] as T
    }

    override fun enqueue(value: T): Boolean {
        //(tail + 1) % capacity == head 则表示队列已满 tail再往后移动一个位置就会和head重合
        if ((tail + 1) % capacity == head) {
            return false
        }
        if (head == -1) {
            head = 0
            tail = 0
            array[head] = value
        } else {
            //tail移动到下一个位置 % capacity 防止越界
            tail = (tail + 1) % capacity
            array[tail] = value
        }
        count++
        return true
    }

}