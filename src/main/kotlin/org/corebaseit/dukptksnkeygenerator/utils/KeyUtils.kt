package org.corebaseit.dukptksnkeygenerator.utils

object KeyUtils {
    fun combinePartialKeys(key1: ByteArray, key2: ByteArray, key3: ByteArray): ByteArray {
        require(key1.size == key2.size && key2.size == key3.size) {
            "All partial keys must have the same length"
        }

        return ByteArray(key1.size) { index ->
            (key1[index].toInt() xor key2[index].toInt() xor key3[index].toInt()).toByte()
        }
    }
}