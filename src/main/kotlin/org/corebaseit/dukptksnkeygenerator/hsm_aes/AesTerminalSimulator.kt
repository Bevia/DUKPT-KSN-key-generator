package org.corebaseit.dukptksnkeygenerator.hsm_aes

import java.security.SecureRandom
import javax.crypto.Cipher
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

    //@TODO: if you are using full ISO Format 4 PIN block then use this instead:
    fun encryptPin(pin: String, pan: String): Pair<ByteArray, ByteArray> {
        val sessionKey = deriveSessionKey(ipek, ksn)
        val pinBlock = generatePinBlock(pin, pan)
        val encrypted = aesEncrypt(sessionKey, pinBlock)
        return Pair(encrypted, ksn)
    }

   /* private fun generatePinBlock(pin: String, pan: String): ByteArray {
        // Create ISO 9564 Format 0 block (simple version)
        val pinBlock = ByteArray(8)
        // PIN block generation logic goes here
        // You can implement Format 1 if needed
        return pinBlock
    }*/

    //@TODO: lets generatePinBlock() properly so it returns a full ISO Format 4 PIN block that's already 16 bytes—no padding needed
    private fun generatePinBlock(pin: String, pan: String): ByteArray {
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
    }

    private fun deriveSessionKey(ipek: ByteArray, ksn: ByteArray): ByteArray {
        // Simplified for illustration—use AES-KDF or AES-CMAC with KSN
        return DukptAES.deriveIPEK(ipek, ksn) // reusing IPEK logic here for demo
    }

    fun padTo16Bytes(input: ByteArray): ByteArray {
        return if (input.size == 16) input
        else input.copyOf(16)
    }

    private fun aesEncrypt(key: ByteArray, data: ByteArray): ByteArray {
        val cipher = Cipher.getInstance("AES/ECB/NoPadding")
        cipher.init(Cipher.ENCRYPT_MODE, SecretKeySpec(key, "AES"))
        return cipher.doFinal(data)
    }
}
