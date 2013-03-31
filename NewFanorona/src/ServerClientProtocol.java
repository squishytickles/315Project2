import java.net.*;
import java.io.*;

import javax.swing.JApplet;
import javax.swing.JOptionPane;
 
public class ServerClientProtocol extends JApplet{
    private static final int START = 0;
    private static final int INFO = 1;
    private static final int MOVE = 2;
 
    private int state = START;
 
 
    public String processInput(String input) {
        String charset = input;
        String delims = "[ ]+";
        String[] tokens = charset.split(delims);
        String info = null;
    	
        //use this to set moves made by other player.. somehow?
        for(int i = 0; i < tokens.length; i++){
        	info = tokens[i];
        }
 
        if(state == START){
            JOptionPane.showMessageDialog(rootPane, "WELCOME!");
            state = INFO;
        }else if(state == INFO) {
           JOptionPane.showMessageDialog(rootPane, "INFO: " + info);
           state = MOVE;
        }else if(state == MOVE) {
            if(info.equalsIgnoreCase("W")){
                //set move for white...
        }else if(info.equalsIgnoreCase("B")){
                //set move for black
            }
        }
        return info; //output;
    }
}
