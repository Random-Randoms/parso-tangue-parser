package org.example.ast

import org.example.token.LetterIdentifier
import org.example.token.Literal
import org.example.token.OperatorIdentifier

sealed interface TopLevelEntity

sealed interface Statement : TopLevelEntity

sealed interface Expression : Statement

data class FileNode(val entities: List<TopLevelEntity>)

data class StatementBlock(
    val statements: List<Statement>,
)

data class VariableDeclaration(
    val name: LetterIdentifier,
    val type: LetterIdentifier,
    val value: Expression,
) : Statement

data class ConstantDeclaration(
    val name: LetterIdentifier,
    val type: LetterIdentifier,
    val value: Expression,
) : Statement

data class FunctionDeclaration(
    val name: LetterIdentifier,
    val arguments: DeclarationArgumentList,
    val returnType: LetterIdentifier,
    val body: StatementBlock,
) : TopLevelEntity

data class ReturnStatement(val value: Expression) : Statement

sealed interface ConditionalStatement : Statement

data class IfStatement(val condition: Expression, val body: StatementBlock) : ConditionalStatement

data class IfElseStatement(
    val condition: Expression,
    val bodyTrue: StatementBlock,
    val bodyFalse: StatementBlock,
) : ConditionalStatement

data class TypedArgument(val name: LetterIdentifier, val type: LetterIdentifier)

data class DeclarationArgumentList(val arguments: List<TypedArgument>)

data class ArgumentList(val arguments: List<Expression>)

data class FunctionCall(
    val function: LetterIdentifier,
    val arguments: ArgumentList,
) : Expression

data class BinaryOperatorExpression(
    val operator: OperatorIdentifier,
    val leftOperand: Expression,
    val rightOperand: Expression,
) : Expression

data class LiteralExpression(val literal: Literal) : Expression

data class IdentifierExpression(val name: LetterIdentifier) : Expression
