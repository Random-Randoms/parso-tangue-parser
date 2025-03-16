package org.example.parser.impl

import org.example.ast.*
import org.example.lexer.impl.IteratorWithCurrent
import org.example.lexer.impl.withCurrent
import org.example.parser.Parser
import org.example.token.*

typealias It = IteratorWithCurrent<Token>

private val assignmentOperator = OperatorIdentifier("=")

fun It.nextOrNull(): Token? {
    if (!hasNext()) return null
    return next()
}

fun It.nextIgnoreNL(): Token? {
    while (true) {
        when (nextOrNull()) {
            null -> return null
            is NewLine -> {}
            else -> return current
        }
    }
}

fun It.checkNextIs(token: Token): Unit? = if (nextOrNull() == token) Unit else null

fun It.checkNextIgnoreNL(token: Token): Boolean {
    return nextIgnoreNL() == token
}

fun It.parseFile(): FileNode {
    val entities = mutableListOf<TopLevelEntity>()

    while (true) entities.addLast(parseTopLevel() ?: return FileNode(entities))
}

fun It.parseTopLevel(): TopLevelEntity? =
    when (nextIgnoreNL()) {
        is ObjectDeclarationKeyword -> parseConstOrVarDeclaration()
        is FunKeyword -> parseFunctionDeclaration()
        else -> null
    }

fun It.parseConstOrVarDeclaration(): Statement? {
    val const: Boolean =
        when (current) {
            VarKeyword -> false
            ValKeyword -> true
            else -> return null
        }

    val name: LetterIdentifier =
        when (nextOrNull()) {
            is LetterIdentifier -> current as LetterIdentifier
            else -> return null
        }

    checkNextIs(Colon) ?: return null

    val type =
        when (nextOrNull()) {
            is LetterIdentifier -> current as LetterIdentifier
            else -> return null
        }

    checkNextIs(assignmentOperator) ?: return null
    nextOrNull() ?: return null

    val value = parseExpression() ?: return null

    return if (const) {
        ConstantDeclaration(name, type, value)
    } else {
        VariableDeclaration(name, type, value)
    }
}

fun It.parseFunctionDeclaration(): FunctionDeclaration? {
    val name =
        when (next()) {
            is LetterIdentifier -> current as LetterIdentifier
            else -> return null
        }

    checkNextIs(LeftParen) ?: return null

    val arguments = parseDeclarationArgumentList() ?: return null

    checkNextIs(Colon) ?: return null

    val returnType =
        when (nextIgnoreNL()) {
            is LetterIdentifier -> current as LetterIdentifier
            else -> return null
        }

    if (!checkNextIgnoreNL(LeftCurl)) return null

    val body = parseStatementBlock() ?: return null

    return FunctionDeclaration(name, arguments, returnType, body)
}

fun It.parseDeclarationArgumentList(): DeclarationArgumentList? {
    val arguments =
        mutableListOf(
            when (nextOrNull()) {
                is RightParen -> return DeclarationArgumentList(listOf())
                else -> parseTypedArgument() ?: return null
            },
        )

    while (true) {
        when (nextOrNull()) {
            is Comma -> nextOrNull() ?: return null
            is RightParen -> return DeclarationArgumentList(arguments)
            else -> return null
        }

        arguments.addLast(parseTypedArgument() ?: return null)
    }
}

fun It.parseTypedArgument(): TypedArgument? {
    val name =
        when (current) {
            is LetterIdentifier -> current as LetterIdentifier
            else -> return null
        }

    checkNextIs(Colon) ?: return null

    val type =
        when (nextOrNull()) {
            is LetterIdentifier -> current as LetterIdentifier
            else -> return null
        }

    return TypedArgument(name, type)
}

fun It.parseExpression(priority: Int = 0): Expression? {
    var accumulated = parseSymbol() ?: return null

    while (true) {
        val operator =
            when (current) {
                is OperatorIdentifier -> current as OperatorIdentifier
                Comma, NewLine, RightParen -> return accumulated
                else -> return null
            }
        if (operator.priority() <= priority) return accumulated
        nextOrNull() ?: return null
        accumulated =
            BinaryOperatorExpression(
                operator,
                accumulated,
                parseExpression(operator.priority()) ?: return null,
            )
    }
}

fun It.parseSymbol(): Expression? {
    when (val cur = current) {
        is Literal -> {
            nextOrNull() ?: return null
            return LiteralExpression(cur)
        }
        is LetterIdentifier -> {
            return when (nextOrNull()) {
                NewLine, Comma, RightParen, is OperatorIdentifier -> IdentifierExpression(cur)
                LeftParen -> {
                    nextOrNull() ?: return null
                    FunctionCall(cur, (parseArgumentList() ?: return null))
                }
                else -> null
            }
        }
        LeftParen -> {
            nextOrNull() ?: return null
            val result = parseExpression()
            nextOrNull() ?: return null
            return result
        }

        else -> return null
    }
}

fun It.parseStatement(): Statement? =
    when (current) {
        is ObjectDeclarationKeyword -> parseConstOrVarDeclaration()
        is ReturnKeyword -> parseReturnStatement()
        is IfKeyword -> parseConditionalStatement()
        else -> parseExpression()
    }

fun It.parseReturnStatement(): Statement? {
    nextOrNull() ?: return null
    val value = parseExpression() ?: return null

    return ReturnStatement(value)
}

fun It.parseConditionalStatement(): ConditionalStatement? {
    checkNextIs(LeftParen) ?: return null

    val condition = parseSymbol() ?: return null
    nextOrNull() ?: return null
    val bodyTrue = parseStatementBlock() ?: return null

    return when (nextOrNull()) {
        is ElseKeyword -> {
            nextOrNull() ?: return null
            IfElseStatement(condition, bodyTrue, parseStatementBlock() ?: return null)
        }
        else -> IfStatement(condition, bodyTrue)
    }
}

fun It.parseStatementBlock(): StatementBlock? {
    val statements = mutableListOf<Statement>()

    while (true) {
        when (nextOrNull()) {
            RightCurl -> return StatementBlock(statements)
            NewLine -> continue
            else -> statements.addLast(parseStatement() ?: return null)
        }
    }
}

fun It.parseArgumentList(): ArgumentList? {
    val arguments = mutableListOf(parseExpression() ?: return null)

    while (true) {
        when (current) {
            is Comma -> {}
            is RightParen -> {
                nextOrNull() ?: return null
                return ArgumentList(arguments)
            }
            else -> return null
        }

        nextOrNull() ?: return null
        arguments.addLast(parseExpression() ?: return null)
    }
}

internal class ParserImpl : Parser {
    override fun parse(tokens: Sequence<Token>): FileNode =
        tokens.filter { it !in listOf(WhiteSpace, UnknownSymbol, InlineComment) }.iterator().withCurrent().parseFile()
}
