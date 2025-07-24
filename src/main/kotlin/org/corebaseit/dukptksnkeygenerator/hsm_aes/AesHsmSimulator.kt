package org.corebaseit.dukptksnkeygenerator.hsm_aes

import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

class AesHsmSimulator(private val bdk: ByteArray) {

    //@TODO: use for AES-128 + Format 4 + ECB
 /*   fun decryptPin(encrypted: ByteArray, ksn: ByteArray, pan: String): String {
        val ipek = DukptAES.deriveIPEK(bdk, ksn)
        val sessionKey = DukptAES.deriveIPEK(ipek, ksn) // mimic session key derivation
        val decrypted = aesDecrypt(sessionKey, encrypted)
        return extractPinFromBlock(decrypted, pan)
    }*/

    //@TODO: use for AES-256 + Format 4 + CBC
    fun decryptPin(encrypted: ByteArray, ksn: ByteArray, iv: ByteArray): String {
        val ipek = DukptAES.deriveIPEK(bdk, ksn)
        val sessionKey = DukptAES.deriveIPEK(ipek, ksn) // mimic session key derivation
        val decrypted = aesDecrypt(sessionKey, iv, encrypted)
        return extractPinFromBlock(decrypted)
    }


    //@TODO: use for AES-128 + Format 4 + ECB
    /*private fun aesDecrypt(key: ByteArray, encrypted: ByteArray): ByteArray {
        val cipher = Cipher.getInstance("AES/ECB/NoPadding")
        cipher.init(Cipher.DECRYPT_MODE, SecretKeySpec(key, "AES"))
        return cipher.doFinal(encrypted)
    }*/

    //@TODO: use for AES-256 + Format 4 + CBC
    private fun aesDecrypt(key: ByteArray, iv: ByteArray, encrypted: ByteArray): ByteArray {
        val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
        val secretKey = SecretKeySpec(key, "AES")
        val ivSpec = IvParameterSpec(iv)
        cipher.init(Cipher.DECRYPT_MODE, secretKey, ivSpec)
        return cipher.doFinal(encrypted)
    }

    //@TODO: use for AES-128 + Format 4 + ECB
/*    private fun extractPinFromBlock(pinBlock: ByteArray, pan: String): String {
        // Extract actual PIN value from decrypted block
        return "1234" // 🧪 Stubbed for now—insert your block parser logic here
    }*/

    //@TODO: use for AES-256 + Format 4 + CBC
    private fun extractPinFromBlock(pinBlock: ByteArray): String {
        val pinLength = pinBlock[1].toInt()
        val pinDigits = pinBlock.slice(2 until (2 + pinLength))
        return pinDigits.joinToString("") { it.toString() }
    }

}
