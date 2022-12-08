import java.io.IOException;
import java.net.Socket;

public class ServerThread implements Runnable {
    BankServer s = null;
    Socket client = null;
    int cid;

    ServerThread(Socket client, int c, BankServer ser) throws IOException {
        this.client = client;
        this.s = s;
        this.cid = c;
        System.out.println("CONNECTION ESTABLISHED WITH CLIENT " + c);
    }

    @Override
    public void run() {
        Account obj = new Account(client);
    }
}
