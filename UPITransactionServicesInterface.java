package AG_FinTrust;

/**
 * AG_FinTrust - UPITransactionServicesInterface.java
 *
 * <p>This interface defines the contract for UPI-based transaction services in the AG_FinTrust system.</p>
 *
 * <p>Responsibilities include:</p>
 * <ul>
 *   <li>Performing UPI-based fund transfers between accounts</li>
 *   <li>Logging UPI transactions with optional descriptions</li>
 * </ul>
 *
 * <p>This interface is implemented by {@link UPITransactionServices}, which provides
 * the concrete behavior for secure and validated UPI transactions.</p>
 *
 * @author Aditya Gupta
 * @version 2.0.0
 * @since August 02, 2025
 */
public sealed interface UPITransactionServicesInterface extends AG_FinTrustInterface permits UPITransactionServices {
    void UPITransaction();
    boolean UPILogTransaction();

}
