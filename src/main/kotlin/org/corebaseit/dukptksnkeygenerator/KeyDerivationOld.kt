package org.corebaseit.dukptksnkeygenerator

import org.corebaseit.dukptksnkeygenerator.encryption.TripleDesUtil
import kotlin.experimental.and
import kotlin.experimental.xor

object DukptOld {
    fun deriveIPEK(bdk: ByteArray, ksn: ByteArray): ByteArray {
        // Key registers
        val leftRegister = ByteArray(8)
        val rightRegister = ByteArray(8)

        // Copy first 8 bytes of BDK to left register, last 8 bytes to right register
        System.arraycopy(bdk, 0, leftRegister, 0, 8)
        System.arraycopy(bdk, 8, rightRegister, 0, 8)

        // Create masked KSN (first 8 bytes only)
        val maskedKsn = ByteArray(8)
        System.arraycopy(ksn, 0, maskedKsn, 0, 8)
        maskedKsn[7] = maskedKsn[7] and 0xE0.toByte() // Mask with E0

        // Derive IPEK left half
        val ipekLeft = TripleDesUtil.encrypt(maskedKsn, bdk)

        // XOR with Key Register
        val xoredKey = xorBytes(bdk, KEY_REGISTER_MASK)

        // Derive IPEK right half
        val ipekRight = TripleDesUtil.encrypt(maskedKsn, xoredKey)

        // Combine left and right halves
        return ipekLeft + ipekRight
    }

    fun xorBytes(a: ByteArray, b: ByteArray): ByteArray {
        return a.zip(b) { x, y -> x xor y }.toByteArray()
    }

    private val KEY_REGISTER_MASK = byteArrayOf(
        0xC0.toByte(), 0xC0.toByte(), 0xC0.toByte(), 0xC0.toByte(),
        0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(),
        0xC0.toByte(), 0xC0.toByte(), 0xC0.toByte(), 0xC0.toByte(),
        0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte()
    )
}

