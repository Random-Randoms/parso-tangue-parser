package org.example.token

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Interface for ParsoTangue tokens
 */
@Serializable
sealed interface Token

/**
 * Letter identifier token
 *
 * Consists of nonempty string of letters, digits or underscores,
 * starting with letter or underscore
 *
 * @property string name of the identifier
 */
@Serializable
data class LetterIdentifier(val string: String) : Token

/**
 * Letter identifier token
 *
 * Consists of nonempty string of operator symbols: "+", "-", "*", "/", "%", "=", ">", "<"
 *
 * @property string name of the identifier
 */
@Serializable
data class OperatorIdentifier(val string: String) : Token

/**
 * Literal token
 */
@Serializable
sealed interface Literal : Token

/**
 * Integer literal token
 *
 * @property value the represented value
 */
@Serializable
sealed interface IntegerLiteral : Literal {
    val value: Int
}

/**
 * Binary literal token
 *
 * Consists of string of zeroes or ones, prepended with ```0b``` or ```0B``` prefix
 *
 * Represents non-negative integer with corresponding binary form
 */
@Serializable
@SerialName("BinaryLiteral")
data class BinaryLiteral(override val value: Int) : IntegerLiteral

/**
 * Decimal literal token
 *
 * Consists of string of zeroes or ones, possibly prepended with a minus
 */
@Serializable
@SerialName("DecimalLiteral")
data class DecimalLiteral(override val value: Int) : IntegerLiteral

/**
 * Hexadecimal literal token
 *
 * Consists of string of hexadecimal digits (decimal digits and letters from ```a``` to ```f``` inclusively),
 * prepended with ```0x``` or ```0X``` prefix
 *
 * Represents non-negative integer with corresponding binary form
 */
@Serializable
@SerialName("HexadecimalLiteral")
data class HexadecimalLiteral(override val value: Int) : IntegerLiteral

/**
 * String literal token.
 *
 * Consists of string of any symbols (but newline, double quotes), surrounded by double quotes
 *
 * Represents a string
 */
@Serializable
@SerialName("StringLiteral")
data class StringLiteral(val value: String) : Literal

/**
 * Bad string literal token
 *
 * Consists of a malformed string literal --- not having en enclosing double quote on the same line
 */
@Serializable
@SerialName("BadStringLiteral")
data object BadStringLiteral : Literal

/**
 * Char literal token.
 *
 * Consists of a single character, surrounded by single quotes
 *
 * Represents a char
 */
@Serializable
@SerialName("CharLiteral")
data class CharLiteral(val value: Char) : Literal

/**
 * Bad char literal token
 *
 * Consists of a string, surrounded by single quotes, containing incorrect char:
 * no symbols, more than two symbols, two symbols in incorrect backslash notation
 */
@Serializable
@SerialName("BadCharLiteral")
data object BadCharLiteral : Literal

/**
 * Inline comment token
 *
 * Consists of a string, prepended with a ```#``` symbol
 */
data object InlineComment : Token

/**
 * Whitespace token
 *
 * Consists of whitespace characters: spaces and tabs
 */
data object WhiteSpace : Token

/**
 * Newline token
 *
 * LF or CRLF
 */
data object NewLine : Token

data object FunKeyword : Token

data object ReturnKeyword : Token

sealed interface ObjectDeclarationKeyword : Token

data object ValKeyword : ObjectDeclarationKeyword

data object VarKeyword : ObjectDeclarationKeyword

data object IfKeyword : Token

data object ElseKeyword : Token

data object LeftCurl : Token

data object RightCurl : Token

data object LeftParen : Token

data object RightParen : Token

data object Colon : Token

data object Comma : Token

data object EOF : Token

data object UnknownSymbol : Token
