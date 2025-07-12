package AG_FinTrust;

import java.sql.Connection;

interface PinServicesInterface extends AG_FinTrustInterface{
    boolean isAccountExists(Connection conn);
    void updatePin(Connection conn);

}
