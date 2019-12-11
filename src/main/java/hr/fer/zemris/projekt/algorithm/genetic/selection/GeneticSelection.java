package hr.fer.zemris.projekt.algorithm.genetic.selection;

import java.util.Collection;

import hr.fer.zemris.projekt.algorithm.solution.Solution;

public interface GeneticSelection<S extends Solution<?>> {
	
	S selectFromPopulation(Collection<? extends S> population);

}
