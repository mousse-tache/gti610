import java.net.*;
import java.io.*;

public class ServeurTCP extends TCP{

	Socket socket;
	ServerSocket serveur;
	int portNumber;
	public ServeurTCP(int portNumber) {this.portNumber=portNumber;}

	public void closeSockets() {
		try {
			socket.close(); 
			serveur.close();
		} catch (IOException e) { System.out.println(e); }

	}

	public void openSocket(int portNumber) {
		try {
			serveur = new ServerSocket(portNumber);
		} catch (IOException e) { System.out.println(e); }

		try {
			socket = serveur.accept();
		} catch (IOException e) { System.out.println(e); }

	}

	public void exchangeData(String data) {
		try {
			OutputStream out = socket.getOutputStream();
			out.write(data.getBytes() );
		} catch (IOException e) {System.out.println(e);}

	}
}

