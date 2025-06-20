package org.corebaseit.dukptksnkeygenerator

class HsmSimulator(private val bdk: ByteArray) {
    fun decryptPin(encryptedPin: ByteArray, ksn: ByteArray, pan: String): String {
        // Derive IPEK using the BDK and KSN
        val ipek = Dukpt.deriveIPEK(bdk, ksn)

        // Decrypt the PIN block
        val pinBlock = Dukpt.xorBytes(encryptedPin, ipek)

        // Remove PAN block (reverse the PIN block creation)
        val panPart = "0000${pan.substring(pan.length - 13, pan.length - 1)}"
        val panBytes = HexUtils.hexToBytes(panPart)
        val clearPinBlock = Dukpt.xorBytes(pinBlock, panBytes)

        // Extract PIN from PIN block
        val pinHex = HexUtils.bytesToHex(clearPinBlock)
        val pinLength = pinHex[1].toString().toInt()

        return pinHex.substring(2, 2 + pinLength)
    }
}