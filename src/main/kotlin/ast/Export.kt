package org.example.ast

import kotlinx.serialization.json.Json

private val json = Json { prettyPrint = true }

fun AstNode.exportAsJson(): String = json.encodeToString(this)
