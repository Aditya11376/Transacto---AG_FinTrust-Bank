package AG_FinTrust;

interface TransactionServicesInterface extends AG_FinTrustInterface{
    boolean logTransaction();
    boolean checkAccountExist(int acc);
    boolean checkSufficientBalance();
    void moneyTransferTransaction();
    void moneyWithdrawTransaction();
    void moneyDepositTransaction();
}
