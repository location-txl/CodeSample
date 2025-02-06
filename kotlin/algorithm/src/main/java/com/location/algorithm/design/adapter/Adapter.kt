package com.location.algorithm.design.adapter

interface Adapter<in T> {
    fun log(item:T)
}