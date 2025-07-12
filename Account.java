package AG_FinTrust;

import java.sql.*;
import java.time.LocalDate;

import static AG_FinTrust.InputUtil.sc;

class Account implements AccountInterface {
    private Connection connection;
    private String account_name;
    private int age;
    private String gender;
    private String email;
    private long contact_number; //pass it as the string in the table (longtext);
    private double balance;
    private String address;
    private LocalDate date_of_birth;
    int account_number = 2507000;

    //take details
    void takeDetailsToCreateAccount(){
        System.out.println("\n Please Enter Your Details : \n");

        System.out.print(" 1. Enter your Name : ");
        sc.nextLine(); // flush before nextLine
        account_name = sc.nextLine();

        while(true){
            System.out.print(" 2. Enter your Age : ");
            age = sc.nextInt();
            if(isValidAge(age)){
                break;
            }else{
                System.out.println("Invalid Age!");
            }
        }

        while(true){
            System.out.print(" 3. Enter your Gender ('MALE'/'FEMALE'/'OTHER') : ");
            sc.nextLine(); // flush before next
            gender = sc.nextLine();
            if(isValidGender(gender.toUpperCase())){
                break;
            }else{
                System.out.println("Invalid Gender!");
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

                date_of_birth = LocalDate.of(year, month, day);
                break;
            }catch (Exception e){
                System.out.println("\nInvalid Date Of Birth.\nPlease try again.");
            }
        }

        sc.nextLine(); // flush again before reading address
        System.out.print(" 5. Enter your Address : ");
        address = sc.nextLine();

        while(true){
            System.out.print(" 6. Enter your Contact Number : ");
            contact_number = sc.nextLong();
            if(isValidContact(contact_number)){
                break;
            }else{
                System.out.println("Invalid Contact Number!");
            }
        }

        System.out.print(" 7. Enter your Email : ");
        sc.nextLine(); // flush
        email = sc.nextLine();

        System.out.print(" 8. Enter the Deposit Amount : ");
        balance = sc.nextDouble();
        System.out.println();
    }

    //creating an account
    @Override
    public void createMyAccount(Connection conn){
        int viewMyAccountNumber = 0;
        try{
            this.connection = conn;
            takeDetailsToCreateAccount();
            String insertQuery = "insert into accounts (account_name,age,gender,date_of_birth,email,contact_number,account_number,balance,address) values(?,?,?,?,?,?,?,?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
            preparedStatement.setString(1,account_name);
            preparedStatement.setInt(2,age);
            preparedStatement.setString(3,gender);
            preparedStatement.setDate(4,java.sql.Date.valueOf(date_of_birth));
            preparedStatement.setString(5,email);
            preparedStatement.setString(6,String.valueOf(contact_number));
            preparedStatement.setInt(7,account_number);
            preparedStatement.setDouble(8,balance);
            preparedStatement.setString(9,address);

            int rowsAffected = preparedStatement.executeUpdate();

            preparedStatement.close();

            if (rowsAffected > 0) {
                //storing the account number
                viewMyAccountNumber = getAccountNumber(conn);
                System.out.println("🎉 Congratulations! Your account has been created successfully.\n");

                //setup of PIN
                setPinNumberForMyAccount(conn,viewMyAccountNumber);

                //log transaction;
                TransactionServicesInterface obj = new TransactionServices(account_name,viewMyAccountNumber,conn,this.balance,"Deposit");

                obj.logTransaction();

                //print account details;
                printMyAccountReceipt(viewMyAccountNumber);

            } else {
                System.out.println("⚠️ Sorry! An error occurred while creating your account.\nPlease try again later or contact support.");
            }

        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    //check age
    static boolean isValidAge(int age){
        return age>0;
    }

    //check gender
    static boolean isValidGender(String gender){
        return gender.equals("MALE") || gender.equals("FEMALE") || gender.equals("OTHER");
    }

    //check contact
    static boolean isValidContact(long contact){
        String number = Long.toString(contact);
        return number.length()==10;
    }

    //allot a accountNumber
    int getAccountNumber(Connection connection){
        int r = 0;
        try {
            //get the serial_number;
            String query = "select serial_number from accounts where account_name = ? and account_number = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setString(1,account_name);
            preparedStatement.setInt(2,account_number);

            ResultSet resultSet = preparedStatement.executeQuery();

            int serial_number = 0;
            if(resultSet.next()){
                serial_number = resultSet.getInt("serial_number");
                account_number += serial_number;
            }
            preparedStatement.close();

            //update the account_number;
            String updateQuery = "update accounts set account_number = ? where serial_number = ? and account_name =? ";
            PreparedStatement preparedStatement1 = connection.prepareStatement(updateQuery);

            preparedStatement1.setInt(1,account_number);
            preparedStatement1.setInt(2,serial_number);
            preparedStatement1.setString(3,account_name);

            r = preparedStatement1.executeUpdate();
            preparedStatement1.close();
            //store account_number;
            r = account_number;

            //roll back the account number
            account_number = 2507000;

        }catch (SQLException e ){
            e.printStackTrace();
        }
        //return account number
        return r;
    }

    //get account details;
    void printMyAccountReceipt(int viewMyAccountNumber){
        //details
        System.out.println("\n🧾 Account Details:");
        System.out.println("-----------------------------");
        System.out.println("👤 Name           : " + account_name);
        System.out.println("🏦 Account Number : " + viewMyAccountNumber);
        System.out.println("💰 Current Balance : ₹" + balance);
        System.out.println("📞 Contact Number : " + contact_number);
        System.out.println("-----------------------------\n");
    }

    //set pin_number;
    void setPinNumberForMyAccount(Connection conn,int viewMyAccountNumber){
        try{
            //take and check pin_number
            String pin_number;
            while(true){
                System.out.print("Enter your 6 Digit Pin Number here : ");
                pin_number = sc.next();
                sc.nextLine();
                System.out.println("(Note :-> Remember your pin & never share it with anyone.)\n");
                if(isValidPin(pin_number)){
                    break;
                }else{
                    System.out.print("❌ Invalid Pin. Please enter a 6-digit number.");
                }
            }

            //store PIN;
            String query = "insert into pin_details(account_number,pin) values(?,?)";
            PreparedStatement preparedStatement = conn.prepareStatement(query);

            preparedStatement.setInt(1,viewMyAccountNumber);
            preparedStatement.setString(2,PinEncoderDecoder.encode(pin_number));

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected>0){
                System.out.println("🎉 Congratulations! Your account pin number has been created successfully.\n");
            }else{
                System.out.println("⚠️ Sorry! An error occurred while creating your account pin.\nPlease try again later or contact support.");
            }

            preparedStatement.close();

        }catch (SQLException e){
            e.printStackTrace();
        }

    }

    //check pin_number
    static boolean isValidPin(String str){
        return str != null && str.matches("\\d+");
    }

}
