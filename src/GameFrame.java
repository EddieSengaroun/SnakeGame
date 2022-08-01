import javax.swing.JFrame;

public class GameFrame extends JFrame {
	
	GameFrame() {
		
		GamePanel panel = new GamePanel();	// Creates an instance of GamePanel Class
		
		this.add(panel);	// Add new GamePanel to frame
		this.setTitle("Snake");	// Add title to frame
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	// Method to specify option for close button
		this.setResizable(false);	// Prevent frame from re-sizing
		this.pack();	// "pack" fits JFrame around all components added to frame
		this.setVisible(true);	// Make the frame appear on the screen
		this.setLocationRelativeTo(null);	// Make the window appear in middle of monitor
	}
}
