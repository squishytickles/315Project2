import java.awt.*;
import java.awt.event.ActionEvent; 
import java.awt.event.ActionListener; 
import java.util.*;

public class MinimaxTree {
	
	private Node root; 

	//Constructor 
	public MinimaxTree(){
		super(); 
	}

	public MinimaxTree(Node rt){
		root = rt; 
	}

	//Return the root Node of the Tree 
	public Node getRoot(){
		return this.root; 
	}

	//Set the root for the Tree 
	public void setRoot(Node rootElement){
		this.root = rootElement; 
	}
	//*****need to finish implementation 
	public void BuildMiniMaxTree(Node n, int depth){ 
		if(depth >= 0){
			n = new Node();
			root = n; 
			//place all move values within the tree 
			for(int i = 0; i < validMoves.size(); ++i){
				n.setNodePiece(validMoves.get(i).destRow, validMoves.get(i).destColumn, validMoves.get(i).direction, validMoves.get(i).color);
				children.add(n);
				//needs work 
				BuildMiniMaxTree(n.children.get(i),depth-1);
				
			}
		}
		else
			n = new Node();  
	}
	//MiniMax Algorithm 
	public movePiece getBestMove(){

		ArrayList<Node> root_children = root.getChildren();

	
		int index_max = 0; 
		int index_min = 0; 
		int value = root_children.get(0).getValue();	 
		int max = value; 
		int min = value; 

		if(root.isLeaf()){
			//call evaluate function 
			evalBoard(); 
		}
		for(int i = 1; i < root_children.size(); ++i){

			if(value > max){
				max = value; 
				index_max = i; 
			}
			if(value < min){
				min = value; 
				index_min = i; 
			}
		}

		if(root.isMax()){
			//return move
			pieceMove bestMove = new pieceMove(root_children.get(index_max).x, root_children.get(index_max).y, root_children.get(index_max).direction, root_children.get(index_max).color);
			return movePiece(bestMove.destRow, bestMove.destColumn); 
		}
		if(root.isMin()){
			//return move 
			pieceMove bestMove = new pieceMove(root_children.get(index_min).x, root_children.get(index_min).y, root_children.get(index_min).direction, root_children.get(index_min).color);
			return movePiece(bestMove.destRow, bestMove.destColumn); 
		}


	}

}

public class Node{ 
	
	private int value; 
	private int depth; 
	public ArrayList<Node> children;

	//Holds each piecemove values 
	private int x;
	private int y;
	private String direction; 
	private int color; //color in pieceMove is private, need to change 
	
	

	//Constructor 
	public Node(){
		super();
	}

	public Node(int _value){ 
		value = _value; 
	}

	public Node(int _value, int _depth){
		value = _value; 
		depth = _depth; 
	}

	public int getValue(){
		return value; 
	}

	public int setValue(int _value){
		value = _value; 
	}

	//Adds child to the list of children 
	public void addChild(Node child){
		if(children == null){
			children = new ArrayList<Node>(); 
		}
		children.add(child);
	}

	public void setChildren(ArrayList<Node> children){
		this.children = children; 
	}

	//Return the children of Node<T>
	public ArrayList<Node> getChildren(){
		if(this.children == null){
			return new ArrayList<Node>(); 
		}
		else
			return children; 
	}

	public boolean isMax(){
		if(this.depth%2 == 0){
			return true; 
		}
	}
	public boolean isMin(){
		if(this.depth%2 != 0){
			return true; 
		}
	}
	public boolean isLeaf(){
		if(children.isEmpty()){
			return true;
		} 
		else 
			return false; 
	}
	public void setNodePiece(int _x, int _y, String _s, int _c){
		x = _x;
		y = _y;
		direction = _s; 
		color = _c; 
	}
}
