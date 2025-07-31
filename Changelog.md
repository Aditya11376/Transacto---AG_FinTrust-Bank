# Changelog – AG_FinTrust

All notable changes to this project will be documented in this file.

---

## [2.0.0] – August 2, 2025

### 🚀 New Features
- Introduced **UPI Services** module.
  - Users can now **create a UPI ID** linked to their account.
  - System validates UPI input format (alphanumeric and dot characters only).
  - UPI IDs follow format: `<custom_id>@agft`.
  - Only one UPI ID allowed per account.
- Added option to **display account details using UPI ID** with colorful console output.
- Introduced:
  - `UPI.java` – a sealed class handling UPI creation & validation.
  - `UPITransactionServices.java` – a sealed class handling UPI creation & validation.
  - `UPITransactionServices` interface – abstracting UPI operations.

### 🔧 Enhancements
- Database schema extended with `upi_details` table.
- Console output enhanced using ANSI colors for better UX.
- Updated documentation and project versioning across all modules.

---

## [1.0.0] – July 20, 2025

### 🎉 Initial Release
- Core features implemented:
  - Account creation with user details and PIN setup
  - Secure PIN encoding using Base64
  - Deposit, Withdrawal, and Transfer functionalities
  - Account balance and transaction viewing
- JDBC integration with MySQL backend
- Modular class structure with separate service interfaces

---

