package org.corebaseit.dukptksnkeygenerator.transactionsimulator.emv

class EMVTransactionBuilder {
    var amount: String = "000000010000" // Amount Authorized
    var currencyCode: String = "0978"   // EUR
    var aip: String = "1800"            // AIP: CVM + terminal risk management

    fun buildField55(): ByteArray {
        return TLVBuilder()
            .add(EMVTags.AMOUNT_AUTHORIZED, amount)
            .add(EMVTags.TRANSACTION_CURRENCY_CODE, currencyCode)
            .add(EMVTags.APPLICATION_INTERCHANGE_PROFILE, aip)
            .build()
    }

    fun buildField55Hex(): String = buildField55().joinToString("") { "%02X".format(it) }
}