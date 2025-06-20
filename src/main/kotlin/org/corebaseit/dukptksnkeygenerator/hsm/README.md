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
