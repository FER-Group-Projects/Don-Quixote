package hr.fer.zemris.projekt.algorithm.differential.selection;

import hr.fer.zemris.projekt.algorithm.solution.Solution;

public class SelectBetterDifferentialSelection<S extends Solution<?>> implements DifferentialSelection<S> {

    @Override
    public S select(S targetVector, S trialVector) {
        return trialVector.getFitness() >= targetVector.getFitness() ? trialVector : targetVector;
    }

}
