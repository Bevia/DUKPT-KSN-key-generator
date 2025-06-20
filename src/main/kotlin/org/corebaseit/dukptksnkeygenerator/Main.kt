package org.corebaseit.dukptksnkeygenerator

fun main() {
    val args = arrayOf(
        "-k1", "0123456789ABCDEF0123456789ABCDEF", // First partial key
        "-k2", "FEDCBA9876543210FEDCBA9876543210", // Second partial key
        "-k3", "FEDCBAAA76543210FEDCBBC876543210", // Third partial key
        "-k", "0123456789ABCDEF0123"               // KSN value
    )
    DukptCliTool(args).run()
}