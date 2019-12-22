package hr.fer.zemris.projekt.algorithm.tree_based_GP_implementation;

import java.util.Random;

import hr.fer.zemris.projekt.algorithm.solution.DoubleArraySolution;
import hr.fer.zemris.projekt.algorithm.solution.Solution;

public class Tree implements Solution<Node>,Comparable<Tree>{
	//nonterminal cvorovi su operacije
	//terminal cvorovi su indexi u listi inputa
	
	private int size; 
	public static String[] nonTerminal = {"+","-","*","sin+","sin-","cos+","cos-","tan+","tan-","%"};
	public static int nonTerminalLength = nonTerminal.length;
	public static int terminalLength = 8; //duljina inputa 
	public Node head;
	private double fitness;
	private boolean isEvaluated;
	private static final Random rand = new Random();
	
	public Tree() { 
	}
	
	public Tree(Node head) {		
		this.head = head;
	}
	
	//broj svih cvorova
	public static int size(Node head) {
		if (head == null) return 0;
		else {
			return (size(head.getLeft()) + 1 + size(head.getRight()));
		}	
	}
	
	//broj non-T cvorova
	public static int nodeSize(Node head) {
		if (head.isTerminal()) return 0;
		else {
			return (nodeSize(head.getLeft()) + 1 + nodeSize(head.getRight()));
		}
	}
	
	//odabire random nonTerminal znak 
	public static String selectNonTerminal() {
		int index = rand.nextInt(nonTerminalLength);
		return (nonTerminal[index]);
	}
	
	public static String selectTerminal() {
		return (String.valueOf(rand.nextInt(terminalLength)));
	}
			
	//ispisuje stablo 
	@Override 
	public String toString() {
		Node p = this.head;
		return(p.toString());				
	}
	
	@Override
	public int compareTo(Tree o) {
		if (this.getFitness() < o.getFitness()) return -1;
		else if (this.getFitness() > o.getFitness()) return 1;
		else {
			return 0;
		}	
	}
	
	public int getSize() {
		return this.size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	public boolean getIsEvaluated() {
		return this.isEvaluated;
	}
	public void setIsEvaluated(boolean bool) {
		this.isEvaluated = bool;
	}

	@Override
	public double getFitness() {
		return this.fitness;
	}

	@Override
	public void setFitness(double newFitness) {
		this.fitness = newFitness;	
	}

	@Override
	public boolean isEvaluated() {
		return this.isEvaluated;
	}

	@Override
	public int getNumberOfGenes() {
		throw new UnsupportedOperationException("no genes in tree gp");
	}

	@Override
	public Node getGeneAt(int index) {
		throw new UnsupportedOperationException("no genes in tree gp");
		
	}
	
	@Override
	public void setGeneAt(int index, Node newValue) {
		throw new UnsupportedOperationException("no genes in tree gp");	
	}

	@Override
	public Solution<Node> copy() {
		return null;
	}
	

	
	
	
}
