package AG_FinTrust;

import java.sql.Connection;
/**
 * AG_FinTrust - AccountDetailsInterface.java
 *
 * <p>
 * Interface for viewing account and transaction details in the
 * Transacto: AG_FinTrust banking system.
 * </p>
 *
 * <p>
 * Implementing classes must define how to securely retrieve and
 * display account information and transaction history using a valid
 * database connection.
 * </p>
 *
 * <p><strong>Responsibilities:</strong></p>
 * <ul>
 *   <li>Display account holder information (name, balance, contact, etc.)</li>
 *   <li>Display past transactions made to or from the account</li>
 * </ul>
 *
 * @author Aditya Gupta
 * @version 2.0.0
 * @since August 02, 2025
 */

public interface AccountDetailsInterface extends AG_FinTrustInterface{
    void viewMyAccountDetails(Connection conn);
    void viewMyTransaction(Connection conn);
}
