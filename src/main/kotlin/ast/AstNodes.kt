package org.example.ast

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.example.token.LetterIdentifier
import org.example.token.Literal
import org.example.token.OperatorIdentifier

@Serializable
sealed interface AstNode

@Serializable
sealed interface TopLevelEntity : AstNode

@Serializable
sealed interface Statement : TopLevelEntity

@Serializable
sealed interface Expression : Statement

@Serializable
@SerialName("FileNode")
data class FileNode(val entities: List<TopLevelEntity>) : AstNode

@Serializable
@SerialName("Statement block")
data class StatementBlock(
    val statements: List<Statement>,
) : AstNode

@Serializable
@SerialName("Variable declaration")
data class VariableDeclaration(
    val name: LetterIdentifier,
    @SerialName("varType")
    val type: LetterIdentifier,
    val value: Expression,
) : Statement

@Serializable
@SerialName("Constant declaration")
data class ConstantDeclaration(
    val name: LetterIdentifier,
    @SerialName("valType")
    val type: LetterIdentifier,
    val value: Expression,
) : Statement

@Serializable
@SerialName("Function declaration")
data class FunctionDeclaration(
    val name: LetterIdentifier,
    val arguments: DeclarationArgumentList,
    val returnType: LetterIdentifier,
    val body: StatementBlock,
) : TopLevelEntity

@Serializable
@SerialName("Return statement")
data class ReturnStatement(val value: Expression) : Statement

@Serializable
sealed interface ConditionalStatement : Statement

@Serializable
@SerialName("If statement")
data class IfStatement(val condition: Expression, val body: StatementBlock) : ConditionalStatement

@Serializable
@SerialName("If else statement")
data class IfElseStatement(
    val condition: Expression,
    val bodyTrue: StatementBlock,
    val bodyFalse: StatementBlock,
) : ConditionalStatement

@Serializable
@SerialName("Typed argument")
data class TypedArgument(val name: LetterIdentifier, val type: LetterIdentifier) : AstNode

@Serializable
@SerialName("Declaration argument list")
data class DeclarationArgumentList(val arguments: List<TypedArgument>) : AstNode

@Serializable
@SerialName("Argument list")
data class ArgumentList(val arguments: List<Expression>)

@Serializable
@SerialName("Function call")
data class FunctionCall(
    val function: LetterIdentifier,
    val arguments: ArgumentList,
) : Expression

@Serializable
@SerialName("Binary operator expression")
data class BinaryOperatorExpression(
    val operator: OperatorIdentifier,
    val leftOperand: Expression,
    val rightOperand: Expression,
) : Expression

@Serializable
@SerialName("Literal expression")
data class LiteralExpression(val literal: Literal) : Expression

@Serializable
@SerialName("Identifier expression")
data class IdentifierExpression(val name: LetterIdentifier) : Expression
