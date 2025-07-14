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

        // 2. Get rightmost 12 digits before a check digit from PAN
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
