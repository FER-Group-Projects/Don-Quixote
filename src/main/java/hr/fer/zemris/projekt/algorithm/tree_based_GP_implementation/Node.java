package hr.fer.zemris.projekt.algorithm.tree_based_GP_implementation;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Node implements Serializable {
	
	private String value; 
	private Node left;
	private Node right;
	boolean isTerminal;
	private int level;

	public Node(String value) {
		this.value = value;
		this.level = 0;
		List<String> list = Arrays.asList(Tree.nonTerminal);
		if (list.contains(value)) this.isTerminal = false;
		else {
			this.isTerminal = true;
		}
	}
	
	public Node(String value, int level) {
		this(value);
		this.level = level;
	}
	
	public Node(String value, Node left, Node right) {
		this(value);
		this.setLeft(left);
		this.setRight(right);
	}
	
	public StringBuilder toString(StringBuilder prefix, boolean isTail, StringBuilder sb) {
	    if(this.getRight()!=null) {
	        this.getRight().toString(new StringBuilder().append(prefix).append(isTail ? "|   " : "    "), false, sb);
	    }
	    sb.append(prefix).append(isTail ? "|_ " : "|= ").append(this.getValue().toString()).append("\n");
	    if(this.getLeft()!=null) {
	        this.getLeft().toString(new StringBuilder().append(prefix).append(isTail ? "    " : "|   "), true, sb);
	    }
	    return sb;
	}
	
	//ispisuje stablo uzimajuci da je node glava
	@Override
	public String toString() {
	    return this.toString(new StringBuilder(), true, new StringBuilder()).toString();
	}
	
	public Node copy() {
        Node left = null;
        Node right = null;
        if (this.getLeft() != null) {
            left = this.getLeft().copy();
        }
        if (this.getRight() != null) {
            right = this.getRight().copy();
        }
        return new Node(this.getValue(), left, right);
    }
	
	public String getValue() { 
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Node getLeft() {
		return left;
	}

	public void setLeft(Node left) {
		this.left = left;
	}

	public Node getRight() {
		return right;
	}

	public void setRight(Node right) {
		this.right = right;
	}

	public boolean isTerminal() {
		return isTerminal;
	}

	public void setTerminal(boolean isTerminal) {
		this.isTerminal = isTerminal;
	}
	
	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}
	
}
