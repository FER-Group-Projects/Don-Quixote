package hr.fer.zemris.projekt.algorithm.differential.selection;

import hr.fer.zemris.projekt.algorithm.solution.Solution;

public interface DifferentialSelection<S extends Solution<?>> {

    S select(S targetVector, S trialVector);

}
