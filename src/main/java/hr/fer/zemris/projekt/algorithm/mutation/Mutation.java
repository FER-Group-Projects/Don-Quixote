package hr.fer.zemris.projekt.algorithm.mutation;

import hr.fer.zemris.projekt.algorithm.solution.Solution;

public interface Mutation<S extends Solution<?>> {

    S mutate(S solutionToMutate);

}
