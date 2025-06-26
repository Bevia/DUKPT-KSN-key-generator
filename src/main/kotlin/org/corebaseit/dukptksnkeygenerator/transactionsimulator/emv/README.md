📦 Field 55 = “EMV Sequence”

✳️ Where Field 55 Is Used
•	In online EMV authorization (DE55)
•	In EMV test simulators and cert tools
•	During ARQC generation and issuer validation
•	For EMV kernel debug and logs


✅ What is Field 55?

Field 55 in ISO 8583 (used in EMV transactions) contains the EMV TLV data exchanged between the POS terminal and the acquirer/issuer.

It’s essentially a container for the full EMV cryptographic context during an online authorization.

⸻

🔐 What’s Inside Field 55?

Field 55 is made of TLV (Tag-Length-Value) encoded EMV data like:

* Tag	Meaning	                                Example
* 9F02	Amount, Authorized	                    000000010000
* 82	AIP (Application Interchange Profile)	1800
* 9F36	ATC (Application Transaction Counter)	0023
* 9F27	CID (Cryptogram Information Data)	    80
* 9F26	ARQC (Authorization Request Cryptogram)	Encrypted 8 bytes
* 9F10	Issuer Application Data	                ICC-defined
* 9F1A	Terminal Country Code	                0978
* 5F2A	Transaction Currency Code	            0978
* 9C	Transaction Type	                    00 = purchase

-------------

Here’s a clear breakdown of the additional EMV tags you’ve just added — 
including what each one means and what role it plays during an EMV transaction.

⸻

🔍 Breakdown of EMV Tags

1. CID – Cryptogram Information Data (9F27)
   •	Length: 1 byte
   •	Purpose: Tells the issuer what kind of cryptogram is included (e.g., ARQC, TC, AAC)
   •	Common values:
   •	80 → ARQC (Authorization Request Cryptogram)
   •	40 → TC (Transaction Certificate)
   •	00 → AAC (Application Authentication Cryptogram—declined)

🔐 This tells the issuer how the card processed the transaction.

⸻

2. IAD – Issuer Application Data (9F10)
   •	Length: Variable (usually 7 to 20 bytes)
   •	Purpose: Proprietary data from the issuer stored in the card and returned in ARQC or TC
   •	Contents: Depends on the card issuer — may include CVR (Card Verification Results), counters, cryptographic flags, etc.

🧠 This is where the card stores internal state the issuer can later verify.

⸻

3. ATC – Application Transaction Counter (9F36)
   •	Length: 2 bytes
   •	Purpose: Increments on every EMV transaction. Used in cryptograms (ARQC/TC).
   •	Example: 0001, 0002, 0003…

🔐 This ensures freshness and helps detect transaction replay fraud.

⸻

4. TVR – Terminal Verification Results (95)
   •	Length: 5 bytes
   •	Purpose: Bitfield showing terminal checks (e.g., CVM passed/failed, fallback used, etc.)
   •	Each bit signals something like:
   •	Offline data auth failed
   •	CVM failed
   •	Issuer authentication failed
   •	Etc.

🧪 Think of it as a report from the terminal about how the transaction went.

⸻

5. UN – Unpredictable Number (9F37)
   •	Length: 4 bytes
   •	Purpose: Random challenge from the terminal to prevent replay
   •	Value: Should be random or pseudo-random (e.g., CAFEBABE, 12345678)

🔐 Combined with ATC to prevent cloned transactions.

⸻

6. ARQC – Authorization Request Cryptogram (9F26)
   •	Length: 8 bytes
   •	Purpose: The cryptographic MAC generated using the DUKPT session key
   •	Includes: UN + ATC + AIP + TVR (and sometimes more)

🔐 The most important field in online EMV auth. The issuer uses it to verify that the card is genuine.

⸻

✅ Why All These Matter

Together, these fields form the cryptographic fingerprint of the transaction. When the issuer receives them, it:
•	Validates the ARQC using the session key derived from PAN + ATC
•	Checks IAD and CID to confirm the card state
•	Uses TVR to decide risk level (e.g., was PIN entered, was fallback used?)
•	Updates counters and logs via ATC and UN

