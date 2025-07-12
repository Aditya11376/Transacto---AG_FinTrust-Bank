package AG_FinTrust;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static AG_FinTrust.InputUtil.sc;

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
    public void viewMyTransaction(Connection connection){
        String queryTransaction =  "SELECT * FROM transaction_history AS th " +
                "JOIN pin_details AS p ON (p.account_number = th.from_account or p.account_number = th.to_account) " +
                "WHERE p.account_number = ? AND p.pin = ? " +
                "ORDER BY th.at_time ASC";
        takeDetailsToViewAccountDetails();

        try(PreparedStatement p = connection.prepareStatement(queryTransaction)){

            p.setInt(1,accountNumber);
            p.setString(2,PinEncoderDecoder.encode(PIN));

            ResultSet resultSet = p.executeQuery();
            boolean hasResults = false;
            System.out.println("\n====== Transaction History ======\n");

            int count = 1;
            while (resultSet.next()) {
                hasResults = true;

                int fromAccount = resultSet.getInt("from_account");
                int toAccount = resultSet.getInt("to_account");
                double amount = resultSet.getDouble("balance");
                String type = resultSet.getString("transfer_type");
                String time = resultSet.getString("at_time");

                System.out.println("\nTransaction #" + (count++));
                System.out.println("→ Type          : " + type);
                System.out.println("→ From Account  : " + fromAccount);
                System.out.println("→ To Account    : " + toAccount);
                System.out.printf ("→ Amount        : ₹%.2f\n", amount);
                System.out.println("→ Timestamp     : " + time);
                System.out.println("-----------------------------");
            }

            if(!hasResults){
                System.out.println("No Transaction found for this account");
            }

            resultSet.close();

        }catch (SQLException e){
            System.out.println("Error fetching transactions: ");
        }
    }

}
