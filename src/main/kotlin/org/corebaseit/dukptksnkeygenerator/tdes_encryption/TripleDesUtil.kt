package org.corebaseit.dukptksnkeygenerator.tdes_encryption

import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec

object TripleDesUtil {
    fun encrypt(data: ByteArray, key: ByteArray): ByteArray {
        require(key.size == 16 || key.size == 24) { "Key must be 16 or 24 bytes for Triple DES" }

        // If 16-byte key, extend it to 24 bytes for Triple DES
        val fullKey = if (key.size == 16) {
            key + key.slice(0..7).toByteArray()
        } else {
            key
        }

        val secretKey: SecretKey = SecretKeySpec(fullKey, "DESede")
        val cipher = Cipher.getInstance("DESede/ECB/NoPadding")
        cipher.init(Cipher.ENCRYPT_MODE, secretKey)

        return cipher.doFinal(data)
    }

    fun decrypt(data: ByteArray, key: ByteArray): ByteArray {
        require(key.size == 16 || key.size == 24) { "Key must be 16 or 24 bytes for Triple DES" }

        // If 16-byte key, extend it to 24 bytes for Triple DES
        val fullKey = if (key.size == 16) {
            key + key.slice(0..7).toByteArray()
        } else {
            key
        }

        val secretKey: SecretKey = SecretKeySpec(fullKey, "DESede")
        val cipher = Cipher.getInstance("DESede/ECB/NoPadding")
        cipher.init(Cipher.DECRYPT_MODE, secretKey)

        return cipher.doFinal(data)
    }
}