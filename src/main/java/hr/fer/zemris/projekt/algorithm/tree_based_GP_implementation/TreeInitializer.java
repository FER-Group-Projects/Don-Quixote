package hr.fer.zemris.projekt.algorithm.tree_based_GP_implementation;

import java.util.Random;

import hr.fer.zemris.projekt.algorithm.initializer.PopulationInitializer;

public class TreeInitializer implements PopulationInitializer<Tree>{
	private Random r = new Random();
	private int depth =1;//dubina generiranja(nije egzaktno)
							//za veci broj generalno daje veca stabla
	private double terminalChance = 0.5;
	@Override
	public Tree generateSolution() {
		
		Node head = new Node(Tree.selectNonTerminal());
		head.setLeft(generateTree(depth));
		head.setRight(generateTree(depth));
		lTree(head);
		
		return new Tree(head);
	}
	
	private Node generateTree(int depth) {
			
		if (depth < 0) return null;
		
		Node current = r.nextDouble() > terminalChance	? new Node(Tree.selectNonTerminal())	: new Node(Tree.selectTerminal());
		
		if ((depth > 0) && !current.isTerminal()) {
			current.setLeft(generateTree(depth-1));
			current.setRight(generateTree(depth-1));
		}
		return current;	
	}
	
	//metoda koja popunjava prazne non-terminal cvorove
	private static void	lTree(Node head) {
		if ((head.getLeft() == null || head.getRight() == null)	&&	!head.isTerminal()) {
			head.setLeft(new Node(Tree.selectTerminal())); 
			head.setRight(new Node(Tree.selectTerminal()));
			return;
		}
		else {
			if (head.getLeft()!=null) lTree(head.getLeft());
			if (head.getRight()!=null)	lTree(head.getRight());		
		}	
	}		
}


