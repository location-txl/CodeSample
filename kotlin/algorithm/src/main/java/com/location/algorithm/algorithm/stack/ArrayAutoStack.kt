@file:Suppress("UNCHECKED_CAST")

package algorithm.stack

/**
 * 使用数组实现的自动扩容的栈
 */
class ArrayAutoStack<T>(initCapacity: Int = 10) {
    private var array = arrayOfNulls<Any>(initCapacity)
    private var capacity: Int = initCapacity
    private var count: Int = 0


    fun push(value: T): Boolean {
        if (count >= capacity) {
            val newCapacity = (capacity * 1.5f).toInt()
            val newArray = arrayOfNulls<Any>(newCapacity)
            System.arraycopy(array, 0, newArray, 0, count)
            capacity = newCapacity
            array = newArray
        }
        array[count++] = value
        return true
    }


    fun peek(): T? = if (count > 0) {
        array[count - 1] as T
    } else {
        null
    }

    fun pop(): T? = if (count > 0) {
        count--
        array[count] as T
    } else {
        null
    }
}

