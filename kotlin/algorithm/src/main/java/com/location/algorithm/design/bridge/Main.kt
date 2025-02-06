package com.location.algorithm.design.bridge

/**
 * 桥接模式
 * 将抽象部分与实现部分分离，使它们都可以独立的变化
 * 优点：
 * 1.分离抽象接口及其实现部分
 * 2.桥接模式有时类似于多继承方案，但是多继承方案违背了类的单一职责原则（即一个类只有一个变化的原因），
 * 复用性比较差，而且多继承结构中类的个数非常庞大，桥接模式是比多继承方案更好的解决方法
 * 3.桥接模式提高了系统的可扩展性，在两个变化维度中的任何一个扩展都不需要修改原有系统
 * 4.实现细节对客户透明，可以对用户隐藏实现细节
 * 缺点：
 * 1.桥接模式的引入会增加系统的理解与设计难度
 * 2.由于聚合关系建立在抽象层，要求开发者针对抽象化进行设计与编程
 * 3.桥接模式要求正确识别出系统中两个独立变化的维度，因此其使用范围有一定的局限性
 * 4.如果一个系统需要在抽象化和具体化之间增加更多的灵活性，桥接模式有可能会使事情更复杂
 * 5.对于“多维度的变化”设计，桥接模式是比多层继承方案更好的解决方法
 *
 *
 */
fun main() {
    val d1 = Display(StringDisplayImpl("Hello, China."))
    val d2 = CountDisplay(StringDisplayImpl("Hello, World."))
    val d3 = CountDisplay(StringDisplayImpl("Hello, Universe."))
    d1.display()
    d2.display()
    d3.display()
    d3.multiDisplay(5)
}