package AG_FinTrust;
import java.sql.Connection;

interface AccountInterface extends AG_FinTrustInterface{
    public void createMyAccount(Connection connection) ;
}

