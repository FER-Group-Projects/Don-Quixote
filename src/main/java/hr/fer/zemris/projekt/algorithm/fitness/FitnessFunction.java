package hr.fer.zemris.projekt.algorithm.fitness;

import hr.fer.zemris.projekt.algorithm.solution.Solution;

public interface FitnessFunction<S extends Solution<?>> {

    double calculateFitness(S solution);

}
