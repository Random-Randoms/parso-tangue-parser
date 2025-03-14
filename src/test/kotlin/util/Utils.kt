package util

import java.io.File

internal fun loadSource(filename: String) =
    File(filename)
        .inputStream()
        .buffered()
        .iterator()
        .asSequence()
        .map { it.toInt().toChar() }
