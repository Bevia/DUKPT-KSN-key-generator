
package org.corebaseit.dukptksnkeygenerator

import kotlin.experimental.or
import kotlin.experimental.xor

object Dukpt {
    private val KEY_MASK = byteArrayOf(
        0xC0.toByte(), 0xC0.toByte(), 0xC0.toByte(), 0xC0.toByte(),
        0xC0.toByte(), 0xC0.toByte(), 0xC0.toByte(), 0xC0.toByte(),
        0xC0.toByte(), 0xC0.toByte(), 0xC0.toByte(), 0xC0.toByte(),
        0xC0.toByte(), 0xC0.toByte(), 0xC0.toByte(), 0xC0.toByte()
    )

    fun deriveIPEK(bdk: ByteArray, ksn: ByteArray): ByteArray {
        val ksnReg = ksn.copyOf()
        ksnReg[7] = (ksnReg[7].toInt() and 0xE0).toByte() // clear lower 5 bits
        ksnReg[8] = 0x00
        ksnReg[9] = 0x00

        val leftKey = desEncrypt(bdk.sliceArray(0..7), ksnReg)
        val rightKey = desEncrypt(xorBytes(bdk.sliceArray(8..15), KEY_MASK.sliceArray(8..15)), ksnReg)

        return leftKey + rightKey
    }

    fun deriveSessionKey(ipek: ByteArray, ksn: ByteArray): ByteArray {
        val keyReg = ksn.copyOf()
        val counter = ((keyReg[7].toInt() and 0x1F) shl 16) or
                ((keyReg[8].toInt() and 0xFF) shl 8) or
                (keyReg[9].toInt() and 0xFF)

        val ksnBase = ksn.copyOf()
        ksnBase[7] = (ksnBase[7].toInt() and 0xE0).toByte()
        ksnBase[8] = 0x00
        ksnBase[9] = 0x00

        var currentKey = ipek.copyOf()
        for (i in 0..20) {
            if ((counter shr i) and 1 == 1) {
                val reg = ksnBase.copyOf()
                val shift = 1 shl i
                reg[7] = reg[7] or ((shift shr 16) and 0x1F).toByte()
                reg[8] = ((shift shr 8) and 0xFF).toByte()
                reg[9] = (shift and 0xFF).toByte()
                currentKey = nonReversibleKeyGen(currentKey, reg)
            }
        }
        return generateDataKey(currentKey)
    }

    private fun generateDataKey(key: ByteArray): ByteArray {
        val mask = byteArrayOf(
            0x00, 0x00, 0x00, 0x00,
            0x00, 0x00, 0x00, 0x00,
            0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte(),
            0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte()
        )
        return xorBytes(key, mask)
    }

    private fun nonReversibleKeyGen(key: ByteArray, ksn: ByteArray): ByteArray {
        val left = desEncrypt(key.sliceArray(0..7), ksn)
        val right = desEncrypt(key.sliceArray(8..15), ksn)
        return left + right
    }

    private fun desEncrypt(key: ByteArray, data: ByteArray): ByteArray {
        // Placeholder for actual DES encryption
        // Implement using javax.crypto (JVM) or OpenSSL (Native) later
        return xorBytes(key, data.take(8).toByteArray())
    }

    fun xorBytes(a: ByteArray, b: ByteArray): ByteArray {
        return a.zip(b) { x, y -> x xor y }.toByteArray()
    }
}