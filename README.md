
# DUKPT Key Generator Tool

A command-line tool for generating DUKPT (Derived Unique Key Per Transaction) keys, specifically designed for secure key management in financial cryptographic operations. This tool supports generating Initial PIN Encryption Keys (IPEK) from either a Base Derivation Key (BDK) or three partial key components.

## Overview

The DUKPT Key Generator implements the ANSI X9.24 standard for retail financial services. It provides functionality to:
- Generate IPEKs from a BDK and KSN
- Combine three partial key components to create a BDK (recommended for enhanced security)
- Validate hex formats and key lengths
- Process Key Serial Numbers (KSN)

## Requirements

- JDK 17 or higher
- Kotlin 1.9.x
- Gradle build tool

## Building the Project
bash ./gradlew build

## Usage

### Option 1: Using Three Partial Keys (Recommended)

This is the more secure method where the BDK is generated from three separate components:
bash java -jar dukpt-generator.jar
-k1 <partial-key-1>
-k2 <partial-key-2>
-k3 <partial-key-3>
-k


Example:
bash java -jar dukpt-generator.jar
-k1 0123456789ABCDEF0123456789ABCDEF
-k2 FEDCBA9876543210FEDCBA9876543210
-k3 AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA00
-k 0123456789ABCDEF0123



### Key Requirements

- Each partial key (`k1`, `k2`, `k3`) must be:
  - 32 hexadecimal characters
  - Representing 16 bytes (128 bits)
- KSN must be:
  - 20 hexadecimal characters
  - Representing 10 bytes (80 bits)

## Security Considerations

1. **Partial Key Management**:
   - Store each partial key component separately
   - Different authorized personnel should manage different components
   - Never store or transmit all components together

2. **Key Length Requirements**:
   - All partial keys must be exactly 16 bytes (128 bits)
   - The final BDK (generated from XORing partial keys) will also be 16 bytes
   - KSN must be exactly 10 bytes (80 bits)

3. **Key Format**:
   - All keys must be provided in hexadecimal format
   - Valid characters are 0-9 and A-F (case insensitive)

## Program Output

The tool will output:
1. The generated BDK (when using partial keys)
2. The derived IPEK

Example output:
Generated BDK: 89ABCDEF0123456789ABCDEF01234567 
Derived IPEK: FEDCBA9876543210FEDCBA9876543210


## Error Handling

The program includes validation for:
- Hex format of all inputs
- Key lengths
- Component integrity

Error messages will be displayed for:
- Invalid hex format
- Incorrect key lengths
- Missing required parameters

## Development

### Project Structure
src/ ├── main/ │ └── kotlin/ │ └── org/ │ └── corebaseit/ │ └── dukptksnkeygenerator/ │ ├── DukptCliTool.kt │ ├── KeyUtils.kt │ ├── HexUtils.kt │ └── Main.kt └── test/ └── kotlin/ └── org/ └── corebaseit/ └── dukptksnkeygenerator/ └── [test files]


### Key Classes

- `DukptCliTool`: Main CLI interface handling user input
- `KeyUtils`: Utilities for key operations and combinations
- `HexUtils`: Hex string conversion utilities
- `Dukpt`: Core DUKPT operations implementation

## Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## License

[Add your chosen license here]

## References

- ANSI X9.24-1:2009 Retail Financial Services Symmetric Key Management
- [Add other relevant references]

