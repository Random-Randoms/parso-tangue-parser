package parser

import org.example.ast.*
import org.example.lexer.LexerFactory
import org.example.lexer.lexWith
import org.example.parser.ParserFactory
import org.example.parser.parseWith
import org.example.token.*
import org.junit.jupiter.api.assertTimeout
import util.loadSource
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.Duration.Companion.seconds
import kotlin.time.toJavaDuration

class ParserTests {
    fun testParse(
        source: String,
        expected: FileNode?,
    ) {
        val tokens = loadSource(source).lexWith(LexerFactory().createLexer())

        assertEquals(
            expected,
            tokens.parseWith(ParserFactory().createParser()),
        )
    }

    private val eqOp = OperatorIdentifier("==")
    private val assignOp = OperatorIdentifier("=")
    private val plusOp = OperatorIdentifier("+")
    private val minusOp = OperatorIdentifier("-")
    private val timesOp = OperatorIdentifier("*")
    private val divOp = OperatorIdentifier("/")
    private val iDivOp = OperatorIdentifier("%")
    private val ltOp = OperatorIdentifier("<")
    private val gtOp = OperatorIdentifier(">")
    private val leOp = OperatorIdentifier("<=")
    private val geOp = OperatorIdentifier(">=")
    private val plusAssOp = OperatorIdentifier("+=")

    private val varA = LetterIdentifier("a")
    private val varB = LetterIdentifier("b")
    private val funMain = LetterIdentifier("main")
    private val funFoo = LetterIdentifier("foo")

    private val typeInt = LetterIdentifier("Int")
    private val typeBool = LetterIdentifier("Boolean")
    private val typeString = LetterIdentifier("String")
    private val typeChar = LetterIdentifier("Char")
    private val typeVoid = LetterIdentifier("Void")

    private fun decLit(a: Int) = DecimalLiteral(a)

    private fun decLitExp(a: Int) = LiteralExpression(decLit(a))

    private fun strLit(string: String) = LiteralExpression(StringLiteral(string))

    private fun charLit(char: Char) = LiteralExpression(CharLiteral(char))

    private fun idExp(ident: LetterIdentifier) = IdentifierExpression(ident)

    private val expA = idExp(varA)
    private val expB = idExp(varB)

    private fun funCall(
        name: LetterIdentifier,
        args: List<Expression>,
    ) = FunctionCall(name, ArgumentList(args))

    private fun List<Statement>.block() = StatementBlock(this)

    private val noArgs = DeclarationArgumentList(listOf())

    private fun List<TypedArgument>.argList() = DeclarationArgumentList(this)

    private fun typeArg(
        name: LetterIdentifier,
        type: LetterIdentifier,
    ) = TypedArgument(name, type)

    private fun binExp(
        op: OperatorIdentifier,
        left: Expression,
        right: Expression,
    ) = BinaryOperatorExpression(op, left, right)

    private fun sumExp(
        left: Expression,
        right: Expression,
    ) = BinaryOperatorExpression(plusOp, left, right)

    private fun timesExp(
        left: Expression,
        right: Expression,
    ) = BinaryOperatorExpression(timesOp, left, right)

    private fun assignExp(
        left: Expression,
        right: Expression,
    ) = BinaryOperatorExpression(assignOp, left, right)

    private fun retExp(value: Expression) = ReturnStatement(value)

    @Test
    fun smoke_small() =
        testParse(
            "src/test/resources/small_parse_source.txt",
            FileNode(
                listOf(
                    FunctionDeclaration(
                        funMain,
                        noArgs,
                        typeInt,
                        listOf(ConstantDeclaration(varA, typeInt, decLitExp(0))).block(),
                    ),
                ),
            ),
        )

    @Test
    fun smoke_big() =
        testParse(
            "src/test/resources/big_parse_smoke.txt",
            FileNode(
                listOf(
                    ConstantDeclaration(
                        LetterIdentifier("c"),
                        LetterIdentifier("Int"),
                        decLitExp(0),
                    ),
                    VariableDeclaration(
                        LetterIdentifier("d"),
                        LetterIdentifier("Int"),
                        decLitExp(1),
                    ),
                    FunctionDeclaration(
                        LetterIdentifier("foo"),
                        noArgs,
                        typeInt,
                        listOf(
                            ConstantDeclaration(varA, typeInt, decLitExp(1)),
                            ConstantDeclaration(
                                varB,
                                typeBool,
                                BinaryOperatorExpression(
                                    gtOp,
                                    sumExp(
                                        sumExp(decLitExp(1), timesExp(decLitExp(2), decLitExp(3))),
                                        decLitExp(4),
                                    ),
                                    sumExp(
                                        LiteralExpression(BinaryLiteral(5)),
                                        BinaryOperatorExpression(
                                            iDivOp,
                                            timesExp(decLitExp(6), decLitExp(7)),
                                            decLitExp(8),
                                        ),
                                    ),
                                ),
                            ),
                            retExp(decLitExp(0)),
                        ).block(),
                    ),
                    FunctionDeclaration(
                        LetterIdentifier("bar"),
                        DeclarationArgumentList(listOf(TypedArgument(varB, typeInt))),
                        typeInt,
                        listOf(
                            VariableDeclaration(varA, typeInt, decLitExp(6)),
                            assignExp(idExp(varA), sumExp(idExp(varA), decLitExp(1))),
                            IfElseStatement(
                                BinaryOperatorExpression(
                                    eqOp,
                                    BinaryOperatorExpression(iDivOp, idExp(varA), decLitExp(2)),
                                    decLitExp(1),
                                ),
                                listOf(
                                    assignExp(
                                        idExp(varA),
                                        binExp(
                                            minusOp,
                                            sumExp(binExp(timesOp, expA, expA), idExp(varA)),
                                            binExp(
                                                divOp,
                                                timesExp(
                                                    timesExp(
                                                        idExp(varA),
                                                        funCall(funFoo, listOf(sumExp(expA, expA))),
                                                    ),
                                                    expA,
                                                ),
                                                sumExp(timesExp(expA, expA), funCall(funFoo, listOf(expA))),
                                            ),
                                        ),
                                    ),
                                ).block(),
                                listOf(retExp(idExp(varB))).block(),
                            ),
                            retExp(expA),
                        ).block(),
                    ),
                    FunctionDeclaration(
                        LetterIdentifier("bab"),
                        listOf(typeArg(varA, typeInt), typeArg(varB, typeInt)).argList(),
                        typeInt,
                        listOf(
                            IfStatement(
                                binExp(eqOp, sumExp(expA, expB), LiteralExpression(HexadecimalLiteral(170))),
                                listOf(retExp(decLitExp(1))).block(),
                            ),
                            retExp(decLitExp(0)),
                        ).block(),
                    ),
                    FunctionDeclaration(
                        LetterIdentifier("woo"),
                        listOf(typeArg(varA, typeString), typeArg(varB, typeChar)).argList(),
                        typeVoid,
                        listOf(
                            binExp(plusAssOp, idExp(varA), strLit("void\r\n")),
                            binExp(plusAssOp, idExp(varA), idExp(varB)),
                            binExp(plusAssOp, idExp(varA), charLit('v')),
                        ).block(),
                    ),
                    FunctionDeclaration(
                        funMain,
                        noArgs,
                        typeInt,
                        listOf(retExp(decLitExp(0))).block(),
                    ),
                ),
            ),
        )

    @Test
    fun performance() {
        assertTimeout(2.seconds.toJavaDuration()) {
            print(
                loadSource("src/test/resources/parse_long.txt")
                    .lexWith(LexerFactory().createLexer())
                    .parseWith(ParserFactory().createParser()),
            )
        }
    }
}
