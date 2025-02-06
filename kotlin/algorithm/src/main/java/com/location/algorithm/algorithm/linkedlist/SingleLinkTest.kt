package com.location.algorithm.algorithm.linkedlist

/**
 * 单向链表
 */
class Node(val data: Int) {
    companion object{
        private var sId = 0
    }
    var next: Node? = null
    val id = sId++

    override fun toString(): String {
        return "Node(id=$id, data=$data)"
    }

    fun copy():Node = Node(data).apply {
        next = this@Node.next?.copy()
    }
}


class NodeList(){
    var head: Node? = null
        private set
    var tail: Node? = null
        private set

    fun append(node: Node){
        if(head == null){
            head = node
            tail = head
        }else{
            tail?.next = node
            tail = node
        }
    }

    fun deleteLast(){
        tail?.let {
            delete(it)
        }
    }

    fun delete(delete: Node):Boolean{
        if(head == null){
            return false
        }
        if(head == delete){
            tail = if(tail == head){
                delete.next
            }else{
                tail
            }
            head = delete.next
            return true
        }
        var current = head
        //遇到下一个是要删除的节点 停止循环
        while (current?.next != delete){
            current = current?.next
        }
        current.next = delete.next
        if(delete.next == null){
            tail = current
        }
        return true
    }

    fun reverse(){
        var prev: Node? = null
        var node = head
        head = tail
        tail = node
        while (node != null){
            val next = node.next
            //反转是下一个节点是上一个节点 一步步完成反转
            node.next = prev
            prev = node
            node = next
        }
    }

    fun print(){
        var node = head
        println("head:$head")
        println("tail:$tail")
        while (node != null){
            println("${node}-->${node.next}")
            node = node.next
        }
    }


    /**
     * 判断是否是回文字符串
     * test:
     *      1221 true
     *      12321 true
     *      1212 false
     * 思路
     * 使用双指针
     * 第一个指针每次前进两次
     * 第二个指针前进一次
     * 当第一个指针前进结束后就是在链表的末尾
     * 此时第二个指针正好在中间
     * 反转前面的字符串
     * 即 1221 变为 2121
     *
     */
    fun isPalindrome(temp: Boolean = true):Boolean{
        val head = if(temp){
            this.head?.copy()
        }else{
            this.head
        }
        return innerIsPalindrome(head)
    }

    private fun innerIsPalindrome(head:Node?): Boolean {
        if (head == null || head == tail) {
            return true
        }
        var prev: Node? = null
        var fast: Node? = head
        var slow: Node? = head
        while (fast?.next != null) {
            fast = fast.next?.next
            val slowNextTemp = slow?.next
            slow?.next = prev
            prev = slow
            slow = slowNextTemp
        }
        if (fast != null) {
            slow = slow?.next
        }
        while (slow != null && prev != null) {
            if (slow.data != prev.data) {
                return false
            }
            slow = slow.next
            prev = prev.next
        }

        return slow == null && prev == null
    }


}

private fun main() {
    val nodeList = NodeList()
    val node3 = Node(3)
    with(nodeList){
        append(Node(1))
        append(Node(2))
        append(node3)
        append(Node(4))
        append(Node(5))
    }

    nodeList.print()
//    nodeList.reverse()
//    println("reverse")
//    nodeList.print()


    println("delete ")
    nodeList.delete(node3)
    nodeList.print()
}

