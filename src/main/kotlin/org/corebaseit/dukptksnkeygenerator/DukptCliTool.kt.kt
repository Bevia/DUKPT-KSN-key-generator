// File: cli/src/jvmMain/kotlin/org/corebaseit/dukptksnkeygenerator/DukptCliTool.kt

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
        parser.parse(args)

        val bdkBytes = HexUtils.hexToBytes(bdk)
        val ksnBytes = HexUtils.hexToBytes(ksn)

        val ipek = Dukpt.deriveIPEK(bdkBytes, ksnBytes)
        println("Derived IPEK: ${HexUtils.bytesToHex(ipek)}")
    }
}