package AG_FinTrust;

import java.sql.*;
import static AG_FinTrust.InputUtil.sc;

public class Main {
    private static final String url = "";
    private static final String username = "";
    private static final String password = "";

    static void checkServices(){
        System.out.print("\nWhat can I help you ? (Please select our services) : ");
        System.out.println("\n 1. Account Details");
        System.out.println(" 2. Deposit");
        System.out.println(" 3. Withdraw");
        System.out.println(" 4. Transfer");
        System.out.println(" 5. Transaction History");
        System.out.println(" 6. Update PIN");
        System.out.println(" 7. Exit\n");
    }

    //take input balance;
    static double inputBalanceForTransaction(){
        double balance;
        while (true) {
            System.out.print("Enter Amount : ");
            if (sc.hasNextDouble()) {
                balance = sc.nextDouble();
                if (balance > 0) {
                    break;
                } else {
                    System.out.println("Invalid amount. Amount must be greater than zero.");
                }
            } else {
                System.out.println("Invalid input. Please enter a number.");
                sc.next(); // Clear invalid input
            }
        }
        return balance;
    }

    static void provideServices(Connection conn){
        while(true){

            checkServices();

            System.out.print("Enter your choice : ");
            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice){
                case 1 : //account details;
                {
                    AccountDetailsInterface obj = new AccountDetails();
                    obj.viewMyAccountDetails(conn);
                }
                break;

                case 2 : //deposit
                {
                    System.out.print("Enter Your Name : ");
                    String name = sc.next();

                    System.out.print("Enter Your Account Number : ");
                    int number = sc.nextInt();

                    TransactionServicesInterface obj = new TransactionServices(name,number,conn,inputBalanceForTransaction(),"Deposit");
                    obj.moneyDepositTransaction();
                }
                    break;

                case 3 : //withdraw
                {
                    System.out.print("Enter Your Name : ");
                    String name = sc.next();

                    System.out.print("Enter Your Account Number : ");
                    int number = sc.nextInt();

                    TransactionServicesInterface obj = new TransactionServices(number,name,conn,inputBalanceForTransaction(),"Withdraw");
                    obj.moneyWithdrawTransaction();

                }
                    break;

                case 4 : //transfer
                {
                    System.out.print("Enter Your Name : ");
                    String name = sc.next();

                    System.out.println("Enter Your Account Number : ");
                    System.out.print("From : ");
                    int number1 = sc.nextInt();
                    System.out.print("To : ");
                    int number2 = sc.nextInt();

                    TransactionServicesInterface obj = new TransactionServices(number1,name,number2,conn,inputBalanceForTransaction(),"Transfer");
                    obj.moneyTransferTransaction();

                }
                    break;

                case 5 : //transaction history
                {
                    AccountDetailsInterface obj = new AccountDetails();
                    obj.viewMyTransaction(conn);
                }
                    break;

                case 6 : //update my pin
                {
                    PinServices obj = new PinServices();
                    obj.takeDetailsToUpdatePin();     // Take input
                    if(obj.isAccountExists(conn)) {   // Then check if that input exists in DB
                        obj.updatePin(conn);          // Then do the update
                    }else{
                        System.out.println("No Such Account Exist.");
                        return;
                    }
                }
                    break;

                case 7 : //exit
                    System.out.println("\n===================================================================\nThank you for visiting us!\nHave a nice day.\n===================================================================");
                    return;

                default :
                    System.out.println("\n===================================================================\nInvalid Choice!\nPlease try again.\nThank you!\n===================================================================");

            }
        }
    }

    public static void main(String[] args) {
        System.out.println(String.format("===================================================================\n|||||==========>>>>>  Welcome To 'AG FinTrust' <<<<<==========|||||\n===================================================================\n"));

        //loading driver
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
        }catch (ClassNotFoundException e){
            System.out.println(e.getMessage());
        }

        try{
            //establishing connection
            Connection connection = DriverManager.getConnection(url,username,password);

            //welcome to AG FinTrust
            System.out.print("Do you have an account in * AG FinTrust * bank ? : ( Yes[Y] / No[N] ) ");
            char choice1 = sc.next().charAt(0);

            if(choice1=='N' || choice1=='n'){

                System.out.print("\nDo you want to create an account ? : ( Yes[Y] / No[N] ) ");
                char choice2 = sc.next().charAt(0);

                if(choice2 == 'n' || choice2=='N'){
                    System.out.println("\n===================================================================\nThank you for visiting us!\nHave a nice day.\n===================================================================");
                    return;

                }else if(choice2 == 'y' || choice2=='Y'){
                    //creating an account;
                    AccountInterface myAccount = new Account();
                    myAccount.createMyAccount(connection);
                    //services;
                    provideServices(connection);

                }else{
                    System.out.println("\n===================================================================\nInvalid Statement!\nPlease try again.\nThank you!\n===================================================================");
                    return;

                }
            }else if(choice1=='Y' || choice1=='y'){
                //services;
                provideServices(connection);

            }else{
                System.out.println("\n===================================================================\nInvalid Statement!\nPlease try again.\nThank you!\n===================================================================");
                return;

            }
            connection.close();

        }catch (SQLException e){
            System.out.println(e.getMessage());
        }

    }
}

