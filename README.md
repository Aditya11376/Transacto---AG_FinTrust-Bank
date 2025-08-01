# 💳 Transacto: AG_FinTrust

**Transacto: AG_FinTrust** is a secure command-line banking system developed in Java. It enables users to create accounts, manage balances, perform transactions (deposit, withdrawal, fund transfer), and securely update PINs — all integrated with a MySQL database using JDBC.

---

## 📌 Features

- 🔐 **Secure PIN Management** with Base64 encoding
- 🧾 **Account Creation** with user input validation
- 💰 **Transactions**: Deposit, Withdraw, and Transfer funds
- 📄 **Account and Transaction History View**
- 🔁 **Transaction rollback** on failure to ensure data integrity
- 🔗 **JDBC-based MySQL integration**
- 🧮 **Unique Account Number** generation
- 🧪 **Robust Input Handling** using shared utility scanner

---

## 🏗️ Project Structure

```
AG_FinTrust/
├── Interfaces/
│   ├── AccountInterface.java
│   ├── AccountDetailsInterface.java
│   ├── TransactionServicesInterface.java
│   └── PinServicesInterface.java
├── Main.java                   # Entry point – Service menu and user navigation
├── Account.java                # Account creation, validation, PIN setup
├── AccountDetails.java         # View account details and transaction history
├── TransactionServices.java    # Deposit, withdrawal, transfer functionality
├── PinServices.java            # Update PIN after verifying identity
├── InputUtil.java              # Centralized Scanner utility
├── PinEncoderDecoder.java      # Base64 encoding and decoding of PIN
```

---

## 🛠️ Technologies Used

- **Java 21**
- **MySQL**
- **JDBC (Java Database Connectivity)**
- **Base64 (for PIN encryption)**
- **OOP (Object-Oriented Programming)**

---

## 🗃️ Database Schema

1. **accounts**
    - `serial_number`  (UNI)
    - `account_number` (PK)
    - `account_name`
    - `age`
    - `email`
    - `date_of_birth`
    - `address`
    - `dob`
    - `gender`
    - `contact_number`
    - `balance`
    - `at_time`

2. **pin_details**
    - `serial_number`  (PK)
    - `account_number` (FK)
    - `pin` (encoded)

3. **transaction_history**
    - `serial_number` (PK)
    - `from_account`  (FK)
    - `to_account`    (FK)
    - `transfer_type` (Deposit/Withdraw/Transfer)
    - `balance`
    - `at_time`

---

## 🚀 Getting Started

1. **Clone the repository**
   ```bash
   git clone https://github.com/aditya11376/transacto-ag-fintrust.git
   cd transacto-ag-fintrust
   ```

2. **Setup MySQL Database**
    - Create database: `ag_fintrust`
    - Create tables: `accounts`, `pin_details`, `transaction_history`

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

- Create a new account with PIN
- Deposit or withdraw amount
- Transfer funds between accounts
- View account details and transactions details
- Reset/update forgotten PIN with identity verification

---

### 📈 Future Enhancements

- **OTP-Based Authentication**  
  Integrate One-Time Password (OTP) verification during login and sensitive operations to enhance account security.

- **Secure PIN Storage with Hashing**  
  Upgrade the current Base64 encoding system to a more secure hashing mechanism (e.g., SHA-256 or bcrypt) for storing and verifying PINs, ensuring better protection of user credentials.

- **Interest Calculation on Account Balance**  
  Introduce periodic interest computation on user account balances, similar to savings accounts, with configurable interest rates and automated balance updates.

---

## 📷 Hierarchical Class Structure

![Class Structure](docs/class_structure.png)

---

## 📌 Version History

Want to see what's changed?  
👉 [View full changelog →](./Changelog.md)

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
