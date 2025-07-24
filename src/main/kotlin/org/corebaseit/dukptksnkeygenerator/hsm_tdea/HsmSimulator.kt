package org.corebaseit.dukptksnkeygenerator

import org.corebaseit.dukptksnkeygenerator.tdes_encryption.TripleDesUtil
import org.corebaseit.dukptksnkeygenerator.utils.HexUtils

class HsmSimulator(private val bdk: ByteArray) {
    fun decryptPin(encryptedPin: ByteArray, ksn: ByteArray, pan: String): String {
        require(encryptedPin.size == 8) { "Encrypted PIN block must be 8 bytes" }
        require(ksn.size == 10) { "KSN must be 10 bytes" }
        require(pan.length >= 13) { "PAN must be at least 13 digits" }

        // Step 1: Derive IPEK using the BDK and KSN
        val ipek = Dukpt.deriveIPEK(bdk, ksn)

        // Step 2: Decrypt the PIN block using Triple DES
        val pinBlock = TripleDesUtil.decrypt(encryptedPin, ipek)

        // Step 3: Extract PAN information for PIN block format
        val panPart = createPanPart(pan)
        val panBytes = HexUtils.hexToBytes(panPart)

        // Step 4: Remove PAN mask from PIN block
        val clearPinBlock = Dukpt.xorBytes(pinBlock, panBytes)

        return extractPin(clearPinBlock)
    }

    private fun createPanPart(pan: String): String {
        // Get 12 rightmost digits of the PAN excluding the check digit
        val relevantPanDigits = pan.substring(pan.length - 13, pan.length - 1)
        return "0000$relevantPanDigits"
    }

    private fun extractPin(clearPinBlock: ByteArray): String {
        val pinBlockHex = HexUtils.bytesToHex(clearPinBlock)

        // Validate PIN block format
        require(pinBlockHex[0] == '0') { "Invalid PIN block format" }

        // Extract PIN length and validate
        val pinLength = pinBlockHex[1].toString().toInt()
        require(pinLength in 4..12) { "Invalid PIN length: $pinLength" }

        // Extract PIN digits
        val pin = pinBlockHex.substring(2, 2 + pinLength)
        require(pin.all { it.isDigit() }) { "Invalid PIN format" }

        return pin
    }

    companion object {
        private const val PIN_BLOCK_SIZE = 8 // bytes
        private const val KSN_SIZE = 10 // bytes
    }
}