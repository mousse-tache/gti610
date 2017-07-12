import java.net.*;
import java.io.*;
import java.util.Scanner;

public class ServeurTCP extends TCP{

	Socket socket;
	ServerSocket serveur;
	int portNumber;
	Scanner reader;
	public ServeurTCP() {
		openSocket();
		exchangeData();
		closeSockets();
	}

	public void closeSockets() {
		try {
			socket.close(); 
			serveur.close();
		} catch (IOException e) { System.out.println(e); }
		openSocket();

	}

	public void openSocket() {

		reader = new Scanner(System.in);
		System.out.println("Entrer un port au-dessus de 1023:");
        this.portNumber = reader.nextInt();
		try {
			serveur = new ServerSocket(portNumber);
			socket = serveur.accept();
			System.out.println("Port occupé");
		} catch (IOException e) { System.out.println(e); }

	}

	public void exchangeData() {

		reader = new Scanner(System.in);
		int MAXLENGTH=1024;
		byte[ ] buff = new byte[MAXLENGTH];

		try {
			InputStream in = this.socket.getInputStream();
			in.read(buff);
			String msg = new String(buff);
			msg = msg.toUpperCase()+", ip:port: "+socket.getRemoteSocketAddress().toString();
			System.out.println(msg);

			OutputStream out = socket.getOutputStream();
			out.write(msg.getBytes());

			Thread.sleep(1000);

		} catch (IOException e) {System.out.println(e);}


	}
}

