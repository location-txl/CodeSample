package com.location.algorithm.design.iterator

fun main() {
    val bookShelf = BookShelf(20)
    bookShelf.add(Book("java", 100))
    bookShelf.add(Book("kotlin", 200))
    bookShelf.add(Book("python", 300))
    bookShelf.add(Book("c++", 400))
    bookShelf.add(Book("c#", 500))
    bookShelf.add(Book("c", 600))
    bookShelf.add(Book("go", 700))
    bookShelf.add(Book("swift", 800))
    bookShelf.add(Book("ruby", 900))
    bookShelf.add(Book("php", 1000))
    bookShelf.add(Book("javascript", 1100))
    val iterator = bookShelf.iterator()
    while (iterator.hasNext()) {
        val book = iterator.next()
        println(book)
    }
}