import java.net;

public class ServeurTCP {

	Socket connexionSocket;
	ServerSocket serveur;
	public ServeurTCP(int portNumber) {

		instantiateServerSocket(portNumber);
		establishConnexion();
		String data = "Hello world";
		exchangeData(data);
		closeSockets();

	}

	public void closeSockets() {
		try {
			connexionSocket.close(); 
			serveur.close();
		} catch (IOException e) { System.out.println(e); }

	}

	public void instantiateServerSocket(int portNumber) {
		try {
			serveur = new ServerSocket(portNumber);
		} catch (IOException e) { System.out.println(e); }

	}

	public void exchangeData(data) {
		try {
			OutputStream out = connexionSocket.getOutputStream();
			out.write(data.getBytes() );
		} catch (IOException e) {System.out.println(e);}

	}

	public void establishConnexion() {
		try {
			connexionSocket = serveur.accept();
		} catch (IOException e) { System.out.println(e); }

	}


}

