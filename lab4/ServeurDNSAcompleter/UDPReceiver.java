import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Cette classe permet la reception d'un paquet UDP sur le port de reception
 * UDP/DNS. Elle analyse le paquet et extrait le hostname
 * 
 * Il s'agit d'un Thread qui ecoute en permanance pour ne pas affecter le
 * deroulement du programme
 * 
 * @author Max
 *
 */

public class UDPReceiver extends Thread {
	/**
	 * Les champs d'un Packet UDP 
	 * --------------------------
	 * En-tete (12 octects) 
	 * Question : l'adresse demande 
	 * Reponse : l'adresse IP
	 * Autorite :
	 * info sur le serveur d'autorite 
	 * Additionnel : information supplementaire
	 */

	/**
	 * Definition de l'En-tete d'un Packet UDP
	 * --------------------------------------- 
	 * Identifiant Parametres 
	 * QDcount
	 * Ancount
	 * NScount 
	 * ARcount
	 * 
	 * L'identifiant est un entier permettant d'identifier la requete. 
	 * parametres contient les champs suivant : 
	 * 		QR (1 bit) : indique si le message est une question (0) ou une reponse (1). 
	 * 		OPCODE (4 bits) : type de la requete (0000 pour une requete simple). 
	 * 		AA (1 bit) : le serveur qui a fourni la reponse a-t-il autorite sur le domaine? 
	 * 		TC (1 bit) : indique si le message est tronque.
	 *		RD (1 bit) : demande d'une requete recursive. 
	 * 		RA (1 bit) : indique que le serveur peut faire une demande recursive. 
	 *		UNUSED, AD, CD (1 bit chacun) : non utilises. 
	 * 		RCODE (4 bits) : code de retour.
	 *                       0 : OK, 1 : erreur sur le format de la requete,
	 *                       2: probleme du serveur, 3 : nom de domaine non trouve (valide seulement si AA), 
	 *                       4 : requete non supportee, 5 : le serveur refuse de repondre (raisons de s�ecurite ou autres).
	 * QDCount : nombre de questions. 
	 * ANCount, NSCount, ARCount : nombre d�entrees dans les champs �Reponse�, Autorite,  Additionnel.
	 */

	protected final static int BUF_SIZE = 1024;
	protected String SERVER_DNS = null;//serveur de redirection (ip)
	protected int portRedirect = 53; // port  de redirection (par defaut)
	protected int port; // port de r�ception
	private String adrIP = null; //bind ip d'ecoute
	private String DomainName = "none";
	private String DNSFile = null;
	private boolean RedirectionSeulement = false;

	private class ClientInfo { //quick container
		public String client_ip = null;
		public int client_port = 0;
	};

	private HashMap<Integer, ClientInfo> Clients = new HashMap<>();
	
	private boolean stop = false;

	
	public UDPReceiver() {
	}

	public UDPReceiver(String SERVER_DNS, int Port) {
		this.SERVER_DNS = SERVER_DNS;
		this.port = Port;
	}
	
	
	public void setport(int p) {
		this.port = p;
	}

	public void setRedirectionSeulement(boolean b) {
		this.RedirectionSeulement = b;
	}

	public String gethostNameFromPacket() {
		return DomainName;
	}

	public String getAdrIP() {
		return adrIP;
	}

	private void setAdrIP(String ip) {
		adrIP = ip;
	}

	public String getSERVER_DNS() {
		return SERVER_DNS;
	}

	public void setSERVER_DNS(String server_dns) {
		this.SERVER_DNS = server_dns;
	}



	public void setDNSFile(String filename) {
		DNSFile = filename;
	}

	public void run() {
		try {
			DatagramSocket serveur = new DatagramSocket(this.port); // *Creation d'un socket UDP

			int test = 0;

			// *Boucle infinie de recpetion
			while (!this.stop) {
				byte[] buff = new byte[1024];
				DatagramPacket paquetRecu = new DatagramPacket(buff,buff.length);
				System.out.println("Serveur DNS  "+serveur.getLocalAddress()+"  en attente sur le port: "+ serveur.getLocalPort());

				// *Reception d'un paquet UDP via le socket
				serveur.receive(paquetRecu);
				
				System.out.println("paquet recu du  "+paquetRecu.getAddress()+"  du port: "+ paquetRecu.getPort());

				DataInputStream din = new DataInputStream(new ByteArrayInputStream(buff));

				// Lecture header
				char id = din.readChar();
				char flag = din.readChar();
				char que = din.readChar();
				char ans = din.readChar();
				char auth = din.readChar();
				char add = din.readChar();
				System.out.println("Transaction ID: " + String.format("%04x", (int) id));
				System.out.println("Flags: " + String.format("%04x", (int) flag));
				System.out.println("Questions: " + String.format("%04x", (int) que));
				System.out.println("Answers RRs: " + String.format("%04x", (int) ans));
				System.out.println("Authority RRs: " + String.format("%04x", (int) auth));
				System.out.println("Additional RRs: " + String.format("%04x", (int) add));

				String domainName = "";
				if( this.Clients.containsKey((int)id) == false )
				{
					ClientInfo clientInfo = new ClientInfo();
					clientInfo.client_ip = paquetRecu.getAddress().getHostAddress();
					clientInfo.client_port = paquetRecu.getPort();
					this.Clients.put((int)id, clientInfo);
				}


				// Lecture question
				int recLen = 0;
				while ((recLen = din.readByte()) > 0) {
					byte[] record = new byte[recLen];

					for (int i = 0; i < recLen; i++) {
						record[i] = din.readByte();
					}

					System.out.println("Record: " + new String(record, "UTF-8"));
					domainName += new String(record, "UTF-8");
				}

				// Lecture Reponse (pas de lecture du rdlength ni du Rdata)
				char qType = din.readChar();
				char qClass = din.readChar();
				char field = din.readChar();
				char answerType = din.readChar();
				char answerClass = din.readChar();
				char answerTtl = din.readChar();
				din.readChar();
				System.out.println("Record Type: " + String.format("%04x", (int) qType));
				System.out.println("Class: " + String.format("%04x", (int) qClass));
				System.out.println("Field: " + String.format("%04x", (int) field));
				System.out.println("Type: " + String.format("%04x", (int) answerType));
				System.out.println("Class: " + String.format("%04x", (int) answerClass));
				System.out.println("TTL: " + String.format("%04x", (int) answerTtl));

				// Test first bit if 0 or 1
				if( (flag & 0x8000) != 0x8000 )
				{
					System.out.println("Request");

					if( this.RedirectionSeulement )
					{
						UDPSender udpSender = new UDPSender(this.SERVER_DNS, this.portRedirect, serveur);
						udpSender.SendPacketNow(paquetRecu);
					}
					else
					{
						QueryFinder queryFinder = new QueryFinder(this.DNSFile);
						List<String> ipList = queryFinder.StartResearch(domainName);

						if( ipList.size() == 0 )
						{
							UDPSender udpSender = new UDPSender(this.SERVER_DNS, this.portRedirect, serveur);
							udpSender.SendPacketNow(paquetRecu);
						}
						else
						{
							UDPAnswerPacketCreator udpAnswerPacketCreator = UDPAnswerPacketCreator.getInstance();
							byte[] answerPacket  = udpAnswerPacketCreator.CreateAnswerPacket(buff, ipList);
							DatagramPacket udpResponse = new DatagramPacket(answerPacket, answerPacket.length);
							ClientInfo clientInfo = Clients.get((int)id);
							UDPSender udpSender = new UDPSender(clientInfo.client_ip, clientInfo.client_port, serveur);
							udpSender.SendPacketNow(udpResponse);
						}
					}
				}
				else
				{
					System.out.println("Response");

					//Lecture rdlenght et rdata
					char addrLen = din.readChar();
					System.out.println("Len: " + String.format("%04x", (int) addrLen));

					List<String> ipList = new ArrayList<>();
					String ip = "";
					int count = 0;
					for (int i = 0; i < (((int)addrLen)&(0x00FF)); i++ ) {
						ip += String.format("%d", (din.readByte() & 0xFF)) + ".";
						if( count == 2 )
						{
							ip += String.format("%d", (din.readByte() & 0xFF));
							ipList.add(new String(ip));
							count = -1;
						}
						count++;
					}

					for(int i = 0; i < ipList.size(); i++ ) {
						System.err.println(ipList.get(i));
					}

					if( ipList.size() == 1 ) {
						AnswerRecorder answerRecorder = new AnswerRecorder(this.DNSFile);
						answerRecorder.StartRecord(domainName, ipList.get(0));

						UDPAnswerPacketCreator udpAnswerPacketCreator = UDPAnswerPacketCreator.getInstance();
						byte[] answerPacket  = udpAnswerPacketCreator.CreateAnswerPacket(buff, ipList);
						DatagramPacket udpResponse = new DatagramPacket(answerPacket, answerPacket.length);
						ClientInfo clientInfo = Clients.get((int)id);
						UDPSender udpSender = new UDPSender(clientInfo.client_ip, clientInfo.client_port, serveur);
						udpSender.SendPacketNow(udpResponse);
					}
				}
			}
			serveur.close(); //closing server
		} catch (Exception e) {
			System.err.println("Probl�me � l'ex�cution :");
			e.printStackTrace(System.err);
		}
	}
}
