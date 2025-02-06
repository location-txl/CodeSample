package com.location.algorithm.design.prototype

class Manager {

    private val showcase = mutableMapOf<String, Product>()

    fun register(name: String, proto: Product) {
        showcase[name] = proto
    }

    fun create(name: String): Product {
        val p = showcase[name]
        return p?.createClone() ?: throw IllegalArgumentException("No such product")
    }
}