package AG_FinTrust;

import java.sql.Connection;
/**
 * Interface for PIN-related services in the Transacto: AG_FinTrust system.
 * Declares methods for checking account existence and securely updating PINs.
 *
 * Implementations of this interface manage user identity validation
 * and ensure secure PIN updates using encoding mechanisms.
 *
 * @author Aditya Gupta
 * @version 1.0.0
 */
interface PinServicesInterface extends AG_FinTrustInterface{
    boolean isAccountExists(Connection conn);
    void updatePin(Connection conn);

}
