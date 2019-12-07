package hr.fer.zemris.projekt.algorithm.initializer;

import hr.fer.zemris.projekt.algorithm.solution.DoubleArraySolution;
import hr.fer.zemris.projekt.algorithm.solution.Solution;

import java.util.Random;

public class RandomPopulationInitializer implements PopulationInitializer<Solution<Double>> {

    private final int numberOfDimensions;

    private final double minimumValue;
    private final double maximumValue;

    private final Random random = new Random();

    public RandomPopulationInitializer(int numberOfDimensions, double minimumValue, double maximumValue) {
        if (numberOfDimensions <= 0) {
            throw new IllegalArgumentException("Number of dimensions cannot be less than or equal to 0.");
        }
        if (minimumValue > maximumValue) {
            throw new IllegalArgumentException("Minimum value cannot be larger than the maximum value.");
        }

        this.numberOfDimensions = numberOfDimensions;
        this.minimumValue = minimumValue;
        this.maximumValue = maximumValue;
    }

    @Override
    public Solution<Double> generateSolution() {
        double[] solution = new double[numberOfDimensions];

        for (int dimensionIndex = 0; dimensionIndex < numberOfDimensions; dimensionIndex++) {
            solution[dimensionIndex] = minimumValue + (maximumValue - minimumValue) * random.nextDouble();
        }

        return new DoubleArraySolution(solution);
    }

}
