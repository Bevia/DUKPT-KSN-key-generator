## DUKPT with AES encryption works with Format 4, not Format 0.

### 🔐 Here's the breakdown:

Format 0 is traditionally used with TDES (Triple DES) encryption and follows the ISO 9564-1 standard 
for PIN block formatting. It’s widely supported but considered less secure today.

Format 4 is designed specifically for AES encryption, offering stronger security. 
It uses random padding and a different structure to accommodate AES block sizes and encryption modes

🔧 Format 4 Construction Overview
Format 4 is defined in ISO 9564-1 and is designed for AES encryption. 
It differs from older formats (like Format 0 for TDES) by using random padding 
and a structure that aligns with AES block sizes.

### 🧱 Structure of Format 4 PIN Block:
PIN Length Indicator: 1 byte (e.g., 04 for a 4-digit PIN)

PIN Digits: Actual PIN digits (e.g., 1234)

Random Padding: Random values to fill the block to 16 bytes

Encryption: Entire block is encrypted using AES (typically AES-128 or AES-256)

This format avoids predictable patterns and supports stronger cryptographic practices.

### 🛠️ Practical Implementation Steps
Generate the PIN Block:

Start with the PIN length and digits.

Add random padding to reach 16 bytes.

Example (hex): 041234A1B2C3D4E5F6A7B8C9D0E1F2

Encrypt with AES:

Use AES in a secure mode (e.g., CBC or CTR).

Include an Initialization Vector (IV) if required by the mode.

Encrypt the full 16-byte block.

Transmit Securely:

Send the encrypted PIN block along with metadata (e.g., IV, key ID).

Ensure key management follows DUKPT standards.