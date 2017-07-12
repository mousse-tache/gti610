import java.io.IOException;
import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class ClientTCP extends TCP{

	Socket socket;
	String ip;
	int portNumber;
	Scanner reader;
	public ClientTCP() {

		openSocket();
		exchangeData();
		closeSockets();

	}

	public void closeSockets() {
		try {
			socket.close();
		} catch (IOException e) { System.out.println(e); }

		openSocket();

	}

	public void openSocket() {
		reader = new Scanner(System.in);
		System.out.println("Enter un ip:");
        this.ip = reader.next();
        System.out.println("Entrer un port au-dessus de 1023:");
        this.portNumber = reader.nextInt();
		try {
			this.socket = new Socket(ip , portNumber);
			System.out.println("Connexion établie");
		} catch (IOException e) { System.out.println(e); }


	}

	public void exchangeData() {
		reader = new Scanner(System.in);
		int MAXLENGTH=1024;
		byte[ ] buff = new byte[MAXLENGTH];

		try {
			OutputStream out = socket.getOutputStream();
			System.out.println("Message à envoyer au serveur");
			out.write((reader.next()).getBytes());
			
			InputStream in = this.socket.getInputStream();
			in.read(buff);
			System.out.println(new String(buff));

		} catch (IOException e) {System.out.println(e);}


	}

}


