// FOR REFERENCE ONLY; WILL RUN ON SERVER

import java.io.*;
import java.text.*;
import java.util.*;
import java.net.*;

/**
 * Handles server requests
 * 
 * @author - Kaitlyn
 */
public class ServerSide {
	// Initialize socket and input stream
	private static final int PORT = 5000;

	/**
	 * Keeps accepting clients and creates Threads for each to handle multiplayer
	 * 
	 * @param args - String[] array
	 */
	public static void main(String[] args) {
		try {
			ServerSocket server = new ServerSocket(PORT);
			// Infinite loop to receive client requests
			while (true) {
				// Reset socket
				Socket s = null;
				// Receive incoming requests
				s = server.accept();
				// Input and output streams
				DataInputStream in = new DataInputStream(s.getInputStream());
				DataOutputStream out = new DataOutputStream(s.getOutputStream());
				// Assigns new thread
				Thread t = new ClientHandler(server, in, out);
				t.start();
			}
		} catch (Exception e) {
			System.out.println(e);
		}
	}
}

/**
 * Handles each host and player
 * 
 * @author - Kaitlyn
 */
class ClientHandler extends Thread {
	private boolean readyToFight = false;
	private boolean done = false;
	private boolean correct = false;
	// Collection of active hosts
	private static ArrayList<ClientHandler> hostList = new ArrayList<ClientHandler>();
	private static int nextCode = (int) (Math.random() * 500_000 + 100_000);
	// True if user is host
	private boolean host;
	private int code;
	private String name;
	private int score = 0;
	// Players associated with host
	private ArrayList<ClientHandler> playerList = new ArrayList<ClientHandler>();
	// Host associated with player
	private ClientHandler myHost;

	private ServerSocket server;
	private DataInputStream in;
	private DataOutputStream out;

	/**
	 * Creates new Thread handling the user
	 * 
	 * @param server - ServerSocket for client
	 * @param in     - Reads input
	 * @param out    - Sends output
	 */
	public ClientHandler(ServerSocket server, DataInputStream in, DataOutputStream out) {
		// Assign instance variables
		this.server = server;
		this.in = in;
		this.out = out;
		try {
			// Reads user type from client
			String line = in.readUTF();
			if (line.equals("host")) {
				host = true;
			}
			if (line.equals("player")) {
				host = false;
			}
		} catch (IOException e) {
			System.out.println(e);
		}
	}

	/**
	 * Runs the thread for both player and host and accepts respective commands from
	 * client
	 */
	@Override
	public void run() {
		if (host) {
			// Adds host to list of hosts and assigns next code
			hostList.add(this);
			code = nextCode;
			nextCode++;
			// Sends code to client
			try {
				out.writeUTF(code + "");
				// Starts accepting commands
				String line = in.readUTF();
				while (!line.equals("exit")) {
					switch (line) {
					case "give questions":
						for (ClientHandler c : playerList) {
							if (c != null && c.isAlive()) {
								c.out.writeUTF("start questions");
							}
						}
						break;
					case "start boss fight":
						for (ClientHandler c : playerList) {
							if (c != null && c.isAlive()) {
								c.out.writeUTF("start boss fight for player");
							}
						}
						out.writeUTF("display ending");
						break;
					case "display rest":
						int winners = 0;
						for (ClientHandler c : playerList) {
							if (c != null && c.correct) {
								winners++;
							}
						}
						if (((double) winners / playerList.size() > 0.8)) {
							out.writeUTF("true");
						} else {
							out.writeUTF("false");
						}
						for (ClientHandler c : playerList) {
							if (c != null && !c.done) {
								out.writeUTF(c.name);
								out.writeUTF(c.score + "");
							}
						}
						out.writeUTF("complete");
						break;
					}
					line = in.readUTF();
				}
				// End game for all players
				for (ClientHandler c : playerList) {
					if (c != null) {
						c.out.writeUTF("exit");
						c.out.close();
						c.in.close();
					}
				}
			} catch (Exception e) {
				System.out.println(e);
			}
		} else {
			try {
				String line = in.readUTF();
				while (!line.equals("exit")) {
					switch (line) {
					case "new code":
						// Reads player's code from client
						String newCode = in.readUTF();
						code = Integer.parseInt(newCode);
						boolean found = false;
						// Finds host associated with the code
						for (int i = 0; i < hostList.size(); i++) {
							if (hostList.get(i).getCode() == code) {
								hostList.get(i).addPlayer(this);
								myHost = hostList.get(i);
								found = true;
							}
						}
						if (!found) {
							out.writeUTF("invalid");
						} else {
							out.writeUTF("valid");
							name = in.readUTF();
							myHost.out.writeUTF("light");
							myHost.out.writeUTF(name);
						}
						break;
					case "ready to fight":
						score = Integer.parseInt(in.readUTF());
						readyToFight = true;
						boolean allReady = true;
						for (ClientHandler c : myHost.playerList) {
							if (!c.readyToFight) {
								allReady = false;
							}
						}
						if (allReady) {
							myHost.out.writeUTF("starting fight automatically");
						}
						break;
					case "done":
						myHost.out.writeUTF("player done");
						myHost.out.writeUTF(name);
						myHost.out.writeUTF(score + "");
						done = true;
						if (in.readUTF().equals("true")) {
							correct = true;
						}
						boolean allDone = true;
						for (ClientHandler c : myHost.playerList) {
							if (!c.done) {
								allDone = false;
							}
						}
						if (allDone) {
							myHost.out.writeUTF("display ending");
						}
						break;
					}
					line = in.readUTF();
				}
			} catch (Exception e) {
				System.out.println(e);
			}
		}
	}

	/**
	 * Helper method adds player to a host's game
	 * 
	 * @param c - Player to be added for respective host
	 */
	public void addPlayer(ClientHandler c) {
		playerList.add(c);
	}

	/**
	 * Getter method for code
	 * 
	 * @return - Code connecting the player and host
	 */
	public int getCode() {
		return code;
	}
}