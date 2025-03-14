package org.example.lexer.impl

import org.example.lexer.Lexer
import org.example.token.Token

internal class LexerImpl : Lexer {
    override fun lex(symbols: Sequence<Char>): Sequence<Token> =
        sequence {
            val automaton = LexingAutomaton()

            symbols.forEach { yieldAll(automaton.process(it)) }

            yieldAll(automaton.process(Char(0)))
        }
}
