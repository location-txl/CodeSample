package com.location.algorithm.design.template

class StringDisplay: AbsDisplay() {
    override fun open() {
        println("open")
    }

    override fun print() {
        println("print")
    }

    override fun close() {
        println("close")
    }
}