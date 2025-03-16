package org.example.lexer.impl

internal fun Char.isLetter() = this in 'a'..'z' || this in 'A'..'Z'

internal fun Char.isDigit() = this in '0'..'9'

internal fun Char.isBinaryDigit() = this in '0'..'1'

internal fun Char.isHexadecimalDigit() = isDigit() || this in 'a'..'f' || this in 'A'..'F'

internal fun Char.isBinaryPrefix() = this == 'b' || this == 'B'

internal fun Char.isHexadecimalPrefix() = this == 'x' || this == 'X'

internal fun Char.isComma() = this == ','

internal fun Char.isLParen() = this == '('

internal fun Char.isRParen() = this == ')'

internal fun Char.isLCurl() = this == '{'

internal fun Char.isRCurl() = this == '}'

internal fun Char.isColon() = this == ':'

internal fun Char.isHashTag() = this == '#'

internal fun Char.isZero() = this == '0'

internal fun Char.isUnderscore() = this == '_'

internal fun Char.isOperatorSymbol() = this in listOf('-', '+', '*', '/', '%', '=', '>', '<')

internal fun Char.isCR() = this == '\r'

internal fun Char.isLF() = this == '\n'

internal fun Char.isWhiteSpace() = this in listOf(' ', '\t')

internal fun Char.isBackSlash() = this == '\\'

internal fun Char.isDoubleQuotes() = this == '"'

internal fun Char.isSingleQuotes() = this == '\''

internal fun Char.isEOF() = this == Char(0)

internal val backslashNotation =
    mapOf(
        'n' to '\n',
        'r' to '\r',
        '\\' to '\\',
        't' to '\t',
        'b' to '\b',
        '\'' to '\'',
        '"' to '"',
        '$' to '\$',
    )
