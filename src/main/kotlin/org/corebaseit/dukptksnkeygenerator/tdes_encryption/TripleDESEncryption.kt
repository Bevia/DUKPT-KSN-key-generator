package org.corebaseit.dukptksnkeygenerator.tdes_encryption


import java.security.Key
import javax.crypto.Cipher
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.DESedeKeySpec

object TripleDESEncryption {

    /**
     * Encrypts a block using 3DES in ECB mode with no padding.
     * @param keyBytes must be 16 or 24 bytes.
     * @param data must be exactly 8 bytes.
     */
    fun encrypt(keyBytes: ByteArray, data: ByteArray): ByteArray {
        require(data.size == 8) { "Data must be exactly 8 bytes." }
        require(keyBytes.size == 16 || keyBytes.size == 24) { "3DES key must be 16 or 24 bytes." }

        val fullKey = normalizeKey(keyBytes)
        val secretKey = generateKey(fullKey)
        val cipher = Cipher.getInstance("DESede/ECB/NoPadding")

        cipher.init(Cipher.ENCRYPT_MODE, secretKey)
        return cipher.doFinal(data)
    }

    /**
     * Decrypts a block using 3DES in ECB mode with no padding.
     */
    fun decrypt(keyBytes: ByteArray, data: ByteArray): ByteArray {
        require(data.size == 8) { "Data must be exactly 8 bytes." }
        require(keyBytes.size == 16 || keyBytes.size == 24) { "3DES key must be 16 or 24 bytes." }

        val fullKey = normalizeKey(keyBytes)
        val secretKey = generateKey(fullKey)
        val cipher = Cipher.getInstance("DESede/ECB/NoPadding")

        cipher.init(Cipher.DECRYPT_MODE, secretKey)
        return cipher.doFinal(data)
    }

    private fun normalizeKey(key: ByteArray): ByteArray {
        return if (key.size == 16) {
            // 2-key 3DES: K1 | K2 | K1
            key + key.sliceArray(0 until 8)
        } else key // already 24 bytes
    }

    private fun generateKey(key: ByteArray): Key {
        val spec = DESedeKeySpec(key)
        val factory = SecretKeyFactory.getInstance("DESede")
        return factory.generateSecret(spec)
    }
}