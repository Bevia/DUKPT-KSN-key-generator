package org.corebaseit.dukptksnkeygenerator.hsm

import org.corebaseit.dukptksnkeygenerator.Dukpt
import org.corebaseit.dukptksnkeygenerator.utils.HexUtils
import org.corebaseit.dukptksnkeygenerator.HsmSimulator
import org.corebaseit.dukptksnkeygenerator.TerminalSimulator

class DukptSimulator {

    private fun isHex(input: String): Boolean = input.matches(Regex("^[0-9A-Fa-f]{16}$"))

    fun runSimulation(pin: String = "1234", pan: String = "4532111122223333") {
        try {
            // Test data with validation
            val pin = pin.also { validatePin(it) }
            val pan = pan.also { validatePan(it) }

            printSimulationHeader(pin, pan)

            // Generate BDK (in real world, this would be in the HSM)
            val bdk = generateBdk()
            println("BDK (in HSM): ${HexUtils.bytesToHex(bdk)}")

            // Generate initial KSN
            val ksn = generateInitialKsn()
            println("Initial KSN: ${HexUtils.bytesToHex(ksn)}")

            // Derive IPEK (in real world, this would be injected into terminal)
            val ipek = Dukpt.deriveIPEK(bdk, ksn)
            println("IPEK (in Terminal): ${HexUtils.bytesToHex(ipek)}")
            println()

            simulateTransaction(pin, pan, ipek, ksn, bdk)

        } catch (e: IllegalArgumentException) {
            System.err.println("Validation Error: ${e.message}")
        } catch (e: Exception) {
            System.err.println("Simulation Error: ${e.message}")
        }
    }

    private fun validatePin(pin: String) {
        if (isHex(pin)) return // Skip validation, assume pre-encoded PIN block
        require(pin.length in 4..12) { "PIN must be between 4 and 12 digits" }
        require(pin.all { it.isDigit() }) { "PIN must contain only digits" }
    }
    private fun validatePan(pan: String) {
        require(pan.length >= 13) { "PAN must be at least 13 digits" }
        require(pan.all { it.isDigit() }) { "PAN must contain only digits" }
    }

    private fun printSimulationHeader(pin: String, pan: String) {
        println("Simulating DUKPT PIN encryption/decryption")
        println("=========================================")
        println("PIN: ${maskPin(pin)}")  // For security, mask the PIN in logs
        println("PAN: ${maskPan(pan)}")  // For security, mask the PAN in logs
        println()
    }

    private fun generateBdk(): ByteArray {
        // In production, this would be securely stored in HSM
        return HexUtils.hexToBytes("0123456789ABCDEF0123456789ABCDEF")
    }

    private fun generateInitialKsn(): ByteArray {
        return HexUtils.hexToBytes("FFFF9876543210E00000")
    }

    private fun simulateTransaction(
        pin: String,
        pan: String,
        ipek: ByteArray,
        ksn: ByteArray,
        bdk: ByteArray
    ) {
        // Create terminal and HSM simulators
        val terminal = TerminalSimulator(ipek, ksn)
        val hsm = HsmSimulator(bdk)

        // Simulate PIN encryption at terminal
        println("Terminal encrypting PIN...")
        val (encryptedPin, currentKsn) = terminal.encryptPin(pin, pan)
        println("Encrypted PIN block: ${HexUtils.bytesToHex(encryptedPin)}")
        println("Current KSN: ${HexUtils.bytesToHex(currentKsn)}")
        println()

        // Simulate PIN decryption at HSM
        println("HSM decrypting PIN...")
        val decryptedPin = hsm.decryptPin(encryptedPin, currentKsn, pan)
        println("Decrypted PIN: ${maskPin(decryptedPin)}")  // Mask the decrypted PIN in logs

        // Verify the decryption
        if (pin != decryptedPin) {
            throw IllegalStateException("PIN verification failed!")
        }
    }

    private fun maskPin(pin: String): String {
        return MASK_CHARACTER.repeat(pin.length)
    }


    private fun maskPan(pan: String): String {
        return when {
            pan.length <= 4 -> pan
            else -> "${pan.take(6)}${"*".repeat(pan.length - 10)}${pan.takeLast(4)}"
        }
    }


    companion object {
        private const val MASK_CHARACTER = "*"
        private const val BDK_LENGTH = 16 // bytes
        private const val KSN_LENGTH = 10 // bytes
    }
}