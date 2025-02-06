package algorithm.stack

/**
 * 占位符解析器
 * 解析{}中间的字符
 */
class PlaceholderParse(private val data: String) {
    companion object {
        const val LEFT = '{'
        const val RIGHT = '}'
    }

    private val symbolStack = ArrayAutoStack<SymbolData>()
    private val placeholderList = mutableListOf<String>()

    val placeholder: List<String>
        get() = placeholderList.toList()

    init {
        val charArray = data.toCharArray()
        charArray.forEachIndexed { index, c ->
            if (c == LEFT) {
                symbolStack.push(SymbolData(index))
            } else if (c == RIGHT) {
                val symbolData = symbolStack.pop()
                if (symbolData == null) {
                    throw IllegalArgumentException("非法的占位符")
                } else {
                    placeholderList.add(data.substring(symbolData.index + 1, index))
                    println("占位符：${data.substring(symbolData.index + 1, index)}")
                }
            }
        }
        if (symbolStack.pop() != null) {
            throw IllegalArgumentException("非法的占位符")
        }
    }


    private data class SymbolData(val index: Int)

}