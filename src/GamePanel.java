import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import javax.swing.Timer;

public class GamePanel extends JPanel implements ActionListener {
	
	static final int screenWidth = 600;	// Set screen width
	static final int screenHeight = 600;	// Set screen height
	static final int unitSize = 25;	// Set object(s) size
	static final int gameUnits = (screenWidth * screenHeight) / unitSize;	// Calculate how many objects screen can fit
	static final int delay = 75; // Speed of game. Higher number, slower game
	
	// Arrays to hold coordinates for snake head and body
	final int x[] = new int[gameUnits];	// Holds all x-coordinates of snake head and body
	final int y[] = new int[gameUnits];	// Holds all y-coordinates of snake head and body
	
	int body = 6;	// Initial starting size of snake
	int applesEaten = 0;	// Holds number of apples eaten
	int appleX;	// X-coordinate of where apple is located
	int appleY;	// Y-coordinate of where apple is located
	char direction = 'R';	// Starting direction ('R' = right)
	boolean running = false;
	Timer timer;	// Declare a timer
	Random random;	// Instance of the random class
	
	GamePanel() {
		random = new Random();	// Create instance of the random class
		
		// Construct game panel
		this.setPreferredSize(new Dimension(screenWidth, screenHeight));	// Set preferred size of game panel, pass in dimension
		this.setBackground(Color.black);	// Set background color
		this.setFocusable(true);	// Lets component (JPanel) have power of getting focused
		this.addKeyListener(new MyKeyAdapter());	// Listener interface for receiving keyboard events (keystrokes)
		
		startGame();	// Call startGame() method
	}
	// Method to start the game
	public void startGame() {
		newApple();	// Call newApple() method
		running = true;	// Set boolean running to true
		// Create instance of timer, passing "delay" to dictate game speed, and "this" for ActionListener interface
		timer = new Timer(delay, this);
		timer.start();	// Take timer and use "start" function to start timer
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);	// Pass the graphics context off to the component's UI delegate, which paints the panel's background
		draw(g);	// Call draw() method passing "g" as argument
	}
	// Method to draw the snake and apple
	public void draw(Graphics g) {
		// If game is running, execute block
		if (running) {
			// To display grid lines
			/* for (int i = 0; i < screenHeight/unitSize; i++) {
				g.drawLine(i*unitSize, 0, i*unitSize, screenHeight);
				g.drawLine(0, i*unitSize, screenWidth, i*unitSize);
			} */
			g.setColor(Color.red);	// Set the color of the apple to red
			g.fillOval(appleX, appleY, unitSize, unitSize);	// Fill the color of the apple, passing coordinates and unitSize for size
		
			// Set the color of snake's head
			for (int i = 0; i < body; i++) {
				if (i == 0) {
					g.setColor(Color.green);	// Set color of head
					g.fillOval(x[i], y[i], unitSize, unitSize);	// Fill the color of the head, passing coordinates and unitSize for size
				}
				// Set the color of the snake's body
				else {
					g.setColor(new Color(45,180,0));	// Set color of body
					g.fillOval(x[i], y[i], unitSize, unitSize);	// Fill the color of the body, passing coordinates and unitSize for size
				}
			}
			// Display the score (number of apples eaten) at the top of the game
			g.setColor(Color.red);	// Set the font color
			g.setFont(new Font("Arial", Font.BOLD, 25));	// Set the font style
			FontMetrics metrics = getFontMetrics(g.getFont());	// Create instance of FontMetrics to line up text in middle of screen
			// Display "Score" string at the top, center of screen
			g.drawString("Score: " + applesEaten, (screenWidth - metrics.stringWidth("Score: " + applesEaten))/2, g.getFont().getSize());
		}
		// else if game is not running, execute gameOver() method
		else {
			gameOver(g);	// Call the gameOver() method passing "g" as argument
		}
	}
	// Method to move the snake
	public void move() {
		// for loop to iterate through all of the body parts of the snake
		// Set "i" equal to body parts, continue loop as long as "i" is less than 0, and decrement "i" after each loop
		for (int i = body; i > 0; i--) {
			x[i] = x[i-1];	// Shifts all coordinates of array over one spot
			y[i] = y[i-1];	// Shifts all coordinates of array over one spot
		}
		// Switch to change direction of snake
		switch(direction) {
		case 'U':	// Switch case for up
			y[0] = y[0] - unitSize;
			break;
		case 'D':	// Switch case for down
			y[0] = y[0] + unitSize;
			break;
		case 'L':	// Switch case for left
			x[0] = x[0] - unitSize;
			break;
		case 'R':	// Switch case for right
			x[0] = x[0] + unitSize;
			break;
		}
	}
	// Method to randomly populate a new apple
	public void newApple() {
		appleX = random.nextInt((int)(screenWidth/unitSize)) * unitSize;
		appleY = random.nextInt((int)(screenHeight/unitSize)) * unitSize;
	}
	// Method 
	public void checkApple() {
		// Check coordinates of snake and coordinates of apple
		if ((x[0] == appleX) && (y[0] == appleY)) {
			body++;	// If "if" statement is true, increment body by 1
			applesEaten++;	// Increment applesEaten by 1 for score
			newApple();	// Call newApple() method to generate a new random apple
		}
	}
	// Method to check for collisions with border or body
	public void checkCollision() {
		// Checks if head collides with body
		// for loop to iterate through body of snake
		for (int i = body; i > 0; i--) {
			if ((x[0] == x[i] && y[0] == y[i])) {
				// If "if" statement is true, means head collided with body, set running to false to trigger end of game
				running = false; 
			}
		}
		// Checks if head collides with left border
		if (x[0] < 0) {
			running = false;
		}
		// Checks if head collides with right border
		if (x[0] > screenWidth) {
			running = false;
		}
		// Checks if head collides with top border
		if (y[0] < 0) {
			running = false;
		}
		// Checks if head collides with bottom border
		if (y[0] > screenHeight) {
			running = false;
		}
		// If running is false, stop timer
		if (!running) {
			timer.stop();
		}	
	}
	// Method to display /
	public void gameOver(Graphics g) {
		// Score text
		g.setColor(Color.red);	// Set color to red
		g.setFont(new Font("Arial", Font.BOLD, 25));	// Set font style
		FontMetrics metrics1 = getFontMetrics(g.getFont());	// FontMetrics to line up text in middle of screen
		// Display the string text "Score: " and score at the top, middle of screen
		g.drawString("Score: " + applesEaten, (screenWidth - metrics1.stringWidth("Score: " + applesEaten))/2, g.getFont().getSize());
		
		// Game Over text
		g.setColor(Color.red);	// Set color to red
		g.setFont(new Font("Ink Free", Font.BOLD, 75));	// Set font style
		FontMetrics metrics2 = getFontMetrics(g.getFont());	// FontMetrics to line up text in middle of screen
		// Display the string text "Game Over" in the center of the screen
		g.drawString("Game Over", (screenWidth - metrics2.stringWidth("Game Over"))/2, screenHeight/2);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// Check if game is running
		if (running) {
			move();	// Call move() function
			checkApple();	// Call checkApple() method to check if apple is eaten
			checkCollision();	// Call checkCollision() method to check if snake has run into body or border
		}
		repaint();	// If game is no longer running, call repaint() method
	}
	
	public class MyKeyAdapter extends KeyAdapter {
		@Override
		public void keyPressed(KeyEvent e) {
			// Switch to examine e KeyEvent, and getKeyCode
			switch(e.getKeyCode()) {
			case KeyEvent.VK_LEFT:	// KeyEvent for left arrow key
				if (direction != 'R') {	// If direction does not equal right
					direction = 'L';	// Set direction to left
				}
				break;
			case KeyEvent.VK_RIGHT:	// KeyEvent for right arrow key
				if (direction != 'L') {	// If direction does not equal left
					direction = 'R';	// Set direction to right
				}
				break;
			case KeyEvent.VK_UP:	// KeyEvent for up arrow key
				if (direction != 'D') {	// If direction does not equal down
					direction = 'U';	// Set direction to up
				}
				break;
			case KeyEvent.VK_DOWN:	// KeyEvent for down arrow key
				if (direction != 'U') {	// If direction does not equal up
					direction = 'D';	// Set direction to down
				}
				break;
			}
		}
	}
}

