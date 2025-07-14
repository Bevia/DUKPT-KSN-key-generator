package org.corebaseit.dukptksnkeygenerator.transactionsimulator

data class SimulatedTransaction(
    val amount: Int,
    val cardPAN: String,
    val pinBlock: ByteArray,
    val encryptedPinBlock: ByteArray,
    val derivedKey: ByteArray,
    val ksnUsed: ByteArray
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SimulatedTransaction

        if (amount != other.amount) return false
        if (cardPAN != other.cardPAN) return false
        if (!pinBlock.contentEquals(other.pinBlock)) return false
        if (!encryptedPinBlock.contentEquals(other.encryptedPinBlock)) return false
        if (!derivedKey.contentEquals(other.derivedKey)) return false
        if (!ksnUsed.contentEquals(other.ksnUsed)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = amount
        result = 31 * result + cardPAN.hashCode()
        result = 31 * result + pinBlock.contentHashCode()
        result = 31 * result + encryptedPinBlock.contentHashCode()
        result = 31 * result + derivedKey.contentHashCode()
        result = 31 * result + ksnUsed.contentHashCode()
        return result
    }
}