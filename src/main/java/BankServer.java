import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BankServer {
    int port;//port number
    ServerSocket Server = null;//server object
    Socket client = null;//client object
    ExecutorService pool = null;//to execute the program with threads
    int c = 0;//Client number

    public static void main(String args[]) throws IOException {
        BankServer obj = new BankServer(1534);//Server object created
        obj.startServer();
    }//Server started

    BankServer(int port) {//create threads in server to keep the count of clients and then send the client address to the server
        this.port = port;
        pool = Executors.newFixedThreadPool(10);
    }//upto ten clients are allowed

    public void startServer() throws IOException {//Server will be established for each client
        Server = new ServerSocket(1534);//Server is created and then wait for client to join
        System.out.println("WAITING FOR THE CONNECTION WITH CLIENT......");
        System.out.println("**********WELCOME**********");
        while (true) {//Loop is used for multiple client. Whenever client runs, new connection is established with the same server
            client = Server.accept();//After client joins, server is established between client and server
            c++;
            ServerThread runnable = new ServerThread(client, c, this);
            pool.execute(runnable);
        }
    }//to execute the code written in ServerThread
}
