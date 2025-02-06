package com.location.algorithm.algorithm.linkedlist.twoway


class Node(var data: Int) {
    companion object{
        private var sId = 0
    }
    var next: Node? = null
    var prev: Node? = null
    val id = sId++

    override fun toString(): String {
        return "Node(id=$id, data=$data)"
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
            node.prev = tail
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
                //只有一个节点 delete.next为null
                delete.next
            }else{
                tail
            }
            head = delete.next
            if(tail == head){
                tail?.prev = null
            }
            return true
        }
        var current = head
        while (current?.next != delete){
            current = current?.next
        }
        current.next = delete.next
        delete.next?.prev = current
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
            val currentNext = node.next
            /**
             * 双相链表的反转
             * next是反转前的上一个节点
             * prev节点就是自身的next节点
             */
            node.next = prev
            node.prev = currentNext
            prev = node
            node = currentNext
        }
    }

    fun print(){
        var node = head
        println("head:$head")
        println("tail:$tail")
        while (node != null){
            println("${node.prev}<--${node}-->${node.next}")
            node = node.next
        }
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
    nodeList.reverse()
    println("reverse")
    nodeList.print()


//    println("delete ")
//    nodeList.delete(node3)
//    nodeList.print()

}

