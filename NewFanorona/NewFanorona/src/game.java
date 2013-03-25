import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

//import org.javatuples.Pair;
import javax.imageio.ImageIO;
import javax.swing.*;

public class game extends JApplet implements MouseListener{

	JPanel content;
	private MainPanel mainWin;
	private InstrPanel instrWin;
	private GamePanel gameWin;

	static final int MAIN_WIDTH = 750;
	static final int MAIN_HEIGHT = 450;
	static final int SPEED = 100;

	static final int LENGTH = 9;
	static final int HEIGHT = 5;
	public int[][] pieceMap = new int[HEIGHT][LENGTH];

	static final int RADIUS = 15;
	static final int SPACE_BTW_X = 81;
	static final int SPACE_BTW_Y = 87;
	static final int MARGIN = 50;
	int lastValid = 0;
	int lastRow = 0;
	int lastColumn = 0;

	Graphics g;

	Button instructions;
	Button newGame;

	//initialize all Panels
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

		gameWin.addMouseListener(this);

		content.add(mainWin, "Main Menu");
		content.add(instrWin, "Instructions");
		content.add(gameWin, "Fanorona");

		mainMenu.setContentPane(content);
        mainMenu.pack();   
        mainMenu.setLocationByPlatform(true);
        mainMenu.setVisible(true);
	}

	//Panel Classes
	class MainPanel extends JPanel
	{
		private JPanel content;
		private JLabel text;
		private JButton instructions;
		private JButton newGame;

		private BufferedImage mainImage;

		//create the main Panel
		public MainPanel(JPanel mainWin){
			content = mainWin;

			//set welcome text
			text = new JLabel("Welcome to Fanorona");

			//set size of main window
			setPreferredSize(new Dimension(750, 450));
			this.setLayout(new FlowLayout());

			//create buttons
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

			repaint();
			mapInitPieces();
			add(instructions);
			add(newGame);
			add(text);
		}

		//draw the main menu's background
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
		    g.drawImage(mainImage, 0, 0, null);
		  }
	}

	class InstrPanel extends JPanel {
		private JPanel content;
		private JLabel instr;
		private JButton back;

		//create the Instructions Panel
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
		private JButton reset;
		private JButton hint;

		private BufferedImage gameBoard;

		//create the GamePanel
		public GamePanel(JPanel mainWin) {
			content = mainWin;

			this.setLayout(new FlowLayout());
			setPreferredSize(new Dimension(750,450));
			back = new JButton("Back");
			reset = new JButton("Restart");
			hint = new JButton("Hint");

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

			//repaint the gameboard to beginning state
			reset.addActionListener( new ActionListener()
	        {
	            public void actionPerformed(ActionEvent e)
	            {
	               repaint();
	            }
	        });

			//show user a place to move
			hint.addActionListener( new ActionListener()
	        {
				public void actionPerformed(ActionEvent e)
	            {
	              //
	            }
	        });


			repaint();
			add(back);
			add(hint);
			add(reset);	

		}

		//method used with repaint() in GamePanel to draw compenents on the screen
		public void paintComponent(Graphics g)
		{
			super.paintComponent(g);
			g.drawImage(gameBoard, 0, 0, null);
			drawPieces(pieceMap, g);
		}

		//method used to draw a circle
		public void drawCircle(Color c, int r, int x, int y, Graphics g){
			g.setColor(c);
			g.fillOval(x - r, y - r, r * 2, r * 2);
		}

		//method used to draw pieces (calls drawCricle twice, one make a border, once to fill B/W)
		public void drawPiece(Color c, int x, int y, Graphics g){
			drawCircle(Color.BLACK, RADIUS, x, y, g);
			repaint();
			drawCircle(c, RADIUS - 2, x, y, g);
			repaint();
		}
		
		//draw all the pieces on the board
		public void drawPieces(int[][] pieceMap, Graphics g){
			// 0 indicates an empty place
			// 1 indicates a black piece
			// 2 indicates a white piece	
			for (int i = 0; i < 9; i ++) {
				for (int j = 0; j < 5; j ++) {

					// calculate the positions on the gameboard
					int this_x = MARGIN + (i * SPACE_BTW_X);
					int this_y = MARGIN + (j * SPACE_BTW_Y);

					switch(pieceMap[j][i]) {			
						case 1: {
							drawPiece(Color.BLACK, this_x, this_y, g);
						}
						break;
						case 2: {
							drawPiece(Color.WHITE, this_x, this_y, g);
						}
						break;
						case 3: {
							drawPiece(Color.WHITE, this_x, this_y, g);
						}
						break;
					}
				}
			}
		}
	}

	//maps where each game piece will go initially
	public void mapInitPieces(){

		// map top 2 rows as 1
		for (int i = 0; i < 2; ++i){
			for (int j = 0; j < LENGTH; ++j){
				pieceMap[i][j] = 1;
			}
		}

		// map bottom 2 rows as 2		
		for (int i = 4; i > 2; --i){
			for (int j = 0; j < LENGTH; ++j){
				pieceMap[i][j] = 2;
			}
		}	

		// manually set middle row
		pieceMap[2][0] = 1; pieceMap[2][2] = 1; pieceMap[2][5] = 1; pieceMap[2][7] = 1;
		pieceMap[2][1] = 2; pieceMap[2][3] = 2; pieceMap[2][6] = 2; pieceMap[2][8] = 2;
		pieceMap[2][4] = 0;

	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {

		Point currentPosition = MouseInfo.getPointerInfo().getLocation();
		SwingUtilities.convertPointFromScreen(currentPosition, e.getComponent());

		double xMin = currentPosition.getX() - 30;
		double xMax = currentPosition.getX() + 30;
		double yMin = currentPosition.getY() - 30;
		double yMax = currentPosition.getY() + 30;

		int rowIndex = 0;
		int columnIndex = 0;

		for (int i = 0; i < 9; ++i) {
			for (int j = 0; j < 5; ++j) {

				int this_x = MARGIN + (i * SPACE_BTW_X);
				int this_y = MARGIN + (j * SPACE_BTW_Y);

				if((this_x < xMax) && (this_x > xMin)){
					columnIndex = i;			
				}
				if((this_y < yMax) && (this_y > yMin)){			
					rowIndex = j;
				}	
			}
		}
		
		int currentValid = pieceMap[rowIndex][columnIndex];
		System.out.println("Last valid: " + lastValid + " (" + lastRow + "," + lastColumn + ")");
		System.out.println("Current valid: " + currentValid + " (" + rowIndex + "," + columnIndex + ")");
		
		if((lastValid == 1 || lastValid == 2) && (currentValid == 0))
		{
			//evaluator!
			eval(rowIndex, columnIndex, lastValid, lastRow, lastColumn);
			movePiece(rowIndex, columnIndex, lastRow, lastColumn);
			
			System.out.println(rowIndex + " " + columnIndex + " " + lastRow +" "+ lastColumn);
		}
		lastValid = pieceMap[rowIndex][columnIndex];
		lastRow = rowIndex;
		lastColumn = columnIndex;
		
	}
	
	void movePiece(int rowIndex, int columnIndex, int lastRow, int lastColumn){
		pieceMap[rowIndex][columnIndex] = pieceMap[lastRow][lastColumn];
		pieceMap[lastRow][lastColumn] = 0;
	}
	
	void eval(int rowIndex, int columnIndex, int lastValid, int lastRow, int lastColumn){
		//check which players turn it is (black or white)
		
		//check valid moves
		
		//Remove opponent pieces after move 
		if(lastValid == 1){
			//moved up
			if(pieceMap[rowIndex][columnIndex] == pieceMap[lastRow-1][lastColumn]){
				for(int i = rowIndex-1; i >= 0; --i){
					if(pieceMap[i][columnIndex] == 2){
						pieceMap[i][columnIndex] = 0;
					}
				}
			}
			//moved down
			if(pieceMap[rowIndex][columnIndex] == pieceMap[lastRow+1][lastColumn] ){
				for(int i = rowIndex+1; i < 5; ++i){
					if(pieceMap[i][columnIndex] == 2){
						pieceMap[i][columnIndex] = 0;
					}
				}
			}	
			//moved right
			if(pieceMap[rowIndex][columnIndex] == pieceMap[lastRow][lastColumn+1] ){
				for(int i = columnIndex+1; i < 9; ++i){
					if(pieceMap[rowIndex][i] == 2){
						pieceMap[i][columnIndex] = 0;
					}
				}
			}
			//moved left 
			if(pieceMap[rowIndex][columnIndex] == pieceMap[lastRow][lastColumn-1]){
				for(int i = columnIndex-1; i >= 0; --i){
					if(pieceMap[rowIndex][i] == 2){
						pieceMap[i][columnIndex] = 0;
					}
				}
			}
		}
		if(lastValid == 2){
			//moved up
			if(pieceMap[rowIndex][columnIndex] == pieceMap[lastRow-1][lastColumn]){
				for(int i = rowIndex-1; i >= 0; --i){
					if(pieceMap[i][columnIndex] == 1){
						pieceMap[i][columnIndex] = 0;
					}
				}
			}
			//moved down
			if(pieceMap[rowIndex][columnIndex] == pieceMap[lastRow+1][lastColumn] ){
				for(int i = rowIndex+1; i < 5; ++i){
					if(pieceMap[i][columnIndex] == 1){
						pieceMap[i][columnIndex] = 0;
					}
				}
			}	
			//moved right
			if(pieceMap[rowIndex][columnIndex] == pieceMap[lastRow][lastColumn+1] ){
				for(int i = columnIndex+1; i < 9; ++i){
					if(pieceMap[rowIndex][i] == 1){
						pieceMap[i][columnIndex] = 0;
					}
				}
			}
			//moved left 
			if(pieceMap[rowIndex][columnIndex] == pieceMap[lastRow][lastColumn-1]){
				for(int i = columnIndex-1; i >= 0; --i){
					if(pieceMap[rowIndex][i] == 1){
						pieceMap[i][columnIndex] = 0;
					}
				}
			}
		}
	}
}





