/**
 * Creates the home screen User has a choice of host or player
 */
public class Main {
	/** Connect: 73.202.231.180 for server; 127.0.0.1 for local */
	public static final String ADDRESS = "73.202.231.180";
	/** Connect to port 5000 default */
	public static final int PORT = 5000;
	private static Screen screen;

	/**
	 * Start of program
	 * 
	 * @author - Kaitlyn
	 * @param args - String array
	 */
	public static void main(String[] args) {
		screen = new Screen("Play CSGO!");
		screen.setVisible(true);
	}

	/**
	 * Returns the main game screen
	 * 
	 * @author - Kaitlyn
	 * @return - Screen for the game
	 */
	public static Screen getScreen() {
		return screen;
	}
}
