package org.corebaseit.dukptksnkeygenerator.transactionsimulator

class TransactionSimulator(private val engine: DukptEngine, private val baseKSN: ByteArray) {

    fun simulateTransaction(pan: String, clearPinBlock: ByteArray): SimulatedTransaction {
        val ksn = engine.nextKSN(baseKSN)
        val derivedKey = engine.deriveKey(ksn)
        val encryptedPinBlock = engine.encryptData(derivedKey, clearPinBlock)

        return SimulatedTransaction(
            amount = 1000, // e.g. 10.00 €
            cardPAN = pan,
            pinBlock = clearPinBlock,
            encryptedPinBlock = encryptedPinBlock,
            derivedKey = derivedKey,
            ksnUsed = ksn
        )
    }
}