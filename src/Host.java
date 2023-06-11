import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Handles host side of game
 */
public class Host extends Thread {
	private boolean ready = false;
	private boolean readyToFight = false;
	private boolean done = false;
	private Socket socket = null;
	private DataInputStream in = null;
	private DataOutputStream out = null;

	/**
	 * Creates new host
	 * 
	 * @author - Kaitlyn
	 */
	public Host() {
		try {
			socket = new Socket(Main.ADDRESS, Main.PORT);
			// Allows output to be sent and received to and from the socket
			out = new DataOutputStream(socket.getOutputStream());
			in = new DataInputStream(socket.getInputStream());
			out.writeUTF("host");
		} catch (Exception e) {
			System.out.println(e);
			System.exit(0);
		}
		host();
	}

	/**
	 * Begins hosting and displays code
	 * 
	 * @author - Kaitlyn
	 */
	public void host() {
		String code = "";
		try {
			code = in.readUTF();
		} catch (Exception e) {
			System.out.println(e);
		}
		Main.getScreen().displayCode(code);
		start();
	}

	/**
	 * Runs a thread which awaits input from server
	 * 
	 * @author - Kaitlyn
	 */
	@Override
	public void run() {
		while (!ready) {
			try {
				if (in.readUTF().equals("light")) {
					Main.getScreen().addStar(in.readUTF());
				}
			} catch (Exception e) {
				System.out.println(e);
			}
		}
		while (!readyToFight) {
			try {
				String line = in.readUTF();
				if (line.equals("starting fight automatically")) {
					readyToFight = true;
					startBossFight();
				}
			} catch (Exception e) {
				System.out.println(e);
			}
		}
		while (!done) {
			try {
				String line = in.readUTF();
				if (line.equals("player done")) {
					String name = in.readUTF();
					int score = Integer.parseInt(in.readUTF());
					Main.getScreen().changeStarColor(name, score);
				} else if (line.equals("display ending")) {
					endBossFight();
				} else if (line.equals("end")) {
					break;
				}
			} catch (Exception e) {
				System.out.println(e);
			}
		}
	}

	/**
	 * Sends message to server to give players the questions
	 * 
	 * @author - Kaitlyn
	 */
	public void giveQuestions() {
		ready = true;
		try {
			out.writeUTF("give questions");
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	/**
	 * Sends message to server to begin begin boss fight for players
	 * 
	 * @author - Kaitlyn
	 */

	public void startBossFight() {
		try {
			readyToFight = true;
			out.writeUTF("start boss fight");
			Main.getScreen().displayBossProgress();
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	/**
	 * Sends message to server to end boss fight
	 * 
	 * @author - Kaitlyn
	 */

	public void endBossFight() {
		try {
			done = true;
			out.writeUTF("display rest");
			boolean win = false;
			String l = in.readUTF();
			if (l.equals("true")) {
				win = true;
			}
			if (win) {
				Main.getScreen().getT().cancel();
				Main.getScreen().getTimer().setText("You Won!");
				Main.getScreen().repaint();
			} else {
				Main.getScreen().getT().cancel();
				Main.getScreen().getTimer().setText("Game Over!");
				Main.getScreen().repaint();
			}
			String line = in.readUTF();
			while (!line.equals("complete")) {
				String score = in.readUTF();
				Main.getScreen().changeStarColor(line, Integer.parseInt(score));
				line = in.readUTF();
			}
		} catch (Exception e) {
			System.out.println(e);
		}
	}
}
