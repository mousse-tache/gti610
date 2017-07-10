import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class ClientTCP extends TCP{

	Socket socket;
	String ip;
	int portNumber;
	public ClientTCP(String ip, int portNumber) {

		this.ip=ip;
		this.portNumber=portNumber;

	}

	public void closeSockets() {
		try {
			socket.close();
		} catch (IOException e) { System.out.println(e); }

	}

	public void openSocket() {
		try {
			Socket socket = new Socket(this.ip , this.portNumber);
		} catch (IOException e) { System.out.println(e); }

	}

	public void exchangeData() {
		int MAXLENGTH=256;
		byte[ ] buff = new byte[MAXLENGTH];
		try {
			InputStream in = socket.getInputStream();
			in.read(buff);
		} catch (IOException e) {System.out.println(e);}

	}

}


