package org.example.parser

import org.example.parser.impl.ParserImpl

class ParserFactory {
    fun createParser(): Parser = ParserImpl()
}
