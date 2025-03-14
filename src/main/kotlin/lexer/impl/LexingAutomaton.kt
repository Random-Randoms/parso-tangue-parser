package org.example.lexer.impl

import org.example.token.Token

internal class LexingAutomaton {
    var accumulated = ""
    private var node: LexingAutomatonNode = Initial()

    fun switch(newNode: LexingAutomatonNode) {
        node = newNode
    }

    private fun flush() {
        accumulated = ""
    }

    fun process(symbol: Char): List<Token> {
        val tokens = mutableListOf<Token>()

        while (true) {
            accumulated += symbol
            val result = node.process(this, symbol)

            if (result.second != null) {
                flush()
                tokens.addLast(result.second)
            }

            if (result.first) {
                break
            }
        }

        return tokens
    }
}
