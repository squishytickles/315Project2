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
	
	public void BuildMiniMaxTree(Node rt, int depth){
		root = rt; 
		Node child; 
		if(depth >= 0){
			for(int = 0; i < /*size of move list*/; ++i){
				//place all move values within the tree 
				child = new Node(/*move list value*/, depth-1);
				children.add(child);
			}
		}
	}
	//MiniMax Algorithm 
	public void getBestMove(){

		ArarayList<Node> root_children = root.getChildren();

	
		int index_max = 0; 
		int index_min = 0; 
		int value = root_children.get(0).getValue();	 
		int max = value; 
		int min = value; 

		if(root.isLeaf()){
			//call evaluate function 
		}
		for(int i = 0; i < root_children.size(); ++i){

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
			//return root_children.get(index_max)
		}
		if(root.isMin()){
			//return move 
			//return root_children.get(index_min)
		}


	}

}

public class Node{ 
	
	private int value; 
	private int depth; 
	public ArrayList<Node> children;
	

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
}
