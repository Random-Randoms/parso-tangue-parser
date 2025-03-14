package org.example.lexer

import org.example.lexer.impl.LexerImpl

class LexerFactory {
    fun createLexer(): Lexer = LexerImpl()
}
