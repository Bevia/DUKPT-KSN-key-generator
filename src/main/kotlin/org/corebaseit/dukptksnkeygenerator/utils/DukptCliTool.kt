package org.corebaseit.dukptksnkeygenerator.utils

import kotlinx.cli.ArgParser
import kotlinx.cli.ArgType
import kotlinx.cli.required
import org.corebaseit.dukptksnkeygenerator.Dukpt

class DukptCliTool(private val args: Array<String>) {
    private val parser = ArgParser("dukpt-cli")
    private val partialKey1: String by parser.option(
        ArgType.String,
        shortName = "k1",
        description = "First Partial Key Component (hex)"
    ).required()
    private val partialKey2: String by parser.option(
        ArgType.String,
        shortName = "k2",
        description = "Second Partial Key Component (hex)"
    ).required()
    private val partialKey3: String by parser.option(
        ArgType.String,
        shortName = "k3",
        description = "Third Partial Key Component (hex)"
    ).required()
    private val ksn: String by parser.option(
        ArgType.String,
        shortName = "k",
        description = "Key Serial Number (hex)"
    ).required()

    fun run() {
        try {
            parser.parse(args)

            // Validate the hex format for all inputs
            listOf(partialKey1, partialKey2, partialKey3, ksn).forEach { key ->
                if (!isValidHex(key)) {
                    throw IllegalArgumentException("Invalid hex format for key: $key")
                }
            }

            // Convert partial keys to byte arrays
            val key1Bytes = HexUtils.hexToBytes(partialKey1)
            val key2Bytes = HexUtils.hexToBytes(partialKey2)
            val key3Bytes = HexUtils.hexToBytes(partialKey3)
            val ksnBytes = HexUtils.hexToBytes(ksn)

            // Validate key lengths
            if (key1Bytes.size != 16 || key2Bytes.size != 16 || key3Bytes.size != 16) {
                throw IllegalArgumentException("Each partial key must be 16 bytes (128 bits)")
            }
            if (ksnBytes.size != 10) {
                throw IllegalArgumentException("KSN must be 10 bytes (80 bits)")
            }

            // Combine partial keys to create BDK
            val bdkBytes = KeyUtils.combinePartialKeys(key1Bytes, key2Bytes, key3Bytes)
            println("Generated BDK: ${HexUtils.bytesToHex(bdkBytes)}")

            // Generate IPEK
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