package org.corebaseit.dukptksnkeygenerator

import kotlin.experimental.xor

object Dukpt {
    fun deriveIPEK(bdk: ByteArray, ksn: ByteArray): ByteArray {
        // Sample fixed key mask (Key Mask: 0xC0C0C0C000000000C0C0C0C000000000)
        val keyMask = byteArrayOf(
            0xC0.toByte(), 0xC0.toByte(), 0xC0.toByte(), 0xC0.toByte(),
            0xC0.toByte(), 0xC0.toByte(), 0xC0.toByte(), 0xC0.toByte(),
            0xC0.toByte(), 0xC0.toByte(), 0xC0.toByte(), 0xC0.toByte(),
            0xC0.toByte(), 0xC0.toByte(), 0xC0.toByte(), 0xC0.toByte()
        )

        // Normally requires Triple DES — placeholder below
        return xorBytes(bdk, keyMask)
    }

    fun xorBytes(a: ByteArray, b: ByteArray): ByteArray {
        return a.zip(b) { x, y -> x xor y }.toByteArray()
    }
}


