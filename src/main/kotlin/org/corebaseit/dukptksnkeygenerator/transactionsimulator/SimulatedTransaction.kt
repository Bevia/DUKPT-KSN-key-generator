package org.corebaseit.dukptksnkeygenerator.transactionsimulator

data class SimulatedTransaction(
    val amount: Int,
    val cardPAN: String,
    val pinBlock: ByteArray,
    val encryptedPinBlock: ByteArray,
    val derivedKey: ByteArray,
    val ksnUsed: ByteArray
)