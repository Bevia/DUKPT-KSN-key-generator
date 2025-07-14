package org.corebaseit.dukptksnkeygenerator.terminal

import org.corebaseit.dukptksnkeygenerator.Dukpt
import org.corebaseit.dukptksnkeygenerator.utils.HexUtils

object PinBlockUtil {
    fun createPinBlock(pin: String, pan: String): ByteArray {
        require(pin.length in 4..12) { "PIN must be between 4 and 12 digits" }
        require(pin.all { it.isDigit() }) { "PIN must be digits only" }

        // 1. Create PIN block: 0N + PIN + pad F
        val pinLength = pin.length
        val pinBlockStr = "0${pinLength}$pin".padEnd(16, 'F')
        val pinBlock = hexToBytes(pinBlockStr)

        // 2. Get rightmost 12 digits before check digit from PAN
        val pan12 = pan.dropLast(1).takeLast(12)
        val panBlockStr = "0000$pan12"
        val panBlock = hexToBytes(panBlockStr)

        // 3. XOR both blocks
        return ByteArray(8) { i ->
            (pinBlock[i].toInt() xor panBlock[i].toInt()).toByte()
        }
    }

    private fun hexToBytes(hex: String): ByteArray =
        hex.chunked(2).map { it.toInt(16).toByte() }.toByteArray()
}

/*
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
}*/
