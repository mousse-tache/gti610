/**
 * Created by AP19310 on 2017-07-10.
 */
import java.util.Scanner;

public class ServerThread extends Thread {
    ServeurTCP server;

    static Scanner reader = new Scanner(System.in);

    public ServerThread(int port) {

        server = new ServeurTCP(port);

        server.openSocket();

    }
    public static void main(String[] args) {
        System.out.println("Enter a port:");
        int port = reader.nextInt();
        ServerThread t = new ServerThread(port);
        t.start();
    }
}
