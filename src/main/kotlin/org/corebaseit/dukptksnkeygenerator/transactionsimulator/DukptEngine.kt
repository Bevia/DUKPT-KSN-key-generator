package org.corebaseit.dukptksnkeygenerator.transactionsimulator

import org.corebaseit.dukptksnkeygenerator.Dukpt
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

class DukptEngine(private val bdk: ByteArray) {
    private var transactionCounter: Int = 0

    fun nextKSN(baseKSN: ByteArray): ByteArray {
        // Increments the KSN counter part
        val ksn = baseKSN.copyOf()
        // Increment last 21 bits (rightmost 3 bytes) appropriately
        transactionCounter += 1
        // Apply updated transactionCounter to KSN here...
        return ksn
    }

    fun deriveKey(ksn: ByteArray): ByteArray {
        // Full DUKPT key derivation logic
        return Dukpt.deriveIPEK(bdk, ksn) // Placeholder for now
    }


    fun encryptData(derivedKey: ByteArray, data: ByteArray): ByteArray {
        // Ensure the key is 24 bytes (192 bits) for Triple DES
        val adjustedKey = when (derivedKey.size) {
            16 -> derivedKey + derivedKey.slice(0..7).toByteArray() // Extend 16-byte key to 24 bytes
            24 -> derivedKey // Key is already correct size
            else -> throw IllegalArgumentException("Invalid key length: ${derivedKey.size} bytes. Expected 16 or 24 bytes.")
        }

        val keySpec = SecretKeySpec(adjustedKey, "DESede")
        val cipher = Cipher.getInstance("DESede/ECB/NoPadding")
        cipher.init(Cipher.ENCRYPT_MODE, keySpec)
        return cipher.doFinal(data)
    }
}