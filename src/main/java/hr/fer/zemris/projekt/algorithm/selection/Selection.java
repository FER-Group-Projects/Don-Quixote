package hr.fer.zemris.projekt.algorithm.selection;

import hr.fer.zemris.projekt.algorithm.solution.Solution;

import java.util.Collection;

public interface Selection<S extends Solution<?>> {

    S selectFromPopulation(Collection<? extends S> population);

}
