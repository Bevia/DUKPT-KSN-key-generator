package org.corebaseit.dukptksnkeygenerator

import kotlinx.cli.*

class DukptCliTool(private val args: Array<String>) {
    private val parser = ArgParser("dukpt-cli")
    private val bdk: String by parser.option(
        ArgType.String,
        shortName = "b",
        description = "Base Derivation Key (hex)"
    ).required()
    private val ksn: String by parser.option(
        ArgType.String,
        shortName = "k",
        description = "Key Serial Number (hex)"
    ).required()

    fun run() {
        try {
            parser.parse(args)

            if (!isValidHex(bdk)) {
                throw IllegalArgumentException("Invalid BDK hex format")
            }
            if (!isValidHex(ksn)) {
                throw IllegalArgumentException("Invalid KSN hex format")
            }

            val bdkBytes = HexUtils.hexToBytes(bdk)
            val ksnBytes = HexUtils.hexToBytes(ksn)

            // Validate key lengths (example values - adjust according to your requirements)
            if (bdkBytes.size != 16) { // 128 bits
                throw IllegalArgumentException("BDK must be 16 bytes (128 bits)")
            }
            if (ksnBytes.size != 10) { // 80 bits
                throw IllegalArgumentException("KSN must be 10 bytes (80 bits)")
            }

            val ipek = Dukpt.deriveIPEK(bdkBytes, ksnBytes)
            println("Derived IPEK: ${HexUtils.bytesToHex(ipek)}")
        } catch (e: Exception) {
            System.err.println("Error: ${e.message}")
            System.exit(1)
        }
    }

    private fun isValidHex(input: String): Boolean {
        return input.matches("[0-9A-Fa-f]+".toRegex())
    }
}