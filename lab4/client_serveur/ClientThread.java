import java.util.Scanner;

public class ClientThread extends Thread {
	
	ClientTCP client;
	public ClientThread () {
		client = new ClientTCP();

	}
	public static void main(String[] args) {
        
        ClientThread t = new ClientThread();
        t.start();
    }

}