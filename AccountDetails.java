package AG_FinTrust;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static AG_FinTrust.InputUtil.sc;

/**
 * AG_FinTrust - AccountDetails.java
 *
 * This class provides functionalities to view the details and transaction history
 * of an account in the Transacto: AG_FinTrust system.
 *
 * <p>Responsibilities include:</p>
 * <ul>
 *   <li>Verifying account credentials (Account Number + PIN)</li>
 *   <li>Displaying personal account details like name, balance, and contact</li>
 *   <li>Displaying detailed transaction history of the account</li>
 * </ul>
 *
 * <p>This class extends {@link Account} and implements {@link AccountDetailsInterface}.</p>
 *
 * @author Aditya Gupta
 * @version 2.0.0
 * @since August 02, 2025
 */

public class AccountDetails extends Account implements AccountDetailsInterface {
    private int accountNumber ;
    private String PIN;

    void takeDetailsToViewAccountDetails(){
        System.out.println("\nEnter your details :- ");

        System.out.print("Enter your Account Number : ");
        accountNumber = sc.nextInt();

        while(true){
            System.out.print("Enter your 6-Digit PIN : ");
            PIN = sc.next();
            if(Account.isValidPin(PIN)){
                break;
            }else{
                System.out.println("Invalid PIN.\n");
            }
        }
    }

    @Override
    public void viewMyAccountDetails(Connection conn){
        try{
            takeDetailsToViewAccountDetails();
            String query = "select a.account_name ,a.account_number, a.balance, a.contact_number " +
                    "from accounts as a " +
                    "join pin_details as p " +
                    "on a.account_number = p.account_number " +
                    "where p.account_number = ? and p.pin = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(query);

            preparedStatement.setInt(1,accountNumber);
            preparedStatement.setString(2,PinEncoderDecoder.encode(PIN));

            ResultSet resultSet = preparedStatement.executeQuery();

            if(resultSet.next()){
                System.out.println("\n===== Account Details =====");
                System.out.println("👤 Account Holder Name : " + resultSet.getString("account_name"));
                System.out.println("🏦 Account Number       : " + resultSet.getString("account_number"));
                System.out.println("💰 Available Balance    : ₹" + resultSet.getString("balance"));
                System.out.println("📞 Contact Number       : " + resultSet.getString("contact_number"));
                System.out.println("==============================\n");
            }else{
                System.out.println("No Such Account Exists.\nThank You!");
            }
            preparedStatement.close();

        }catch (SQLException e ){
            System.out.println(e.getMessage());
        }
    }

    //view my transaction
    @Override
    public void viewMyTransaction(Connection connection) {
        String queryTransaction = "SELECT * FROM transaction_history AS th " +
                "JOIN pin_details AS p ON (p.account_number = th.from_account OR p.account_number = th.to_account) " +
                "WHERE p.account_number = ? AND p.pin = ? " +
                "ORDER BY th.at_time ASC";

        takeDetailsToViewAccountDetails();

        try (PreparedStatement p = connection.prepareStatement(queryTransaction)) {
            p.setInt(1, accountNumber);
            p.setString(2, PinEncoderDecoder.encode(PIN));

            try (ResultSet resultSet = p.executeQuery()) {
                boolean hasResults = false;

                System.out.println("\n" + "=".repeat(114));
                System.out.printf("%" + (114 / 2 + 12) + "s\n", " Transaction History ");
                System.out.println("=".repeat(114));

                System.out.printf("%-5s  %-10s  %-10s  %-10s  %-10s %-20s %-10s %-20s\n",
                        "Txn#", "From A/C", "To A/C", "Type", "Amount", "Time", "Intent", "Description");
                System.out.println("-----  ----------  ----------  ----------  ---------- -------------------- ---------- --------------------");

                int count = 1;
                while (resultSet.next()) {
                    hasResults = true;

                    int fromAccount = resultSet.getInt("from_account");
                    int toAccount = resultSet.getInt("to_account");
                    double amount = resultSet.getDouble("balance");
                    String type = resultSet.getString("transfer_type");
                    String time = resultSet.getString("at_time");
                    String intent = resultSet.getString("intent");
                    String des = resultSet.getString("description");

                    System.out.printf("%-5d  %-10s  %-10s  %-10s  ₹%-9.2f %-20s %-10s %-20s\n",
                            count++, (fromAccount == 0 ? "-" : String.valueOf(fromAccount)), (toAccount == 0 ? "-": String.valueOf(toAccount)), type, amount, time, intent,
                            (des == null || des.isEmpty() ? "-" : des));
                }

                if (!hasResults) {
                    System.out.println("\nNo transactions found for this account.");
                }

                System.out.println("=".repeat(114));
            }
        } catch (SQLException e) {
            System.out.println("e.getMessage()");
        }
    }


}
