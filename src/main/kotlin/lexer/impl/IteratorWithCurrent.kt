package org.example.lexer.impl

class IteratorWithCurrent<T>(private val it: Iterator<T>) : Iterator<T> by it {
    var current: T? = null
        private set

    override fun next(): T {
        current = it.next()
        return current ?: throw IllegalAccessException("iterator wasn't moved yet")
    }
}

fun <T> Iterator<T>.withCurrent() = IteratorWithCurrent(this)
