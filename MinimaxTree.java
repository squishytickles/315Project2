import java.awt.*;
import java.awt.event.ActionEvent; 
import java.awt.event.ActionListener; 
import java.util.*;

public class MinimaxTree extends game{

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
	public void BuildMiniMaxTree(Node r, int depth){ 

		root = r; 
		//place all move values within the tree 
		for(int i = 0; i < validMoves.size(); ++i){
			Node n = mainTree.new Node();
			n.setNodePiece(validMoves.get(i).sourceRow, validMoves.get(i).sourceColumn, validMoves.get(i).destRow, validMoves.get(i).destColumn, validMoves.get(i).direction, validMoves.get(i).color);
			//n.value = set_score(validMoves.get(i));
			//System.out.println("SCORE: " + n.value);
			r.addChild(n);			
		}
		
		for(int i = 0; i < r.children.size(); ++i) {
			//BuildMiniMaxTree(r.children.get(i),depth+1);
		}
	}
	
	//MiniMax Algorithm 
	public pieceMove getBestMove(){

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
			pieceMove bestMove = new pieceMove(root_children.get(index_max).x, root_children.get(index_max).y, root_children.get(index_max).x2, root_children.get(index_max).y2, root_children.get(index_max).direction, root_children.get(index_max).color);
			return bestMove;
			//return movePiece(bestMove.destRow, bestMove.destColumn); 
		}
		if(root.isMin()){
			//return move 
			pieceMove bestMove = new pieceMove(root_children.get(index_min).x, root_children.get(index_min).y, root_children.get(index_max).x2, root_children.get(index_max).y2, root_children.get(index_min).direction, root_children.get(index_min).color);
			return bestMove;
			//return movePiece(bestMove.destRow, bestMove.destColumn); 
		}

		return new pieceMove(0,0,0,0,"none",0);
	}

	public class Node { 

		private int value; 
		private int depth; 
		public ArrayList<Node> children;

		//Holds each piecemove values 
		public int x, x2;
		public int y, y2;
		public String direction; 
		private int color; //color in pieceMove is private, need to change 
		
		//Constructor 
		public Node(){
			value = 0;
			depth = 0;
			//super();
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
	
		public void setValue(int _value){
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
			} else 
				return false;
		}
		public boolean isMin(){
			if(this.depth%2 != 0){
				return true; 
			} else 
				return false;
		}
		public boolean isLeaf(){
			if(children.isEmpty()){
				return true;
			} else 
				return false; 
		}
		public void setNodePiece(int _x, int _y, int _x2, int _y2, String _s, int _c){
			x = _x;
			y = _y;
			x2 = _x2;
			y2 = _y2;
			direction = _s; 
			color = _c; 
		}
	}
}
