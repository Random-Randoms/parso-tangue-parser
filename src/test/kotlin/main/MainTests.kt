package main

import org.example.main
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow

class MainTests {
    @Test
    fun testRuns() {
        assertDoesNotThrow {
            main(
                arrayOf(
                    "--source",
                    "src/test/resources/parse_long.txt",
                    "--output",
                    "src/test/resources/out.json",
                ),
            )
        }
    }
}
