package hr.fer.zemris.projekt.algorithm.differential.crossover;

import hr.fer.zemris.projekt.algorithm.solution.Solution;

import java.util.Random;

public class ExponentialDifferentialCrossover implements DifferentialCrossover<Solution<Double>> {

    private final Random random = new Random();

    private final double probabilityToCopyNextComponent;

    public ExponentialDifferentialCrossover(double probabilityToCopyNextComponent) {
        this.probabilityToCopyNextComponent = probabilityToCopyNextComponent;
    }

    @Override
    public Solution<Double> crossover(Solution<Double> targetVector, Solution<Double> mutantVector) {
        Solution<Double> trialVector = targetVector.copy();
        int startIndex = random.nextInt(mutantVector.getNumberOfGenes());
        int currentIndex = startIndex;

        do {
            trialVector.setGeneAt(currentIndex, mutantVector.getGeneAt(currentIndex));

            currentIndex = (currentIndex + 1) % mutantVector.getNumberOfGenes();

            // We ended up looping around, might as well break
            if (currentIndex == startIndex) {
                break;
            }
        } while (random.nextDouble() < probabilityToCopyNextComponent);

        return trialVector;
    }

}
