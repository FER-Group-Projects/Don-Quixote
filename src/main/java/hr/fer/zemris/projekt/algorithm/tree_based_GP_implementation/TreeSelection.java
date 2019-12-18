package hr.fer.zemris.projekt.algorithm.tree_based_GP_implementation;

import java.util.Collection;

import hr.fer.zemris.projekt.algorithm.selection.Selection;

public class TreeSelection implements Selection<Tree>{

	@Override
	public Tree selectFromPopulation(Collection<? extends Tree> population) {
		double maxFit = 0;
		for (Tree t : population) {
			if (t.getFitness() > maxFit) maxFit = t.getFitness();			
		}
		for (Tree t: population) {
			if (t.getFitness() == maxFit) return t;
			
		}
		return new Tree();
	}

}
