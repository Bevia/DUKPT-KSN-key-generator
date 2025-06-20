package org.corebaseit.dukptksnkeygenerator

import org.corebaseit.dukptksnkeygenerator.hsm.DukptSimulator

fun main(args: Array<String> = DEFAULT_ARGS) {

    try {
        when {
            args.isEmpty() -> {
                printUsage()
                return
            }
            args.contains("--simulate") -> {
                runSimulation(args)
            }

            else -> {
                runKeyGeneration(args)
            }
        }
    } catch (e: Exception) {
        System.err.println("Error: ${e.message}")
        System.exit(1)
    }
}

private val DEFAULT_ARGS = arrayOf(
    "-k1", "0123456789ABCDEF0123456789ABCDEF",
    "-k2", "FEDCBA9876543210FEDCBA9876543210",
    "-k3", "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA00",
    "-k", "0123456789ABCDEF0123"
)

fun printUsage() {
    println("""
        DUKPT Key Generator Tool Usage:
        
        1. Key Generation Mode:
           java -jar dukpt-generator.jar -k1 <key1> -k2 <key2> -k3 <key3> -k <ksn>
           
        2. Simulation Mode:
           java -jar dukpt-generator.jar --simulate [options]
           
           Simulation Options:
           --pin <pin>      Custom PIN (default: 1234)
           --pan <pan>      Custom PAN (default: 4532111122223333)
           
        Examples:
           java -jar dukpt-generator.jar -k1 0123456789ABCDEF0123456789ABCDEF -k2 FEDCBA9876543210FEDCBA9876543210 -k3 AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA00 -k 0123456789ABCDEF0123
           java -jar dukpt-generator.jar --simulate
           java -jar dukpt-generator.jar --simulate --pin 5678 --pan 4111111111111111
    """.trimIndent())
}

fun runKeyGeneration(args: Array<String>) {
    DukptCliTool(args).run()
}

fun runSimulation(args: Array<String>) {
    val pin = args.getOptionValue("--pin", "1234")
    val pan = args.getOptionValue("--pan", "4532111122223333")

    println("\n=== Running PIN Encryption/Decryption Simulation ===\n")
    DukptSimulator().runSimulation(pin, pan)
}

private fun Array<String>.getOptionValue(option: String, default: String): String {
    val index = this.indexOf(option)
    return if (index != -1 && index + 1 < this.size) {
        this[index + 1]
    } else {
        default
    }
}