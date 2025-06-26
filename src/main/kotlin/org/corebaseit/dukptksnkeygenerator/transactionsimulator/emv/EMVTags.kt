package org.corebaseit.dukptksnkeygenerator.transactionsimulator.emv

object EMVTags {
    const val AMOUNT_AUTHORIZED = "9F02"         // Amount, Authorized (Numeric)
    const val TRANSACTION_CURRENCY_CODE = "5F2A" // Transaction Currency Code
    const val APPLICATION_INTERCHANGE_PROFILE = "82" // AIP

    // Common optional tags you can add later:
    const val TERMINAL_COUNTRY_CODE = "9F1A"
    const val TRANSACTION_DATE = "9A"
    const val TRANSACTION_TYPE = "9C"
    const val AMOUNT_OTHER = "9F03"
    const val TERMINAL_CAPABILITIES = "9F33"
    const val CVM_RESULTS = "9F34"
    const val UNPREDICTABLE_NUMBER = "9F37"
    const val APPLICATION_TRANSACTION_COUNTER = "9F36"
    const val CRYPTOGRAM_INFORMATION_DATA = "9F27"
    const val AUTH_REQUEST_CRYPTOGRAM = "9F26"
    const val APP_VERSION_NUMBER = "9F09"
    const val ISSUER_APPLICATION_DATA = "9F10"
    const val DEDICATED_FILE_NAME = "84"
    const val TRACK2_EQUIVALENT = "57"
}