package hr.fer.zemris.projekt.algorithm.genetic.crossover;

import hr.fer.zemris.projekt.algorithm.solution.Solution;

public interface GeneticCrossover<S extends Solution<?>> {

    S crossover(S firstParent, S secondParent);

}
