import java.net.*;
import java.io.*;

public class ServeurTCP {

	Socket connexionSocket;
	int portNumber;
	ServerSocket serveur;
	public ServeurTCP(int portNumber) {

		this.portNumber = portNumber;

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

	public void exchangeData(String data) {
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

