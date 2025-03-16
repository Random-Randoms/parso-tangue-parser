package org.example.ast

import org.example.token.OperatorIdentifier

private val priorities =
    mapOf(
        OperatorIdentifier("=") to 1,
        OperatorIdentifier(">") to 2,
        OperatorIdentifier("<") to 2,
        OperatorIdentifier(">=") to 2,
        OperatorIdentifier("<=") to 2,
        OperatorIdentifier("==") to 2,
        OperatorIdentifier("!=") to 2,
        OperatorIdentifier("+") to 3,
        OperatorIdentifier("-") to 3,
        OperatorIdentifier("*") to 4,
        OperatorIdentifier("/") to 4,
        OperatorIdentifier("%") to 4,
    )

private const val UNKNOWN_OPERATOR_PRIORITY = 1

internal fun OperatorIdentifier.priority() = priorities[this] ?: UNKNOWN_OPERATOR_PRIORITY
