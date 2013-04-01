import javax.swing.JOptionPane;
 
public class ServerClientProtocol extends game{
    private static final int START = 0;
    private static final int INFO = 1;
    private static final int MOVE = 2;
    private static final int END = 3;
 
    private int state = START;
 
 
    public String processInput(String input) {
        byte[] bytes = input.getBytes();
        
        String info = new String(bytes);
        
 
        if(state == START)
        {
            JOptionPane.showMessageDialog(rootPane, "WELCOME!");
            state = INFO;
        }
        else if(state == INFO)
        {
           JOptionPane.showMessageDialog(rootPane, "INFO: " + info);
           state = MOVE;
           return info; 
        }
        else if(state == MOVE) {
            if(info.equalsIgnoreCase("W"))
            {
                //set move for white...
             //send updated board to server
            }
            else if(info.equalsIgnoreCase("B"))
            {
                //set move for black
            	//send updated board to client
            }
            else
            	//send to server/client that move was invalid..
            	JOptionPane.showMessageDialog(rootPane, "INVALID");
            return "MOVE";
        }
        else if(state == END)
        {
        	JOptionPane.showMessageDialog(rootPane, "GAME OVER");
        }
		return "END ";
    }
}
