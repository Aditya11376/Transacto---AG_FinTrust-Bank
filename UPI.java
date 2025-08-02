package AG_FinTrust;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import static AG_FinTrust.InputUtil.sc;

/**
 * AG_FinTrust - UPI.java
 *
 * Handles UPI-related services including validation, creation, and retrieval of UPI IDs
 * linked to user bank accounts. This class interacts with the database to manage UPI records
 * and display associated account information.
 *
 * <p><strong>Key Features:</strong></p>
 * <ul>
 *   <li>Validates UPI ID format (alphanumeric and dot characters only, up to 45 characters)</li>
 *   <li>Generates standardized UPI handles ending with "@agft"</li>
 *   <li>Associates UPI IDs with verified account numbers</li>
 *   <li>Prevents duplicate UPI assignment for the same account</li>
 *   <li>Fetches and displays account details using UPI ID with colored console output</li>
 * </ul>
 *
 * <p>This class is a <em>sealed class</em> and may only be extended by {@code UPIServices}.</p>
 *
 * @author Aditya Gupta
 * @version 2.0.0
 * @since August 02, 2025
 */

public sealed class UPI permits UPITransactionServices {
    protected Connection conn;
    private int account_number;
    private String defaultUPI;
    private String UPI;

    //UPI Constructor
    UPI(Connection conn){
        this.conn = conn;
    }

    //check upi name is valid or not
    final boolean checkUPI(String s){
        if(s.isEmpty()||s==null) return false;
        if(s.length()>45)return false;
        //(a-z,A-Z,0-9,.)
        return s.matches("^[a-zA-Z0-9.]+$");
    }

    //check the upi exist or not
    final boolean checkUPIExist(){
        String checkUPI = "select count(*) from upi_details where account_number = ?";
        try (PreparedStatement p = conn.prepareStatement(checkUPI)) {
            p.setInt(1, this.account_number);
            try (ResultSet resultSet = p.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1) == 1;
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    //create the upi
    private void createUPI(){
        System.out.println("\n--------------------------------------------------------------------------------------------");
        System.out.println("\u001B[31m"+"(Write only id name. It should not contain any special character (example - /,&,%,@ etc.. & character limit = 45))"+"\u001B[0m");
        System.out.print("Enter your UPI-ID : ");
        sc.nextLine();
        defaultUPI = sc.next().trim().toLowerCase();
        if (!checkUPI(defaultUPI)) {
            System.out.println("Invalid UPI-ID name.\nTry Again..\n");
            return;
        }else{
            UPI = defaultUPI + "@agft";
        }
    }

    //set the upi in upi_Details schema
    protected final void setUPI(){
        System.out.print("Enter the Account number : ");
        account_number = sc.nextInt();
        boolean flag = new TransactionServices(conn).checkAccountExist(this.account_number);
        if(!flag){
            System.out.println("\u001B[31m"+"\nAccount is not exists. \n"+"\u001B[0m");
            return;
        }
        if(checkUPIExist()){
            System.out.println("\u001B[31m"+"You have already an UPI-ID\n"+"\u001B[0m");
            return;
        }
        String query = "insert into upi_details values(?,?)";
        createUPI();
        try(PreparedStatement p = this.conn.prepareStatement(query)){
            p.setInt(1,this.account_number);
            p.setString(2,this.UPI);
            int rows = p.executeUpdate();
            if (rows > 0) {
                System.out.println("\u001B[32m✅ Your UPI-ID has been created successfully: " + this.UPI + "\nThank You\n\n\u001B[0m");
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    //get the account details
    protected void displayAccountDetails(){
        System.out.print("Enter Your UPI-ID : ");
        sc.nextLine();
        String upiID = sc.next().trim().toLowerCase();

        String query = " select a.account_name as accountName , a.account_number as accountNumber , a.balance as balance , upi.upi as `UPI-ID` from accounts as a join upi_details as upi on upi.account_number = a.account_number where upi.upi = ?";
        try(PreparedStatement p = this.conn.prepareStatement(query)){
            p.setString(1,upiID);
            try(ResultSet resultSet = p.executeQuery()){
                if(resultSet.next()){
                    int accNum = resultSet.getInt("accountNumber");
                    String name = resultSet.getString("accountName");
                    double amount = resultSet.getDouble("balance");
                    String upiId = resultSet.getString("UPI-ID");

                    // ANSI color codes
                    final String RESET = "\u001B[0m";
                    final String CYAN = "\u001B[36m";
                    final String YELLOW = "\u001B[33m";
                    final String GREEN = "\u001B[32m";
                    final String BLUE = "\u001B[34m";
                    final String MAGENTA = "\u001B[35m";
                    final String BOLD = "\u001B[1m";

                    System.out.println(BOLD + "\n========= Account Details =========" + RESET);
                    System.out.println(YELLOW + "Account Number : " + RESET + CYAN + accNum + RESET);
                    System.out.println(YELLOW + "Account Holder : " + RESET + GREEN + name + RESET);
                    System.out.println(YELLOW + "Current Balance: ₹" + RESET + BLUE + amount + RESET);
                    System.out.println(YELLOW + "UPI ID         : " + RESET + MAGENTA + upiId + RESET);
                    System.out.println(BOLD + "===================================\n" + RESET);
                }else{
                    System.out.println("\u001B[31mNo account details found for the provided account.\u001B[0m");
                }
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }
}
