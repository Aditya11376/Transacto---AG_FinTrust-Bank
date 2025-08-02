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

- 📂 **Database Schema Extended**
    - Introduced `upi_details` table for UPI-based account mapping and transactions.
    - Enhanced `transaction_history` table by adding two new columns:
        - `intent` – indicates transaction mode (`Direct` or `UPI`)
        - `description` – allows storing optional notes about each transaction

- 🧾 **UPI Services Introduced**  
  Added UPI-based transaction support:
    - Users can generate UPI IDs linked to their accounts
    - Transfer money using UPI between accounts
    - View UPI-linked account details

- 🎨 **Enhanced Console Output**  
  Used ANSI color codes in the terminal to improve readability and user experience for service menus and success/error messages.

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