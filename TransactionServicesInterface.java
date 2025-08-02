package AG_FinTrust;
/**
 * Interface defining the core transactional services such as deposit, withdrawal,
 * and money transfer for the Transacto: AG_FinTrust system.
 *
 * Implementing classes must handle transaction validation, balance checks,
 * and transaction logging.
 *
 * @author Aditya Gupta
 * @version 2.0.0
 * @since August 02, 2025
 */

public interface TransactionServicesInterface extends AG_FinTrustInterface{
    boolean logTransaction();
    boolean checkAccountExist(int acc);
    boolean checkSufficientBalance();
    void moneyTransferTransaction();
    void moneyWithdrawTransaction();
    void moneyDepositTransaction();
}
