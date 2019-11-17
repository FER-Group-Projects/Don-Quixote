package hr.fer.zemris.projekt.algorithm.crossover;

import hr.fer.zemris.projekt.algorithm.solution.Solution;

public interface Crossover<S extends Solution<?>> {

    S crossover(S firstParent, S secondParent);

}
