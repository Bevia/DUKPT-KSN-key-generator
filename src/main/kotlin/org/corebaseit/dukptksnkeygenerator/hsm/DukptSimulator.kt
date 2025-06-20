package org.corebaseit.dukptksnkeygenerator

class DukptSimulator {
    fun runSimulation() {
        // Test data
        val pin = "1234"
        val pan = "4532111122223333"

        println("Simulating DUKPT PIN encryption/decryption")
        println("=========================================")
        println("PIN: $pin")
        println("PAN: $pan")
        println()

        // Generate BDK (in real world, this would be in the HSM)
        val bdk = HexUtils.hexToBytes("0123456789ABCDEF0123456789ABCDEF")
        println("BDK (in HSM): ${HexUtils.bytesToHex(bdk)}")

        // Generate initial KSN
        val ksn = HexUtils.hexToBytes("FFFF9876543210E00000")
        println("Initial KSN: ${HexUtils.bytesToHex(ksn)}")

        // Derive IPEK (in real world, this would be injected into terminal)
        val ipek = Dukpt.deriveIPEK(bdk, ksn)
        println("IPEK (in Terminal): ${HexUtils.bytesToHex(ipek)}")
        println()

        // Create terminal and HSM simulators
        val terminal = TerminalSimulator(ipek, ksn)
        val hsm = HsmSimulator(bdk)

        // Simulate PIN encryption at terminal
        println("Terminal encrypting PIN...")
        val (encryptedPin, currentKsn) = terminal.encryptPin(pin, pan)
        println("Encrypted PIN block: ${HexUtils.bytesToHex(encryptedPin)}")
        println("Current KSN: ${HexUtils.bytesToHex(currentKsn)}")
        println()

        // Simulate PIN decryption at HSM
        println("HSM decrypting PIN...")
        val decryptedPin = hsm.decryptPin(encryptedPin, currentKsn, pan)
        println("Decrypted PIN: $decryptedPin")
    }
}