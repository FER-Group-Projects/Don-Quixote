package hr.fer.zemris.projekt.algorithm.genetic.crossover;

import hr.fer.zemris.projekt.algorithm.solution.Solution;

import java.util.Random;

// http://www.tomaszgwiazda.com/blendX.htm
public class BlendCrossover<S extends Solution<Double>> implements GeneticCrossover<S> {

    private final Random random = new Random();

    private final double alpha;

    public BlendCrossover(double alpha) {
        this.alpha = alpha;
    }

    @Override
    public S crossover(S firstParent, S secondParent) {
        S child = (S) firstParent.copy();

        for (int dimensionIndex = 0; dimensionIndex < child.getNumberOfGenes(); dimensionIndex++) {
            double difference = Math.abs(firstParent.getGeneAt(dimensionIndex) - secondParent.getGeneAt(dimensionIndex));

            double minimum = Math.min(firstParent.getGeneAt(dimensionIndex), secondParent.getGeneAt(dimensionIndex)) - alpha * difference;
            double maximum = Math.max(firstParent.getGeneAt(dimensionIndex), secondParent.getGeneAt(dimensionIndex)) + alpha * difference;

            double dimension = random.nextDouble() * (maximum - minimum) + minimum;

            child.setGeneAt(dimensionIndex, dimension);
        }

        return child;
    }

}
