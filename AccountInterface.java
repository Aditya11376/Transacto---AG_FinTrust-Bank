package AG_FinTrust;
import java.sql.Connection;


/**
 * Interface for account creation in the Transacto: AG_FinTrust banking system.
 * Defines the contract for initializing a new bank account.
 *
 * @author Aditya Gupta
 * @version 1.0.0
 */
interface AccountInterface extends AG_FinTrustInterface{
    public void createMyAccount(Connection connection) ;
}

