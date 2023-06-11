import java.util.Scanner;
import java.io.*;
import java.net.*;

/**
 * Handles player side of game
 */
public class Player extends Thread {
	private String name;
	private String code;
	private int score = 0;
	private static Scanner scan = new Scanner(System.in);
	// initialize socket and input output streams
	private Socket socket = null;
	private DataInputStream in = null;
	private DataOutputStream out = null;
	private QuestionBank b;

	private String bossResponse;

	/**
	 * Creates new player
	 * 
	 * @author - Kaitlyn
	 */
	public Player() {
		// Establish a connection with server
		try {
			socket = new Socket(Main.ADDRESS, Main.PORT);
			// Allows output to be sent and received to and from the socket
			out = new DataOutputStream(socket.getOutputStream());
			in = new DataInputStream(socket.getInputStream());
			out.writeUTF("player");
		} catch (Exception e) {
			System.out.println(e);
		}
		Main.getScreen().displayOpening();
	}

	/**
	 * Setter for player nickname
	 * 
	 * @author - Ritali
	 * @param n - player name
	 */
	public void setNickname(String n) {
		name = n;
	}

	/**
	 * Getter for player nickname
	 * 
	 * @author - Ritali
	 * @return - player name
	 */
	public String getNickname() {
		return name;
	}

	/**
	 * Setter for player's response to boss question
	 * 
	 * @author - Ritali
	 * @param n - player name
	 */
	public void setBossResponse(String n) {
		bossResponse = n;
	}

	/**
	 * Getter for player's response to boss question
	 * 
	 * @author - Ritali
	 * @param n - player name
	 * @return - String for the player's response to boss question
	 */
	public String getBossResponse() {
		return bossResponse;
	}

	/**
	 * Setter for player code
	 * 
	 * @author - Ritali and Kaitlyn
	 * @param c - player code
	 * @return - true if code is valid and false if not
	 */
	public boolean setCode(String c) {
		code = c;
		try {
			int chckedCode = Integer.parseInt(c);
			out.writeUTF("new code");
			out.writeUTF(c);
			if (in.readUTF().equals("invalid")) {
				return false;
			} else {
				out.writeUTF(getNickname());
			}
		} catch (NumberFormatException ne) {
			System.out.println(ne);
			return false;
		} catch (Exception e) {
			System.out.println(e);
		}
		return true;
	}

	public void acceptingQuestions() {
		start();
	}

	/**
	 * Thread to await responses from server
	 * 
	 * @author - Kaitlyn
	 */
	@Override
	public void run() {
		try {
			if (in.readUTF().equals("start questions")) {
				b = new QuestionBank();
				b.getRandomQuestion(false);
			}
			if (in.readUTF().equals("start boss fight for player")) {
				b.getRandomQuestion(true);
				Thread.sleep(60000); // 1 minute countdown until auto submit
				if (getBossResponse() == null) {
					Main.getScreen().displayBadEnding();
					done(false);
				}
			}
			if (in.readUTF().equals("exit")) {
				in.close();
				out.close();
				socket.close();
			}
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	/**
	 * Sends message to server that player is ready to fight boss
	 * 
	 * @author - Ritali
	 */
	public void readyToFightBoss() {
		try {
			out.writeUTF("ready to fight");
			out.writeUTF(score + "");
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	/**
	 * Getter method for question bank
	 * 
	 * @author - Ritali
	 * @return - the Question Bank
	 */
	public QuestionBank getQuestionBank() {
		return b;
	}

	/**
	 * Changes player's score if answer is correct
	 * 
	 * @author - Ritali
	 * @param b - true if answer is correct and false if not
	 */
	public void checkAnswer(boolean b) {
		if (b) {
			score++;
		}
	}

	/**
	 * Sends message to server if player is done with the boss fight and if they got
	 * the final question right
	 * 
	 * @author - Kaitlyn
	 * @param correct - true if player got boss question correct and false if not
	 */
	public void done(boolean correct) {
		try {
			out.writeUTF("done");
			out.writeUTF(correct ? "true" : "false");
		} catch (Exception e) {
			System.out.println(e);
		}
	}
}
