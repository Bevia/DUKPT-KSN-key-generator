package org.corebaseit.dukptksnkeygenerator

fun main() {
    // Run the original DUKPT key generation
    val args = arrayOf(
        "-k1", "0123456789ABCDEF0123456789ABCDEF",
        "-k2", "FEDCBA9876543210FEDCBA9876543210",
        "-k3", "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA00",
        "-k", "0123456789ABCDEF0123"
    )
    DukptCliTool(args).run()

    println("\n=== Running PIN Encryption/Decryption Simulation ===\n")

    // Run the PIN encryption/decryption simulation
    DukptSimulator().runSimulation()
}