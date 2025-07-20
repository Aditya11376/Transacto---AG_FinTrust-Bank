package AG_FinTrust;

import java.sql.Connection;

/**
 * Interface for viewing account and transaction details
 * in the Transacto: AG_FinTrust banking system.
 *
 * @author Aditya Gupta
 * @version 1.0.0
 * @since July 20, 2025
 */

public interface AccountDetailsInterface extends AG_FinTrustInterface{
    void viewMyAccountDetails(Connection conn);
    void viewMyTransaction(Connection conn);
}
