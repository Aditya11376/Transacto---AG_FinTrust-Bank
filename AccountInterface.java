package AG_FinTrust;
import java.sql.Connection;

/**
 * Interface for account creation in the Transacto: AG_FinTrust banking system.
 * Defines the contract for initializing a new bank account.
 *
 * @author Aditya Gupta
 * @version 2.0.0
 * @since August 02, 2025
 */
public interface AccountInterface extends AG_FinTrustInterface{
    public void createMyAccount(Connection connection) ;
}

