package org.corebaseit.dukptksnkeygenerator.terminal

import org.corebaseit.dukptksnkeygenerator.Dukpt
import org.corebaseit.dukptksnkeygenerator.HexUtils

object PinBlockUtil {
    fun createPinBlock(pin: String, pan: String): ByteArray {
        require(pin.length in 4..12) { "PIN must be between 4 and 12 digits" }
        require(pin.all { it.isDigit() }) { "PIN must contain only digits" }
        require(pan.length >= 12) { "PAN must be at least 12 digits" }
        require(pan.all { it.isDigit() }) { "PAN must contain only digits" }

        // Format PIN block (ISO Format 0)
        val pinLength = pin.length
        val pinBlockHead = "0${pinLength}${pin}".padEnd(16, 'F')

        // Get last 12 digits of PAN excluding check digit
        val panPart = "0000${pan.substring(pan.length - 13, pan.length - 1)}"

        // Convert to byte arrays
        val pinBlockBytes = HexUtils.hexToBytes(pinBlockHead)
        val panBytes = HexUtils.hexToBytes(panPart)

        // XOR the PIN block with the PAN block
        return Dukpt.xorBytes(pinBlockBytes, panBytes)
    }
}