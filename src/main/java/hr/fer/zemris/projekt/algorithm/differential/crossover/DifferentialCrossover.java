package hr.fer.zemris.projekt.algorithm.differential.crossover;

import hr.fer.zemris.projekt.algorithm.solution.Solution;

public interface DifferentialCrossover<S extends Solution<?>> {

    S crossover(S targetVector, S mutantVector);

}
