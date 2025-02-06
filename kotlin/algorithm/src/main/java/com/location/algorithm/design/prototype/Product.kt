package com.location.algorithm.design.prototype

interface Product {
    fun use(s: String)

    fun createClone(): Product
}