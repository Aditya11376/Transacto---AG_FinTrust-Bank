package AG_FinTrust;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static AG_FinTrust.InputUtil.sc;

/**
 * Handles UPI-based fund transfer between two accounts in the AG_FinTrust system.
 * It validates UPI IDs, checks balance and PIN, and logs the transaction with optional description.
 *
 * <p>This class extends the {@link UPI} class and implements {@link UPITransactionServicesInterface}.
 * It performs atomic transactions using JDBC with commit and rollback support.
 *
 * @author Aditya Gupta
 * @version 2.0.0
 * @since August 02, 2025
 */

public final class UPITransactionServices extends UPI implements UPITransactionServicesInterface{
    private String upiFrom;
    private String upiTo;
    private double balance;

    private int from;
    private int to;

    private String description;

    UPITransactionServices(Connection conn,String upi1,String upi2,double amount) {
        this.upiFrom= upi1;
        this.upiTo= upi2;
        this.balance=amount;
        super(conn);
    }

    private boolean checkUPIisAvailable(String upi){
        String query = "select count(*) from upi_Details where upi = ?";
        try(PreparedStatement p = super.conn.prepareStatement(query)){
            p.setString(1,upi);
            try(ResultSet resultSet = p.executeQuery()){
                if(resultSet.next()){
                    return resultSet.getInt(1)==1;
                }
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return false ;
    }

    private boolean checkSufficientBalance(String upi){
        String query = "select a.balance as amount from accounts as a join upi_Details as upi on upi.account_number = a.account_number where upi.upi = ?";
        try(PreparedStatement p = super.conn.prepareStatement(query)){
            p.setString(1,upi);
            try(ResultSet resultSet = p.executeQuery()){
                if(resultSet.next()){
                    return resultSet.getDouble("amount")>=this.balance;
                }
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return false;
    }

    private boolean matchPIN(String pin,String UPI){
        String query = "select count(*) from accounts as a join upi_details as upi on upi.account_number = a.account_number join pin_details as pin on pin.account_number = a.account_number where pin.pin = ? and upi.upi = ?";
        try(PreparedStatement p = super.conn.prepareStatement(query)){
            p.setString(1,PinEncoderDecoder.encode(pin));
            p.setString(2,UPI);
            try(ResultSet resultSet = p.executeQuery()){
                if(resultSet.next()){
                    return resultSet.getInt(1)==1;
                }
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return false;
    }

    @Override
    public boolean UPILogTransaction(){
        sc.nextLine();
        System.out.print("\nWant to add any description? (Yes/No): ");
        String response = sc.nextLine().trim().toLowerCase();

        if (response.equals("y") || response.equals("yes")) {
            System.out.print("Write description: ");
            this.description = sc.nextLine().trim();
        } else {
            this.description = null;
        }

        String setQuery = " select upi.account_number as accNumber from upi_details as upi where upi.upi = ?";
        try(PreparedStatement p = super.conn.prepareStatement(setQuery)){
            p.setString(1,this.upiFrom);
            try(ResultSet resultSet = p.executeQuery()){
                if(resultSet.next()){
                    this.from = resultSet.getInt("accNumber");
                }else{
                    return false;
                }
            }
            p.clearParameters();
            p.setString(1,this.upiTo);
            try(ResultSet resultSet = p.executeQuery()){
                if(resultSet.next()){
                    this.to = resultSet.getInt("accNumber");
                }else{
                    return false;
                }
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }

        String upiLogQuery = "insert into transaction_history(from_account,to_account,balance,transfer_type,intent,description) values (?,?,?,'Transfer','UPI',?)";
        try(PreparedStatement preparedStatement = super.conn.prepareStatement(upiLogQuery)){
            preparedStatement.setInt(1,this.from);
            preparedStatement.setInt(2,this.to);
            preparedStatement.setDouble(3,this.balance);
            preparedStatement.setString(4, this.description);


            int rows = preparedStatement.executeUpdate();
            if (rows > 0) {
                return true;
            } else {
                return false;
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return false;
    }

    @Override
    public void UPITransaction(){
        if (!checkUPIisAvailable(upiFrom)) {
            System.out.println("\n\u001B[31m⚠️ " + upiFrom + " Not Found! Transaction cancelled! 😔\u001B[0m");

            return;
        }

        if (upiFrom.equals(upiTo)) {
            System.out.println("\n\u001B[30mTransaction cancelled! ❌\u001B[0m");
            return;
        }

        if (!checkUPIisAvailable(upiTo)) {
            System.out.println("\n\u001B[31m⚠️ " + upiTo + " Not Found! Transaction cancelled! 😔\u001B[0m");
            return;
        }

        System.out.print("\n\nEnter your PIN : ");
        String pin = sc.next().trim();

        if (!pin.matches("\\d{4,6}") || pin.isEmpty()) {
            System.out.println("\n\u001B[31m❗ Invalid PIN format! Transaction cancelled. 😞\u001B[0m");
            return;
        }

        if (!matchPIN(pin, this.upiFrom)) {
            System.out.println("\n\u001B[31m❗ Invalid PIN entered!\nTransaction cancelled. 😞\u001B[0m");
            return;
        }

        String s1 = "UPDATE accounts AS a\n" +
                "JOIN upi_details AS upi ON upi.account_number = a.account_number\n" +
                "SET a.balance = a.balance - ?\n" +
                "WHERE upi.upi = ?;\n";
        String s2 = "UPDATE accounts AS a\n" +
                "JOIN upi_details AS upi ON upi.account_number = a.account_number\n" +
                "SET a.balance = a.balance + ?\n" +
                "WHERE upi.upi = ?;\n";

        try {
            super.conn.setAutoCommit(false);

            PreparedStatement p1 = super.conn.prepareStatement(s1);
            PreparedStatement p2 = super.conn.prepareStatement(s2);

            if ((!checkSufficientBalance(upiFrom))) {
                super.conn.rollback();
                System.out.println("\n\u001B[33mYou have not sufficient balance! Transaction cancelled! ❌\u001B[0m");
                return;
            }

            p1.setDouble(1, this.balance);
            p1.setString(2, this.upiFrom);
            p2.setDouble(1, this.balance);
            p2.setString(2, upiTo);

            int a = p1.executeUpdate();
            int b = p2.executeUpdate();

            if (a > 0 && b > 0) {
                if (UPILogTransaction()) {
                    super.conn.commit();
                    System.out.println("Transaction Status: ✅ Success.");
                } else {
                    System.out.println("Transaction Status: ❌ Failed.");
                    super.conn.rollback();
                }
            }
        } catch (SQLException e) {
            try {
                super.conn.rollback();
            } catch (SQLException ex) {
                System.out.println("Error: " + ex.getMessage());
            }
            System.out.println("Error: " + e.getMessage());
        } finally {
            try {
                super.conn.setAutoCommit(true);
            } catch (SQLException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
}
