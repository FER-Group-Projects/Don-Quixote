package hr.fer.zemris.projekt.algorithm.differential.crossover;

import hr.fer.zemris.projekt.algorithm.solution.Solution;

import java.util.Random;

public class BinomialDifferentialCrossover implements DifferentialCrossover<Solution<Double>> {

    private final Random random = new Random();

    private final double probabilityToCopyMutantComponent;

    public BinomialDifferentialCrossover(double probabilityToCopyMutantComponent) {
        this.probabilityToCopyMutantComponent = probabilityToCopyMutantComponent;
    }

    @Override
    public Solution<Double> crossover(Solution<Double> targetVector, Solution<Double> mutantVector) {
        Solution<Double> trialVector = targetVector.copy();
        int copyIndex = random.nextInt(mutantVector.getNumberOfGenes());

        for (int mutantIndex = 0; mutantIndex < mutantVector.getNumberOfGenes(); mutantIndex++) {
            if (random.nextDouble() < probabilityToCopyMutantComponent || mutantIndex == copyIndex) {
                trialVector.setGeneAt(mutantIndex, mutantVector.getGeneAt(mutantIndex));
            }
        }

        return trialVector;
    }

}
