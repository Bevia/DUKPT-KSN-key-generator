This simulation demonstrates:
1. Terminal side:
    - Has the IPEK (which would be injected during terminal initialization)
    - Creates ISO Format 0 PIN blocks
    - Encrypts PIN blocks using the derived key
    - Manages KSN increment

2. HSM side:
    - Has the BDK
    - Derives the IPEK from BDK and KSN
    - Decrypts the PIN block
    - Extracts the original PIN

Note: This is a simplified simulation that:
- Uses XOR instead of actual Triple DES encryption
- Has a simplified KSN management
- Doesn't implement the full DUKPT key derivation process
- Doesn't include all security measures that would be present in a real implementation

In a real-world scenario:
1. The BDK would be stored securely in the HSM
2. The IPEK would be injected into the terminal during initialization
3. The terminal would never have access to the BDK
4. Proper Triple DES encryption would be used
5. Full DUKPT key derivation would be implemented
6. Additional security measures would be in place

When you run this, you'll see the complete flow of PIN encryption and decryption, simulating how it would work between a terminal and an HSM.


## HSM Simulation

The project includes a simulation of PIN encryption/decryption process between a terminal and HSM (Hardware Security Module).

### Simulation Components

1. **Terminal Simulator**
   - Holds the IPEK (Initial PIN Encryption Key)
   - Creates ISO Format 0 PIN blocks
   - Manages KSN (Key Serial Number) incrementing
   - Encrypts PIN data

2. **HSM Simulator**
   - Stores the BDK securely
   - Derives IPEK from BDK and KSN
   - Decrypts encrypted PIN blocks
   - Validates PIN format

### Simulation Flow

1. **Terminal-side Process**:
   ```bash
   PIN + PAN → PIN Block → Encrypt with IPEK → Encrypted PIN Block + KSN
   ```

2. **HSM-side Process**:
   ```bash
   Encrypted PIN Block + KSN + PAN → Decrypt → Original PIN
   ```

### Usage Example
bash java -jar dukpt-generator.jar --simulate
--pin "1234"
--pan "4532111122223333"

Example output:
=== DUKPT PIN Encryption/Decryption Simulation === PIN: 1234 PAN: 4532111122223333
BDK (in HSM): 0123456789ABCDEF0123456789ABCDEF Initial KSN: FFFF9876543210E00000 IPEK (in Terminal): FEDCBA9876543210FEDCBA9876543210
Terminal encrypting PIN... Encrypted PIN block: A1B2C3D4E5F6A7B8 Current KSN: FFFF9876543210E00001
HSM decrypting PIN... Decrypted PIN: 1234


### Security Notes

This simulation is for educational purposes and includes several simplifications:
- Uses XOR instead of actual Triple DES encryption
- Implements basic KSN management
- Omits some security measures present in real HSMs

In production environments:
- BDK never leaves the HSM
- IPEK is securely injected into terminals
- Full Triple DES encryption is implemented
- Comprehensive security measures are in place
- Complete DUKPT key derivation process is used

### Limitations

The simulation has the following limitations:
1. Simplified encryption (XOR instead of Triple DES)
2. Basic KSN management
3. No physical security simulation
4. No key backup/restore procedures
5. No key verification values (KVVs)
6. No PIN block format variations

For production use, consult:
- ANSI X9.24 Part 1
- PCI PIN Security Requirements
- Local regulatory requirements