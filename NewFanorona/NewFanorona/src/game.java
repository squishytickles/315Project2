import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.imageio.ImageIO;
import javax.swing.*;

public class game extends JApplet{

	JPanel content;
	private MainPanel mainWin;
	private InstrPanel instrWin;
	private GamePanel gameWin;

	static final int MAIN_WIDTH = 750;
	static final int MAIN_HEIGHT = 450;
	static final int SPEED = 100;
	
	Button instructions;
	Button newGame;

	//initial screen
	public void init()
	{
		//create main menu window
		JFrame mainMenu = new JFrame("FANORONA");
		mainMenu.setSize(MAIN_WIDTH, MAIN_HEIGHT);
		mainMenu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel content = new JPanel();
		content.setLayout(new CardLayout());
		
		mainWin = new MainPanel(content);
		instrWin = new InstrPanel(content);
		gameWin = new GamePanel(content);
		
		content.add(mainWin, "Main Menu");
		content.add(instrWin, "Instructions");
		content.add(gameWin, "Fanorona");
		
		mainMenu.setContentPane(content);
        mainMenu.pack();   
        mainMenu.setLocationByPlatform(true);
        mainMenu.setVisible(true);
		
	}
}

//Panel Classes
class MainPanel extends JPanel
{
	private JPanel content;
	private JLabel text;
	private JButton instructions;
	private JButton newGame;
	
	private BufferedImage mainImage;
	
	public MainPanel(JPanel mainWin){
		content = mainWin;
		
		//set welcome text
		text = new JLabel("<html><br /><br />Welcome to Fanorona<html>" + " ", SwingConstants.CENTER);
		
		//set size of main window
		setPreferredSize(new Dimension(750, 450));
		setLayout(new FlowLayout());
		
		//create and add buttons
		instructions = new JButton("Instructions");
		newGame = new JButton("New Game");
		
		//import game board image
		try {
			mainImage = ImageIO.read(new File("img/main.jpg"));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
				
		instructions.addActionListener( new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                CardLayout layout = (CardLayout) content.getLayout();
                layout.next(content);
            }
        });
		
		newGame.addActionListener( new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                CardLayout layout = (CardLayout) content.getLayout();
                layout.last(content);
            }
        });
		
		add(text);
		add(instructions);
		add(newGame);
		
		
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
	    g.drawImage(mainImage, 0, 0, null);
	  }
}

class InstrPanel extends JPanel {
	private JPanel content;
	private JLabel instr;
	private JButton back;
	
	public InstrPanel(JPanel mainWin) {
		content = mainWin;
		
		//set size of window
		setLayout(new FlowLayout());
		setPreferredSize(new Dimension(750,450));
		
		//create instructions text
		instr = new JLabel("<html>INSTRUCTIONS: <br><html>" 
			   + "<html>Step 1: click the piece you want to move<br><html>"
			   + "<html>Step 2: click the place on the board you want to move to<br><html>"
			   + "<html>Step 3: blah blah... to be continued<br><html>");
		
		//create back button
		back = new JButton("Back");
		
		//set button action to go back to main menu
		back.addActionListener( new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                CardLayout layout = (CardLayout) content.getLayout();
                layout.first(content);
            }
        });
		
		//add button and text to window
		add(instr);
		add(back);
	}
}

class GamePanel extends JPanel {
	private JPanel content;
	private JButton back;
	
	Graphics g;
	private BufferedImage gameBoard;
	
	public GamePanel(JPanel mainWin) {
		content = mainWin;
		
		setPreferredSize(new Dimension(750,450));
		back = new JButton("Back");
		
		//import game board image
		try {
			gameBoard = ImageIO.read(new File("img/gameboard.png"));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		//set button action to go back to main menu
		back.addActionListener( new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                CardLayout layout = (CardLayout) content.getLayout();
                layout.first(content);
            }
        });
		
		//add button
		add(back);
		repaint();
		
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
	    g.drawImage(gameBoard, 0, 0, null);
	  }
}

