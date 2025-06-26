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
            .add(EMVTags.TERMINAL_COUNTRY_CODE, "9F1A")
            .add(EMVTags.TRANSACTION_DATE, "9A")
            .add(EMVTags.TRANSACTION_TYPE, "9C")
            .add(EMVTags.AUTH_REQUEST_CRYPTOGRAM, "9F37")
            .add(EMVTags.TERMINAL_CAPABILITIES, "9F33")
            .add(EMVTags.UNPREDICTABLE_NUMBER, "9F37")
            .build()
    }

    fun buildField55Hex(): String = buildField55().joinToString("") { "%02X".format(it) }
}