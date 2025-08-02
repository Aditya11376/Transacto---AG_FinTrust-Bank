# 💳 Transacto: AG_FinTrust

**Transacto: AG_FinTrust** is a secure command-line banking system developed in Java. It enables users to create accounts, manage balances, perform transactions (deposit, withdrawal, fund transfer), and securely update PINs — all integrated with a MySQL database using JDBC.

---

## 🚀 Features – Transacto: AG_FinTrust

- 🔐 **Secure PIN Management**  
  All PINs are securely encoded using Base64 before being stored or matched in the database.

- 🧾 **Account Creation with Validation**  
  User input is validated for age, contact number, gender, and other fields before account creation.

- 💳 **Unique Account Number Generation**  
  Each new account is assigned a unique account number using a serial generator.

- 💰 **Banking Transactions**
   - **Deposit**: Add funds to your account securely.
   - **Withdraw**: Withdraw funds after PIN verification.
   - **Transfer**: Transfer money between two accounts with full transactional safety.

- 🔁 **Transactional Safety with Rollback/Commit**  
  Ensures data integrity — transactions are committed only on success; otherwise, a rollback is triggered.

- 📄 **Account and Transaction History Viewer**  
  View detailed account info and transaction logs including time, amount, intent, and description.

- 🔗 **JDBC-Based MySQL Integration**  
  The entire system uses robust SQL queries via JDBC `PreparedStatement` to prevent SQL injection.

- 🔐 **PIN Update Module**  
  Allows users to securely update their PIN after verifying personal identity (DOB, contact, etc.).

- 📲 **UPI Integration**
   - Create UPI IDs linked to your account
   - Transfer money using UPI ID with secure PIN validation
   - Log transactions with optional custom description

- 🧪 **Robust Input Handling**  
  A centralized `InputUtil` class offers a shared `Scanner` instance for consistent input management across modules.

- 🧠 **Interface-Driven Modular Design**  
  System follows interface-based separation for flexibility and future enhancements.

---

## 🏗️ Project Structure

```
AG_FinTrust/
├── Interfaces/
│   ├── AG_FinTrustInterface.java              # Marker interface for all service interfaces
│   ├── AccountInterface.java                  # Interface for account creation
│   ├── AccountDetailsInterface.java           # Interface for viewing account and transaction details
│   ├── TransactionServicesInterface.java      # Interface for deposit, withdraw, and transfer operations
│   ├── PinServicesInterface.java              # Interface for PIN update service
│   └── UPITransactionServicesInterface.java   # Interface for UPI-based transactions
│
├── Main.java                   # Entry point – UI menu for banking & UPI services
├── InputUtil.java              # Centralized Scanner utility (static access)
├── PinEncoderDecoder.java      # Secure Base64 encoder/decoder for PINs
│
├── Account.java                # Handles account creation, input validation, and PIN setting
├── AccountDetails.java         # View account details and transaction history
├── TransactionServices.java    # Handles deposit, withdraw, and fund transfer (with logging)
├── PinServices.java            # Updates PIN after identity verification
│
├── UPI.java                    # Sets up UPI ID and fetches account via UPI
├── UPITransactionServices.java# UPI-based transfer logic with PIN and description

```

---

## 🛠️ Technologies Used

- **Java 21**
- **MySQL**
- **JDBC (Java Database Connectivity)**
- **Base64 (for PIN encryption)**
- **OOP (Object-Oriented Programming)**

---

# 🗄️ Database Schema – Transacto: AG_FinTrust

---

## 📘 Table: `accounts`

| Field          | Type         | Null | Key | Default           | Extra             |
|----------------|--------------|------|-----|-------------------|-------------------|
| serial_number  | int          | NO   | UNI | NULL              | auto_increment    |
| account_name   | varchar(50)  | NO   |     | NULL              |                   |
| age            | int          | NO   |     | NULL              |                   |
| gender         | varchar(15)  | YES  |     | NULL              |                   |
| date_of_birth  | date         | NO   |     | NULL              |                   |
| email          | varchar(100) | YES  |     | NULL              |                   |
| contact_number | varchar(10)  | YES  |     | NULL              |                   |
| account_number | int          | NO   | PRI | NULL              |                   |
| balance        | double       | YES  |     | 0                 |                   |
| address        | text         | YES  |     | NULL              |                   |
| created_at     | timestamp    | YES  |     | CURRENT_TIMESTAMP | DEFAULT_GENERATED |

---

## 🔐 Table: `pin_details`

| Field          | Type | Null | Key | Default | Extra          |
|----------------|------|------|-----|---------|----------------|
| serial_number  | int  | NO   | PRI | NULL    | auto_increment |
| account_number | int  | NO   | UNI | NULL    |                |
| pin            | text | NO   |     | NULL    |                |

---

## 💳 Table: `upi_details`

| Field          | Type        | Null | Key | Default | Extra |
|----------------|-------------|------|-----|---------|-------|
| account_number | int         | NO   | PRI | NULL    |       |
| upi            | varchar(50) | NO   | UNI | NULL    |       |

---

## 🔁 Table: `transaction_history`

| Field         | Type                                  | Null | Key | Default           | Extra             |
|---------------|---------------------------------------|------|-----|-------------------|-------------------|
| serial_number | int                                   | NO   | PRI | NULL              | auto_increment    |
| from_account  | int                                   | YES  | MUL | NULL              |                   |
| to_account    | int                                   | YES  | MUL | NULL              |                   |
| balance       | double                                | NO   |     | NULL              |                   |
| transfer_type | enum('Deposit','Transfer','Withdraw') | NO   |     | NULL              |                   |
| at_time       | timestamp                             | YES  |     | CURRENT_TIMESTAMP | DEFAULT_GENERATED |
| intent        | enum('Direct','UPI')                  | NO   |     | Direct            |                   |
| description   | varchar(255)                          | YES  |     | NULL              |                   |


---

## 🚀 Getting Started

1. **Clone the repository**
   ```bash
   git clone https://github.com/aditya11376/transacto-ag-fintrust.git
   cd transacto-ag-fintrust
   ```

2. **Setup MySQL Database**
    - Create database: `ag_fintrust`
    - Create tables: `accounts`, `pin_details`, `transaction_history`, `upi_Details`

3. **Update DB Credentials**
    - Inside `Main.java` or a config section:
      ```java
      String url = "jdbc:mysql:-------------/ag_fintrust";
      String username = "your_username";
      String password = "your_password";
      ```

4. **Run the Application**
   ```bash
   javac AG_FinTrust/*.java
   java AG_FinTrust.Main
   ```

---

## 🧪 Sample Functionalities

- 🏦 **Create New Account** with proper validation and secure PIN setup
- 💵 **Deposit or Withdraw Funds** with real-time balance update and PIN verification
- 🔁 **Transfer Money** between accounts using `Account Number` or `UPI-ID`
- 📄 **View Account Details** securely after PIN authentication
- 🧾 **Access Transaction History** in tabular format (timestamp, intent, description)
- 🔐 **Update/Reset PIN** after verifying identity (name, contact, DOB, etc.)
- ⚙️ **JDBC-backed** operations for MySQL integration with rollback safety


---

### 📈 Future Enhancements

- **🔐 OTP-Based Authentication**  
  Integrate One-Time Password (OTP) verification during login and sensitive operations to enhance account security. *(Planned for v3.2.0)*

- **🔒 Secure PIN Storage with Hashing (bcrypt)**  
  Upgrade the current Base64 encoding system to a stronger password hashing algorithm like `bcrypt`, ensuring robust protection of user credentials.  
  ✅ Introduced in: **v2.1.0**  
  📦 Patch UPI Account Support: **v2.1.1**

- **🚫 PIN Attempt Limiting**  
  Enforce a maximum of 3 incorrect PIN attempts to prevent brute-force attacks. Automatically exit or block access after limit exceeded.  
  ✅ Fixed in: **v2.1.2**

- **💸 Interest Calculation on Account Balance**  
  Add support for periodic interest accumulation on savings, configurable interest rates, and automatic crediting to balance.  
  ✅ Introduced in: **v3.1.0**

---

### 📦 Version Roadmap (Summary)

- **v1.0.0** – Initial release of `AG_FinTrust` core banking system *(Released: 20 July 2025)*
- **v2.0.0** – Added UPI services module *(Released: 02 August 2025)*
- **v2.1.0** – Minor patch: UPI-specific `bcrypt` account handling
- **v2.1.1** – Bugfix: Exits after 3 failed PIN attempts
- **v3.1.0** – Major upgrade: Added interest calculation module

- ### 📌 Version History
Want to see what's changed?  
👉 [View full changelog →](./Changelog.md)

---

## 📷 Hierarchical Class Structure

![Class Structure](docs/class_structure.png)

---

## 📄 License

This project is licensed under the **Apache License 2.0**. See the [LICENSE](License) file for details.

---

## 👨‍💻 Author

- **Aditya Gupta**
- GitHub: [@aditya11376](https://github.com/aditya11376)

---

## 🎓 Academic Info

> This project was developed for the subject **"Minor Project in Java"** (IT – 4th Semester) under self-guided supervision.
