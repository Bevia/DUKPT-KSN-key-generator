package org.corebaseit.dukptksnkeygenerator.hsm_aes

import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

class AesHsmSimulator(private val bdk: ByteArray) {

    fun decryptPin(encrypted: ByteArray, ksn: ByteArray, pan: String): String {
        val ipek = DukptAES.deriveIPEK(bdk, ksn)
        val sessionKey = DukptAES.deriveIPEK(ipek, ksn) // mimic session key derivation
        val decrypted = aesDecrypt(sessionKey, encrypted)
        return extractPinFromBlock(decrypted, pan)
    }

    private fun aesDecrypt(key: ByteArray, encrypted: ByteArray): ByteArray {
        val cipher = Cipher.getInstance("AES/ECB/NoPadding")
        cipher.init(Cipher.DECRYPT_MODE, SecretKeySpec(key, "AES"))
        return cipher.doFinal(encrypted)
    }

    private fun extractPinFromBlock(pinBlock: ByteArray, pan: String): String {
        // Extract actual PIN value from decrypted block
        return "1234" // 🧪 Stubbed for now—insert your block parser logic here
    }
}
