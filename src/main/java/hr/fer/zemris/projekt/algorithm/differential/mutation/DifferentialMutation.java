package hr.fer.zemris.projekt.algorithm.differential.mutation;

import hr.fer.zemris.projekt.algorithm.solution.Solution;

import java.util.List;

public interface DifferentialMutation<S extends Solution<?>> {

    List<S> createMutantPopulation(List<S> population);

}
