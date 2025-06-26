package org.corebaseit.dukptksnkeygenerator.transactionsimulator.emv

class TLVBuilder {
    private val tags = mutableListOf<Pair<String, String>>()

    fun add(tag: String, valueHex: String): TLVBuilder {
        tags.add(tag to valueHex)
        return this
    }

    fun build(): ByteArray {
        val output = mutableListOf<Byte>()
        for ((tag, valueHex) in tags) {
            val tagBytes = hexToBytes(tag)
            val valueBytes = hexToBytes(valueHex)
            val lengthBytes = encodeLength(valueBytes.size)
            output += tagBytes.toList() + lengthBytes.toList() + valueBytes.toList()
        }
        return output.toByteArray()
    }

    fun buildHex(): String = build().joinToString("") { "%02X".format(it) }

    private fun encodeLength(length: Int): ByteArray {
        return if (length < 0x80) {
            byteArrayOf(length.toByte())
        } else {
            val lenBytes = length.toByte().toInt()
            byteArrayOf((0x81).toByte(), lenBytes.toByte())
        }
    }

    private fun hexToBytes(hex: String): ByteArray =
        hex.chunked(2).map { it.toInt(16).toByte() }.toByteArray()
}