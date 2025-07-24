
package org.corebaseit.dukptksnkeygenerator

import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class DukptApplication : CommandLineRunner {
    override fun run(vararg args: String) {
        executeWithErrorHandling {
            processCommandLineArgs(args)
        }
    }

    private fun processCommandLineArgs(args: Array<out String>) {
        val effectiveArgs = if (args.isEmpty()) DEFAULT_ARGS else args
    }

    private fun executeWithErrorHandling(block: () -> Unit) {
        try {
            block()
        } catch (e: Exception) {
            System.err.println("Error: ${e.message}")
            System.exit(1)
        }
    }

    private fun getOptionValueSafe(args: Array<out String>, option: String, default: String): String {
        val index = args.indexOf(option)
        return if (index != -1 && index + 1 < args.size) {
            args[index + 1]
        } else {
            default
        }
    }

    companion object {
        private val DEFAULT_ARGS = arrayOf(
            "-k1", "0123456789ABCDEF0123456789ABCDEF",
            "-k2", "FEDCBA9876543210FEDCBA9876543210",
            "-k3", "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA00",
            "-k", "0123456789ABCDEF0123"
        )
    }
}

// This must be in a file named DukptApplication.kt
fun main(args: Array<String>) {
    runApplication<DukptApplication>(*args)
}