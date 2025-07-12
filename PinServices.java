package AG_FinTrust;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import static AG_FinTrust.InputUtil.sc;

class PinServices extends Account implements PinServicesInterface {
    //take details (name,account_number,dob,mob_no)
    private int accountNumber;
    private long contactNumber;
    private LocalDate dob;
    private String accountName;

//  Take Details of Account Holder;
    void takeDetailsToUpdatePin(){
        System.out.println("\nEnter your details for change/update of your account PIN :- \n");

        System.out.print(" 1. Enter your Account Name : ");
        accountName = sc.nextLine();

        System.out.print(" 2. Enter your Account Number : ");
        accountNumber = sc.nextInt();

        while(true){
            System.out.print(" 3. Enter your Contact Number : ");
            contactNumber = sc.nextLong();
            if(Account.isValidContact(contactNumber)){
                break;
            }else{
                System.out.println("Invalid Contact Number!");
            }
        }

        while(true){
            try{
                System.out.println(" 4. Enter your Date Of Birth - ");
                System.out.print("Year : ");
                int year = sc.nextInt();
                System.out.print("Month : ( 1-12 ) ");
                int month = sc.nextInt();
                System.out.print("Date : ");
                int day = sc.nextInt();

                dob = LocalDate.of(year, month, day);
                break;
            }catch (Exception e){
                System.out.println("\nInvalid Date Of Birth.");
            }
        }

    }

//  Check Match of account
    @Override
    public boolean isAccountExists(Connection conn){
       try{
           String query = "Select count(*) from accounts where lower(account_name) = ? and contact_number = ? and date_of_birth = ? and account_number = ?";
           PreparedStatement  preparedStatement = conn.prepareStatement(query);
           preparedStatement.setString(1,accountName.toLowerCase());
           preparedStatement.setString(2,String.valueOf(contactNumber));
           preparedStatement.setDate(3,java.sql.Date.valueOf(dob));
           preparedStatement.setInt(4,accountNumber);


           ResultSet resultSet = preparedStatement.executeQuery();
           if(resultSet.next() && resultSet.getInt(1)==1){
               preparedStatement.close();
               return true;
           }
           preparedStatement.close();
       }catch (SQLException e){
           System.out.println(e.getMessage());
       }
       return false;
    }

    @Override
//  If exists then go ahead else stop
    public void updatePin(Connection conn){
        try{
                //take and check pin_number
                String pin_number;
                System.out.println("==========================================================================");
                while(true){
                    System.out.print("\nUPDATE your 6-Digit Pin Number here : ");
                    pin_number = sc.next();
                    System.out.println("(Note :-> Remember your pin & never share it with anyone.)\n");
                    if(Account.isValidPin(pin_number)){
                        break;
                    }else{
                        System.out.print("❌ Invalid Pin (PIN limit exceeded/no PIN entered). Please re-enter a 6-digit number.");
                    }
                }
                //update query
                String query = "Update pin_details set pin = ? where account_number = ?";
                PreparedStatement preparedStatement = conn.prepareStatement(query);

                preparedStatement.setString(1,PinEncoderDecoder.encode(pin_number));
                preparedStatement.setInt(2,accountNumber);

                int affect = preparedStatement.executeUpdate();
                System.out.println(affect>0? "\nPIN updated\n":"Some issue occurs!\nPIN is not updated.\nPlease try again.\n");
                preparedStatement.close();

        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }

}
