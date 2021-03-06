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
        
        if(server){
        	
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
	        	
	        	JOptionPane.showMessageDialog(rootPane, "MOVE: " + info);	
	        	return info;
	        }
	        else if(state == END)
	        {
	        	JOptionPane.showMessageDialog(rootPane, "GAME OVER");
	        }
			return "END ";
        } else {
        	
        	state = MOVE;
        	
        	if(state == MOVE) {
	        	
	        	JOptionPane.showMessageDialog(rootPane, "MOVE: " + info);	
	        	return info;
	        }
	        else if(state == END)
	        {
	        	JOptionPane.showMessageDialog(rootPane, "GAME OVER");
	        }
			return "END ";
        	
        }
    }
}
