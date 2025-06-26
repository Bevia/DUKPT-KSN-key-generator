package org.corebaseit.dukptksnkeygenerator.transactionsimulator

import org.corebaseit.dukptksnkeygenerator.utils.HexUtils

class TransactionTestRunner {
    fun runTest() {
        val bdk = HexUtils.hexToBytes("0123456789ABCDEFFEDCBA9876543210")
        val baseKSN = HexUtils.hexToBytes("FFFF9876543210E00000")

        val engine = DukptEngine(bdk)
        val simulator = TransactionSimulator(engine, baseKSN)

        val pan = "4000000000000002"
        val clearPinBlock = HexUtils.hexToBytes("041273FE98AB7654") // Example block

        val transaction = simulator.simulateTransaction(pan, clearPinBlock)

        println("PAN: ${transaction.cardPAN}")
        println("KSN: ${HexUtils.bytesToHex(transaction.ksnUsed)}")
        println("Derived Key: ${HexUtils.bytesToHex(transaction.derivedKey)}")
        println("Encrypted PIN Block: ${HexUtils.bytesToHex(transaction.encryptedPinBlock)}")
    }
}

fun main() {
    val runner = TransactionTestRunner()
    runner.runTest()
}