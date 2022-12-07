import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Client {
    public static void main(String args[]) throws IOException {
        System.out.println("CONNECTING TO THE BANK SERVER");
        InetAddress address = InetAddress.getLocalHost();
        Socket s = new Socket(address, 1534);
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        BufferedReader serverinput = new BufferedReader(new InputStreamReader(s.getInputStream()));
        PrintStream serverOutput = new PrintStream(s.getOutputStream());
        serverOutput.println(address);//Sending Client Address
        System.out.println("**********WELCOME TO OUR LOGIN PAGE**********");
        System.out.println("ENTER YOUR NAME");
        String name = input.readLine();
        System.out.println("ENTER YOUR USERNAME");
        String username = input.readLine();
        System.out.println("ENTER YOUR PASSWORD");
        String password = input.readLine();
        serverOutput.println(name);//Sending name to the server
        serverOutput.println(username);//Sending username to the server
        serverOutput.println(password);//Sending password to the server
        int verify = Integer.parseInt(serverinput.readLine());
        if (verify == 0)//Checks whether the login detail are valid or not. If not then program will terminate.
            System.out.println("LOGIN FAILED\nPLEASE TRY AGAIN LATER!!\nTHANK YOU!!");
        else {
            System.out.println("**********LOGIN SUCCESSFULL**********");
            while (true) {//till the programis terminated, it'll ask the user for the choice
                System.out.println("ENTER YOUR CHOICE:\n1. DEPOSIT MONEY TO ANOTHER ACCOUNT\n2. WITHDRAW(DEDUCT) MONEY FROM YOUR ACCOUNT\n"
                        + "3. DISPLAY YOUR CURRENT BALANCE\n4. DISPLAY YOUR PASSBOOK\n5. EXIT");
                int choice = Integer.parseInt(input.readLine());
                serverOutput.println(choice);//Sending the choice to the server
                String result;
                float amt;
                switch (choice) {//Taking inputs from client and sending it to the server based on the choice
                    case 1:
                        System.out.println("ENTER THE NAME OF THE PERSON TO WHICH AMOUNT HAS TO BE DEPOSITED");
                        String n = input.readLine();
                        System.out.println("ENTER THE USERNAME OF " + n);
                        String un = input.readLine();
                        System.out.println("ENTER THE AMOUNT TO BE DEPOSITED");
                        amt = Float.parseFloat(input.readLine());
                        serverOutput.println(n);
                        serverOutput.println(amt);
                        result = serverinput.readLine();
                        System.out.println(result);//Display a message to client after request is processed
                        System.out.print("TRANSACTION DATE AND TIME: ");
                        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                        LocalDateTime now = LocalDateTime.now();
                        System.out.println(dtf.format(now));
                        break;
                    case 2:
                        System.out.println("ENTER THE AMOUNT TO BE WITHDRAWN/DEDUCT");
                        amt = Float.parseFloat(input.readLine());
                        serverOutput.println(amt);
                        result = serverinput.readLine();
                        System.out.println(result);//Display a message to client after request is processed
                        System.out.print("TRANSACTION DATE AND TIME: ");
                        DateTimeFormatter dtf1 = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                        LocalDateTime now1 = LocalDateTime.now();
                        System.out.println(dtf1.format(now1));
                        break;
                    case 3:
                        result = serverinput.readLine();
                        System.out.println(result);//Display a message to client after request is processed
                        break;
                    case 4:
                        result = "";
                        int a = 6;
                        System.out.println("NAME: " + name + "\nUSERNAME: " + username);
                        System.out.println("ENTRIES:");
                        System.out.println("WITHDRAW  DEPOSIT  BALANCE");
                        //while((result.compareToIgnoreCase("Done"))==0)
                        while (a > 0) {
                            a--;//only for 4 entries
                            result = serverinput.readLine();
                            System.out.println(result);
                        }
                        break;
                    case 5:
                        serverOutput.println(name);
                        serverinput.close();
                        input.close();
                        serverOutput.close();
                        System.out.println("THANK YOU FOR VISTING!!\nBYE BYE!!");
                        System.exit(0);
                        break;
                    default://If the choice is not valid
                        System.out.println("WRONG CHOICE!!\nPLEASE ENTER YOUR CHOICE AGAIN CORRECTLY");
                        serverOutput.println(name);
                }
            }
        }
    }
}
