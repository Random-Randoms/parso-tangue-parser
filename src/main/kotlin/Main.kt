package org.example

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.main
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.required
import org.example.ast.exportAsJson
import org.example.lexer.LexerFactory
import org.example.lexer.lexWith
import org.example.parser.ParserFactory
import org.example.parser.parseWith
import java.io.File

class App : CliktCommand() {
    private val source by option("-s", "--source", help = "source file").required()
    private val output by option("-o", "--output", help = "output file").required()

    override fun run() {
        File(output)
            .apply { if (!exists()) createNewFile() }
            .outputStream().bufferedWriter().use {
                it.write(
                    File(source)
                        .apply {
                            if (!exists()) return
                        }
                        .inputStream()
                        .buffered()
                        .iterator()
                        .asSequence()
                        .map { b -> b.toInt().toChar() }
                        .lexWith(LexerFactory().createLexer())
                        .parseWith(ParserFactory().createParser())
                        .exportAsJson(),
                )
                it.close()
            }
    }
}

fun main(args: Array<String>) = App().main(args)
