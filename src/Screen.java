import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;
import javax.swing.*;

/**
 * Game screen
 */
public class Screen extends JFrame implements ActionListener {
	/** Host of the game */
	private Host h;
	/** Player of the game */
	private Player p;
	/** Panels containing game components */
	private JPanel playGame, hostPanel, codePanel, stars, questionPanel1, questionPanel2, questionPanelOverall,
			animationPanel;
	/** Buttons with commands and answer choices */
	private JButton host, player, a1, a2, a3, a4;
	/** Displays text */
	private TextField display1, display2, output;
	private JTextArea displayQu, displayM;
	private JLabel displayLabel1, displayLabel2, descLabel, imageLabel, otherImageLabel, timer;
	/** ArrayList containing information on the current question being displayed */
	private ArrayList<String> currentQ;
	private ArrayList<JLabel> theStars, theStarNames;
	private int width, height, questionCount = 0, i = 60;
	private Timer t;
	private Font newFont = new Font("Academy Engraved LET", Font.BOLD, 75);
	private Font inputFont = new Font("Palatino", Font.BOLD, 70);
	private Font nameFont = new Font("Comic Sans MS", Font.BOLD, 30);
	private Font codeFont = new Font("Courier NEW MS", Font.PLAIN, 30);
	/** Colors to be used */
	private Color c = new Color(238, 238, 238);
	private Color yel = new Color(255, 243, 138);
	private Color blu = new Color(166, 228, 255);
	private Color pin = new Color(255, 207, 235);
	private Color pur = new Color(218, 195, 255);

	private static int starCount = 0;

	/**
	 * Creates new home screen
	 * 
	 * @author - Kaitlyn
	 * @param title - Header of the frame
	 */
	public Screen(String title) {
		super(title);
		// Sets game screen size to computer screen size
		setSize(Toolkit.getDefaultToolkit().getScreenSize());

		// Gets current height and width of screen
		height = (int) getSize().getHeight();
		width = (int) getSize().getWidth();
		createHomeScreen();
		setResizable(false);
	}

	/**
	 * Creates home screen for user
	 * 
	 * @author - Nivedita
	 */
	public void createHomeScreen() {
		// Create components
		playGame = new JPanel();
		playGame.setLayout(new FlowLayout());

		host = new JButton("Host Game");
		host.setFont(newFont);
		host.setPreferredSize(new Dimension(width / 3, height / 3));
		host.setBackground(new Color(255, 197, 228));
		host.setOpaque(true);
		host.setBorderPainted(false);

		player = new JButton("Play Game");
		player.setFont(newFont);
		player.setPreferredSize(new Dimension(width / 3, height / 3));
		player.setBackground(new Color(136, 221, 228));
		player.setOpaque(true);
		player.setBorderPainted(false);

		// Set commands
		host.setActionCommand("host");
		player.setActionCommand("player");

		// Set listeners
		host.addActionListener(this);
		player.addActionListener(this);

		// Add
		playGame.add(Box.createRigidArea(new Dimension(1, (int) (height * 2 / 3))));
		playGame.add(host);
		playGame.add(Box.createRigidArea(new Dimension(width / 6, 1)));
		playGame.add(player);
		add(playGame);
		playGame.setBackground(new Color(192, 207, 255));
		playGame.setPreferredSize(new Dimension(width, height));

		setLayout(new FlowLayout());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	/**
	 * Creates host screen to display code for players
	 * 
	 * @author - Inaya and Ritali
	 * @param c - Unique code
	 */
	public void displayCode(String c) {
		// Clears screen
		getContentPane().removeAll();
		// New panel to hold the code and stars
		hostPanel = new JPanel();
		hostPanel.setPreferredSize(new Dimension(width, height));
		hostPanel.setLayout(new BoxLayout(hostPanel, BoxLayout.Y_AXIS));

		// New panel to hold code
		codePanel = new JPanel();
		codePanel.setPreferredSize(new Dimension(width, height / 10));
		codePanel.setLayout(new BoxLayout(codePanel, BoxLayout.X_AXIS));

		// New panel to hold stars
		stars = new JPanel();
		stars.setPreferredSize(new Dimension(getContentPane().getWidth(), height));
		stars.setMaximumSize(stars.getPreferredSize());
		FlowLayout layout = (FlowLayout) stars.getLayout();
		stars.setLayout(layout);
		layout.setVgap(-100);

		// TextField with code displayed
		display1 = new TextField(6);
		display1.setFont(newFont);
		display1.setEditable(false);
		display1.setBackground(new Color(238, 238, 238));
		display1.setText(c);
		display1.setFont(new Font("Comic Sans MS", Font.BOLD, 75));

		// Button to start game for all players
		host = new JButton("Start Game");
		host.setFont(new Font("Comic Sans MS", Font.PLAIN, 26));
		host.addActionListener(this);
		host.setActionCommand("start game");
		host.setBackground(blu);
		host.setOpaque(true);
		host.setBorderPainted(false);

		// Add the stars to screen
		theStars = new ArrayList<JLabel>();
		theStarNames = new ArrayList<JLabel>();
		for (int i = 0; i < 40; i++) {
			// Panel to hold both the star and name
			JPanel starPanel = new JPanel();
			starPanel.setLayout(new OverlayLayout(starPanel));
			starPanel.setMaximumSize(new Dimension(width / 9, height / 5));

			// Name of player
			JLabel name = new JLabel();
			name.setOpaque(false);
			name.setPreferredSize(new Dimension(3 * starPanel.getWidth() / 4, 3 * starPanel.getHeight() / 4));

			// Star
			JLabel star = new JLabel("★");
			star.setFont(new Font("Verdana", Font.PLAIN, 230));
			star.setForeground(Color.gray);
			star.setSize(3 * starPanel.getWidth() / 5, 3 * starPanel.getHeight() / 4);

			starPanel.add(star);
			starPanel.add(name);
			starPanel.setOpaque(true);
			stars.add(starPanel);

			// Add to ArrayList
			theStars.add(star);
			theStarNames.add(name);
		}
		stars.setOpaque(true);
		stars.setAlignmentX(Component.CENTER_ALIGNMENT);

		codePanel.add(Box.createRigidArea(new Dimension(21 * width / 48, height / 10)));
		codePanel.add(display1);
		codePanel.add(Box.createRigidArea(new Dimension(width / 8, height / 10)));
		codePanel.add(host);
		codePanel.add(Box.createRigidArea(new Dimension(width / 8, height / 10)));

		hostPanel.add(codePanel);
		hostPanel.add(stars);

		add(hostPanel);
		repaint();
		setVisible(true);
	}

	/**
	 * Adds yellow star to host waiting screen with a nickname, signifying another
	 * player has joined
	 * 
	 * @author - Inaya
	 * @param name - name of the player being added
	 */
	public void addStar(String name) {
		Color yellow = new Color(255, 243, 138);
		theStars.get(starCount).setForeground(yellow);
		theStarNames.get(starCount).setText(name);
		theStarNames.get(starCount).setFont(nameFont);
		theStarNames.get(starCount).setOpaque(true);
		starCount++;
	}

	/**
	 * Displays opening sequence for the player
	 * 
	 * @author - April and Nivedita
	 */
	public void displayOpening() {
		getContentPane().removeAll();

		// Reset panels
		playGame = new JPanel();
		playGame.setLayout(new BoxLayout(playGame, BoxLayout.Y_AXIS));
		playGame.setPreferredSize(new Dimension(4 * width / 5, 5 * height / 6));
		questionPanelOverall = new JPanel();
		questionPanelOverall.setLayout(new BoxLayout(questionPanelOverall, BoxLayout.Y_AXIS));
		// Holds nickname
		questionPanel1 = new JPanel();
		questionPanel1.setLayout(new BoxLayout(questionPanel1, BoxLayout.X_AXIS));
		// Holds code
		questionPanel2 = new JPanel();
		questionPanel2.setLayout(new BoxLayout(questionPanel2, BoxLayout.X_AXIS));
		// Holds image
		animationPanel = new JPanel();
		animationPanel.setLayout(new BoxLayout(animationPanel, BoxLayout.Y_AXIS));

		// TextField and JLabel for nickname
		displayLabel1 = new JLabel("Enter your nickname: ");
		displayLabel1.setFont(newFont);
		display1 = new TextField(15);
		display1.setEditable(true);
		display1.addActionListener(this);
		display1.setPreferredSize(new Dimension(height / 3, width / 9));
		display1.setFont(inputFont);

		// TextField and JLabel for code
		displayLabel2 = new JLabel("Enter your code: ");
		displayLabel2.setFont(newFont);
		display2 = new TextField(15);
		display2.setEditable(true);
		display2.addActionListener(this);
		display2.setPreferredSize(new Dimension(height / 3, width / 9));
		display2.setFont(inputFont);

		// Output TextField to display any errors that occur
		output = new TextField(15);
		output.setEditable(false);
		output.setFont(newFont);
		output.setBackground(c);

		// Quest for players
		descLabel = new JLabel(
				"<html><center>Your quest is to defeat the boss with your fellow companions! Good luck!</center></html>");
		descLabel.setFont(new Font("Academy Engraved LET", Font.PLAIN, 35));
		descLabel.setAlignmentX(CENTER_ALIGNMENT);
		try {
			String relName = "assets/boss.png";
			ImageIcon ii = new ImageIcon(Toolkit.getDefaultToolkit().createImage(getClass().getResource(relName)));
			imageLabel = new JLabel();
			imageLabel.setIcon(ii);
		} catch (Exception e) {
			System.out.println(e);
		}

		// Add to screen
		questionPanel1.add(displayLabel1);
		questionPanel1.add(display1);
		questionPanel2.add(displayLabel2);
		questionPanel2.add(display2);
		questionPanelOverall.add(questionPanel1);
		questionPanelOverall.add(questionPanel2);
		questionPanelOverall.add(output);
		animationPanel.add(descLabel);
		animationPanel.add(imageLabel);
		playGame.add(Box.createRigidArea(new Dimension(1, height / 20)));
		playGame.add(questionPanelOverall);
		playGame.add(Box.createRigidArea(new Dimension(1, height / 20)));
		playGame.add(animationPanel);

		add(playGame);
		repaint();
		setVisible(true);
	}

	/**
	 * Displays player's progress in game on host screen
	 * 
	 * @author - April
	 */
	public void displayProgress() {
		getContentPane().removeAll();
		// Recreate panels
		hostPanel = new JPanel();
		hostPanel.setLayout(new BoxLayout(hostPanel, BoxLayout.Y_AXIS));
		hostPanel.setPreferredSize(new Dimension(4 * width / 5, 5 * height / 6));
		animationPanel = new JPanel();
		animationPanel.setLayout(new BorderLayout());

		// Get image of player
		try {
			String relName1 = "assets/char.gif";
			ImageIcon ii1 = new ImageIcon(Toolkit.getDefaultToolkit().createImage(getClass().getResource(relName1)));
			imageLabel = new JLabel();
			// Resize image
			Image img1 = ii1.getImage();
			int iiw1 = ii1.getIconWidth();
			int iih1 = ii1.getIconHeight();
			Image newimg1 = img1.getScaledInstance(height / 3 / iih1 * iiw1, height / 3, java.awt.Image.SCALE_DEFAULT);
			ii1 = new ImageIcon(newimg1);
			imageLabel.setIcon(ii1);

			String relName2 = "assets/plant.png";
			ImageIcon ii2 = new ImageIcon(Toolkit.getDefaultToolkit().createImage(getClass().getResource(relName2)));
			otherImageLabel = new JLabel();
			// Resize image
			Image img2 = ii2.getImage();
			int iiw2 = ii2.getIconWidth();
			int iih2 = ii2.getIconHeight();
			Image newimg2 = img2.getScaledInstance(height / 2 / iih2 * iiw2, height / 2, java.awt.Image.SCALE_DEFAULT);
			ii2 = new ImageIcon(newimg2);
			otherImageLabel.setIcon(ii2);
		} catch (Exception e) {
			System.out.println(e);
		}

		// Create JLabel and start button
		displayLabel1 = new JLabel("<html>Answer questions to prepare for the final boss fight... GO GO GO!</html>");
		displayLabel1.setFont(new Font("Academy Engraved LET", Font.PLAIN, 50));
		host = new JButton("Fight boss now");
		host.setFont(new Font("Palatino", Font.PLAIN, 45));
		host.setPreferredSize(new Dimension(width / 18, height / 15));
		host.addActionListener(this);
		host.setActionCommand("fight now");

		animationPanel.add(host, BorderLayout.NORTH);
		animationPanel.add(imageLabel, BorderLayout.EAST);
		animationPanel.add(otherImageLabel, BorderLayout.WEST);
		hostPanel.add(displayLabel1);
		hostPanel.add(Box.createRigidArea(new Dimension(1, height / 20)));
		hostPanel.add(animationPanel);

		add(hostPanel);
		repaint();
		setVisible(true);
	}

	/**
	 * Displays a random output question ArrayList q contains the problem, method,
	 * four answer choices, and the answer in that respective order
	 * 
	 * @author - Ritali
	 * @param q - ArrayList of information on question to be displayed
	 */
	public void displayQ(ArrayList<String> q) {
		currentQ = q;
		getContentPane().removeAll();
		questionPanel1 = new JPanel();
		questionPanel1.setLayout(new BoxLayout(questionPanel1, BoxLayout.Y_AXIS));
		questionPanel2 = new JPanel();
		questionPanel2.setLayout(new FlowLayout()); // Change this if needed
		questionPanelOverall = new JPanel();
		questionPanelOverall.setLayout(null);
		questionPanelOverall.setPreferredSize(new Dimension(width, height));

		displayQu = new JTextArea(currentQ.get(0));
		displayQu.setEditable(false);
		displayQu.setFont(codeFont);
		displayQu.setBackground(c);
		displayM = new JTextArea(currentQ.get(1));
		displayM.setEditable(false);
		displayM.setFont(codeFont);
		displayM.setBackground(c);
		questionPanel1.add(displayQu);
		questionPanel1.add(displayM);
		questionPanel1.setMinimumSize(new Dimension(width / 2, height));

		a1 = new JButton(currentQ.get(2));
		a1.setPreferredSize(new Dimension(3 * width / 16, height / 3));
		a1.setFont(new Font("Comics Sans MS", Font.PLAIN, 35));
		a1.addActionListener(this);
		a1.setActionCommand("submit");
		a1.setBackground(yel);
		a1.setOpaque(true);
		a1.setBorderPainted(false);
		a2 = new JButton(currentQ.get(3));
		a2.setPreferredSize(new Dimension(3 * width / 16, height / 3));
		a2.setFont(new Font("Comics Sans MS", Font.PLAIN, 35));
		a2.addActionListener(this);
		a2.setActionCommand("submit");
		a2.setBackground(blu);
		a2.setOpaque(true);
		a2.setBorderPainted(false);
		a3 = new JButton(currentQ.get(4));
		a3.setPreferredSize(new Dimension(3 * width / 16, height / 3));
		a3.setFont(new Font("Comics Sans MS", Font.PLAIN, 35));
		a3.addActionListener(this);
		a3.setActionCommand("submit");
		a3.setBackground(pin);
		a3.setOpaque(true);
		a3.setBorderPainted(false);
		a4 = new JButton(currentQ.get(5));
		a4.setPreferredSize(new Dimension(3 * width / 16, height / 3));
		a4.setFont(new Font("Comics Sans MS", Font.PLAIN, 35));
		a4.addActionListener(this);
		a4.setActionCommand("submit");
		a4.setBackground(pur);
		a4.setOpaque(true);
		a4.setBorderPainted(false);
		player = new JButton("Next");
		player.setPreferredSize(new Dimension(width / 4, height / 6));
		player.setFont(new Font("Comics Sans MS", Font.PLAIN, 35));
		player.setVisible(false);
		player.addActionListener(this);
		player.setActionCommand("next");

		questionPanel2.add(a1);
		questionPanel2.add(a2);
		questionPanel2.add(a3);
		questionPanel2.add(a4);
		questionPanel2.add(player);

		questionPanelOverall.add(questionPanel1);
		questionPanel1.setBounds(width / 20, height / 20, width / 2 - width / 10, height - height / 10);
		questionPanelOverall.add(questionPanel2);
		questionPanel2.setBounds(11 * width / 20, height / 20, width / 2 - width / 10, height - height / 10);

		add(questionPanelOverall);
		repaint();
		setVisible(true);
	}

	/**
	 * Shows correct answer
	 * 
	 * @author - Kaitlyn
	 * @param evt - Button which triggered the event
	 * @return - Player's answer
	 */
	public String showCorrect(ActionEvent evt) {
		a1.setBackground(Color.gray);
		a2.setBackground(Color.gray);
		a3.setBackground(Color.gray);
		a4.setBackground(Color.gray);
		((JButton) evt.getSource()).setBackground(Color.red);
		String answer = getAnswer();
		if (a1.getText().equals(answer)) {
			a1.setBackground(Color.green);
		} else if (a2.getText().equals(answer)) {
			a2.setBackground(Color.green);
		} else if (a3.getText().equals(answer)) {
			a3.setBackground(Color.green);
		} else {
			a4.setBackground(Color.green);
		}
		a1.setOpaque(true);
		a2.setOpaque(true);
		a3.setOpaque(true);
		a4.setOpaque(true);
		a1.setEnabled(false);
		a2.setEnabled(false);
		a3.setEnabled(false);
		a4.setEnabled(false);

		return ((JButton) evt.getSource()).getText();
	}

	/**
	 * Displays player ready screen before host starts
	 * 
	 * @author - Ritali
	 */
	public void playerReady() {
		getContentPane().removeAll();
		JPanel container = new JPanel();
		container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
		animationPanel = new JPanel();
		animationPanel.setLayout(new BoxLayout(animationPanel, BoxLayout.X_AXIS));

		displayLabel1 = new JLabel("Waiting for the host to start the game...");
		displayLabel1.setFont(newFont);
		display1.setPreferredSize(new Dimension(height / 3, width / 9));
		displayLabel2 = new JLabel();
		display2.setPreferredSize(new Dimension(height / 3, width / 9));

		// Sets gif
		try {
			String relName = "assets/snoopy.gif";
			ImageIcon ii = new ImageIcon(Toolkit.getDefaultToolkit().createImage(getClass().getResource(relName)));
			displayLabel2.setIcon(ii);
		} catch (NullPointerException exception) {
			exception.printStackTrace();
			displayLabel2.setText("Your game will begin shortly");
			displayLabel2.setFont(newFont);
		}

		animationPanel.add(Box.createHorizontalGlue());
		animationPanel.add(displayLabel2);
		animationPanel.add(Box.createHorizontalGlue());
		container.add(Box.createRigidArea(new Dimension(1, height / 9)));
		container.add(displayLabel1);
		container.add(Box.createRigidArea(new Dimension(1, height / 9)));
		container.add(animationPanel);
		container.add(Box.createRigidArea(new Dimension(1, height / 9)));
		add(container);
		repaint();
		setVisible(true);
	}

	/**
	 * Shows waiting screen for player after finishing 10 questions
	 * 
	 * @author - Kaitlyn
	 */
	public void playerReadyToFightBoss() {
		getContentPane().removeAll();
		myPanel animate = new myPanel();
		add(animate);
		repaint();
		setVisible(true);
	}

	/**
	 * Displays stars again for each player finished
	 * 
	 * @author - Ritali and Inaya
	 */
	public void displayBossProgress() {
		// Clears screen
		getContentPane().removeAll();
		// New panel to hold the timer and stars
		hostPanel = new JPanel();
		hostPanel.setPreferredSize(new Dimension(width, height));
		hostPanel.setLayout(new BoxLayout(hostPanel, BoxLayout.Y_AXIS));

		// New panel to hold timer
		codePanel = new JPanel();
		codePanel.setPreferredSize(new Dimension(width, height / 10));
		codePanel.setLayout(new BoxLayout(codePanel, BoxLayout.X_AXIS));

		// New panel to hold stars
		stars = new JPanel();
		stars.setPreferredSize(new Dimension(getContentPane().getWidth(), height));
		stars.setMaximumSize(stars.getPreferredSize());
		FlowLayout layout = (FlowLayout) stars.getLayout();
		stars.setLayout(layout);
		layout.setVgap(-100);

		// JLabel with timer displayed
		timer = new JLabel("60");
		timer.setFont(newFont);

		// Add the stars to screen
		theStars = new ArrayList<JLabel>();
		theStarNames = new ArrayList<JLabel>();
		starCount = 0;
		for (int i = 0; i < 40; i++) {
			// Panel to hold both the star and name
			JPanel starPanel = new JPanel();
			starPanel.setLayout(new OverlayLayout(starPanel));
			starPanel.setMaximumSize(new Dimension(width / 9, height / 5));

			// Name of player
			JLabel name = new JLabel();
			name.setOpaque(false);
			name.setPreferredSize(new Dimension(3 * starPanel.getWidth() / 4, 3 * starPanel.getHeight() / 4));

			// Star
			JLabel star = new JLabel("★");
			star.setFont(new Font("Verdana", Font.PLAIN, 230));
			star.setForeground(Color.gray);
			star.setSize(3 * starPanel.getWidth() / 5, 3 * starPanel.getHeight() / 4);

			starPanel.add(star);
			starPanel.add(name);
			starPanel.setOpaque(true);
			stars.add(starPanel);

			// Add to ArrayList
			theStars.add(star);
			theStarNames.add(name);
		}
		stars.setOpaque(true);
		stars.setAlignmentX(Component.CENTER_ALIGNMENT);

		codePanel.add(timer);

		hostPanel.add(codePanel);
		hostPanel.add(stars);

		hostPanel.add(codePanel);
		hostPanel.add(stars);

		add(hostPanel);
		repaint();
		setVisible(true);

		// Timer part with delay and period
		int del = 1000;
		int per = 1000;
		t = new Timer();
		// Performs the specified task at certain intervals
		t.scheduleAtFixedRate(new TimerTask() {
			// task to be performed
			public void run() {
				timer.setText("" + seti());
			}
		}, del, per);
	}

	/**
	 * Helper method for timer
	 * 
	 * @author - Kaitlyn
	 * @return - current time
	 */
	private final int seti() {
		// if interval is 1, cancel
		if (i == 0) {
			h.endBossFight();
		}
		return --i;
	}

	/**
	 * Getter method to get JLabel Timer
	 * 
	 * @author - Kaitlyn
	 * @return - JLabel Object of timer
	 */
	public JLabel getTimer() {
		return timer;
	}

	/**
	 * Getter method to get Timer
	 * 
	 * @author - Kaitlyn
	 * @return - Timer Object t
	 */
	public Timer getT() {
		return t;
	}

	/**
	 * Changes the stars color based on player score
	 * 
	 * @author - Kaitlyn, Inaya, and Ritali
	 * @param name  - Name of the player
	 * @param score - Score of the player
	 */
	public void changeStarColor(String name, int score) {
		Color darkGreen = new Color(108, 194, 74);
		Color lightGreen = new Color(191, 245, 170);
		Color lightYellow = new Color(255, 236, 156);
		Color darkYellow = new Color(255, 219, 0);
		double res = (double) score / 10;
		if (res > 0.75) {
			theStars.get(starCount).setForeground(darkGreen);
		} else if (res > 0.50) {
			theStars.get(starCount).setForeground(lightGreen);
		} else if (res > 0.25) {
			theStars.get(starCount).setForeground(lightYellow);
		} else {
			theStars.get(starCount).setForeground(darkYellow);
		}
		theStarNames.get(starCount).setText(name);
		theStarNames.get(starCount).setFont(nameFont);
		theStarNames.get(starCount).setOpaque(true);
		starCount++;
	}

	/**
	 * Create final fight question
	 * 
	 * @author - Kaitlyn
	 * @param q - ArrayList containing the problem, method, answer choices, and
	 *          answer for the question
	 */
	public void finalFight(ArrayList<String> q) {
		displayQ(q);
		displayQu.setText("Answer correct & you shall deal a\ncritical blow unto the boss!"
				+ "\nAnswer wrong & drag your companions\ninto the abyss with you...\n\n" + displayQu.getText());
		repaint();
		player.setActionCommand("final");
	}

	/**
	 * Gets answer to current question
	 * 
	 * @author - Kaitlyn
	 * @return - String of answer
	 */
	public String getAnswer() {
		return currentQ.get(6);
	}

	/**
	 * Displays user defeating boss
	 * 
	 * @author - April
	 */
	public void displayGoodEnding() {
		getContentPane().removeAll();
		animationPanel = new JPanel();
		animationPanel.setLayout(new BoxLayout(animationPanel, BoxLayout.Y_AXIS));
		displayLabel1 = new JLabel("Your companions are glad to have you on their side!");
		displayLabel1.setFont(newFont);

		// Sets image
		try {
			String relName = "assets/youwin.png";
			ImageIcon win = new ImageIcon(Toolkit.getDefaultToolkit().createImage(getClass().getResource(relName)));
			Image imgWin = win.getImage();
			int iiw = win.getIconWidth();
			int iih = win.getIconHeight();
			Image newWin = imgWin.getScaledInstance(2 * width / 3, 2 * width / 3 / iiw * iih,
					java.awt.Image.SCALE_SMOOTH);
			win = new ImageIcon(newWin);
			displayLabel2 = new JLabel(win);
		} catch (NullPointerException exception) {
			exception.printStackTrace();
		}

		animationPanel.add(displayLabel1);
		animationPanel.add(displayLabel2);
		add(animationPanel);
		repaint();
		setVisible(true);
	}

	/**
	 * Displays user getting defeated by boss
	 * 
	 * @author - April
	 */
	public void displayBadEnding() {
		getContentPane().removeAll();
		animationPanel = new JPanel();
		animationPanel.setLayout(new BoxLayout(animationPanel, BoxLayout.Y_AXIS));
		displayLabel1 = new JLabel("You must rely on your companions to win...");
		displayLabel1.setFont(newFont);

		try {
			String relName = "assets/youlose.png";
			ImageIcon lose = new ImageIcon(Toolkit.getDefaultToolkit().createImage(getClass().getResource(relName)));
			Image imgWin = lose.getImage();
			int iiw = lose.getIconWidth();
			int iih = lose.getIconHeight();
			Image newlose = imgWin.getScaledInstance(2 * width / 3, 2 * width / 3 / iiw * iih,
					java.awt.Image.SCALE_SMOOTH);
			lose = new ImageIcon(newlose);
			displayLabel2 = new JLabel(lose);
		} catch (NullPointerException exception) {
			exception.printStackTrace();
		}

		animationPanel.add(displayLabel1);
		animationPanel.add(displayLabel2);
		add(animationPanel);
		repaint();
		setVisible(true);
	}

	/**
	 * Displays You Win or Game Over depending on if 80% of the players got the boss
	 * question correct
	 * 
	 * @author - Kaitlyn
	 * @param win - true if the game was won, false if not
	 */
	public void displayEnding(boolean win) {
		getContentPane().removeAll();
		String res = "";
		if (win) {
			res = "YOU WON!";
		} else {
			res = "GAME OVER";
		}
		display1 = new TextField(res);
		add(display1);
		repaint();
		setVisible(true);

	}

	/**
	 * Called when button is clicked and redirects to respective user type
	 * 
	 * @author - Kaitlyn
	 * @param evt - Generated event
	 */
	public void actionPerformed(ActionEvent evt) {
		if (evt.getActionCommand().equals("host")) {
			h = new Host();
		} else if (evt.getActionCommand().equals("player")) {
			p = new Player();
		} else if (evt.getActionCommand().equals("start game")) {
			displayProgress();
			h.giveQuestions();
		} else if (evt.getActionCommand().equals("submit")) {
			String a = showCorrect(evt);
			p.checkAnswer(a.equals(currentQ.get(6)));
			player.setVisible(true);

			if (player.getActionCommand().equals("final")) {
				p.setBossResponse(a);
			}
		} else if (evt.getActionCommand().equals("next")) {
			questionCount++;
			if (questionCount < 10) { // Change this later
				p.getQuestionBank().getRandomQuestion(false);
			} else {
				playerReadyToFightBoss();
				p.readyToFightBoss();
			}
		} else if (evt.getActionCommand().equals("final")) {
			if (p.getBossResponse().equals(getAnswer())) {
				displayGoodEnding();
				p.done(true);
			} else {
				displayBadEnding();
				p.done(false);
			}
		} else if (evt.getActionCommand().equals("fight now")) {
			h.startBossFight();
		} else if (!display1.getText().trim().equals("") && !display2.getText().equals("")) {
			String nickname = display1.getText().trim();
			if (nickname.length() > 15) {
				output.setText("Name is too long!");
			} else {
				p.setNickname(display1.getText());
				if (p.setCode(display2.getText())) {
					playerReady();
					p.acceptingQuestions();
				} else {
					output.setText("Invalid code!");
				}
			}
		}
	}
}