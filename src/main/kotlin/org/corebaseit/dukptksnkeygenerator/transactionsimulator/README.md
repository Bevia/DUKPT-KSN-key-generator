build a DUKPT key derivation simulator that:
1.	Generates derived keys per transaction (per KSN increment)
2.	Shows what gets encrypted using that key (e.g. PIN blocks, track data)
3.	Provides a way to trace or test each step — mimicking a real payment terminal behavior

This will evolve into a solid test harness or teaching tool. Let’s break this into a clear Kotlin class design.

⸻

🧠 What Happens in Real DUKPT Flow

Each transaction:
1.	Has a unique KSN (Key Serial Number) with a transaction counter.
2.	The terminal derives a Data Encryption Key (DEK) from the BDK + KSN.
3.	This derived key is used to encrypt sensitive data:
•	PIN block
•	Track 2 Equivalent Data
•	Cardholder Name / PAN (less common)