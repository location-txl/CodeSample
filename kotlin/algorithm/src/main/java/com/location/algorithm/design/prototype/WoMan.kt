package com.location.algorithm.design.prototype

class WoMan: Product {
    override fun use(s: String) {
        println("WoMan:$s")
    }

    override fun createClone(): Product {
        return WoMan()
    }
}