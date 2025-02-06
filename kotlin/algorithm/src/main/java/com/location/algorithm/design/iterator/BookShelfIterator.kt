package com.location.algorithm.design.iterator

class BookShelfIterator(private val bookShelf: BookShelf) : Iterator<Book> {
    private var index = 0

    /**
     * 是否有下一个元素
     * 确认接下来是否可以调用next方法
     */
    override fun hasNext(): Boolean {
        return index < bookShelf.count
    }

    /**
     * 获取当前元素并将指针移动到下一个元素
     */
    override fun next(): Book {
        return bookShelf.getBookAt(index++)
    }
}