package AG_FinTrust;

import java.sql.Connection;

public interface AccountDetailsInterface extends AG_FinTrustInterface{
    void viewMyAccountDetails(Connection conn);
    void viewMyTransaction(Connection conn);
}
