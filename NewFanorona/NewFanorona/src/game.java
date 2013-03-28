import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Random;
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

	static final int SPACE_BTWN = 80;
	static final int MARGIN = 80;
	static int BOARD_LENGTH = 9;
	static int BOARD_HEIGHT = 5;
	static int WIN_LENGTH = (BOARD_LENGTH*SPACE_BTWN) + MARGIN;
	static int WIN_HEIGHT = (BOARD_HEIGHT*SPACE_BTWN) + MARGIN;
	private int MAX_MOVES = 10 * BOARD_HEIGHT;

	public int[][] pieceMap = new int[BOARD_HEIGHT][BOARD_LENGTH];

	static final int RADIUS = 15;

	
	int firstValid = 0;
	int firstRow = 0;
	int firstColumn = 0;
	
	public pieceMove[] gameMoves = new pieceMove[MAX_MOVES];
	private LinkedList<pieceMove> validMoves = new LinkedList<pieceMove>();
	
	public pieceMove newMove = new pieceMove(0, 0, "NONE", 0);
	int movesMade = 0;
	
	public Boolean advance = true, retreat = false, onePlayer;
	
	
	
	Graphics g;

	//initialize all Panels
	public void init()
	{
		//create main menu window
		JFrame mainMenu = new JFrame("FANORONA");
		mainMenu.setSize(WIN_LENGTH, WIN_HEIGHT);
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
		private JButton onePlayerGame;
		private JButton twoPlayerGame;

		private BufferedImage mainImage;

		//create the main Panel
		public MainPanel(JPanel mainWin){
			content = mainWin;

			//set welcome text
			text = new JLabel("Welcome to Fanorona");

			//set size of main window
			setPreferredSize(new Dimension(WIN_LENGTH, WIN_HEIGHT));
			this.setLayout(new FlowLayout());

			//create buttons
			instructions = new JButton("Instructions");
			onePlayerGame = new JButton("1 Player");
			twoPlayerGame = new JButton("2 Player");

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

			onePlayerGame.addActionListener( new ActionListener()
	        {
	            public void actionPerformed(ActionEvent e)
	            {
	            	onePlayer = true;
	                CardLayout layout = (CardLayout) content.getLayout();
	                layout.last(content);
	            }
	        });
			
			twoPlayerGame.addActionListener( new ActionListener()
	        {
	            public void actionPerformed(ActionEvent e)
	            {
	            	onePlayer = false;
	                CardLayout layout = (CardLayout) content.getLayout();
	                layout.last(content);
	            }
	        });

			repaint();
			mapInitPieces();
			add(instructions);
			add(onePlayerGame);
			add(twoPlayerGame);
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
		private JButton advanceButton;
		private JButton retreatButton;
		private Color buttonColor;

		//create the GamePanel
		public GamePanel(JPanel mainWin) {
			content = mainWin;

			this.setLayout(new FlowLayout());
			
			back = new JButton("Back");
			reset = new JButton("Restart");
			hint = new JButton("Hint");
			
			advanceButton = new JButton("Advance");
			retreatButton = new JButton("Retreat");
			buttonColor = advanceButton.getBackground();
			advanceButton.setBackground(Color.GREEN);
			
			
			//set button action to go back to main menu
			back.addActionListener( new ActionListener()
	        {
	            public void actionPerformed(ActionEvent e)
	            {
	            	movesMade = 0;
	            	gameMoves[movesMade] = newMove;
	            	gameMoves[movesMade].color = 1;
	                mapInitPieces();
	                CardLayout layout = (CardLayout) content.getLayout();
	                layout.first(content);
	            }
	        });

			//repaint the gameboard to beginning state
			reset.addActionListener( new ActionListener()
	        {
	            public void actionPerformed(ActionEvent e)
	            {
	            	movesMade = 0;
	            	gameMoves[movesMade].color = 1;
	                mapInitPieces();
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
			
			//enable capturing pieces only when moved toward
			advanceButton.addActionListener( new ActionListener()
	        {
				public void actionPerformed(ActionEvent e)
	            {
					if(advance == false) {
						advance = true;
						retreat = false;
						retreatButton.setBackground(buttonColor);
						advanceButton.setBackground(Color.GREEN);
					}
	            }
	        });
			
			//enable capturing pieces only when moved away from
			retreatButton.addActionListener( new ActionListener()
	        {
				public void actionPerformed(ActionEvent e)
	            {
					if(retreat == false) {
						retreat = true;
						advance = false;
						advanceButton.setBackground(buttonColor);
						retreatButton.setBackground(Color.GREEN);
					}
	            }
	        });
			
			repaint();
			add(back);
			add(hint);
			add(reset);	
			add(advanceButton);
			add(retreatButton);
		}

		//method used with repaint() in GamePanel to draw compenents on the screen
		public void paintComponent(Graphics g)
		{
			super.paintComponent(g);

			//draw horizontal lines
			for(int i = 0; i < BOARD_HEIGHT; i++){
				g.drawLine(MARGIN, MARGIN+(i*SPACE_BTWN), WIN_LENGTH-MARGIN, MARGIN+(i*SPACE_BTWN));
			}

			//draw vertical lines
			for(int i = 0; i < BOARD_LENGTH; i++){
				g.drawLine(MARGIN+(i*SPACE_BTWN), MARGIN, MARGIN+(i*SPACE_BTWN),BOARD_HEIGHT*SPACE_BTWN);
			}

			//draw diagonal lines from top (negative sloping ones)
			for(int i = 0; i < BOARD_LENGTH; i++){
				int from_x = MARGIN + (i*SPACE_BTWN);
				int from_y = MARGIN;
				int to_x = from_x;
				int to_y = from_y;

				while(to_x < (BOARD_LENGTH*SPACE_BTWN) && to_y < (BOARD_HEIGHT*SPACE_BTWN)){
					to_x += SPACE_BTWN;
					to_y += SPACE_BTWN;
					if(i%2 == 0)
						g.drawLine(from_x, from_y, to_x, to_y);
				}
			}

			//draw diagonal lines from top (positive sloping ones)
			for(int i = 0; i < BOARD_LENGTH; i++){
				int from_x = MARGIN + (i*SPACE_BTWN);
				int from_y = ((BOARD_HEIGHT-1)*SPACE_BTWN)+MARGIN;

				int to_x = from_x;
				int to_y = from_y;

				while(to_x < (BOARD_LENGTH*SPACE_BTWN) && to_y-SPACE_BTWN> 0){
					to_x += SPACE_BTWN;
					to_y -= SPACE_BTWN;
					if(i%2 == 0)
						g.drawLine(from_x, from_y, to_x, to_y);
				}
			}

			//draw pieces
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
			for (int i = 0; i < BOARD_LENGTH; i ++) {
				for (int j = 0; j < BOARD_HEIGHT; j ++) {

					// calculate the positions on the gameboard
					int this_x = MARGIN + (i * SPACE_BTWN);
					int this_y = MARGIN + (j * SPACE_BTWN);

					switch(pieceMap[j][i]) {			
						case 1: {
							drawPiece(Color.WHITE, this_x, this_y, g);
						}
						break;
						case 2: {
							drawPiece(Color.BLACK, this_x, this_y, g);
						}
						break;
						case 3: {
							drawPiece(Color.BLACK, this_x, this_y, g);
						}
						break;
					}
				}
			}
		}
	}

	//maps where each game piece will go initially
	public void mapInitPieces(){

		int middleRow = BOARD_HEIGHT/2;
		int middleColumn = BOARD_LENGTH/2;
		
		for (int i = 0; i < middleRow; ++i){
			for (int j = 0; j < BOARD_LENGTH; ++j){
				pieceMap[i][j] = 1;
			}
		}
	
		for (int i = middleRow + 1; i < BOARD_HEIGHT; ++i){
			for (int j = 0; j < BOARD_LENGTH; ++j){
				pieceMap[i][j] = 2;
			}
		}	

		// manually set middle row
		int alternator = 0;
		
		for(int i = 0; i < BOARD_LENGTH; ++i) {
			
			if(i == middleColumn) {
				pieceMap[middleRow][i] = 0;
				
				if((middleColumn % 2) == 0) {
					alternator--;
				}
			} else if((alternator % 2) == 0) {
				pieceMap[middleRow][i] = 1;
			} else {
				pieceMap[middleRow][i] = 2;
			}
			
			alternator++;
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {}
	@Override
	public void mouseEntered(MouseEvent e) {}
	@Override
	public void mouseExited(MouseEvent e) {}
	@Override
	public void mousePressed(MouseEvent e) {}

	@Override
	public void mouseReleased(MouseEvent e) {

		Point currentPosition = MouseInfo.getPointerInfo().getLocation();
		SwingUtilities.convertPointFromScreen(currentPosition, e.getComponent());

		double xMin = currentPosition.getX() - 30;
		double xMax = currentPosition.getX() + 30;
		double yMin = currentPosition.getY() - 30;
		double yMax = currentPosition.getY() + 30;

		int secondRow = 0;
		int secondColumn = 0;

		//obtain array row and column values for click position
		for (int i = 0; i < BOARD_LENGTH; ++i) {
			for (int j = 0; j < BOARD_HEIGHT; ++j) {

				int this_x = MARGIN + (i * SPACE_BTWN);
				int this_y = MARGIN + (j * SPACE_BTWN);

				if((this_x < xMax) && (this_x > xMin)){
					secondColumn = i;			
				}
				if((this_y < yMax) && (this_y > yMin)){			
					secondRow = j;
				}	
			}
		}
		
		int currentValid = pieceMap[secondRow][secondColumn];	
		
		//only move if second space clicked on was empty
		if((firstValid == 1 || firstValid == 2) && (currentValid == 0))
		{
			gameMoves[movesMade] = newMove;			
			if(validMove(secondRow, secondColumn)) {
				capturePieces(gameMoves[movesMade]);
				movePiece(secondRow, secondColumn);
			}
			evalBoard();
		}
		firstValid = pieceMap[secondRow][secondColumn];
		firstRow = secondRow;
		firstColumn = secondColumn;
		
	}
	
	Boolean validMove(int secondRow, int secondColumn){
		
		//gameMoves[movesMade] = newMove;	
		
		//check that the correct color is trying to move
		int currentColor = pieceMap[firstRow][firstColumn];	

		if(movesMade > 0) {
			if(currentColor == gameMoves[movesMade-1].color) {
				System.out.println("NOT YOUR TURN!");
				return false;
			}
		} else {
			if(currentColor == 1) {
				System.out.println("NOT YOUR TURN!");
				return false;
			}
		}
		System.out.println("reaches validmove()");
		
		//SWEST
		if((secondRow == (firstRow + 1)) && (secondColumn == (firstColumn - 1))) {
			
			//check that line is available to move along
			if(!linePresent(firstRow, firstColumn)) {
				System.out.println("INVALID MOVE!");
				return false;
			}
			
			//check that last move wasn't in same direction
			if(movesMade >0) {
				if(gameMoves[movesMade-1].direction == "SWEST" && (gameMoves[movesMade-1].destRow == (firstRow + 1) && (gameMoves[movesMade-1].destColumn == (firstColumn - 1)))) {
					System.out.println("CANT MOVE SAME DIRECTION!");
					return false;
				}
			}
			gameMoves[movesMade].setDirection("SWEST");

			//determine whether requested move is valid/invalid
			//act differently based on whether or not piece is moved to outer edge of array (due to out of bounds errors)
			if((firstRow == 0) || (firstColumn == (BOARD_LENGTH-1))) {
				if((pieceMap[secondRow + 1][secondColumn - 1] == 0) || (pieceMap[secondRow + 1][secondColumn - 1] == currentColor)) {
					System.out.println("INVALID MOVE!");
					return false;
				}
			} else if((secondRow == (BOARD_LENGTH - 1)) || (secondColumn == 0)) {
				if((pieceMap[firstRow - 1][firstColumn + 1] == 0) || (pieceMap[firstRow - 1][firstColumn + 1] == currentColor)) {
					System.out.println("INVALID MOVE!");
					return false;					
				}
			} else if(((pieceMap[secondRow + 1][secondColumn - 1] == 0) || (pieceMap[secondRow + 1][secondColumn - 1] == currentColor))			
			         && ((pieceMap[firstRow - 1][firstColumn + 1] == 0) || (pieceMap[firstRow - 1][firstColumn + 1] == currentColor))) {
				System.out.println("INVALID MOVE!");
				return false;
			}
			
			if(pieceMap[secondRow][secondColumn] != 0) {
				System.out.println("INVALID MOVE: nonempty space");
				return false;					
			}
		}
			
		//SEAST
		if((secondRow == (firstRow + 1)) && (secondColumn == (firstColumn + 1))) {
			
			//check that line is available to move along
			if(!linePresent(firstRow, firstColumn)) {
				System.out.println("INVALID MOVE!");
				return false;
			}
			
			if(movesMade >0) {
				if(gameMoves[movesMade-1].direction == "SEAST" && (gameMoves[movesMade-1].destRow == (firstRow + 1) && (gameMoves[movesMade-1].destColumn == (firstColumn - 1)))) {
					System.out.println("CANT MOVE SAME DIRECTION!");
					return false;				
				}
			}
			gameMoves[movesMade].setDirection("SEAST");

			//determine whether requested move is valid/invalid
			//act differently based on whether or not piece is moved to outer edge of array (due to out of bounds errors)
			if((firstRow == 0) || (firstColumn == 0)) {
				if((pieceMap[secondRow + 1][secondColumn + 1] == 0) || (pieceMap[secondRow + 1][secondColumn + 1] == currentColor)) {
					System.out.println("INVALID MOVE!");
					return false;						
				}
			} else if((secondRow == (BOARD_LENGTH - 1)) || (secondColumn == (BOARD_LENGTH - 1))) {
				if((pieceMap[firstRow - 1][firstColumn - 1] == 0) || (pieceMap[firstRow - 1][firstColumn - 1] == currentColor)) {
					System.out.println("INVALID MOVE!");
					return false;						
				}
			} else if(((pieceMap[secondRow + 1][secondColumn + 1] == 0) || (pieceMap[secondRow + 1][secondColumn + 1] == currentColor)) 
			         && ((pieceMap[firstRow - 1][firstColumn - 1] == 0) || (pieceMap[firstRow - 1][firstColumn - 1] == currentColor))) {		
				System.out.println("INVALID MOVE!");
				return false;		
			} 
			
			if(pieceMap[secondRow][secondColumn] != 0) {
				System.out.println("INVALID MOVE!");
				return false;					
			}
		}
			
		//NWEST
		if((secondRow == (firstRow - 1)) && (secondColumn == (firstColumn - 1))) {
			
			//check that line is available to move along
			if(!linePresent(firstRow, firstColumn)) {
				System.out.println("INVALID MOVE!");
				return false;
			}
			
			if(movesMade >0) {
				if(gameMoves[movesMade-1].direction == "NWEST" && (gameMoves[movesMade-1].destRow == (firstRow + 1) && (gameMoves[movesMade-1].destColumn == (firstColumn - 1)))) {
					System.out.println("CANT MOVE SAME DIRECTION!");
					return false;				
				}
			}
			gameMoves[movesMade].setDirection("NWEST");

			//determine whether requested move is valid/invalid
			//act differently based on whether or not piece is moved to outer edge of array (due to out of bounds errors)
			if((firstRow == (BOARD_LENGTH - 1)) || (firstColumn == (BOARD_LENGTH - 1))) {
				if((pieceMap[secondRow - 1][secondColumn - 1] == 0) || (pieceMap[secondRow - 1][secondColumn - 1] == currentColor)) {
					System.out.println("INVALID MOVE!");
					return false;						
				}
			} else if((secondRow <= 0) || (secondColumn <= 0)) {
				if((pieceMap[firstRow + 1][firstColumn + 1] == 0) || (pieceMap[firstRow + 1][firstColumn + 1] == currentColor)) {
					System.out.println("INVALID MOVE!");
					return false;						
				}
			} else if(((pieceMap[secondRow - 1][secondColumn - 1] == 0) || (pieceMap[secondRow - 1][secondColumn - 1] == currentColor)) 
			         && ((pieceMap[firstRow + 1][firstColumn + 1] == 0) || (pieceMap[firstRow + 1][firstColumn + 1] == currentColor))) {		
				System.out.println("INVALID MOVE!");
				return false;		
			}
				
			if(pieceMap[secondRow][secondColumn] != 0) {
				System.out.println("INVALID MOVE!");
				return false;					
			}
		}
			
		//NEAST
		if((secondRow == (firstRow - 1)) && (secondColumn == (firstColumn + 1))) {

			//check that line is available to move along
			if(!linePresent(firstRow, firstColumn)) {
				System.out.println("INVALID MOVE!");
				return false;
			}
			
			if(movesMade >0) {
				if(gameMoves[movesMade-1].direction == "NEAST" && (gameMoves[movesMade-1].destRow == (firstRow + 1) && (gameMoves[movesMade-1].destColumn == (firstColumn - 1)))) {
					System.out.println("CANT MOVE SAME DIRECTION!");
					return false;				
				}
			}
			gameMoves[movesMade].setDirection("NEAST");
			
			//determine whether requested move is valid/invalid
			//act differently based on whether or not piece is moved to outer edge of array (due to out of bounds errors)
			if((firstRow == (BOARD_LENGTH - 1)) || (firstColumn == 0)) {
				if((pieceMap[secondRow - 1][secondColumn + 1] == 0) || (pieceMap[secondRow - 1][secondColumn + 1] == currentColor)) {
					System.out.println("INVALID MOVE!");
					return false;						
				}	
			} else if ((secondRow == 0) || (secondColumn == (BOARD_LENGTH - 1))) {
				if((pieceMap[firstRow + 1][firstColumn - 1] == 0) || (pieceMap[firstRow + 1][firstColumn - 1] == currentColor)) {
					System.out.println("INVALID MOVE!");
					return false;						
				}
			} else if(((pieceMap[secondRow - 1][secondColumn + 1] == 0) || (pieceMap[secondRow - 1][secondColumn + 1] == currentColor)) 
			         && ((pieceMap[firstRow + 1][firstColumn - 1] == 0) || (pieceMap[firstRow + 1][firstColumn - 1] == currentColor))) {		
				System.out.println("INVALID MOVE!");
				return false;		
			}

			if(pieceMap[secondRow][secondColumn] != 0) {
				System.out.println("INVALID MOVE!");
				return false;					
			}
		}
			
		//SOUTH
		if((secondRow == (firstRow + 1)) && (secondColumn == firstColumn)) {
			
			if(movesMade >0) {
				if(gameMoves[movesMade-1].direction == "SOUTH" && (gameMoves[movesMade-1].destRow == (firstRow + 1) && (gameMoves[movesMade-1].destColumn == (firstColumn - 1)))) {
					System.out.println("CANT MOVE SAME DIRECTION!");
					return false;				
				}
			}
			gameMoves[movesMade].setDirection("SOUTH");
				
			//determine whether requested move is valid/invalid
			//act differently based on whether or not piece is moved to outer edge of array (due to out of bounds errors)
			if(secondRow == (BOARD_LENGTH - 1)) {
				if((pieceMap[firstRow - 1][firstColumn] == 0) || (pieceMap[firstRow - 1][firstColumn] == currentColor)) {
					System.out.println("INVALID MOVE!");
					return false;					
				}		
			} else if(firstRow == 0) {
				if((pieceMap[secondRow + 1][secondColumn] == 0) || (pieceMap[secondRow + 1][secondColumn] == currentColor)) {
					System.out.println("INVALID MOVE!");
					return false;					
				}			
			} else if(((pieceMap[secondRow + 1][secondColumn] == 0) || (pieceMap[secondRow + 1][secondColumn] == currentColor)) 
			         && ((pieceMap[firstRow - 1][firstColumn] == 0) || (pieceMap[firstRow - 1][firstColumn] == currentColor))) {		
				System.out.println("INVALID MOVE!");
				return false;		
			}
			 
			if(pieceMap[secondRow][secondColumn] != 0) {
				System.out.println("INVALID MOVE!");
				return false;					
			}
		}
		
		//NORTH
		if((secondRow == (firstRow - 1)) && (secondColumn == firstColumn)) {
			
			if(movesMade >0) {
				if((gameMoves[movesMade-1].direction == "NORTH") && (gameMoves[movesMade-1].destRow == firstRow) && (gameMoves[movesMade-1].destColumn == firstColumn)) {
					System.out.println("CANT MOVE SAME DIRECTION!");
					return false;				
				}
			}
			System.out.println("Moves: " + movesMade);
			gameMoves[movesMade].setDirection("NORTH");
			
			if(secondRow == 0) {
				if((pieceMap[firstRow + 1][firstColumn] == 0) || (pieceMap[firstRow + 1][firstColumn] == currentColor)) {
					System.out.println("INVALID MOVE!");
					return false;					
				}
				
			} else if(firstRow == (BOARD_LENGTH - 1)) {
				if((pieceMap[secondRow - 1][secondColumn] == 0) || (pieceMap[secondRow - 1][secondColumn] == currentColor)) {		
					System.out.println("INVALID MOVE!");
					return false;
				}
			} else if(((pieceMap[secondRow - 1][secondColumn] == 0) || (pieceMap[secondRow - 1][secondColumn] == currentColor)) 
		             && ((pieceMap[firstRow + 1][firstColumn] == 0) || (pieceMap[firstRow + 1][firstColumn] == currentColor))) {		
				System.out.println("INVALID MOVE!");
				return false;	
				
			}
			
			if(pieceMap[secondRow][secondColumn] != 0) {
				System.out.println("INVALID MOVE!");
				return false;					
			}
		}
			
		//EAST
		if((secondRow == firstRow) && (secondColumn == (firstColumn + 1))) {
			
			if(movesMade >0) {
				if(gameMoves[movesMade-1].direction == "EAST" && (gameMoves[movesMade-1].destRow == (firstRow + 1) && (gameMoves[movesMade-1].destColumn == (firstColumn - 1)))) {
					System.out.println("CANT MOVE SAME DIRECTION!");
					return false;				
				}
			}
			gameMoves[movesMade].setDirection("EAST");
			
			if(firstColumn == 0) {
				if((pieceMap[secondRow][secondColumn + 1] == 0) || (pieceMap[secondRow][secondColumn + 1] == currentColor)) {
					System.out.println("INVALID MOVE!");
					return false;							
				}
			} else if(secondColumn == (BOARD_LENGTH - 1)) {
				if((pieceMap[firstRow][firstColumn - 1] == 0) || (pieceMap[firstRow][firstColumn - 1] == currentColor)) {
					System.out.println("INVALID MOVE!");
					return false;							
				}
			} else if(((pieceMap[secondRow][secondColumn + 1] == 0) || (pieceMap[secondRow][secondColumn + 1] == currentColor)) 
			         && ((pieceMap[firstRow][firstColumn - 1] == 0) || (pieceMap[firstRow][firstColumn - 1] == currentColor))) {		
				System.out.println("INVALID MOVE!");
				return false;		
			}
			
			if(pieceMap[secondRow][secondColumn] != 0) {
				System.out.println("INVALID MOVE!");
				return false;					
			}
		}
			
		//WEST
		if((secondRow == firstRow) && (secondColumn == (firstColumn - 1))) {
			
			if(movesMade >0) {
				if(gameMoves[movesMade-1].direction == "WEST" && (gameMoves[movesMade-1].destRow == (firstRow + 1) && (gameMoves[movesMade-1].destColumn == (firstColumn - 1)))) {
					System.out.println("CANT MOVE SAME DIRECTION!");
					return false;				
				}
			}
			gameMoves[movesMade].setDirection("WEST");
			
			if(firstColumn == (BOARD_LENGTH - 1)) {
				if((pieceMap[secondRow][secondColumn - 1] == 0) || (pieceMap[secondRow][secondColumn - 1] == currentColor)) {
					System.out.println("INVALID MOVE!");
					return false;						
				}
			} else if(secondColumn == 0) {
				if((pieceMap[firstRow][firstColumn + 1] == 0) || (pieceMap[firstRow][firstColumn + 1] == currentColor)) {
					System.out.println("INVALID MOVE!");
					return false;						
				}
			} else if(((pieceMap[secondRow][secondColumn - 1] == 0) || (pieceMap[secondRow][secondColumn - 1] == currentColor)) 
			   && ((pieceMap[firstRow][firstColumn + 1] == 0) || (pieceMap[firstRow][firstColumn + 1] == currentColor))) {		
				System.out.println("INVALID MOVE!");
				return false;		
			}
			
			if(pieceMap[secondRow][secondColumn] != 0) {
				System.out.println("INVALID MOVE!");
				return false;					
			}
		} else {
			//System.out.println("INVALID MOVE!");
			//return false;	
		}
		
		newMove.setSourceRow(firstRow);
		newMove.setSourceColumn(firstColumn);
		newMove.setDestRow(secondRow); 
		newMove.setDestColumn(secondColumn);
		newMove.setColor(currentColor);
		gameMoves[movesMade] = newMove;
		
		return true;
	}
	
	void capturePieces(pieceMove move) {
		
		int currentColor = pieceMap[firstRow][firstColumn];	
		
		Boolean computerEval;
		if((currentColor == 1) && onePlayer) {
			computerEval = true;
		} else {
			computerEval = false;
		}		
		
		if(move.direction == "SWEST") {
			//start removing other teams pieces
			if(advance || computerEval) {
				int i = move.destRow+1;
				int j = move.destColumn-1;
				while((i <= (BOARD_LENGTH - 1)) && (j >= 0) && (pieceMap[i][j] != currentColor) && (pieceMap[i][j] != 0)) {
					pieceMap[i][j] = 0;
					++i;--j;
				}	
			}
	
			if(retreat  || computerEval) {
				int k = firstRow-1;
				int z = firstColumn+1;
				while((k >= 0) && (z <= (BOARD_LENGTH - 1)) && (pieceMap[k][z] != currentColor) && (pieceMap[k][z] != 0)) {
					pieceMap[k][z] = 0;
					--k;++z;
				}
			}	
		} else if(move.direction == "SEAST") {
			
			if(advance || computerEval) {
				int i = move.destRow+1;
				int j = move.destColumn+1;
				while((i <= (BOARD_LENGTH - 1)) && (j <= (BOARD_LENGTH - 1)) && (pieceMap[i][j] != currentColor) && (pieceMap[i][j] != 0)) {
					pieceMap[i][j] = 0;						
					++i;++j;					
				}	
			}

			if(retreat || computerEval) {
				int k = firstRow-1;
				int z = firstColumn-1;
				while((k >= 0) && (z >= 0) && (pieceMap[k][z] != currentColor) && (pieceMap[k][z] != 0)) {
					pieceMap[k][z] = 0;								
					--k;--z;					
				}
			}			
		} else if(move.direction == "NWEST") {
			
			if(advance || computerEval) {
				int i = move.destRow-1;
				int j = move.destColumn-1;
				while((i >= 0) && (j >= 0) && (pieceMap[i][j] != currentColor) && (pieceMap[i][j] != 0)) {
					pieceMap[i][j] = 0;							
					--i;--j;					
				}	
			}
				
			if(retreat  || computerEval) {
				int k = firstRow+1;
				int z = firstColumn+1;
				while((k <= (BOARD_LENGTH - 1)) && (z <= (BOARD_LENGTH - 1)) && (pieceMap[k][z] != currentColor) && (pieceMap[k][z] != 0)) {
					pieceMap[k][z] = 0;								
					++k;++z;										
				}
			}			
		} else if(move.direction == "NEAST") {
			
			if(advance || computerEval) {
				int i = move.destRow-1;
				int j = move.destColumn+1;
				while((i >= 0) && (j <= (BOARD_LENGTH - 1)) && (pieceMap[i][j] != currentColor) && (pieceMap[i][j] != 0)) {
					pieceMap[i][j] = 0;								
					--i;++j;					
				}	
			}

			if(retreat || computerEval) {
				int k = firstRow+1;
				int z = firstColumn-1;
				while((k <= (BOARD_LENGTH - 1)) && (z >= 0) && (pieceMap[k][z] != currentColor) && (pieceMap[k][z] != 0)) {
					pieceMap[k][z] = 0;							
					++k;--z;					
				}
			}			
		} else if(move.direction == "SOUTH") {
			
			if(advance || computerEval) {
				for(int i = move.destRow+1; (i <= (BOARD_LENGTH - 1)) && (pieceMap[i][move.destColumn] != currentColor) && (pieceMap[i][move.destColumn] != 0); ++i) {
					pieceMap[i][move.destColumn] = 0;
				}
			}

			if(retreat || computerEval) {
				for(int i = firstRow - 1; (i >= 0) && (pieceMap[i][move.destColumn] != currentColor) && (pieceMap[i][move.destColumn] != 0); --i) {
					pieceMap[i][move.destColumn] = 0;
				}
			}			
		} else if(move.direction == "NORTH") {
					
			if(advance || computerEval) {
				for(int i = move.destRow-1; (i >= 0) && (pieceMap[i][move.destColumn] != currentColor) && (pieceMap[i][move.destColumn] != 0); --i) {
					pieceMap[i][move.destColumn] = 0;
				}
			}

			if(retreat || computerEval) {
				for(int i = firstRow + 1; (i <= (BOARD_LENGTH - 1)) && (pieceMap[i][move.destColumn] != currentColor) && (pieceMap[i][move.destColumn] != 0); ++i) {				
					pieceMap[i][move.destColumn] = 0;
				}
			}			
		} else if(move.direction == "EAST") {
			
			if(advance || computerEval) {
				for(int i = move.destColumn+1; (i <= (BOARD_LENGTH - 1)) && (pieceMap[move.destRow][i] != currentColor) && (pieceMap[move.destRow][i] != 0); ++i) {		
					pieceMap[move.destRow][i] = 0;
				}
			}

			if(retreat || computerEval) {
				for(int i = firstColumn - 1; (i >= 0) && (pieceMap[move.destRow][i] != currentColor) && (pieceMap[move.destRow][i] != 0); --i) {		
					pieceMap[move.destRow][i] = 0;
				}
			}			
		} else if(move.direction == "WEST") {
			
			if(advance || computerEval) {
				for(int i = move.destColumn-1; (i >= 0) && (pieceMap[move.destRow][i] != currentColor) && (pieceMap[move.destRow][i] != 0); --i) {
					pieceMap[move.destRow][i] = 0;				
				}
			}

			if(retreat || computerEval) {
				for(int i = firstColumn + 1; (i <= (BOARD_LENGTH - 1)) && (pieceMap[move.destRow][i] != currentColor) && (pieceMap[move.destRow][i] != 0); ++i) {
					pieceMap[move.destRow][i] = 0;				
				}
			}			
		}
	}
	
	void movePiece(int secondRow, int secondColumn){
		
		int movingColor = pieceMap[firstRow][firstColumn];
		pieceMap[secondRow][secondColumn] = movingColor;
		pieceMap[firstRow][firstColumn] = 0;	
		
		//**************Convert to Graphics****************
		//output information about what colored piece moved last
		if(gameMoves[movesMade].color == 2) 
			System.out.println("Last Move: WHITE");
		else if(gameMoves[movesMade].color == 1)
			System.out.println("Last Move: BLACK");	
		
		movesMade++;
		System.out.println("Moves: " + movesMade);
		if((movingColor == 2) && onePlayer) {
			computerEval();
			evalBoard();
		}
	}
	
	void evalBoard() {
		
		int whiteCount = 0;
		int blackCount = 0;
		
		for(int i = 0; i < BOARD_HEIGHT; ++i) {
			for(int j = 0; j < BOARD_LENGTH; ++j) {		
				if(pieceMap[i][j] == 1)
					blackCount++;
				else if(pieceMap[i][j] == 2)
					whiteCount++;
			}
		}
		
		//**************Convert to Graphics****************
		//output piece count info for current game
		System.out.println("WHITE Pieces Remaining: " + whiteCount);
		System.out.println("BLACK Pieces Remaining: " + blackCount);
		
		//**************Convert to Graphics****************
		//output winning messages
		if(whiteCount == 0) {
			System.out.println("BLACK WINS!!!");
		} else if(blackCount == 0) {
			System.out.println("WHITE WINS!!!");
		}
		
		//**************Convert to Graphics****************
		//output draw message
		if(movesMade == MAX_MOVES)
			System.out.println("GAME ENDS IN DRAW...");
		
	}
	
	Boolean linePresent(int row, int col) {
		
		//check to see if there is a line on board to travel along
		if(((row == 0) || (row == 2) || (row == (BOARD_LENGTH - 1))) && ((col == 1) || (col == 3) || (col == BOARD_HEIGHT) || (col == 7))) {
			return false;
		}
		
		if(((row == 1) || (row == 3)) && ((col == 0) || (col == 2) || (col == (BOARD_LENGTH - 1)) || (col == 6) || (col == (BOARD_LENGTH - 1)))) {
			return false;
		}
		return true;
	}

	void computerEval() {
		
		for(int row = 0; row < BOARD_HEIGHT; row++) {
			for(int column = 0; column < BOARD_LENGTH; column++) {	
				
				
				if(pieceMap[row][column] == 1) {
					
					firstRow = row;
					firstColumn = column;
					
					if((row-1) >= 0) {
						gameMoves[movesMade] = newMove;	
						if(validMove(row - 1, column)) {
							System.out.println(row + " " + column + " SUCCESS1!!!");
							validMoves.add(gameMoves[movesMade]);	
						}
					}
					if(((row - 1) >= 0) && ((column + 1) <= (BOARD_LENGTH - 1))) {
						gameMoves[movesMade] = newMove;	
						if(validMove(row - 1, column + 1)) {
							System.out.println(row + " " + column + " SUCCESS2!!!");
							validMoves.add(gameMoves[movesMade]);
						}
					}
					if((column + 1) <= (BOARD_LENGTH - 1)) {
						gameMoves[movesMade] = newMove;	
						if(validMove(row, column + 1)) {
							System.out.println(row + " " + column + " SUCCESS3!!!");
							validMoves.add(gameMoves[movesMade]);
						}
					}
					if(((row + 1) <= (BOARD_LENGTH - 1)) && ((column + 1) <= (BOARD_LENGTH - 1))) {
						gameMoves[movesMade] = newMove;	
						if(validMove(row + 1, column + 1)) {
							System.out.println(row + " " + column + " SUCCESS(BOARD_LENGTH - 1)!!!");
							validMoves.add(gameMoves[movesMade]);
						}
					}
					if((row + 1) <= (BOARD_LENGTH - 1)) {
						gameMoves[movesMade] = newMove;	
						if(validMove(row + 1, column)) {
							System.out.println(row + " " + column + " SUCCESSBOARD_HEIGHT!!!");
							validMoves.add(gameMoves[movesMade]);
						}
					}
					if(((row + 1) <= (BOARD_LENGTH - 1)) && ((column - 1) >= 0)) {
						gameMoves[movesMade] = newMove;	
						if(validMove(row + 1, column - 1)) {	
							System.out.println(row + " " + column + " SUCCESS6!!!");
							validMoves.add(gameMoves[movesMade]);
						}
					}		
					if((column - 1) >= 0) {
						gameMoves[movesMade] = newMove;	
						if(validMove(row, column - 1)) {	
							validMoves.add(gameMoves[movesMade]);
						}
					}
					if(((row - 1) >= 0) && ((column - 1) >= 0)) {
						gameMoves[movesMade] = newMove;	
						if(validMove(row - 1, column - 1)) {	
							validMoves.add(gameMoves[movesMade]);
							//System.out.println("Valid Start: " + validMoves.get(0).sourceRow + ',' + validMoves.get(0).sourceColumn);		
						}
					}	
				}
			}
		}
		
		for(int i = 0; i < validMoves.size(); ++i) {
			
			System.out.println("Valid Start: " + validMoves.get(i).sourceRow + ',' + validMoves.get(i).sourceColumn);
			System.out.println("Valid Direction: " + validMoves.get(i).direction);
		}
		computerMove();
	}

	void computerMove() {
		
		System.out.println("Number of Valid Moves: " + validMoves.size());
		
		Random rand = new Random();
		int  randMoveIndex = rand.nextInt(validMoves.size());		
			
		pieceMove randMove = validMoves.get(randMoveIndex);
		
		firstRow = randMove.sourceRow;
		firstColumn = randMove.sourceColumn;
		
		System.out.println("Computer Moving: " + randMove.sourceRow + randMove.sourceColumn);
		
		capturePieces(randMove);
		movePiece(randMove.destRow, randMove.destColumn);
		
		validMoves.clear();
	}
	
	class pieceMove {
		public int destRow, destColumn;
		public int sourceRow, sourceColumn;
		public String direction;
		private int color;
		
		pieceMove(int x, int y, String s, int c) {
			
			destRow = x;
			destColumn = y;
			direction = s;
			color = c;
		}
		
		public void setSourceRow(int inSourceRow) {
			sourceRow = inSourceRow;
		}
		public void setSourceColumn(int inSourceColumn) {
			sourceColumn = inSourceColumn;
		}		
		public void setDestRow(int inDestRow) {
			destRow = inDestRow;
		}
		public void setDestColumn(int inDestColumn) {
			destColumn = inDestColumn;
		}
		public void setDirection(String inDirection) {
			direction = inDirection;
		}
		public void setColor(int inColor) {
			color = inColor;
		}
		
	}
}





