package hr.fer.zemris.projekt.algorithm.tree_based_GP_implementation;

import java.util.Random;

import hr.fer.zemris.projekt.algorithm.solution.DoubleArraySolution;
import hr.fer.zemris.projekt.algorithm.solution.Solution;

public class Tree implements Solution<Double>{
	//nonterminal cvorovi su operacije
	//terminal cvorovi su indexi u listi inputa
	
	private int size; 
	public static String[] nonTerminal = {"+","-","*"};
	public static int nonTerminalLength = nonTerminal.length;
	public static int terminalLength = 4; //duljina inputa 
	public Node head;
	private double fitness;
	private boolean isEvaluated;
	
	public Tree() { 
	}
	
	public Tree(Node head) {		
		this.head = head;
	}
	
	public static int size(Node head) {
		if (head == null) return 0;
		else {
			return (size(head.getLeft()) + 1 + size(head.getRight()));
		}	
	}
	
	public static int nodeSize(Node head) {
		if (head.isTerminal()) return 0;
		else {
			return (nodeSize(head.getLeft()) + 1 + nodeSize(head.getRight()));
		}
	}
	
	//odabire random nonTerminal znak 
	public static String selectNonTerminal() {
		int index = new Random().nextInt(nonTerminalLength);
		return (nonTerminal[index]);
	}
	
	public static String selectTerminal() {
		return (String.valueOf(new Random().nextInt(terminalLength)));
	}
	
	public int getSize() {
		return this.size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	
	//ispisuje stablo 
	@Override 
	public String toString() {
		Node p = this.head;
		return(p.toString());				
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
	public Double getGeneAt(int index) {
		throw new UnsupportedOperationException("no genes in tree gp");
		
	}

	@Override
	public void setGeneAt(int index, Double newValue) {
		throw new UnsupportedOperationException("no genes in tree gp");
	}

	@Override
	public Solution<Double> copy() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
}
