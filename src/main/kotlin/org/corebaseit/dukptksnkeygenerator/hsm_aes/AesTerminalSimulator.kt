package org.corebaseit.dukptksnkeygenerator.hsm_aes

import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

class AesTerminalSimulator(private val ipek: ByteArray, private var ksn: ByteArray) {

    /*fun encryptPin(pin: String, pan: String): Pair<ByteArray, ByteArray> {
        val sessionKey = deriveSessionKey(ipek, ksn)
        val pinBlock = generatePinBlock(pin, pan)

        // 🔧 Pad the pin block to match AES block size
        val paddedPinBlock = padTo16Bytes(pinBlock)

        // 🔒 Encrypt using AES
        val encrypted = aesEncrypt(sessionKey, paddedPinBlock)

        return Pair(encrypted, ksn)
    }*/

    //@TODO: use for AES-128 + Format 4 + ECB
    //@TODO: if you are using full ISO Format 4 PIN block then use this instead:
/*    fun encryptPin(pin: String, pan: String): Pair<ByteArray, ByteArray> {
        val sessionKey = deriveSessionKey(ipek, ksn)
        val pinBlock = generatePinBlock(pin, pan)
        val encrypted = aesEncrypt(sessionKey, pinBlock)
        return Pair(encrypted, ksn)
    }*/

    //@TODO: use for AES-256 + Format 4 + CBC
    fun encryptPin(pin: String): Triple<ByteArray, ByteArray, ByteArray> {
        val sessionKey = deriveSessionKey(ipek, ksn)
        val pinBlock = generatePinBlock(pin)
        val iv = ByteArray(16).apply { SecureRandom().nextBytes(this) }
        val encrypted = aesEncrypt(sessionKey, iv, pinBlock)
        return Triple(encrypted, ksn, iv)
    }

    /* private fun generatePinBlock(pin: String, pan: String): ByteArray {
         // Create ISO 9564 Format 0 block (simple version)
         val pinBlock = ByteArray(8)
         // PIN block generation logic goes here
         // You can implement Format 1 if needed
         return pinBlock
     }*/

    //@TODO: use for AES-128 + Format 4 + ECB
    //@TODO: lets generatePinBlock() properly so it returns a full ISO Format 4 PIN block that's already 16 bytes—no padding needed
   /* private fun generatePinBlock(pin: String, pan: String): ByteArray {
        val block = ByteArray(16)

        // Format ID for ISO Format 4
        block[0] = 0x04

        // PIN length
        block[1] = pin.length.toByte()

        // PIN digits (numeric only), packed into bytes
        var pinIndex = 0
        for (i in 2 until 14) {
            block[i] = if (pinIndex < pin.length) {
                pin[pinIndex].digitToInt().toByte()
            } else {
                0x0F // Filler
            }
            pinIndex++
        }

        // Last two bytes - optional random filler (can be 0xAA, 0x55, etc.)
        val random = SecureRandom()
        block[14] = random.nextInt(256).toByte()
        block[15] = random.nextInt(256).toByte()

        return block
    }*/

    //@TODO: use for AES-256 + Format 4 + CBC
    private fun generatePinBlock(pin: String): ByteArray {
        val block = ByteArray(16)
        block[0] = 0x04
        block[1] = pin.length.toByte()
        for (i in 2 until 14) {
            block[i] = if (i - 2 < pin.length) pin[i - 2].digitToInt().toByte() else 0x0F
        }
        val random = SecureRandom()
        block[14] = random.nextInt(256).toByte()
        block[15] = random.nextInt(256).toByte()
        return block
    }


    private fun deriveSessionKey(ipek: ByteArray, ksn: ByteArray): ByteArray {
        // Simplified for illustration—use AES-KDF or AES-CMAC with KSN
        return DukptAES.deriveIPEK(ipek, ksn) // reusing IPEK logic here for demo
    }

    //@TODO Manually pad the PIN block if full ISO Format 4 PIN block is NOT used
    fun padTo16Bytes(input: ByteArray): ByteArray {
        return if (input.size == 16) input
        else input.copyOf(16)
    }

    //@TODO: use for AES-128 + Format 4 + ECB
    /*private fun aesEncrypt(key: ByteArray, data: ByteArray): ByteArray {
        val cipher = Cipher.getInstance("AES/ECB/NoPadding")
        cipher.init(Cipher.ENCRYPT_MODE, SecretKeySpec(key, "AES"))
        return cipher.doFinal(data)
    }*/

    //@TODO: use for AES-256 + Format 4 + CBC
    private fun aesEncrypt(key: ByteArray, iv: ByteArray, data: ByteArray): ByteArray {
        val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
        val secretKey = SecretKeySpec(key, "AES")
        val ivSpec = IvParameterSpec(iv)
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec)
        return cipher.doFinal(data)
    }

}
