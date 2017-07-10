import java.util.Scanner;

public class ClientThread extends Thread {
	
	ClientTCP client;
    static Scanner reader = new Scanner(System.in);
	public ClientThread (String ip, int port) {
		client = new ClientTCP(ip, port);

        client.openSocket();

	}
	public static void main(String[] args) {
        System.out.println("Enter a IP:");
        String ip = reader.next();
        System.out.println("Enter a port:");
        int port = reader.nextInt();
        ClientThread t = new ClientThread(ip, port);
        t.start();
    }

}