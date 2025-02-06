package algorithm.stack

/**
 * 栈是后入先出  先入后出
 * 只能在一端插入和删除
 *
 * 当某一个数据集合 业务只在一端涉及到插入和删除时，可以考虑使用栈
 *
 * 数组实现的叫顺序栈
 * 链表实现的叫链式栈
 */
class LinkedStack {
    private var head: Node? = null
    fun push(item: Int) {
        val node = Node(item)
        if (head == null) {
            head = node
        } else {
            node.next = head
            head = node
        }
    }


    fun pop(): Int? = if (head == null) {
        null
    } else {
        val value = head
        head = head?.next
        value?.data
    }

    fun peek(): Int? = if (head == null) {
        null
    } else {
        head?.data
    }


    class Node(val data: Int) {
        var next: Node? = null
    }
}