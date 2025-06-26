package org.corebaseit.dukptksnkeygenerator.transactionsimulator

import org.corebaseit.dukptksnkeygenerator.transactionsimulator.emv.ARQCEngine
import org.corebaseit.dukptksnkeygenerator.transactionsimulator.emv.EMVTags
import org.corebaseit.dukptksnkeygenerator.transactionsimulator.emv.EMVTransactionBuilder
import org.corebaseit.dukptksnkeygenerator.transactionsimulator.emv.TLVBuilder
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

        val unpredictableNumber = HexUtils.hexToBytes("CAFEBABE") // 4 bytes
        val atc = HexUtils.hexToBytes("0001")                    // 2 bytes
        val aipBytes = HexUtils.hexToBytes(emvBuilder.aip)       // 2 bytes
        val tvr = HexUtils.hexToBytes("0000000000")              // 5 bytes
        val cid = HexUtils.hexToBytes("80")                      // 1 byte (AAC/CDA/ARQC/etc.)
        val iad = HexUtils.hexToBytes("06010A03000000")          // 7 bytes (mock Issuer App Data)

        val dataBlock = unpredictableNumber + atc + aipBytes + tvr
        val arqc = ARQCEngine.generateARQC(derivedKey, dataBlock)

        val field55 = TLVBuilder()
            .add(EMVTags.AMOUNT_AUTHORIZED, emvBuilder.amount)
            .add(EMVTags.TRANSACTION_CURRENCY_CODE, emvBuilder.currencyCode)
            .add(EMVTags.APPLICATION_INTERCHANGE_PROFILE, emvBuilder.aip)
            .add(EMVTags.UNPREDICTABLE_NUMBER, HexUtils.bytesToHex(unpredictableNumber))
            .add(EMVTags.APPLICATION_TRANSACTION_COUNTER, HexUtils.bytesToHex(atc))
            .add(EMVTags.TERMINAL_VERIFICATION_RESULTS, HexUtils.bytesToHex(tvr))
            .add(EMVTags.CRYPTOGRAM_INFORMATION_DATA, HexUtils.bytesToHex(cid))
            .add(EMVTags.ISSUER_APPLICATION_DATA, HexUtils.bytesToHex(iad))
            .add(EMVTags.AUTH_REQUEST_CRYPTOGRAM, HexUtils.bytesToHex(arqc))
            .build()

        val iso = Iso8583Message().apply {
            this.pan = pan
            this.amount = emvBuilder.amount
            this.field52 = encryptedPinBlock
            this.field55 = field55
        }

        println("\n==== Simulated EMV Transaction ====")
        println("KSN: ${HexUtils.bytesToHex(ksn)}")
        println("Derived Key: ${HexUtils.bytesToHex(derivedKey)}")
        println("Encrypted PIN Block: ${HexUtils.bytesToHex(encryptedPinBlock)}")
        println("ARQC: ${HexUtils.bytesToHex(arqc)}")
        iso.printMessage()
    }
}


fun main() {
    val runner = TransactionTestRunner()
    runner.runTest()
}