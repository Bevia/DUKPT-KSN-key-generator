package org.corebaseit.dukptksnkeygenerator

import org.corebaseit.dukptksnkeygenerator.terminal.PinBlockUtil

class TerminalSimulator(
    private val ipek: ByteArray,
    private val initialKsn: ByteArray
) {
    private var currentKsn: ByteArray = initialKsn.clone()

    fun encryptPin(pin: String, pan: String): Pair<ByteArray, ByteArray> {
        // Create PIN block
        val pinBlock = PinBlockUtil.createPinBlock(pin, pan)

        // In real implementation, derive working key from IPEK and KSN
        // For simulation, we'll use IPEK directly
        val encryptedPin = Dukpt.xorBytes(pinBlock, ipek)

        // Increment KSN (simplified for demonstration)
        incrementKsn()

        return Pair(encryptedPin, currentKsn.clone())
    }

    private fun incrementKsn() {
        // Simplified KSN increment - in reality, this would follow DUKPT specifications
        for (i in currentKsn.indices.reversed()) {
            currentKsn[i]++
            if (currentKsn[i].toInt() != 0) break
        }
    }
}