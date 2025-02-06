package com.location.algorithm.design.template

abstract class AbsDisplay {
    fun display(){
        open()
        repeat(5){
            print()
        }
        close()
    }

    protected abstract fun open()
    protected abstract fun print()
    protected abstract fun close()
}