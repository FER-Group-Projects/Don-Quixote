package hr.fer.zemris.projekt.algorithm.tree_based_GP_implementation;

import java.util.Random;

import hr.fer.zemris.projekt.algorithm.crossover.Crossover;

public class TreeCrossover implements Crossover<Tree>{
	
	//moteoda odabira random cvor u drugom roditelju i kopira ga na random mjesto u drugom
	@Override
	public Tree crossover(Tree firstParent, Tree secondParent) {

		RandomNodes rnodes = new RandomNodes(secondParent.head);
		rnodes.getRandomBranch(secondParent.head);
		Node copy = firstParent.head.copy();
		rnodes.setRandomBranch(copy, rnodes.gennode);
		return new Tree(copy);
	
		
	}
	
	
	

}
