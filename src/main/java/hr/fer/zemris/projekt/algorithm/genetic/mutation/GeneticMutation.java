package hr.fer.zemris.projekt.algorithm.genetic.mutation;

import hr.fer.zemris.projekt.algorithm.solution.Solution;

public interface GeneticMutation<S extends Solution<?>> {
	
	S mutate(S solutionToMutate);

}
