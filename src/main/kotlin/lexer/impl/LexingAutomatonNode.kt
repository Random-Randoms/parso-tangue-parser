package org.example.lexer.impl

import org.example.token.*
import org.example.token.BinaryLiteral
import org.example.token.DecimalLiteral
import org.example.token.InlineComment
import org.example.token.StringLiteral
import org.example.token.WhiteSpace

/**
 * Lexing automaton node abstraction
 */
internal interface LexingAutomatonNode {
    /**
     * Processes one symbol, possibly yielding a token to the SequenceScope
     *
     * @return ```true``` if it consumes the symbol, ```false``` otherwise
     */
    fun process(
        automaton: LexingAutomaton,
        symbol: Char,
    ): Pair<Boolean, Token?>
}

private fun consume() = true to null

private fun LexingAutomaton.ignore(): Pair<Boolean, Token?> {
    accumulated = accumulated.dropLast(1)
    return true to null
}

private fun consumeAndEmit(token: Token): Pair<Boolean, Token?> {
    return true to token
}

private fun LexingAutomaton.consumeEmitAndSwitch(
    token: Token,
    node: LexingAutomatonNode,
): Pair<Boolean, Token?> {
    switch(node)
    return true to token
}

private fun LexingAutomaton.retainAndEmit(token: Token): Pair<Boolean, Token?> {
    switch(Initial())
    return false to token
}

private fun LexingAutomaton.consumeAndSwitch(node: LexingAutomatonNode): Pair<Boolean, Token?> {
    switch(node)
    return true to null
}

private fun LexingAutomaton.filteredIntegerLiteral(cutPrefix: Boolean = true): String =
    accumulated.filter { it != '_' }.dropLast(1).drop(if (cutPrefix) 2 else 0).run {
        ifEmpty { return "0" }
        return this
    }

internal class Initial : LexingAutomatonNode {
    override fun process(
        automaton: LexingAutomaton,
        symbol: Char,
    ): Pair<Boolean, Token?> {
        if (symbol.isLetter() || symbol.isUnderscore()) {
            return automaton.consumeAndSwitch(LetterIdentifier())
        }

        if (symbol.isZero()) {
            return automaton.consumeAndSwitch(ZeroSymbol())
        }

        if (symbol.isDigit()) {
            return automaton.consumeAndSwitch(DecimalLiteral())
        }

        if (symbol.isOperatorSymbol()) {
            return automaton.consumeAndSwitch(OperatorIdentifier())
        }

        if (symbol.isWhiteSpace()) {
            return automaton.consumeAndSwitch(WhiteSpace())
        }

        if (symbol.isHashTag()) {
            return automaton.consumeAndSwitch(InlineComment())
        }

        if (symbol.isLParen()) {
            return consumeAndEmit(LeftParen)
        }

        if (symbol.isSingleQuotes()) {
            return automaton.consumeAndSwitch(CharLiteralBegin())
        }

        if (symbol.isDoubleQuotes()) {
            return automaton.consumeAndSwitch(StringLiteral())
        }

        if (symbol.isRParen()) {
            return consumeAndEmit(RightParen)
        }

        if (symbol.isLCurl()) {
            return consumeAndEmit(LeftCurl)
        }

        if (symbol.isRCurl()) {
            return consumeAndEmit(RightCurl)
        }

        if (symbol.isColon()) {
            return consumeAndEmit(Colon)
        }

        if (symbol.isComma()) {
            return consumeAndEmit(Comma)
        }

        if (symbol.isCR()) {
            return automaton.ignore()
        }

        if (symbol.isLF()) {
            return consumeAndEmit(NewLine)
        }

        if (symbol.isEOF()) {
            return consumeAndEmit(EOF)
        }

        return consumeAndEmit(UnknownSymbol)
    }
}

internal class CharLiteralBegin : LexingAutomatonNode {
    override fun process(
        automaton: LexingAutomaton,
        symbol: Char,
    ): Pair<Boolean, Token?> {
        if (symbol.isSingleQuotes()) {
            return consumeAndEmit(BadCharLiteral)
        }

        if (symbol.isLF() || symbol.isEOF()) {
            return automaton.retainAndEmit(BadCharLiteral)
        }

        if (symbol.isBackSlash()) {
            return automaton.consumeAndSwitch(CharLiteralSlash())
        }

        return automaton.consumeAndSwitch(CharLiteralEnd())
    }
}

internal class CharLiteralEnd : LexingAutomatonNode {
    override fun process(
        automaton: LexingAutomaton,
        symbol: Char,
    ): Pair<Boolean, Token?> {
        if (symbol.isSingleQuotes()) {
            return automaton.consumeEmitAndSwitch(CharLiteral(automaton.accumulated[1]), Initial())
        }

        if (symbol.isLF() || symbol.isEOF()) {
            return automaton.retainAndEmit(BadCharLiteral)
        }

        return automaton.consumeAndSwitch(CharLiteralIncorrect())
    }
}

internal class CharLiteralSlash : LexingAutomatonNode {
    override fun process(
        automaton: LexingAutomaton,
        symbol: Char,
    ): Pair<Boolean, Token?> {
        if (backslashNotation[symbol] != null) {
            automaton.accumulated = automaton.accumulated.dropLast(2) + backslashNotation[symbol]
            return automaton.consumeAndSwitch(CharLiteralEnd())
        }

        if (symbol.isLF() || symbol.isEOF()) {
            return automaton.retainAndEmit(BadCharLiteral)
        }

        return automaton.consumeAndSwitch(CharLiteralIncorrect())
    }
}

internal class CharLiteralIncorrect : LexingAutomatonNode {
    override fun process(
        automaton: LexingAutomaton,
        symbol: Char,
    ): Pair<Boolean, Token?> {
        if (symbol.isSingleQuotes()) {
            return automaton.consumeEmitAndSwitch(BadCharLiteral, Initial())
        }

        if (symbol.isLF() || symbol.isEOF()) {
            return automaton.retainAndEmit(BadCharLiteral)
        }

        return consume()
    }
}

internal class StringLiteral : LexingAutomatonNode {
    override fun process(
        automaton: LexingAutomaton,
        symbol: Char,
    ): Pair<Boolean, Token?> {
        if (symbol.isBackSlash()) {
            return automaton.consumeAndSwitch(SlashInString())
        }

        if (symbol.isLF() || symbol.isEOF()) {
            return automaton.retainAndEmit(BadStringLiteral)
        }

        if (symbol.isDoubleQuotes()) {
            return automaton.consumeEmitAndSwitch(
                StringLiteral(automaton.accumulated.drop(1).dropLast(1)),
                Initial(),
            )
        }

        return consume()
    }
}

internal class SlashInString : LexingAutomatonNode {
    override fun process(
        automaton: LexingAutomaton,
        symbol: Char,
    ): Pair<Boolean, Token?> {
        if (backslashNotation[symbol] != null) {
            automaton.accumulated = automaton.accumulated.dropLast(2) + backslashNotation[symbol]
            return automaton.consumeAndSwitch(StringLiteral())
        }

        if (symbol.isLF() || symbol.isEOF()) {
            return automaton.retainAndEmit(BadStringLiteral)
        }

        return consume()
    }
}

internal class InlineComment : LexingAutomatonNode {
    override fun process(
        automaton: LexingAutomaton,
        symbol: Char,
    ): Pair<Boolean, Token?> {
        if (symbol.isLF() || symbol.isEOF()) {
            return automaton.retainAndEmit(InlineComment)
        }

        return consume()
    }
}

internal class WhiteSpace : LexingAutomatonNode {
    override fun process(
        automaton: LexingAutomaton,
        symbol: Char,
    ): Pair<Boolean, Token?> {
        if (symbol.isWhiteSpace()) {
            return consume()
        }

        return automaton.retainAndEmit(WhiteSpace)
    }
}

internal class OperatorIdentifier : LexingAutomatonNode {
    override fun process(
        automaton: LexingAutomaton,
        symbol: Char,
    ): Pair<Boolean, Token?> {
        if (symbol.isOperatorSymbol() || symbol.isEOF()) {
            return consume()
        }

        return automaton.retainAndEmit(OperatorIdentifier(automaton.accumulated.dropLast(1)))
    }
}

internal class LetterIdentifier : LexingAutomatonNode {
    override fun process(
        automaton: LexingAutomaton,
        symbol: Char,
    ): Pair<Boolean, Token?> {
        if (symbol.isLetter() || symbol.isUnderscore() || symbol.isDigit()) {
            return consume()
        }

        return automaton.retainAndEmit(
            keywords[automaton.accumulated.dropLast(1)] ?: LetterIdentifier(automaton.accumulated.dropLast(1)),
        )
    }
}

internal class ZeroSymbol : LexingAutomatonNode {
    override fun process(
        automaton: LexingAutomaton,
        symbol: Char,
    ): Pair<Boolean, Token?> {
        if (symbol.isBinaryPrefix()) {
            return automaton.consumeAndSwitch(BinaryLiteral())
        }

        if (symbol.isHexadecimalPrefix()) {
            return automaton.consumeAndSwitch(HexadecimalLiteral())
        }

        if (symbol.isDigit() || symbol.isUnderscore()) {
            return automaton.consumeAndSwitch(DecimalLiteral())
        }

        return automaton.retainAndEmit(DecimalLiteral(0))
    }
}

internal class BinaryLiteral : LexingAutomatonNode {
    override fun process(
        automaton: LexingAutomaton,
        symbol: Char,
    ): Pair<Boolean, Token?> {
        if (symbol.isBinaryDigit() || symbol.isUnderscore()) {
            return consume()
        }

        return automaton.retainAndEmit(BinaryLiteral(automaton.filteredIntegerLiteral().toInt(2)))
    }
}

internal class DecimalLiteral : LexingAutomatonNode {
    override fun process(
        automaton: LexingAutomaton,
        symbol: Char,
    ): Pair<Boolean, Token?> {
        if (symbol.isDigit() || symbol.isUnderscore()) {
            return consume()
        }

        return automaton.retainAndEmit(DecimalLiteral(automaton.filteredIntegerLiteral(false).toInt(10)))
    }
}

internal class HexadecimalLiteral : LexingAutomatonNode {
    override fun process(
        automaton: LexingAutomaton,
        symbol: Char,
    ): Pair<Boolean, Token?> {
        if (symbol.isHexadecimalDigit() || symbol.isUnderscore()) {
            return consume()
        }

        return automaton.retainAndEmit(HexadecimalLiteral(automaton.filteredIntegerLiteral().toInt(16)))
    }
}
