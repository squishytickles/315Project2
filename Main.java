import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Image;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


public class Main extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Main frame = new Main();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 * @throws IOException 
	 */
	public Main() throws IOException {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		
		//insert background image	
		/*Image backgroundImage = ImageIO.read(new File("test.jpg"));
		contentPane.setIcon( backgroundImage );
		contentPane.setLayout( new BorderLayout() );*/
		setContentPane(contentPane);
		
		contentPane.setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBounds(150, 129, 113, 96);
		contentPane.add(panel);
		
		JButton btnNewGame = new JButton("New Game");
		panel.add(btnNewGame);
		
		JButton btnInstructions = new JButton("Instructions");
		panel.add(btnInstructions);
		
		JButton btnHistory = new JButton("History");
		panel.add(btnHistory);
		
		JLabel lblNewLabel = new JLabel("Fanorona");
		lblNewLabel.setFont(new Font("Nyala", Font.PLAIN, 42));
		lblNewLabel.setBounds(136, 33, 144, 46);
		contentPane.add(lblNewLabel);
	}
}
