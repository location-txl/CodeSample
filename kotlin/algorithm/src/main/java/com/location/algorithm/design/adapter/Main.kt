package com.location.algorithm.design.adapter

fun main() {
    val userList = UserList<String>()
    val dataList = listOf("1", "2", "3", "4", "5")
    userList.setAdapter(object : Adapter<String> {
        override fun log(item: String) {
            println("item = $item")
        }
    })

    userList.start(dataList)
}