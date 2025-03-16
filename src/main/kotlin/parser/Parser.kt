package org.example.parser

import org.example.ast.FileNode
import org.example.token.Token

interface Parser {
    fun parse(tokens: Sequence<Token>): FileNode
}

fun Sequence<Token>.parseWith(parser: Parser) = parser.parse(this)
