
package org.corebaseit.dukptksnkeygenerator

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class DukptApplication

fun main(args: Array<String>) {
    runApplication<DukptApplication>(*args) {
        when {
            args.isEmpty() -> {
                printUsage()
                return@runApplication
            }
            args.contains("--simulate") -> {
                runSimulation(args)
            }
            else -> {
                runKeyGeneration(args)
            }
        }
    }
}