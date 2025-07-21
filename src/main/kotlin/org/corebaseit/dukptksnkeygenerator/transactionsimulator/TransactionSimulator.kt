package org.corebaseit.dukptksnkeygenerator.transactionsimulator

import org.corebaseit.dukptksnkeygenerator.tdes_encryption.TripleDESEncryption
import org.corebaseit.dukptksnkeygenerator.terminal.PinBlockUtil
import org.corebaseit.dukptksnkeygenerator.utils.HexUtils

class TransactionSimulator(private val engine: DukptEngine, private val baseKSN: ByteArray) {

    fun simulateTransaction(pan: String, pinOrBlock: String): SimulatedTransaction {
        val ksn = engine.nextKSN(baseKSN)

        val pinBlock: ByteArray = if (isHexPinBlock(pinOrBlock)) {
            HexUtils.hexToBytes(pinOrBlock)
        } else {
            PinBlockUtil.createPinBlock(pinOrBlock, pan)
        }

        val derivedKey = engine.deriveKey(ksn)
        val encryptedPinBlock = TripleDESEncryption.encrypt(derivedKey, pinBlock)

        return SimulatedTransaction(
            amount = 1000,
            cardPAN = pan,
            pinBlock = pinBlock,
            encryptedPinBlock = encryptedPinBlock,
            derivedKey = derivedKey,
            ksnUsed = ksn
        )
    }

    fun isHexPinBlock(pin: String): Boolean {
        return pin.length == 16 && pin.all { it.uppercaseChar() in "0123456789ABCDEF" }
    }
}