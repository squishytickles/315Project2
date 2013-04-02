import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.*;
import java.util.LinkedList;
import java.util.Random;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
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
	private static int MAX_MOVES = 10 * BOARD_HEIGHT;

	public static int[][] pieceMap = new int[BOARD_HEIGHT][BOARD_LENGTH];
	public Point[] compass = new Point[8];
	public static int hintcount = 0; 

	static final int RADIUS = 15;
	
	public static int firstValid = 0;
	public static int firstRow = 0;
	public static int firstColumn = 0;
	int whiteCount = 0;
	int blackCount = 0;
	private JButton computerMove;
	
	public static pieceMove[] gameMoves = new pieceMove[MAX_MOVES];
	public static LinkedList<pieceMove> validMoves = new LinkedList<pieceMove>();
	
	//public pieceMove newMove = new pieceMove(0, 0, "NONE", 0);
	int movesMade = 0;
	public Boolean advance = true, retreat = false, onePlayer, searching = false, keepMoving = false;
	public static MinimaxTree mainTree;// = new MinimaxTree();
	
	//client/server variables
	public ServerSocket serverSocket = null;
	public Socket clientSocket = null;
	public int PORT_NUM = 4444;

	
	Graphics g;

	//initialize all Panels
	public void init()
	{		
		//server/client setup
		/*String response = " ";
		String line;	
		boolean server = false;
		boolean wait = false;
		PrintWriter out;
		BufferedReader in;

		response = JOptionPane.showInputDialog("Is this the server? (y/n)");
		if(response.compareTo("y") == 0)
			server = true;

		//server side
		if(server){
			try {
				//create client listener
				serverSocket = new ServerSocket(PORT_NUM);

				//create client socket
			    clientSocket = serverSocket.accept();

			    //establish communication with client
			   out = new PrintWriter(clientSocket.getOutputStream(), true);
			   in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			   String inputLine, outputLine;
			   ServerClientProtocol protocol = new ServerClientProtocol();
			   outputLine = protocol.processInput(null);
			   out.println(outputLine);

				while ((inputLine = in.readLine()) != null) {   
				    outputLine = protocol.processInput(inputLine);
				    out.println(outputLine);
				    if (outputLine.equals("Bye."))
				    break;
				}
			} 
			catch (IOException e) {
			    System.out.println("Accept failed on port: " + PORT_NUM);
			    System.exit(-1);
			}	
		}

		//client side
		else  
		{
			// get a host name to connect to
			response = JOptionPane.showInputDialog("Enter hostname for server: ");
			try {
				clientSocket = new Socket(response, PORT_NUM);

				//establish communication with server
				out = new PrintWriter(clientSocket.getOutputStream(), true);
				in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
				String inputLine, outputLine;
				ServerClientProtocol protocol = new ServerClientProtocol();

				while ((inputLine = in.readLine()) != null) {   
				    outputLine = protocol.processInput(inputLine);
				    out.println(outputLine);
				    if (outputLine.equals("Bye."))
				    break;
			    }
			} catch (IOException e) {
				JOptionPane.showMessageDialog(rootPane, "Accept failed on port: " + PORT_NUM);
				e.printStackTrace();
			}	
		}

		// client doesn't go first
		wait = true;*/

		//---------END SERVER/CLIENT SETUP-----------//
		
		
		//create a set of possible move directions
		compass[0] = new Point(-1,0);compass[1] = new Point(-1,1);compass[2] = new Point(0,1);compass[3] = new Point(1,1);
		compass[4] = new Point(1,0);compass[5] = new Point(1,-1);compass[6] = new Point(0,-1);compass[7] = new Point(-1,-1);

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
			
			computerMove = new JButton("White Move");
			
			//set button action to go back to main menu
			back.addActionListener( new ActionListener()
	        {
	            public void actionPerformed(ActionEvent e)
	            {
	            	movesMade = 0;
	            	gameMoves[movesMade] = new pieceMove(0, 0, 0, 0,"NONE", 0);
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
	            	keepMoving = false;
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
					if(onePlayer){
						//press hint once to show hints 
						//must press hint again to continue playing 
						if(hintcount%2 == 0){
							//gives hints of valid moves for black pieces 
							allValidMoves(2);
							for(int i = 0; i < validMoves.size(); ++i){
								pieceMap[validMoves.get(i).destRow][validMoves.get(i).destColumn] = 4; 
							}	
						}
						if(hintcount%2 != 0){
							for(int i =0; i < BOARD_HEIGHT; ++i){
								for(int j = 0; j < BOARD_LENGTH; ++j){
									if(pieceMap[i][j] == 4){
										pieceMap[i][j] = 0; 
									}
								}
							}
						}
						++hintcount; 
					}
					
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

			computerMove.addActionListener( new ActionListener()
	        {
				public void actionPerformed(ActionEvent e)
	            {
					if((onePlayer && (gameMoves[movesMade-1].color == 2) && !keepMoving) || (onePlayer && (gameMoves[movesMade-1].color == 1) && keepMoving)) {	
						initTree();
						computerMove();
					}
	            }
	        });
			
			repaint();
			add(back);
			add(hint);
			add(reset);	
			add(advanceButton);
			add(retreatButton);
			add(computerMove);
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
						case 4: {
							drawPiece(Color.GREEN, this_x, this_y, g);
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
			gameMoves[movesMade] = new pieceMove(0, 0, 0, 0,"NONE", 0);	
			
			if(keepMoving && (movesMade > 0) && ((firstValid != gameMoves[movesMade-1].color) || ((gameMoves[movesMade-1].destRow != firstRow) || (gameMoves[movesMade-1].destColumn != firstColumn)))) {
				System.out.println("CANNOT MOVE PIECE: " + keepMoving + gameMoves[movesMade-1].color + gameMoves[movesMade-1].destRow + gameMoves[movesMade-1].destColumn);
			} else if(validMove(secondRow, secondColumn)) {
				
				//send/receive move info
				capturePieces(gameMoves[movesMade], 0);
				movePiece(secondRow, secondColumn);
			}
		}
		firstValid = pieceMap[secondRow][secondColumn];
		firstRow = secondRow;
		firstColumn = secondColumn;
		
	}
	
	Boolean validMove(int secondRow, int secondColumn){
		//check that the correct color is trying to move
		int currentColor = pieceMap[firstRow][firstColumn];		
		
		if(movesMade > 0) {
			if(!keepMoving) {
				if(currentColor == gameMoves[movesMade-1].color) {
					System.out.println("NOT YOUR TURN!   ");
					return false;
				}
			}
		} else {
			if(currentColor == 1) {
				System.out.println("NOT YOUR TURN!");
				return false;
			}
		}
		
		String moveDirection = findDirection((secondRow - firstRow), (secondColumn - firstColumn));
		
		if(firstRow == 0) {
			if((moveDirection == "NORTH") || (moveDirection == "NEAST") || (moveDirection == "NWEST")){
				if(!keepMoving)System.out.println("INVALID MOVE!");
				return false;				
			}
		}
		if(firstColumn == 0) {
			if((moveDirection == "SWEST") || (moveDirection == "WEST") || (moveDirection == "NWEST")) {
				if(!keepMoving)System.out.println("INVALID MOVE!");
				return false;					
						
			}
		}
		if(firstRow == (BOARD_HEIGHT-1)) {
			if((moveDirection == "SWEST") || (moveDirection == "SOUTH") || (moveDirection == "SEAST")) {
				if(!keepMoving)System.out.println("INVALID MOVE!");
				return false;					
						
			}					
		}
		if(firstRow == (BOARD_LENGTH-1)) {
			if((moveDirection == "NEAST") || (moveDirection == "EAST") || (moveDirection == "SEAST")) {
				if(!keepMoving)System.out.println("INVALID MOVE!");
				return false;						
			}					
		}		
		
		if(pieceMap[secondRow][secondColumn] != 0) {
			if(!keepMoving)System.out.println("INVALID MOVE: non-empty space");
			return false;			
		}
		
		
		if(((secondRow - firstRow) > 1 ) || ((secondColumn - firstColumn) > 1)) {
			if(!keepMoving)System.out.println("INVALID MOVE!");
			return false;				
		}
		
		//SWEST
		if((secondRow == (firstRow + 1)) && (secondColumn == (firstColumn - 1))) {
			//check that line is available to move along
			if(!linePresent(firstRow, firstColumn, moveDirection)) {
				if(!keepMoving)System.out.println("INVALID MOVE!");
				return false;
			}
			
			//check that last move wasn't in same direction
			if(movesMade > 0) {
				if(gameMoves[movesMade-1].direction == "SWEST" && (gameMoves[movesMade-1].destRow == (firstRow + 1) && (gameMoves[movesMade-1].destColumn == (firstColumn - 1)))) {
					if(!keepMoving)System.out.println("CANT MOVE SAME DIRECTION!");
					return false;
				}
			}
			gameMoves[movesMade].setDirection("SWEST");

			//determine whether requested move is valid/invalid
			//act differently based on whether or not piece is moved to outer edge of array (due to out of bounds errors)
			if((firstRow == 0) || (firstColumn == (BOARD_LENGTH-1))) {
				if((pieceMap[secondRow + 1][secondColumn - 1] == 0) || (pieceMap[secondRow + 1][secondColumn - 1] == currentColor) || (!advance && !searching)) {
					if(!keepMoving)System.out.println("INVALID MOVE!");
					return false;
				}
			} else if((secondRow == (BOARD_HEIGHT - 1)) || (secondColumn == 0)) {
				if((pieceMap[firstRow - 1][firstColumn + 1] == 0) || (pieceMap[firstRow - 1][firstColumn + 1] == currentColor) || (!retreat && !searching)) {
					if(!keepMoving)System.out.println("INVALID MOVE!");
					return false;					
				}
			} else if(((pieceMap[secondRow + 1][secondColumn - 1] == 0) || (pieceMap[secondRow + 1][secondColumn - 1] == currentColor) || (!advance && !searching))			
			         && ((pieceMap[firstRow - 1][firstColumn + 1] == 0) || (pieceMap[firstRow - 1][firstColumn + 1] == currentColor) || (!retreat && !searching))) {
				if(!keepMoving)System.out.println("INVALID MOVE!");
				return false;
			}
		}
		
		//SEAST
		if((secondRow == (firstRow + 1)) && (secondColumn == (firstColumn + 1))) {
			
			//check that line is available to move along
			if(!linePresent(firstRow, firstColumn, moveDirection)) {
				if(!keepMoving)System.out.println("INVALID MOVE!");
				return false;
			}
			
			if(movesMade >0) {
				if(gameMoves[movesMade-1].direction == "SEAST" && (gameMoves[movesMade-1].destRow == (firstRow + 1) && (gameMoves[movesMade-1].destColumn == (firstColumn - 1)))) {
					if(!keepMoving)System.out.println("CANT MOVE SAME DIRECTION!");
					return false;				
				}
			}
			gameMoves[movesMade].setDirection("SEAST");

			//determine whether requested move is valid/invalid
			//act differently based on whether or not piece is moved to outer edge of array (due to out of bounds errors)
			if((firstRow == 0) || (firstColumn == 0)) {
				if((pieceMap[secondRow + 1][secondColumn + 1] == 0) || (pieceMap[secondRow + 1][secondColumn + 1] == currentColor) || (!advance && !searching)) {
					if(!keepMoving)System.out.println("INVALID MOVE!");
					return false;						
				}
			} else if((secondRow == (BOARD_HEIGHT - 1)) || (secondColumn == (BOARD_LENGTH - 1))) {
				if((pieceMap[firstRow - 1][firstColumn - 1] == 0) || (pieceMap[firstRow - 1][firstColumn - 1] == currentColor) || (!retreat && !searching)) {
					if(!keepMoving)System.out.println("INVALID MOVE!");
					return false;						
				}
			} else if(((pieceMap[secondRow + 1][secondColumn + 1] == 0) || (pieceMap[secondRow + 1][secondColumn + 1] == currentColor) || (!advance && !searching)) 
			         && ((pieceMap[firstRow - 1][firstColumn - 1] == 0) || (pieceMap[firstRow - 1][firstColumn - 1] == currentColor) || (!retreat && !searching))) {		
				if(!keepMoving)System.out.println("INVALID MOVE!");
				return false;		
			} 
		}
			
		//NWEST
		if((secondRow == (firstRow - 1)) && (secondColumn == (firstColumn - 1))) {
			
			//check that line is available to move along
			if(!linePresent(firstRow, firstColumn, moveDirection)) {
				if(!keepMoving)System.out.println("INVALID MOVE!");
				return false;
			}
			
			if(movesMade >0) {
				if(gameMoves[movesMade-1].direction == "NWEST" && (gameMoves[movesMade-1].destRow == (firstRow + 1) && (gameMoves[movesMade-1].destColumn == (firstColumn - 1)))) {
					if(!keepMoving)System.out.println("CANT MOVE SAME DIRECTION!");
					return false;				
				}
			}
			gameMoves[movesMade].setDirection("NWEST");

			//determine whether requested move is valid/invalid
			//act differently based on whether or not piece is moved to outer edge of array (due to out of bounds errors)
			if((firstRow == (BOARD_HEIGHT - 1)) || (firstColumn == (BOARD_LENGTH - 1))) {
				if((pieceMap[secondRow - 1][secondColumn - 1] == 0) || (pieceMap[secondRow - 1][secondColumn - 1] == currentColor) || (!advance && !searching)) {
					if(!keepMoving)System.out.println("INVALID MOVE!");
					return false;						
				}
			} else if((secondRow <= 0) || (secondColumn <= 0)) {
				if((pieceMap[firstRow + 1][firstColumn + 1] == 0) || (pieceMap[firstRow + 1][firstColumn + 1] == currentColor) || (!retreat && !searching)) {
					if(!keepMoving)System.out.println("INVALID MOVE!");
					return false;						
				}
			} else if(((pieceMap[secondRow - 1][secondColumn - 1] == 0) || (pieceMap[secondRow - 1][secondColumn - 1] == currentColor) || (!advance && !searching)) 
			         && ((pieceMap[firstRow + 1][firstColumn + 1] == 0) || (pieceMap[firstRow + 1][firstColumn + 1] == currentColor) || (!retreat && !searching))) {		
				if(!keepMoving)System.out.println("INVALID MOVE!");
				return false;		
			}
		}
			
		//NEAST
		if((secondRow == (firstRow - 1)) && (secondColumn == (firstColumn + 1))) {
			
			//check that line is available to move along
			if(!linePresent(firstRow, firstColumn, moveDirection)) {
				if(!keepMoving)System.out.println("INVALID MOVE!");
				return false;
			}
			
			if(movesMade >0) {
				if(gameMoves[movesMade-1].direction == "NEAST" && (gameMoves[movesMade-1].destRow == (firstRow + 1) && (gameMoves[movesMade-1].destColumn == (firstColumn - 1)))) {
					if(!keepMoving)System.out.println("CANT MOVE SAME DIRECTION!");
					return false;				
				}
			}
			gameMoves[movesMade].setDirection("NEAST");
			
			//determine whether requested move is valid/invalid
			//act differently based on whether or not piece is moved to outer edge of array (due to out of bounds errors)
			if((firstRow == (BOARD_HEIGHT - 1)) || (firstColumn == 0)) {
				if((pieceMap[secondRow - 1][secondColumn + 1] == 0) || (pieceMap[secondRow - 1][secondColumn + 1] == currentColor) || (!advance && !searching)) {
					if(!keepMoving)System.out.println("INVALID MOVE!");
					return false;						
				}	
			} else if ((secondRow == 0) || (secondColumn == (BOARD_LENGTH - 1))) {
				if((pieceMap[firstRow + 1][firstColumn - 1] == 0) || (pieceMap[firstRow + 1][firstColumn - 1] == currentColor) || !retreat) {
					if(!keepMoving)System.out.println("INVALID MOVE!");
					return false;						
				}
			} else if(((pieceMap[secondRow - 1][secondColumn + 1] == 0) || (pieceMap[secondRow - 1][secondColumn + 1] == currentColor) || (!advance && !searching)) 
			         && ((pieceMap[firstRow + 1][firstColumn - 1] == 0) || (pieceMap[firstRow + 1][firstColumn - 1] == currentColor) || !retreat)) {		
				if(!keepMoving)System.out.println("INVALID MOVE!");
				return false;		
			}
		}
			
		//SOUTH
		if((secondRow == (firstRow + 1)) && (secondColumn == firstColumn)) {
			
			if(movesMade >0) {
				if(gameMoves[movesMade-1].direction == "SOUTH" && (gameMoves[movesMade-1].destRow == (firstRow + 1) && (gameMoves[movesMade-1].destColumn == (firstColumn - 1)))) {
					if(!keepMoving)System.out.println("CANT MOVE SAME DIRECTION!");
					return false;				
				}
			}			
			gameMoves[movesMade].setDirection("SOUTH");	
			
			
			//determine whether requested move is valid/invalid
			//act differently based on whether or not piece is moved to outer edge of array (due to out of bounds errors)
			if(secondRow == (BOARD_HEIGHT - 1)) {
				if((pieceMap[firstRow - 1][firstColumn] == 0) || (pieceMap[firstRow - 1][firstColumn] == currentColor) || (!retreat && !searching)) {
					if(!keepMoving)System.out.println("INVALID MOVE!");
					return false;					
				}		
			} else if(firstRow == 0) {
				if((pieceMap[secondRow + 1][secondColumn] == 0) || (pieceMap[secondRow + 1][secondColumn] == currentColor) || (!advance && !searching)) {
					if(!keepMoving)System.out.println("INVALID MOVE!");
					return false;					
				}			
			} else if(((pieceMap[secondRow + 1][secondColumn] == 0) || (pieceMap[secondRow + 1][secondColumn] == currentColor) || (!advance && !searching)) 
			         && ((pieceMap[firstRow - 1][firstColumn] == 0) || (pieceMap[firstRow - 1][firstColumn] == currentColor) || (!retreat && !searching))) {		
				if(!keepMoving)System.out.println("INVALID MOVE!");
				return false;		
			}
		}
		
		//NORTH
		if((secondRow == (firstRow - 1)) && (secondColumn == firstColumn)) {
			if(movesMade > 0) {
				if((gameMoves[movesMade-1].direction == "NORTH") && (gameMoves[movesMade-1].destRow == firstRow) && (gameMoves[movesMade-1].destColumn == firstColumn)) {
					if(!keepMoving)System.out.println("CANT MOVE SAME DIRECTION!");
					return false;				
				}
			}
			gameMoves[movesMade].setDirection("NORTH");
			
			if(secondRow == 0) {
				if((pieceMap[firstRow + 1][firstColumn] == 0) || (pieceMap[firstRow + 1][firstColumn] == currentColor) || (!retreat && !searching)) {
					if(!keepMoving)System.out.println("INVALID MOVE!");
					return false;					
				}
				
			} else if(firstRow == (BOARD_HEIGHT - 1)) {
				if((pieceMap[secondRow - 1][secondColumn] == 0) || (pieceMap[secondRow - 1][secondColumn] == currentColor) || (!advance && !searching)) {		
					if(!keepMoving)System.out.println("INVALID MOVE!");
					return false;
				}
			} else if(((pieceMap[secondRow - 1][secondColumn] == 0) || (pieceMap[secondRow - 1][secondColumn] == currentColor) || (!advance && !searching)) 
		             && ((pieceMap[firstRow + 1][firstColumn] == 0) || (pieceMap[firstRow + 1][firstColumn] == currentColor) || (!retreat && !searching))) {		
				if(!keepMoving)System.out.println("INVALID MOVE!");
				return false;	
				
			}
		}
			
		//EAST
		if((secondRow == firstRow) && (secondColumn == (firstColumn + 1))) {
			
			if(movesMade >0) {
				if(gameMoves[movesMade-1].direction == "EAST" && (gameMoves[movesMade-1].destRow == (firstRow + 1) && (gameMoves[movesMade-1].destColumn == (firstColumn - 1)))) {
					if(!keepMoving)System.out.println("CANT MOVE SAME DIRECTION!");
					return false;				
				}
			}
			gameMoves[movesMade].setDirection("EAST");
			
			if(firstColumn == 0) {
				if((pieceMap[secondRow][secondColumn + 1] == 0) || (pieceMap[secondRow][secondColumn + 1] == currentColor) || (!advance && !searching)) {
					if(!keepMoving)System.out.println("INVALID MOVE!");
					return false;							
				}
			} else if(secondColumn == (BOARD_LENGTH - 1)) {
				if((pieceMap[firstRow][firstColumn - 1] == 0) || (pieceMap[firstRow][firstColumn - 1] == currentColor) || (!retreat && !searching)) {
					if(!keepMoving)System.out.println("INVALID MOVE!");
					return false;							
				}
			} else if(((pieceMap[secondRow][secondColumn + 1] == 0) || (pieceMap[secondRow][secondColumn + 1] == currentColor) || (!advance && !searching)) 
			         && ((pieceMap[firstRow][firstColumn - 1] == 0) || (pieceMap[firstRow][firstColumn - 1] == currentColor) || (!retreat && !searching))) {		
				if(!keepMoving)System.out.println("INVALID MOVE!");
				return false;		
			}
		}
			
		//WEST
		if((secondRow == firstRow) && (secondColumn == (firstColumn - 1))) {
			
			if(movesMade >0) {
				if(gameMoves[movesMade-1].direction == "WEST" && (gameMoves[movesMade-1].destRow == (firstRow + 1) && (gameMoves[movesMade-1].destColumn == (firstColumn - 1)))) {
					if(!keepMoving)System.out.println("CANT MOVE SAME DIRECTION!");
					return false;				
				}
			}
			gameMoves[movesMade].setDirection("WEST");
			
			if(firstColumn == (BOARD_LENGTH - 1)) {
				if((pieceMap[secondRow][secondColumn - 1] == 0) || (pieceMap[secondRow][secondColumn - 1] == currentColor) || !advance) {
					if(!keepMoving)System.out.println("INVALID MOVE!");
					return false;						
				}
			} else if(secondColumn == 0) {
				if((pieceMap[firstRow][firstColumn + 1] == 0) || (pieceMap[firstRow][firstColumn + 1] == currentColor) || !retreat) {
					if(!keepMoving)System.out.println("INVALID MOVE!");
					return false;						
				}
			} else if(((pieceMap[secondRow][secondColumn - 1] == 0) || (pieceMap[secondRow][secondColumn - 1] == currentColor) || !advance) 
			   && ((pieceMap[firstRow][firstColumn + 1] == 0) || (pieceMap[firstRow][firstColumn + 1] == currentColor) || !retreat)) {		
				if(!keepMoving)System.out.println("INVALID MOVE!");
				return false;		
			}
		}
	
		gameMoves[movesMade].setSourceRow(firstRow);
		gameMoves[movesMade].setSourceColumn(firstColumn);
		gameMoves[movesMade].setDestRow(secondRow); 
		gameMoves[movesMade].setDestColumn(secondColumn);
		gameMoves[movesMade].setColor(currentColor);
		
		return true;
	}
		
	void capturePieces(pieceMove move, int flag) {
		
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
				while((i <= (BOARD_HEIGHT - 1)) && (j >= 0) && (pieceMap[i][j] != currentColor) && (pieceMap[i][j] != 0)) {
					pieceMap[i][j] = flag;
					++i;--j;
				}	
			}
	
			if(retreat  || computerEval) {
				int k = firstRow-1;
				int z = firstColumn+1;
				while((k >= 0) && (z <= (BOARD_LENGTH - 1)) && (pieceMap[k][z] != currentColor) && (pieceMap[k][z] != 0)) {
					pieceMap[k][z] = flag;
					--k;++z;
				}
			}	
		} else if(move.direction == "SEAST") {
			
			if(advance || computerEval) {
				int i = move.destRow+1;
				int j = move.destColumn+1;
				while((i <= (BOARD_HEIGHT - 1)) && (j <= (BOARD_LENGTH - 1)) && (pieceMap[i][j] != currentColor) && (pieceMap[i][j] != 0)) {
					pieceMap[i][j] = flag;						
					++i;++j;					
				}	
			}

			if(retreat || computerEval) {
				int k = firstRow-1;
				int z = firstColumn-1;
				while((k >= 0) && (z >= 0) && (pieceMap[k][z] != currentColor) && (pieceMap[k][z] != 0)) {
					pieceMap[k][z] = flag;								
					--k;--z;					
				}
			}			
		} else if(move.direction == "NWEST") {
			
			if(advance || computerEval) {
				int i = move.destRow-1;
				int j = move.destColumn-1;
				while((i >= 0) && (j >= 0) && (pieceMap[i][j] != currentColor) && (pieceMap[i][j] != 0)) {
					pieceMap[i][j] = flag;							
					--i;--j;				
				}	
			}
				
			if(retreat  || computerEval) {
				int k = firstRow+1;
				int z = firstColumn+1;
				while((k <= (BOARD_HEIGHT - 1)) && (z <= (BOARD_LENGTH - 1)) && (pieceMap[k][z] != currentColor) && (pieceMap[k][z] != 0)) {
					pieceMap[k][z] = flag;								
					++k;++z;										
				}
			}			
		} else if(move.direction == "NEAST") {
			
			if(advance || computerEval) {
				int i = move.destRow-1;
				int j = move.destColumn+1;
				while((i >= 0) && (j <= (BOARD_LENGTH - 1)) && (pieceMap[i][j] != currentColor) && (pieceMap[i][j] != 0)) {
					pieceMap[i][j] = flag;								
					--i;++j;					
				}	
			}

			if(retreat || computerEval) {
				int k = firstRow+1;
				int z = firstColumn-1;
				while((k <= (BOARD_HEIGHT - 1)) && (z >= 0) && (pieceMap[k][z] != currentColor) && (pieceMap[k][z] != 0)) {
					pieceMap[k][z] = flag;							
					++k;--z;					
				}
			}			
		} else if(move.direction == "SOUTH") {
			
			if(advance || computerEval) {
				for(int i = move.destRow+1; (i <= (BOARD_HEIGHT - 1)) && (pieceMap[i][move.destColumn] != currentColor) && (pieceMap[i][move.destColumn] != 0); ++i) {
					pieceMap[i][move.destColumn] = flag;
				}
			}

			if(retreat || computerEval) {
				for(int i = firstRow - 1; (i >= 0) && (pieceMap[i][move.destColumn] != currentColor) && (pieceMap[i][move.destColumn] != 0); --i) {
					pieceMap[i][move.destColumn] = flag;
				}
			}			
		} else if(move.direction == "NORTH") {
					
			if(advance || computerEval) {
				for(int i = move.destRow-1; (i >= 0) && (pieceMap[i][move.destColumn] != currentColor) && (pieceMap[i][move.destColumn] != 0); --i) {
					pieceMap[i][move.destColumn] = flag;
				}
			}

			if(retreat || computerEval) {
				for(int i = firstRow + 1; (i <= (BOARD_HEIGHT - 1)) && (pieceMap[i][move.destColumn] != currentColor) && (pieceMap[i][move.destColumn] != 0); ++i) {				
					pieceMap[i][move.destColumn] = flag;
				}
			}			
		} else if(move.direction == "EAST") {
			
			if(advance || computerEval) {
				for(int i = move.destColumn+1; (i <= (BOARD_LENGTH - 1)) && (pieceMap[move.destRow][i] != currentColor) && (pieceMap[move.destRow][i] != 0); ++i) {		
					pieceMap[move.destRow][i] = flag;
				}
			}

			if(retreat || computerEval) {
				for(int i = firstColumn - 1; (i >= 0) && (pieceMap[move.destRow][i] != currentColor) && (pieceMap[move.destRow][i] != 0); --i) {		
					pieceMap[move.destRow][i] = flag;
				}
			}			
		} else if(move.direction == "WEST") {
			
			if(advance || computerEval) {
				for(int i = move.destColumn-1; (i >= 0) && (pieceMap[move.destRow][i] != currentColor) && (pieceMap[move.destRow][i] != 0); --i) {
					pieceMap[move.destRow][i] = flag;				
				}
			}

			if(retreat || computerEval) {
				for(int i = firstColumn + 1; (i <= (BOARD_LENGTH - 1)) && (pieceMap[move.destRow][i] != currentColor) && (pieceMap[move.destRow][i] != 0); ++i) {
					pieceMap[move.destRow][i] = flag;			
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
			System.out.println("Last Move: BLACK");
		else if(gameMoves[movesMade].color == 1)
			System.out.println("Last Move: WHITE");	
		
		movesMade++;
		
		gameMoves[movesMade] =  new pieceMove(0, 0, 0, 0,"NONE", 0);
		keepMoving = true;
		allValidMoves(movingColor);
		/*
		for(int i = 0; i < validMoves.size(); ++i) {
			System.out.print("Available Move: ");
			System.out.print(validMoves.get(i).sourceRow);
			System.out.print(validMoves.get(i).sourceColumn);
			System.out.print(validMoves.get(i).direction + '\n');
		}
		*/
		Boolean moveFound = false;
		for(int i = 0; i < validMoves.size(); ++i) {
			if((validMoves.get(i).sourceRow == secondRow) && (validMoves.get(i).sourceColumn == secondColumn) && (validMoves.size() != 0))
				moveFound = true;
		}
		if(!moveFound)
			keepMoving = false;
		
		validMoves.clear();
		evalBoard();
	}
	
	void evalBoard() {
		
		whiteCount = 0; 
		blackCount = 0;
		
		for(int i = 0; i < BOARD_HEIGHT; ++i) {
			for(int j = 0; j < BOARD_LENGTH; ++j) {		
				if(pieceMap[i][j] == 2)
					blackCount++;
				else if(pieceMap[i][j] == 1)
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
		
	//FIX FOR VARIABLE BOARD SIZES *********
	Boolean linePresent(int row, int col, String direct) {
		
		if((row == 0) && (col == 0) && (direct == "SEAST")) 
			return true;
		else if((row == 0) && (col == (BOARD_LENGTH-1)) && (direct == "SWEST"))
			return true;
		else if((row == (BOARD_HEIGHT-1)) && (col == 0) && (direct == "NEAST"))
			return true;
		else if((row == (BOARD_HEIGHT-1)) && (col == (BOARD_LENGTH-1)) && (direct == "NWEST"))
			return true;
		else if((row == 0) && ((col == 2) || (col == 4) || (col == 6)) && ((direct == "SEAST") || (direct == "SWEST")))
			return true;
		else if((row == (BOARD_HEIGHT-1)) && ((col == 2) || (col == 4) || (col == 6)) && ((direct == "NEAST") || (direct == "NWEST")))
			return true;
		
		//check to see if there is a line on board to travel along
		if( ((row == 0) || (row == 2) || (row == (BOARD_HEIGHT - 1))) && ((col == 1) || (col == 3) || (col == 5) || (col == 7)) ) {
			return false;
		}
		
		if(((row == 1) || (row == 3)) && ((col == 0) || (col == 2) || (col == 4) || (col == 6) || (col == (BOARD_LENGTH - 1)))) {
			return false;
		}
			
		return true;
	}

	void allValidMoves(int color) {
		
		searching = true;
		
		for(int row = 0; row < BOARD_HEIGHT; row++) {
			for(int column = 0; column < BOARD_LENGTH; column++) {	
				
				if(pieceMap[row][column] == color) {
					firstRow = row;
					firstColumn = column;
					
					if((row-1) >= 0) {
						if(validMove(row - 1, column)) {
							int x = gameMoves[movesMade].sourceRow;
							int y = gameMoves[movesMade].sourceColumn;
							int x2 = gameMoves[movesMade].destRow;
							int y2 = gameMoves[movesMade].destColumn;
							String d = gameMoves[movesMade].direction;
							int c = gameMoves[movesMade].color;
							validMoves.add(new pieceMove(x, y, x2, y2, d, c));	
						}
					}
					if(((row - 1) >= 0) && ((column + 1) <= (BOARD_LENGTH - 1))) {	
						if(validMove(row - 1, column + 1)) {
							int x = gameMoves[movesMade].sourceRow;
							int y = gameMoves[movesMade].sourceColumn;
							int x2 = gameMoves[movesMade].destRow;
							int y2 = gameMoves[movesMade].destColumn;
							String d = gameMoves[movesMade].direction;
							int c = gameMoves[movesMade].color;
							validMoves.add(new pieceMove(x, y, x2, y2, d, c));	
						}
					}
					if((column + 1) <= (BOARD_LENGTH - 1)) {	
						if(validMove(row, column + 1)) {
							int x = gameMoves[movesMade].sourceRow;
							int y = gameMoves[movesMade].sourceColumn;
							int x2 = gameMoves[movesMade].destRow;
							int y2 = gameMoves[movesMade].destColumn;
							String d = gameMoves[movesMade].direction;
							int c = gameMoves[movesMade].color;
							validMoves.add(new pieceMove(x, y, x2, y2, d, c));	
						}
					}
					if(((row + 1) <= (BOARD_HEIGHT - 1)) && ((column + 1) <= (BOARD_LENGTH - 1))) {
						if(validMove(row + 1, column + 1)) {
							int x = gameMoves[movesMade].sourceRow;
							int y = gameMoves[movesMade].sourceColumn;
							int x2 = gameMoves[movesMade].destRow;
							int y2 = gameMoves[movesMade].destColumn;
							String d = gameMoves[movesMade].direction;
							int c = gameMoves[movesMade].color;
							validMoves.add(new pieceMove(x, y, x2, y2, d, c));	
						}
					}
					if((row + 1) <= (BOARD_HEIGHT - 1)) {
						if(validMove(row + 1, column)) {
							int x = gameMoves[movesMade].sourceRow;
							int y = gameMoves[movesMade].sourceColumn;
							int x2 = gameMoves[movesMade].destRow;
							int y2 = gameMoves[movesMade].destColumn;
							String d = gameMoves[movesMade].direction;
							int c = gameMoves[movesMade].color;
							validMoves.add(new pieceMove(x, y, x2, y2, d, c));	
						}
					}
					if(((row + 1) <= (BOARD_HEIGHT - 1)) && ((column - 1) >= 0)) {
						if(validMove(row + 1, column - 1)) {	
							int x = gameMoves[movesMade].sourceRow;
							int y = gameMoves[movesMade].sourceColumn;
							int x2 = gameMoves[movesMade].destRow;
							int y2 = gameMoves[movesMade].destColumn;
							String d = gameMoves[movesMade].direction;
							int c = gameMoves[movesMade].color;
							validMoves.add(new pieceMove(x, y, x2, y2, d, c));	
						}
					}		
					if((column - 1) >= 0) {
						if(validMove(row, column - 1)) {	
							int x = gameMoves[movesMade].sourceRow;
							int y = gameMoves[movesMade].sourceColumn;
							int x2 = gameMoves[movesMade].destRow;
							int y2 = gameMoves[movesMade].destColumn;
							String d = gameMoves[movesMade].direction;
							int c = gameMoves[movesMade].color;
							validMoves.add(new pieceMove(x, y, x2, y2, d, c));	
						}
					}
					if(((row - 1) >= 0) && ((column - 1) >= 0)) {
						if(validMove(row - 1, column - 1)) {	
							int x = gameMoves[movesMade].sourceRow;
							int y = gameMoves[movesMade].sourceColumn;
							int x2 = gameMoves[movesMade].destRow;
							int y2 = gameMoves[movesMade].destColumn;
							String d = gameMoves[movesMade].direction;
							int c = gameMoves[movesMade].color;
							validMoves.add(new pieceMove(x, y, x2, y2, d, c));	
						}
					}	
				}
			}
		}
		searching = false;
		
		/*for(int i = 0; i < validMoves.size(); ++i) {
			
			System.out.println("Valid Start: " + validMoves.get(i).sourceRow + ',' + validMoves.get(i).sourceColumn);
			System.out.println("Valid Direction: " + validMoves.get(i).direction);
		}
		computerMove();*/
	}
	
	String findDirection(int dRow, int dColumn) {
		
		if((dRow == -1) && (dColumn == 0)) 
			return "NORTH";
		else if((dRow == -1) && (dColumn == 1)) 
			return "NEAST";
		else if((dRow == 0) && (dColumn == 1)) 
			return "EAST";
		else if((dRow == 1) && (dColumn == 1)) 
			return "SEAST";
		else if((dRow == 1) && (dColumn == 0)) 
			return "SOUTH";
		else if((dRow == 1) && (dColumn == -1)) 
			return "SWEST";
		else if((dRow == 0) && (dColumn == -1)) 
			return "WEST";
		else if((dRow == -1) && (dColumn == -1)) 
			return "NWEST";
		else
			return "Invalid Direction";
	}
	
	void computerMove() {	
		int counter = 0;
		/*
		if(gameMoves[movesMade-1].color == 1) {
			
			while(true) {
				
				Random rand = new Random();
				int  randMoveIndex = rand.nextInt(validMoves.size());		
					
				
				pieceMove randMove = validMoves.get(randMoveIndex);
				firstRow = randMove.sourceRow;
				firstColumn = randMove.sourceColumn;					
				firstValid = pieceMap[firstRow][firstColumn];
				
				if((firstValid == gameMoves[movesMade-1].color) && (firstRow == gameMoves[movesMade-1].destRow) && (firstColumn == gameMoves[movesMade-1].destColumn)) {
					capturePieces(randMove, 0);
					movePiece(randMove.destRow, randMove.destColumn);
					
					validMoves.clear();					
					return;
				}
				counter++;
				
				if(counter == 100) {
					break;
				}
			}
		} else {
			
			Random rand = new Random();
			int  randMoveIndex = rand.nextInt(validMoves.size());		
			*/
			pieceMove move= mainTree.getBestMove(); 
			
			firstRow = move.sourceRow;
			firstColumn = move.sourceColumn;			
			
			capturePieces(move, 0);
			movePiece(move.destRow, move.destColumn);
			
			validMoves.clear();					
			return;			
		
	}
	
	void initTree() {

		mainTree = new MinimaxTree();	
		
		gameMoves[movesMade] = new pieceMove(0, 0, 0, 0,"NONE", 0);	
		keepMoving = true;
		allValidMoves(1);
		keepMoving = false;
		
		MinimaxTree.Node rootNode = mainTree.new Node();
		rootNode.setValue(0);
		
		System.out.println("Valid Move Size: " + validMoves.size());
		mainTree.BuildMiniMaxTree(rootNode,0);
		/*
		if(rootNode.children.size() != 0){
			for(int i = 0; i < rootNode.children.size(); ++i) {
				System.out.print("\nValid Move " + i + ": ");
				System.out.print("Node Value: " + mainTree.getRoot().children.get(i).getValue() + "| Location: ");
				System.out.print("[" + mainTree.getRoot().children.get(i).x + "][");
				System.out.print(mainTree.getRoot().children.get(i).y + "] | xDirection:");
				System.out.print(mainTree.getRoot().children.get(i).direction);
				System.out.print('\n');
			}	
		}
		*/
	}
	
	class pieceMove {
		
		public int destRow, destColumn;
		public int sourceRow, sourceColumn;
		public String direction;
		public int color;
		public boolean double_move; 
		
		pieceMove(int x, int y, int x2, int y2, String s, int c) {
			
			sourceRow = x;
			sourceColumn = y;
			destRow = x2;
			destColumn = y2;	
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
		public void setDoubleMove(boolean move){
			double_move = move; 
		}
		public boolean isDoubleMove(){
			return double_move; 
		}
		
	}
	
	double set_score(pieceMove move){
		double score = 0; 
		int pieces_taken = look_ahead(move);
		score = score + pieces_taken; 
		//if(move.isDoubleMove()){
		//	score+=2; 
		//}
		return score; 		
	}
	

	int look_ahead(pieceMove move){
		//checks to see what pieces will be taken for each available valid move 
		int taken_pieces = 0; 
		int currentColor = 1; 

		if(move.direction == "SWEST") {
				if(advance){ 
					int i = move.destRow+1;
					int j = move.destColumn-1;
					while((i <= (BOARD_HEIGHT - 1)) && (j >= 0)&& (pieceMap[i][j] != currentColor)&& (pieceMap[i][j] != 0)) {
						++taken_pieces; 
						++i;--j;
					}
				}
				if(retreat) {
					int k = firstRow-1;
					int z = firstColumn+1;
					while((k >= 0) && (z <= (BOARD_LENGTH - 1)) && (pieceMap[k][z] != currentColor)&& (pieceMap[k][z] != 0)) {
						++taken_pieces; 
						--k;++z;
					}
				}
		} else if(move.direction == "SEAST") {
			if(advance){
				int i = move.destRow+1;
				int j = move.destColumn+1;
				while((i <= (BOARD_HEIGHT - 1)) && (j <= (BOARD_LENGTH - 1))&& (pieceMap[i][j] != currentColor)&& (pieceMap[i][j] != 0)) {
					++taken_pieces; 						
					++i;++j;					
				}
			}
			if(retreat){
				int k = firstRow-1;
				int z = firstColumn-1;
				while((k >= 0) && (z >= 0) && (pieceMap[k][z] != currentColor)&& (pieceMap[k][z] != 0)) {
					++taken_pieces;								
					--k;--z;					
				}
				
			}
		} else if(move.direction == "NWEST") {
			if(advance){
				int i = move.destRow-1;
				int j = move.destColumn-1;
				while((i >= 0) && (j >= 0) && (pieceMap[i][j] != currentColor)&& (pieceMap[i][j] != 0)) {
					++taken_pieces; 							
					--i;--j;				
				}
			}
			if(move.destRow == 0 || move.destRow == 1) {
				int k = firstRow+1;
				int z = firstColumn+1;
				while((k <= (BOARD_HEIGHT-1)) && (z <= (BOARD_LENGTH-1))&& (pieceMap[k][z] != currentColor) && (pieceMap[k][z] != 0)) {
					++taken_pieces; 								
					++k;++z;										
				}
			}
		} else if(move.direction == "NEAST") {
			if(advance){
				int i = move.destRow-1;
				int j = move.destColumn+1;
				while((i >= 0) && (j <= (BOARD_LENGTH - 1))&& (pieceMap[i][j] != currentColor)&& (pieceMap[i][j] != 0)) {
					++taken_pieces; 								
					--i;++j;					
				}		
			}
			if(move.destRow == 0 || move.destRow == 1) {
				int k = firstRow+1;
				int z = firstColumn-1;
				while((k <= (BOARD_HEIGHT - 1)) && (z >= 0)&& (pieceMap[k][z] != currentColor)&& (pieceMap[k][z] != 0)) {
					++taken_pieces; 							
					++k;--z;					
				}
			}
		} else if(move.direction == "SOUTH") {
			if(advance){
				for(int i = move.destRow+1; (i <= (BOARD_HEIGHT - 1)) && (pieceMap[i][move.destColumn] != currentColor)&& (pieceMap[i][move.destColumn] != 0); ++i) {
						++taken_pieces; 
				}	
			}
			if(retreat) {
				for(int i = firstRow - 1; (i >= 0)&& (pieceMap[i][move.destColumn] != currentColor)&& (pieceMap[i][move.destColumn] != 0); --i) {
						++taken_pieces; 
				}
			}
		} else if(move.direction == "NORTH") {
			if(advance){
				for(int i = move.destRow-1; (i >= 0)&& (pieceMap[i][move.destColumn] != currentColor)&& (pieceMap[i][move.destColumn] != 0); --i) {
					++taken_pieces; 
				}
			}
			if(move.destRow == 0 || move.destRow == 1) {
				for(int i = firstRow + 1; (i <= (BOARD_HEIGHT - 1))&& (pieceMap[i][move.destColumn] != currentColor)&& (pieceMap[i][move.destColumn] != 0); ++i) {				
					++taken_pieces; 
				}
			}
					
		} else if(move.direction == "EAST") {
			if(advance){
				for(int i = move.destColumn+1; (i <= (BOARD_LENGTH - 1))&& (pieceMap[move.destRow][i] != currentColor)&& (pieceMap[move.destRow][i] != 0); ++i) {		
					++taken_pieces; 
				}	
			}
			if(retreat) {
				for(int i = firstColumn - 1; (i >= 0)&& (pieceMap[move.destRow][i] != currentColor)&& (pieceMap[move.destRow][i] != 0); --i) {		
					++taken_pieces; 
				}
			}
		} else if(move.direction == "WEST") {
				for(int i = move.destColumn-1; (i >= 0)&& (pieceMap[move.destRow][i] != currentColor)&& (pieceMap[move.destRow][i] != 0); --i) {
					++taken_pieces; 				
				}
				if(retreat) {
					for(int i = firstColumn + 1; (i <= (BOARD_LENGTH - 1))&& (pieceMap[move.destRow][i] != currentColor)&& (pieceMap[move.destRow][i] != 0); ++i) {
						++taken_pieces; 		
					}
				}
		}
		return taken_pieces; 
	}
}






