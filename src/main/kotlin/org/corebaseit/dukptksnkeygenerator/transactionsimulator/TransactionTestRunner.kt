package org.corebaseit.dukptksnkeygenerator.transactionsimulator

import org.corebaseit.dukptksnkeygenerator.transactionsimulator.emv.EMVTransactionBuilder
import org.corebaseit.dukptksnkeygenerator.transactionsimulator.iso.Iso8583Message
import org.corebaseit.dukptksnkeygenerator.utils.HexUtils

class TransactionTestRunner {
    fun runTest() {
        val bdk = HexUtils.hexToBytes("0123456789ABCDEFFEDCBA9876543210")
        val baseKSN = HexUtils.hexToBytes("FFFF9876543210E00000")

        val engine = DukptEngine(bdk)
        val pan = "4000000000000002"
        val clearPinBlock = HexUtils.hexToBytes("041273FE98AB7654") // example PIN block

        val ksn = engine.nextKSN(baseKSN)
        val derivedKey = engine.deriveKey(ksn)
        val encryptedPinBlock = engine.encryptData(derivedKey, clearPinBlock)

        val emvBuilder = EMVTransactionBuilder().apply {
            amount = "000000050000" // 50.00 EUR
            currencyCode = "0978"
            aip = "1800"
        }

        val iso = Iso8583Message().apply {
            this.pan = pan
            this.amount = "000000050000"
            this.field52 = encryptedPinBlock
            this.field55 = emvBuilder.buildField55()
        }

        println("\n==== Simulated EMV Transaction ====")
        println("KSN: ${HexUtils.bytesToHex(ksn)}")
        println("Derived Key: ${HexUtils.bytesToHex(derivedKey)}")
        println("Encrypted PIN Block: ${HexUtils.bytesToHex(encryptedPinBlock)}")
        iso.printMessage()
    }
}


fun main() {
    val runner = TransactionTestRunner()
    runner.runTest()
}