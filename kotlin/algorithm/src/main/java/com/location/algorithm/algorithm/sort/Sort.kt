package algorithm.sort


fun bubblingSort(array: Array<Int>) {
    for (i in array.indices) {
        println("i:${i} end : ${array.size - i - 1}")
        for (j in 0 until array.size - i - 1) {
            if (array[j] > array[j + 1]) {
                val temp = array[j]
                array[j] = array[j + 1]
                array[j + 1] = temp
            }
        }
    }
}


fun bubblingSort2(array: Array<Int>) {
    for (i in array.indices) {
        println("i:${i} end : ${array.size - i - 1}")
        for (j in array.size - 1 downTo i + 1) {
            if (array[j] < array[j - 1]) {
                val temp = array[j]
                array[j] = array[j - 1]
                array[j - 1] = temp
            }
        }
    }
}


fun main() {
    val array = arrayOf(5, 4, 2, 3, 1)
    bubblingSort2(array)
    println(array.contentToString())
}