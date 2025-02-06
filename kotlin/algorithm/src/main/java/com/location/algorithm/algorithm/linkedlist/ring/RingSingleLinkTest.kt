package com.location.algorithm.algorithm.linkedlist.ring

/**
 * 环形链表 尾结点的next是头节点
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

    fun copy(cacheMap:MutableMap<Int, Node>? = null):Node{
        return if(cacheMap?.containsKey(id) == true){
            cacheMap[id]!!
        }else{
            Node(data).apply {
                next = this@Node.next?.copy(
                    (cacheMap?: mutableMapOf()).also {
                        it[this@Node.id] = this
                    }
                )
            }
        }
    }
}


class NodeList{
    var head: Node? = null
        private set

    var tail: Node? = null
        private set

    fun append(node: Node){
        if(head == null){
            head = node
            tail = head
            tail?.next = head
        }else{
            tail?.next = node
            tail = node
            tail?.next = head
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
        if(head == delete && tail == head){
            //只有一项
            head = null
            tail = null
            return true
        }

        if(head == delete){
            //删除头节点
            head = delete.next
            tail?.next = head
            return true
        }
        var current = head
        do{
            current = current?.next
        }while (current?.next != delete)
        current.next = delete.next
        if(delete.next == head){
            tail = current
        }
        return true
    }

    fun reverse(){
        var prev: Node? = null
        var node = head

        do {
            val next = node?.next
            node?.next = prev
            prev = node
            node = next
            //反转时 判断停止条件为不等于头节点
        } while (node != head)
        head = tail
        tail = node
        tail?.next = head
    }

    fun print(){
        if(head == null){
            return
        }
        println("head:$head")
        println("tail:$tail")
        var node = head
        do {
            println("${node}-->${node?.next}")
            node = node?.next
        }while (node != head)
    }
}

private fun main() {
    val nodeList = NodeList()
    val node3 = Node(6)
    with(nodeList){
        append(Node(1))
        append(Node(2))
        append(Node(4))
        append(Node(5))
        append(node3)
    }

    nodeList.print()
//    nodeList.reverse()
//    println("reverse")
//    nodeList.print()


    println("delete ")
    nodeList.delete(node3)
    nodeList.print()
    println("end")

}

