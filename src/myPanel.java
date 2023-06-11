import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * The panel for the map
 * 
 * @author April
 */
public class myPanel extends JPanel implements ActionListener {
	/** instance variables */
	private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	private final int PANEL_WIDTH = screenSize.width;
	private final int PANEL_HEIGHT = screenSize.height;

	/** declare components */
	private Image character;
	private Image boss;
	private Image grass;
	private Image tree;
	private Image treeShad;
	private Image charShad;
	private Image bossShad;

	/** Buttons for movement */
	private JButton moveUp;
	private JButton moveDown;
	private JButton moveLeft;
	private JButton moveRight;

	private JLabel waiting;

	private JButton stopMove;

	private Timer timer;
	private Timer timer1;
	private Timer timer2;
	private Timer timer3;
	private Timer timer4;

	private int xVelocity = 3;
	private int yVelocity = 3;
	private int charX = 0;
	private int charY = 20;
	private int bossX = 100;
	private int bossY = 100;
	private int grassX = 0;
	private int grassY = 0;
	private int treeX = 0;
	private int treeY = 0;

	private int fNum = 0;
	private int bNum = 0;

	/**
	 * Constructor for the panel with RPG map
	 */
	public myPanel() {
		this.setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
		this.setBackground(new Color(192, 207, 255));

		// initialize components
		character = new ImageIcon(this.getClass().getResource("/chars/char0.png")).getImage();
		charShad = new ImageIcon(this.getClass().getResource("/assets/charShad.png")).getImage();
		boss = new ImageIcon(this.getClass().getResource("/assets/boss00.png")).getImage();
		bossShad = new ImageIcon(this.getClass().getResource("/assets/bossShad.png")).getImage();
		grass = new ImageIcon(this.getClass().getResource("/assets/grass.png")).getImage();
		tree = new ImageIcon(this.getClass().getResource("/assets/plant.png")).getImage();
		treeShad = new ImageIcon(this.getClass().getResource("/assets/treeShad.png")).getImage();

		grassX = (int) (Math.random() * PANEL_WIDTH);
		grassY = (int) (Math.random() * PANEL_HEIGHT);

		treeX = PANEL_WIDTH - 300;
		treeY = PANEL_HEIGHT - 800;

		bossX = treeX - 450;
		bossY = treeY + 200;

		waiting = new JLabel("Waiting upon companions....");
		add(waiting);

		stopMove = new JButton("Stop");
		stopMove.setActionCommand("stop");
		stopMove.addActionListener(this);
		add(stopMove);

		moveUp = new JButton("UP");
		moveUp.setActionCommand("move up");
		moveUp.addActionListener(this);
		add(moveUp);

		moveDown = new JButton("DOWN");
		moveDown.setActionCommand("move down");
		moveDown.addActionListener(this);
		add(moveDown);

		moveLeft = new JButton("LEFT");
		moveLeft.setActionCommand("move left");
		moveLeft.addActionListener(this);
		add(moveLeft);

		moveRight = new JButton("RIGHT");
		moveRight.setActionCommand("move right");
		moveRight.addActionListener(this);
		add(moveRight);

		timer = new Timer(300, this);
		timer.setActionCommand("boss frame");
		timer.start();

		timer1 = new Timer(50, this);
		timer1.setActionCommand("char right");

		timer2 = new Timer(50, this);
		timer2.setActionCommand("char left");

		timer3 = new Timer(50, this);
		timer3.setActionCommand("char up");

		timer4 = new Timer(50, this);
		timer4.setActionCommand("char down");
	}

	/**
	 * Paints images
	 * 
	 * @param g - Graphics object
	 */
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D g2D = (Graphics2D) g;

		g2D.drawImage(grass, grassX, grassY, null);
		g2D.drawImage(grass, grassX + 300, grassY + 1000, null);
		g2D.drawImage(grass, grassX - 1000, grassY - 100, null);
		g2D.drawImage(grass, grassX + 500, grassY - 300, null);
		g2D.drawImage(grass, grassX - 200, grassY - 1000, null);
		g2D.drawImage(grass, grassX + 1000, grassY - 100, null);

		float opacity = 0.5f;
		g2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
		g2D.drawImage(treeShad, treeX + 70, treeY + 360, null);
		g2D.drawImage(bossShad, bossX + 65, bossY + 310, null);
		g2D.drawImage(charShad, charX + 20, charY + 165, null);

		g2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
		g2D.drawImage(tree, treeX, treeY, null);
		g2D.drawImage(boss, bossX, bossY, null);
		g2D.drawImage(character, charX, charY, null);
	}

	/**
	 * Responds to inputs
	 * 
	 * @param e - ActionEvent object
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		String fName = "/chars/char";
		String bName = "/assets/boss0";

		if (e.getActionCommand().equals("stop")) {
			timer1.stop();
			timer2.stop();
			timer3.stop();
			timer4.stop();
		}

		if (e.getActionCommand().equals("boss frame")) {
			if (bNum < 3) {
				bNum++;
			} else {
				bNum = 0;
			}
			bName += bNum;
			bName += ".png";
			boss = new ImageIcon(this.getClass().getResource(bName)).getImage();
			repaint();
		}

		if (e.getActionCommand().equals("move right")) {
			timer1.start();
			timer3.stop();
			timer2.stop();
			timer4.stop();
		}

		if (e.getActionCommand().equals("char right")) {
			fName = "/chars/char";
			if (fNum < 22)
				fNum++;
			else
				fNum = 0;
			if (fNum < 10 && fNum > 0) {
				fName += "0" + fNum;
			} else {
				fName += fNum;
			}
			fName += ".png";
			character = new ImageIcon(this.getClass().getResource(fName)).getImage();
			if (charX > PANEL_WIDTH - 200) {
				charX -= xVelocity;
			} else {
				charX += xVelocity;
			}
			repaint();
		}

		if (e.getActionCommand().equals("char left")) {
			fName = "/chars/char";
			if (fNum > 0)
				fNum--;
			else
				fNum = 22;
			if (fNum < 10 && fNum > 0) {
				fName += "0" + fNum;
			} else {
				fName += fNum;
			}
			fName += ".png";
			character = new ImageIcon(this.getClass().getResource(fName)).getImage();
			if (charX < 0) {
				charX += xVelocity;
			} else {
				charX -= xVelocity;
			}
			repaint();
		}

		if (e.getActionCommand().equals("move left")) {
			timer2.start();
			timer1.stop();
			timer3.stop();
			timer4.stop();
		}

		if (e.getActionCommand().equals("char up")) {
			fName = "/chars/char";
			if (fNum < 22)
				fNum++;
			else
				fNum = 0;
			if (fNum < 10 && fNum > 0) {
				fName += "0" + fNum;
			} else {
				fName += fNum;
			}
			fName += ".png";
			character = new ImageIcon(this.getClass().getResource(fName)).getImage();
			if (charY < 20) {
				charY += yVelocity;
			} else {
				charY -= yVelocity;
			}
			repaint();
		}

		if (e.getActionCommand().equals("move up")) {
			timer3.start();
			timer1.stop();
			timer2.stop();
			timer4.stop();
		}

		if (e.getActionCommand().equals("char down")) {
			fName = "/chars/char";
			if (fNum < 22)
				fNum++;
			else
				fNum = 0;
			if (fNum < 10 && fNum > 0) {
				fName += "0" + fNum;
			} else {
				fName += fNum;
			}
			fName += ".png";
			character = new ImageIcon(this.getClass().getResource(fName)).getImage();
			if (charY > PANEL_HEIGHT - 250) {
				charY -= yVelocity;
			} else {
				charY += yVelocity;
			}
			repaint();
		}

		if (e.getActionCommand().equals("move down")) {
			timer4.start();
			timer1.stop();
			timer2.stop();
			timer3.stop();
		}
	}

}