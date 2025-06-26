package org.corebaseit.dukptksnkeygenerator.transactionsimulator.iso

class Iso8583Message {
    var pan: String = "4000000000000002"          // Field 2
    var processingCode: String = "000000"         // Field 3
    var amount: String = "000000010000"           // Field 4
    var stan: String = "123456"                   // Field 11
    var expirationDate: String = "2512"           // Field 14
    var posEntryMode: String = "051"              // Field 22
    var posConditionCode: String = "00"           // Field 25
    var terminalId: String = "TERMID01"            // Field 41
    var merchantId: String = "MID123456789012"     // Field 42
    var currencyCode: String = "978"              // Field 49
    var field52: ByteArray? = null                 // Encrypted PIN block
    var field55: ByteArray? = null                 // EMV TLV data

    fun build(): Map<Int, String> {
        val map = mutableMapOf<Int, String>()
        map[2] = pan
        map[3] = processingCode
        map[4] = amount
        map[11] = stan
        map[14] = expirationDate
        map[22] = posEntryMode
        map[25] = posConditionCode
        map[41] = terminalId
        map[42] = merchantId
        map[49] = currencyCode
        field52?.let { map[52] = it.joinToString("") { b -> "%02X".format(b) } }
        field55?.let { map[55] = it.joinToString("") { b -> "%02X".format(b) } }
        return map
    }

    fun printMessage() {
        println("--- ISO 8583 Message ---")
        build().forEach { (field, value) ->
            println("Field $field: $value")
        }
    }
}
