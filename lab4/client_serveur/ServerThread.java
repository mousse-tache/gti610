/**
 * Created by AP19310 on 2017-07-10.
 */
public class ServerThread extends Thread {
    ServeurTCP server;


    public ServerThread() {

        server = new ServeurTCP();


    }
    public static void main(String[] args) {
        
        ServerThread t = new ServerThread();
        t.start();
    }
}
