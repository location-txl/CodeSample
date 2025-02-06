@file:Suppress("UNCHECKED_CAST")

package algorithm.queue

/**
 * 使用链表实现的无限队列
 */
class LinkedQueue<T : Any> : Queue<T>{
    override val size: Int
        get() = mSize



    var mSize:Int = 0
        private set
    private var head:Node? = null
    private var tail:Node? = null

    override fun enqueue(value:T):Boolean{
        val node = Node(value)
        mSize++
        if(head == null){
            head = node
            tail = node
        }else{
            tail?.next = node
            tail = node
        }
        return true
    }

    override fun dequeue():T?{
        if(head == null){
            return null
        }
        val tempHead = head?.next
        val data = head?.data as? T
        head = tempHead
        mSize--
        return data.also {
            if(size == 0){
                head = null
                tail = null
            }
        }
    }

    override fun peek():T? = head?.data as? T

    private data class Node(val data: Any) {
        var next: Node? = null
    }
}