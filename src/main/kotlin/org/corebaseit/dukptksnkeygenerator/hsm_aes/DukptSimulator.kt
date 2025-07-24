package org.corebaseit.dukptksnkeygenerator.hsm_aes

import org.corebaseit.dukptksnkeygenerator.utils.HexUtils

class DukptSimulator {

    private fun isHex(input: String): Boolean = input.matches(Regex("^[0-9A-Fa-f]{16}$"))

    fun runSimulation(pin: String = "1234", pan: String = "4532111122223333") {
        try {
            val pin = pin.also { validatePin(it) }
            val pan = pan.also { validatePan(it) }

            printSimulationHeader(pin, pan)

            println("DUKPT Format ID: 4 (AES-DUKPT)")
            val bdk = generateBdk()
            println("BDK (AES-128): ${HexUtils.bytesToHex(bdk)}")

            val ksn = generateInitialKsn()
            println("Initial KSN (Format 4): ${HexUtils.bytesToHex(ksn)}")

            val ipek = DukptAES.deriveIPEK(bdk, ksn)
            println("IPEK (Derived using AES): ${HexUtils.bytesToHex(ipek)}")
            println()

            simulateTransaction(pin, pan, ipek, ksn, bdk)

        } catch (e: IllegalArgumentException) {
            System.err.println("Validation Error: ${e.message}")
        } catch (e: Exception) {
            System.err.println("Simulation Error: ${e.message}")
        }
    }

    private fun validatePin(pin: String) {
        if (isHex(pin)) return
        require(pin.length in 4..12) { "PIN must be between 4 and 12 digits" }
        require(pin.all { it.isDigit() }) { "PIN must contain only digits" }
    }

    private fun validatePan(pan: String) {
        require(pan.length >= 13) { "PAN must be at least 13 digits" }
        require(pan.all { it.isDigit() }) { "PAN must contain only digits" }
    }

    private fun printSimulationHeader(pin: String, pan: String) {
        println("Simulating AES-DUKPT PIN encryption/decryption")
        println("==============================================")
        println("PIN: ${maskPin(pin)}")
        println("PAN: ${maskPan(pan)}")
        println()
    }

    private fun generateBdk(): ByteArray {
        return HexUtils.hexToBytes("00112233445566778899AABBCCDDEEFF") // AES-128
    }

    private fun generateInitialKsn(): ByteArray {
        return HexUtils.hexToBytes("FFFF9876543210E00000000000000000") // 16-byte Format 4 KSN
    }

    private fun simulateTransaction(
        pin: String,
        pan: String,
        ipek: ByteArray,
        ksn: ByteArray,
        bdk: ByteArray
    ) {
        val terminal = AesTerminalSimulator(ipek, ksn)
        val hsm = AesHsmSimulator(bdk)

        println("Terminal encrypting PIN using AES...")
        val (encryptedPin, currentKsn) = terminal.encryptPin(pin, pan)
        println("Encrypted PIN block: ${HexUtils.bytesToHex(encryptedPin)}")
        println("Current KSN: ${HexUtils.bytesToHex(currentKsn)}")
        println()

        println("HSM decrypting PIN using AES...")
        val decryptedPin = hsm.decryptPin(encryptedPin, currentKsn, pan)
        println("Decrypted PIN (unmasked): $decryptedPin") // Changed from masked version

        if (pin != decryptedPin) {
            throw IllegalStateException("PIN verification failed!")
        }
    }

    private fun maskPin(pin: String): String = MASK_CHARACTER.repeat(pin.length)

    private fun maskPan(pan: String): String {
        return when {
            pan.length <= 4 -> pan
            else -> "${pan.take(6)}${"*".repeat(pan.length - 10)}${pan.takeLast(4)}"
        }
    }

    companion object {
        private const val MASK_CHARACTER = "*"
    }
}
