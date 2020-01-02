package hr.fer.zemris.projekt.algorithm.genetic.selection;

import hr.fer.zemris.projekt.algorithm.solution.Solution;

import java.util.Collection;

public interface GeneticSelection<S extends Solution<?>> {

    S selectFromPopulation(Collection<? extends S> population);

}
