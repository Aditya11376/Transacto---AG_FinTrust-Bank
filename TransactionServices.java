    package AG_FinTrust;

    import java.sql.Connection;
    import java.sql.PreparedStatement;
    import java.sql.ResultSet;
    import java.sql.SQLException;
    import static AG_FinTrust.InputUtil.sc;

    public class TransactionServices extends AccountDetails implements TransactionServicesInterface {

        private Connection connection;
        private String name;
        private int fromAccountNumber;
        private int toAccountNumber;
        private String PIN;
        private String transactionType;
        private double balance;

        //transferTypeConstructor
        TransactionServices(int fromAccountNumber, String name, int toAccountNumber, Connection conn, double bal, String type){
            this.connection = conn;
            this.toAccountNumber = toAccountNumber;
            this.fromAccountNumber = fromAccountNumber;
            this.name = name;
            this.balance = bal;
            this.transactionType = type;
        }

        //withdrawTypeConstructor
        TransactionServices(int fromAccountNumber, String name, Connection conn,double bal,String type){
            this.connection = conn;
            this.toAccountNumber = -1;
            this.fromAccountNumber = fromAccountNumber;
            this.name = name;
            this.balance = bal;
            this.transactionType = type;
        }

        //depositTypeConstructor
        TransactionServices(String name, int toAccountNumber, Connection conn,double bal,String type){
            this.connection = conn;
            this.toAccountNumber = toAccountNumber;
            this.fromAccountNumber = -1;
            this.name = name;
            this.balance = bal;
            this.transactionType = type;
        }


        //input the PIN
        void enterPIN(){
            System.out.print("Enter Your PIN : ");
            sc.nextLine();
            PIN = sc.nextLine().trim();
        }

        // match pin
        boolean matchPin(int acc) {
            String query = "SELECT COUNT(*) FROM pin_Details WHERE account_number = ? AND pin = ?";
            try (PreparedStatement preparedStatementCheck = this.connection.prepareStatement(query)) {
                preparedStatementCheck.setInt(1, acc);
                preparedStatementCheck.setString(2, PinEncoderDecoder.encode(this.PIN));

                try (ResultSet rs = preparedStatementCheck.executeQuery()) {
                    if (rs.next()) {
                        return rs.getInt(1) == 1;
                    }
                }

            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
            return false;
        }

        //log transaction
        @Override
        public boolean logTransaction(){
            try{
                //add history;
                String addQuery = "Insert into transaction_history (from_account,to_account,balance,transfer_type) values(?,?,?,?)";
                try (PreparedStatement p = connection.prepareStatement(addQuery)) {
                    if (this.fromAccountNumber != -1) {
                        p.setInt(1, this.fromAccountNumber);
                    } else {
                        p.setNull(1, java.sql.Types.INTEGER);
                    }

                    if (this.toAccountNumber != -1) {
                        p.setInt(2, this.toAccountNumber);
                    } else {
                        p.setNull(2, java.sql.Types.INTEGER);
                    }

                    p.setDouble(3, this.balance);
                    p.setString(4, this.transactionType);

                    int rowsAffected = p.executeUpdate();

                    if (rowsAffected > 0) {
                        System.out.println("Transaction Status : Success.");
                        return true;
                    } else {
                        System.out.println("Transaction Status : Failed.");
                        return false;
                    }
                }

            }catch (SQLException e) {
                System.out.println("Transaction Failed: " + e.getMessage());
            }
            return false;
        }

        //check accounts exists function;
        @Override
        public boolean checkAccountExist(int acc){
            try{
                String s = "Select count(*) from accounts where account_number =? ";
                PreparedStatement p = this.connection.prepareStatement(s);

                p.setInt(1,acc);

                ResultSet resultSet = p.executeQuery();
                if(resultSet.next()){
                    //if exists return true
                    return resultSet.getInt(1)==1;
                }
                resultSet.close();
                p.close();
            }
            catch (SQLException e){
                System.out.println(e.getMessage());
            }
            return false;
        }

        //check sufficient balance function;
        @Override
        public boolean checkSufficientBalance(){
            try{
                String s = "Select balance from accounts as a where a.account_number = ? and a.account_name = ?";
                PreparedStatement preparedStatement = this.connection.prepareStatement(s);

                preparedStatement.setInt(1,this.fromAccountNumber);
                preparedStatement.setString(2,this.name);

                ResultSet resultSet = preparedStatement.executeQuery();
                if(resultSet.next()){
                    double amount = resultSet.getDouble("balance");
                    return amount >= this.balance;
                }

                resultSet.close();
                preparedStatement.close();
            }catch (SQLException e ){
                System.out.println(e.getMessage());
            }
            return false;
        }

        //deposit money
        @Override
        public void moneyDepositTransaction(){
            PreparedStatement preparedStatement = null;
            this.fromAccountNumber = -1;

            if(!checkAccountExist(this.toAccountNumber)){
                System.out.println("Transaction:Deposit Is Not Possible.\nInvalid Account Numbers.");
                return;
            }

            enterPIN();

            try{
                //match pin
                if(!matchPin(this.toAccountNumber)){
                    System.out.println("\nIncorrect PIN Number.\nTransaction Failed.\n");
                    return;
                }

                connection.setAutoCommit(false);
                String depositQuery = "update accounts set balance = balance + ? where account_name=? and account_number = ?";
                preparedStatement = connection.prepareStatement(depositQuery);

                preparedStatement.setDouble(1,this.balance);
                preparedStatement.setString(2,this.name);
                preparedStatement.setInt(3,this.toAccountNumber);

                int rowsAffected = preparedStatement.executeUpdate();

                if(rowsAffected>0 && logTransaction()){
                    connection.commit();
                }else{
                    connection.rollback();
                }

            }catch (SQLException e){
                try{
                    connection.rollback();
                }catch (SQLException ex){
                    System.out.println(ex.getMessage());
                }
                System.out.println(e.getMessage());
            }finally {
                try{
                    if(preparedStatement != null) preparedStatement.close();
                    connection.setAutoCommit(true);
                }catch (SQLException e){
                    System.out.println(e.getMessage());
                }
            }

        }

        //withdraw money
        @Override
        public void moneyWithdrawTransaction(){
            PreparedStatement preparedStatement = null;
            this.toAccountNumber = -1;

            if(!checkAccountExist(this.fromAccountNumber)){
                System.out.println("Transaction Is Not Possible.\nInvalid Account Numbers.");
                return;
            }

            enterPIN();

            try{
                //match pin
                if(!matchPin(this.fromAccountNumber)){
                    System.out.println("\nIncorrect PIN Number.\nTransaction Failed.\n");
                    return;
                }
                //check balance is sufficient or not?
                if(!checkSufficientBalance()){
                    System.out.println("Not Sufficient Balance.\nTransaction Failed.\n");
                    return;
                }

                connection.setAutoCommit(false);
                String withdrawQuery = "update accounts set balance = balance - ? where account_number = ? and account_name = ?";
                preparedStatement = connection.prepareStatement(withdrawQuery);

                preparedStatement.setDouble(1,this.balance);
                preparedStatement.setInt(2,this.fromAccountNumber);
                preparedStatement.setString(3,this.name);

                int rowAffected = preparedStatement.executeUpdate();

                if(rowAffected>0 && logTransaction()){
                    connection.commit();
                }else{
                    connection.rollback();
                }

            }catch (SQLException e){
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    System.out.println(ex.getMessage());
                }
                System.out.println(e.getMessage());

            }finally {
                try{
                    if (preparedStatement != null) preparedStatement.close();
                    connection.setAutoCommit(true);
                }catch (SQLException e){
                    System.out.println(e.getMessage());
                }
            }
        }

        //transfer money
        @Override
        public void moneyTransferTransaction(){
            PreparedStatement preparedStatementFrom = null;
            PreparedStatement preparedStatementTo = null;

            //check both account exist or not?
            if(!(checkAccountExist(this.fromAccountNumber)&& checkAccountExist(this.toAccountNumber))) {
                System.out.println("Transaction Is Not Possible.\nInvalid Account Numbers.");
                return;
            }

            enterPIN();

            //match pin
            if(!matchPin(this.fromAccountNumber)){
                System.out.println("\nIncorrect PIN Number.\nTransaction Failed.\n");
                return;
            }

            //same account
            if (this.fromAccountNumber == this.toAccountNumber) {
                System.out.println("Cannot transfer to the same account.");
                return;
            }

            try{
                connection.setAutoCommit(false);

                String fromQuery = "update accounts set balance = balance - ? where account_number= ?";
                String toQuery = "update accounts set balance = balance + ? where account_number = ?";

                preparedStatementFrom = connection.prepareStatement(fromQuery);
                preparedStatementTo = connection.prepareStatement(toQuery);

                if(!checkSufficientBalance()) {
                    System.out.println("Not Sufficient Balance.\nTransaction Failed.\n");
                    connection.rollback();
                    return;
                }

                preparedStatementFrom.setDouble(1,this.balance);
                preparedStatementFrom.setInt(2,this.fromAccountNumber);

                preparedStatementTo.setDouble(1,this.balance);
                preparedStatementTo.setInt(2,this.toAccountNumber);

                int a = preparedStatementFrom.executeUpdate();
                int b = preparedStatementTo.executeUpdate();

                if (a > 0 && b > 0) {
                    if (logTransaction()) {
                        connection.commit();
                        System.out.println("Transfer successful.");
                    } else {
                        System.out.println("Transfer Failed.");
                        connection.rollback();
                    }
                }

            }catch (SQLException e){
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    System.out.println("Error: " + ex.getMessage());
                }
                System.out.println("Error: " + e.getMessage());

            }finally {
                try{
                    if (preparedStatementFrom != null) preparedStatementFrom.close();
                    if (preparedStatementTo != null) preparedStatementTo.close();
                    connection.setAutoCommit(true);
                }catch (SQLException e){
                    System.out.println("Error: " + e.getMessage());
                }
            }

        }

    }
