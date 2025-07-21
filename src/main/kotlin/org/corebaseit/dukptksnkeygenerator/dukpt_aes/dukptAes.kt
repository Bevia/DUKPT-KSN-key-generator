package org.corebaseit.dukptksnkeygenerator.dukpt_aes

import org.bouncycastle.jce.provider.BouncyCastleProvider
import java.security.Security
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

fun main() {
    Security.addProvider(BouncyCastleProvider())
    val blockSize = 16
    val pin = "1234"
    val pinLengthByte = pin.length.toByte()

    // Step 1: Construct Format 4 PIN block
    val pinBlock = ByteArray(blockSize)
    pinBlock[0] = pinLengthByte

    val pinBytes = pin.toByteArray(Charsets.UTF_8)
    System.arraycopy(pinBytes, 0, pinBlock, 1, pinBytes.size)

    val secureRandom = SecureRandom()
    val paddingSize = blockSize - 1 - pinBytes.size
    val padding = ByteArray(paddingSize)
    secureRandom.nextBytes(padding)
    System.arraycopy(padding, 0, pinBlock, 1 + pinBytes.size, paddingSize)

    println("Unencrypted PIN Block (Hex): ${pinBlock.joinToString("") { "%02x".format(it) }}")

    // Step 2: Encrypt using AES-CBC
    val key = ByteArray(16)
    secureRandom.nextBytes(key)

    val iv = ByteArray(16)
    secureRandom.nextBytes(iv)

    val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding", "BC")
    val secretKey = SecretKeySpec(key, "AES")
    val ivSpec = IvParameterSpec(iv)
    cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec)

    val encrypted = cipher.doFinal(pinBlock)
    println("Encrypted Block (Hex): ${encrypted.joinToString("") { "%02x".format(it) }}")
    println("IV (Hex): ${iv.joinToString("") { "%02x".format(it) }}")
}

