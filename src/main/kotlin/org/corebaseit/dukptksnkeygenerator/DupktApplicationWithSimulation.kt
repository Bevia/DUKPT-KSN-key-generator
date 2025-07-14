package org.corebaseit.dukptksnkeygenerator

import org.corebaseit.dukptksnkeygenerator.transactionsimulator.DukptEngine
import org.corebaseit.dukptksnkeygenerator.transactionsimulator.TransactionSimulator
import org.corebaseit.dukptksnkeygenerator.utils.DukptCliTool
import org.corebaseit.dukptksnkeygenerator.utils.HexUtils
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class DukptApplicationWithSimulation : CommandLineRunner {
    override fun run(vararg args: String) {
        executeWithErrorHandling {
            processCommandLineArgs(args)
        }
    }

    private fun processCommandLineArgs(args: Array<out String>) {
        val effectiveArgs = if (args.isEmpty()) DEFAULT_ARGS else args

        when {
            effectiveArgs.contains("--simulate") -> runSimulation(effectiveArgs)
            else -> DukptCliTool(effectiveArgs.copyOf() as Array<String>).run()
        }
    }

    private fun runSimulation(args: Array<out String>) {
        // Only keep arguments that DukptCliTool expects
        val cliToolArgs = args.filterIndexed { index, arg ->
            // Keep the key options and their values
            listOf("-k1", "-k2", "-k3", "-k").contains(arg) ||
                    (index > 0 && listOf("-k1", "-k2", "-k3", "-k").contains(args[index - 1]))
        }.toTypedArray()

        // Parse keys using DukptCliTool
        val tool = DukptCliTool(cliToolArgs)
        tool.run()

        // Extract the extra simulation args (not used by DukptCliTool)
        val pan = getOptionValueSafe(args, "--pan", "4000000000000002")
        val pinBlockHex = getOptionValueSafe(args, "--pin", "1234")

        //val clearPinBlock = HexUtils.hexToBytes(pinBlockHex)

        // Get BDK for simulator
        val engine = DukptEngine(tool.bdk)

        //Start simulating
        val simulator = TransactionSimulator(engine, tool.ksn)
        val tx = simulator.simulateTransaction(pan, pinBlockHex)

        println("===== Simulated Transaction =====")
        println("Amount: ${tx.amount}")
        println("Card PAN: ${tx.cardPAN}")
        println("PIN Block (clear): ${HexUtils.bytesToHex(tx.pinBlock)}")
        println("PIN Block (encrypted): ${HexUtils.bytesToHex(tx.encryptedPinBlock)}")
        println("Derived Key: ${HexUtils.bytesToHex(tx.derivedKey)}")
        println("KSN Used: ${HexUtils.bytesToHex(tx.ksnUsed)}")
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

// This must be in a file named DukptApplicationWithSimulation.kt
fun main(args: Array<String>) {
    runApplication<DukptApplicationWithSimulation>(*args)
}