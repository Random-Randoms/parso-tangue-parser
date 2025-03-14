package org.example.lexer

import org.example.token.Token

interface Lexer {
    /**
     * Processes symbols sequence into sequence of tokens
     */
    fun lex(symbols: Sequence<Char>): Sequence<Token>
}

/**
 * Extension function for lexing with some lexer
 *
 * @returns sequence of tokens
 */
fun Sequence<Char>.lexWith(lexer: Lexer) = lexer.lex(this)
