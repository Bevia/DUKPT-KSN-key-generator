package org.corebaseit.dukptksnkeygenerator.utils

import kotlinx.cli.ArgParser
import kotlinx.cli.ArgType
import kotlinx.cli.required
import org.corebaseit.dukptksnkeygenerator.Dukpt


class DukptCliTool(private val args: Array<String>) {

    private val parser = ArgParser("dukpt-cli")
    private val partialKey1: String by parser.option(
        ArgType.String, shortName = "k1", description = "First Partial Key Component (hex)"
    ).required()
    private val partialKey2: String by parser.option(
        ArgType.String, shortName = "k2", description = "Second Partial Key Component (hex)"
    ).required()
    private val partialKey3: String by parser.option(
        ArgType.String, shortName = "k3", description = "Third Partial Key Component (hex)"
    ).required()
    private val ksnInput: String by parser.option(
        ArgType.String, shortName = "k", description = "Key Serial Number (hex)"
    ).required()

    /** Exposed properties after parsing */
    lateinit var bdk: ByteArray
        private set

    lateinit var ksn: ByteArray
        private set

    fun run() {
        try {
            parser.parse(args)

            listOf(partialKey1, partialKey2, partialKey3, ksnInput).forEach {
                require(isValidHex(it)) { "Invalid hex format for key: $it" }
            }

            val key1Bytes = HexUtils.hexToBytes(partialKey1)
            val key2Bytes = HexUtils.hexToBytes(partialKey2)
            val key3Bytes = HexUtils.hexToBytes(partialKey3)
            ksn = HexUtils.hexToBytes(ksnInput)

            require(key1Bytes.size == 16 && key2Bytes.size == 16 && key3Bytes.size == 16) {
                "Each partial key must be 16 bytes (128 bits)"
            }
            require(ksn.size == 10) {
                "KSN must be 10 bytes (80 bits)"
            }

            bdk = KeyUtils.combinePartialKeys(key1Bytes, key2Bytes, key3Bytes)

            println("Generated BDK: ${HexUtils.bytesToHex(bdk)}")
            val ipek = Dukpt.deriveIPEK(bdk, ksn)
            println("Derived IPEK: ${HexUtils.bytesToHex(ipek)}")

        } catch (e: Exception) {
            System.err.println("Error: ${e.message}")
            System.exit(1)
        }
    }

    private fun isValidHex(input: String): Boolean = input.matches(Regex("^[0-9A-Fa-f]+$"))
}