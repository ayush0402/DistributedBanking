import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class Account {
    String a = "";

    // Fields of the table: Name of the table = $name
    // Username, Password, withdraw, deposit, balance.

    Account(Socket client) {
        try {
            BufferedReader input = new BufferedReader(new InputStreamReader(client.getInputStream()));//To recieve data from client
            PrintStream output = new PrintStream(client.getOutputStream());//To send data to the client
            String address = input.readLine();//recieving address of the client
            System.out.println("CLIENT ADDRESS: " + address);
            String name = input.readLine();
            String username = input.readLine();
            String password = input.readLine();
            Class.forName("com.mysql.jdbc.Driver");//Connecting database mysql
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost/bank", "ayush", "04022002");
            con.setAutoCommit(false);//Since autocommit is always true so setting it to fase initially
            Statement st = con.createStatement();
            String check = "SELECT * from " + name + " WHERE Username= '" + username + "' AND Password= '" + password + "'";
            if ((st.executeQuery(check)).absolute(1))//check if login details are valid or not and send the validity to client
            {
                output.println(1);
                output.flush();
            } else {
                output.println(0);
                output.flush();
            }
            while (true) {//Back end for client where client's request are beign processed
                int choice = Integer.parseInt(input.readLine());
                String result, user1, user2, currentbalance, currentbalance1, currentbalance2, query;
                float balance1 = 0, balance2 = 0, amount;
                switch (choice) {
                    case 1:
                        String n = input.readLine();
                        amount = Float.parseFloat(input.readLine());
                        currentbalance1 = "SELECT balance FROM " + name;
                        currentbalance2 = "SELECT balance FROM " + n;
                        ResultSet res = st.executeQuery(currentbalance1);
                        while (res.next()) {
                            balance1 = res.getFloat("balance");
                        }
                        res.close();
                        ResultSet res1 = st.executeQuery(currentbalance2);
                        while (res1.next()) {
                            balance2 = res1.getFloat("balance");
                        }
                        res1.close();
                        if (balance1 < amount) {
                            result = "INSUFFICIENT BALANCE!!\nCANNOT PROCEED YOUR REQUEST";
                            output.println(result);
                        }//Sendng the result message to the client
                        else {
                            float newbalance1 = balance1 - amount;//New balance of name
                            float newbalance2 = balance2 + amount;//New balance of n
                            user1 = "INSERT into " + name + "(withdraw,deposit,balance) values(" + amount + ",0.0," + newbalance1 + ")";
                            user2 = "INSERT into " + n + "(withdraw,deposit,balance) values(0.0," + amount + "," + newbalance2 + ")";
                            st.executeUpdate(user1);//SQL query to update client's database after withdraw
                            st.executeUpdate(user2);//SQL query to update client's database after deposit
                            result = "DATA COMMITTED AND " + amount + " DEDUCTED FROM YOUR ACCOUNT AND DEPOSITED TO " + n + "'s ACCOUNT";
                            output.println(result);
                        }//Sendng the result message to the client
                        break;
                    case 2:
                        amount = Float.parseFloat(input.readLine());
                        currentbalance = "SELECT balance FROM " + name;
                        ResultSet res3 = st.executeQuery(currentbalance);
                        while (res3.next()) {
                            balance1 = res3.getFloat("balance");
                        }
                        res3.close();
                        if (balance1 < amount) {
                            result = "INSUFFICIENT BALANCE!!\nCANNOT PROCEED YOUR REQUEST";
                            output.println(result);
                        }//Sendng the result message to the client
                        else {
                            query = "INSERT into " + name + "(withdraw,deposit,balance) values(" + amount + ",0.0," + (balance1 - amount) + ")";
                            st.executeUpdate(query);//sql query to deduct money from account
                            result = "DATA COMMITTED AND " + amount + " DEDUCTED FROM YOUR ACCOUNT";
                            output.println(result);
                        }//Sendng the result message to the client
                        break;
                    case 3:
                        currentbalance = "SELECT balance FROM " + name;
                        ResultSet res4 = st.executeQuery(currentbalance);
                        while (res4.next())//To get current balance
                        {
                            balance1 = res4.getFloat("balance");
                        }
                        res4.close();
                        result = "YOUR CURRENT BALANCE IS: " + balance1;
                        output.println(result);//Sendng the result message to the client
                        break;
                    case 4:
                        query = "SELECT withdraw,deposit,balance from " + name;
                        ResultSet res5 = st.executeQuery(query);
                        String s = "";
                        while (res5.next())//Three columns of the table is stored in string and sent to client to display
                        {
                            s = res5.getFloat("withdraw") + "\t  " + res5.getFloat("deposit") + "\t   " + res5.getFloat("balance");
                            output.println(s);//Sendng the result message to the client
                        }
                        output.println("Done");//Sending Done to client inorder to terminate the loop
                        break;
                    case 5:
                        a = input.readLine();
                        con.commit();//Commiting Data
                        break;
                }

            }
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            try {
                System.out.println("CONNECTION CLOSING FOR CLIENT " + a);
                client.close();
            }//closing server connection
            catch (IOException ie) {//if there's any problem while closing server
                System.out.println("Socket Close Error");
            }
        }
    }
}
