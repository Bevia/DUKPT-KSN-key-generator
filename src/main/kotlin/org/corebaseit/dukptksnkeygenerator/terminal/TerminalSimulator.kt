package org.corebaseit.dukptksnkeygenerator

import org.corebaseit.dukptksnkeygenerator.tdes_encryption.TripleDESEncryption
import org.corebaseit.dukptksnkeygenerator.terminal.PinBlockUtil
import org.corebaseit.dukptksnkeygenerator.utils.HexUtils

class TerminalSimulator(
    private val ipek: ByteArray,
    private val initialKsn: ByteArray
) {
    private var currentKsn: ByteArray = initialKsn.clone()

    fun encryptPin(pin: String, pan: String): Pair<ByteArray, ByteArray> {
        val pinBlock: ByteArray = if (isHexPinBlock(pin)) {
            // Already an ISO PIN block in hex format
            HexUtils.hexToBytes(pin)
        } else {
            // Generate from clear PIN
            PinBlockUtil.createPinBlock(pin, pan)
        }

        // 🔍 Debug prints
        println("PIN block (hex) ---------> ${HexUtils.bytesToHex(pinBlock)}")
        println("PIN block size ---------> ${pinBlock.size}")
        println("IPEK size ---------> ${ipek.size}")

        //val encryptedPin = Dukpt.xorBytes(pinBlock, ipek)
        //val encryptedPin = Dukpt.xorBytes(pinBlock, ipek.sliceArray(0 until 8))
        val encryptedPin = TripleDESEncryption.encrypt(ipek, pinBlock)

        incrementKsn() // move to next transaction counter

        return Pair(encryptedPin, currentKsn.clone())
    }

    private fun isHexPinBlock(input: String): Boolean {
        return input.length == 16 && input.all { it in "0123456789abcdefABCDEF" }
    }

    private fun incrementKsn() {
        // Simplified KSN increment - in reality, this would follow DUKPT specifications
        for (i in currentKsn.indices.reversed()) {
            currentKsn[i]++
            if (currentKsn[i].toInt() != 0) break
        }
    }
}