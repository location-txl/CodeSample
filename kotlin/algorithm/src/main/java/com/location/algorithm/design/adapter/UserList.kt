package com.location.algorithm.design.adapter

class UserList<T> {
    private var adapter: Adapter<T>? = null


    fun setAdapter(adapter: Adapter<T>) {
        this.adapter?.let {
            println("取消注册 上一个adapter")
        }
        this.adapter = adapter
    }


    fun start(data: List<T>) {
        adapter?.let {
            data.forEach { item ->
                it.log(item)
            }
        }?: run {
            println("没有注册adapter")
        }
    }


}