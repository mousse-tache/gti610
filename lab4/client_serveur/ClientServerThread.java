import java.util.scanner;


public class ClientServerThread extends Thread {
	
	ClientTCP client;
	ServeurTCP serveur;

	public ClientServerThread (String ip, int port) {
		client = new ClientTCP(ip, port);
		serveur = new ServeurTCP(port);
	}
}