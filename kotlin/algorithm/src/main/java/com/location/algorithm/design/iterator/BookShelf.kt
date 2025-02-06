package com.location.algorithm.design.iterator

class BookShelf(private val size:Int): Aggregate<Book> {
    private var bookArray:Array<Book?> = arrayOfNulls(size)
    private var index = 0
    val count:Int
        get() = index


    fun getBookAt(index:Int): Book {
        if(index >= size) {
            throw IndexOutOfBoundsException("index is out of bounds")
        }
        return bookArray[index]!!
    }

    fun add(book: Book){
        if(index >= size){
            throw Exception("书架已满")
        }
        bookArray[index++] = book
    }





    override fun iterator(): Iterator<Book> = BookShelfIterator(this)

}