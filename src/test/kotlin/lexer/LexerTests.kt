package lexer

import org.example.lexer.LexerFactory
import org.example.lexer.lexWith
import org.example.token.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import util.loadSource

class LexerTests {
    private fun testLexing(
        source: String,
        expected: List<Token>,
    ) {
        val actual = loadSource(source).lexWith(LexerFactory().createLexer()).toList()

        assertEquals(
            expected,
            actual,
            "different lexing results",
        )
    }

    @Test
    fun smoke_small() {
        testLexing(
            "src/test/resources/small_lex_source.txt",
            listOf(
                LetterIdentifier("a"),
                WhiteSpace,
                OperatorIdentifier("+"),
                WhiteSpace,
                LeftParen,
                LetterIdentifier("b"),
                WhiteSpace,
                OperatorIdentifier("*"),
                WhiteSpace,
                LetterIdentifier("c"),
                RightParen,
                NewLine,
                EOF,
            ),
        )
    }

    @Test
    fun smoke() {
        testLexing(
            "src/test/resources/test_source.txt",
            listOf(
                FunKeyword,
                WhiteSpace,
                LetterIdentifier("factorial"),
                LeftParen,
                LetterIdentifier("a"),
                Colon,
                WhiteSpace,
                LetterIdentifier("Int"),
                RightParen,
                WhiteSpace,
                LeftCurl,
                NewLine,
                WhiteSpace,
                IfKeyword,
                WhiteSpace,
                LeftParen,
                LetterIdentifier("a"),
                WhiteSpace,
                OperatorIdentifier("=="),
                WhiteSpace,
                DecimalLiteral(0),
                RightParen,
                WhiteSpace,
                ReturnKeyword,
                WhiteSpace,
                DecimalLiteral(1),
                NewLine,
                WhiteSpace,
                NewLine,
                WhiteSpace,
                ReturnKeyword,
                WhiteSpace,
                LetterIdentifier("a"),
                WhiteSpace,
                OperatorIdentifier("*"),
                WhiteSpace,
                LetterIdentifier("factorial"),
                LeftParen,
                LetterIdentifier("a"),
                WhiteSpace,
                OperatorIdentifier("-"),
                WhiteSpace,
                BinaryLiteral(1),
                RightParen,
                NewLine,
                RightCurl,
                NewLine,
                NewLine,
                FunKeyword,
                WhiteSpace,
                LetterIdentifier("main"),
                LeftParen,
                RightParen,
                WhiteSpace,
                LeftCurl,
                NewLine,
                WhiteSpace,
                ValKeyword,
                WhiteSpace,
                LetterIdentifier("abc"),
                WhiteSpace,
                OperatorIdentifier("="),
                WhiteSpace,
                HexadecimalLiteral(10_486_027),
                NewLine,
                WhiteSpace,
                ValKeyword,
                WhiteSpace,
                LetterIdentifier("_gg0"),
                WhiteSpace,
                OperatorIdentifier("="),
                WhiteSpace,
                LetterIdentifier("factorial"),
                LeftParen,
                DecimalLiteral(10),
                RightParen,
                WhiteSpace,
                InlineComment,
                NewLine,
                NewLine,
                WhiteSpace,
                LetterIdentifier("abc"),
                WhiteSpace,
                OperatorIdentifier("="),
                WhiteSpace,
                LeftParen,
                LetterIdentifier("abc"),
                OperatorIdentifier("+"),
                LetterIdentifier("_gg0"),
                RightParen,
                OperatorIdentifier("/"),
                BinaryLiteral(2),
                NewLine,
                NewLine,
                WhiteSpace,
                ReturnKeyword,
                WhiteSpace,
                DecimalLiteral(0),
                NewLine,
                RightCurl,
                NewLine,
                EOF,
            ),
        )
    }

    @Test
    fun testEverything() =
        testLexing(
            "src/test/resources/lex_test.txt",
            listOf(
                FunKeyword,
                WhiteSpace,
                ValKeyword,
                WhiteSpace,
                IfKeyword,
                WhiteSpace,
                ElseKeyword,
                WhiteSpace,
                LetterIdentifier("funval"),
                Comma,
                LetterIdentifier("fun0"),
                Comma,
                DecimalLiteral(0),
                ElseKeyword,
                NewLine,
                CharLiteral('a'),
                WhiteSpace,
                CharLiteral('\n'),
                WhiteSpace,
                BadCharLiteral,
                WhiteSpace,
                CharLiteral('j'),
                CharLiteral('v'),
                WhiteSpace,
                BadCharLiteral,
                BadCharLiteral,
                NewLine,
                StringLiteral("ololo"),
                WhiteSpace,
                StringLiteral("ol\r\n"),
                WhiteSpace,
                StringLiteral("\""),
                WhiteSpace,
                BadStringLiteral,
                NewLine,
                LetterIdentifier("a"),
                WhiteSpace,
                OperatorIdentifier("++"),
                WhiteSpace,
                OperatorIdentifier("+"),
                WhiteSpace,
                LetterIdentifier("b"),
                OperatorIdentifier("+++"),
                WhiteSpace,
                OperatorIdentifier("++"),
                LetterIdentifier("v"),
                WhiteSpace,
                OperatorIdentifier("-==+"),
                WhiteSpace,
                LetterIdentifier("g"),
                Colon,
                Colon,
                LetterIdentifier("g"),
                WhiteSpace,
                OperatorIdentifier("+"),
                LetterIdentifier("_"),
                OperatorIdentifier("+"),
                WhiteSpace,
                LetterIdentifier("__001"),
                WhiteSpace,
                OperatorIdentifier("+"),
                WhiteSpace,
                LetterIdentifier("_llp"),
                NewLine,
                DecimalLiteral(110),
                WhiteSpace,
                DecimalLiteral(16),
                WhiteSpace,
                DecimalLiteral(771),
                WhiteSpace,
                DecimalLiteral(3),
                NewLine,
                LeftCurl,
                RightParen,
                LeftParen,
                RightCurl,
                WhiteSpace,
                LeftParen,
                OperatorIdentifier("+"),
                LeftParen,
                RightParen,
                OperatorIdentifier("-"),
                RightCurl,
                WhiteSpace,
                LetterIdentifier("kk0"),
                WhiteSpace,
                DecimalLiteral(0),
                LetterIdentifier("kk"),
                WhiteSpace,
                LetterIdentifier("hhh"),
                OperatorIdentifier("-"),
                DecimalLiteral(1),
                NewLine,
                BinaryLiteral(0),
                WhiteSpace,
                HexadecimalLiteral(0),
                WhiteSpace,
                BinaryLiteral(0),
                WhiteSpace,
                HexadecimalLiteral(0),
                WhiteSpace,
                HexadecimalLiteral(2570),
                WhiteSpace,
                BinaryLiteral(2),
                WhiteSpace,
                BinaryLiteral(1),
                WhiteSpace,
                BinaryLiteral(3),
                DecimalLiteral(2),
                WhiteSpace,
                BinaryLiteral(1),
                LetterIdentifier("b1"),
                NewLine,
                BadCharLiteral,
                EOF,
            ),
        )
}
