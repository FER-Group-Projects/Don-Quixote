package hr.fer.zemris.projekt.algorithm.fitness;

import hr.fer.zemris.projekt.algorithm.solution.Solution;

import static java.lang.Math.PI;
import static java.lang.Math.cos;

// Maximum is f(x) = 0, for x with all variables set to zero.
// Search interval for every variable is [-5.12, 5.12].
//
// Useful for testing behaviour with a lot of variables and local optima.
//
// https://en.wikipedia.org/wiki/Rastrigin_function
public class RastriginFitnessFunction implements FitnessFunction<Solution<Double>> {

    private static final double A = 10;

    private final int numberOfDimensions;

    public RastriginFitnessFunction(int numberOfDimensions) {
        this.numberOfDimensions = numberOfDimensions;
    }

    @Override
    public double calculateFitness(Solution<Double> solution) {
        if (solution.getNumberOfGenes() != numberOfDimensions) {
            throw new IllegalArgumentException("Wrong number of genes, expected " + numberOfDimensions + ", got " + solution.getNumberOfGenes());
        }

        double fitness = -A * numberOfDimensions;

        for (int variableIndex = 0; variableIndex < numberOfDimensions; variableIndex++) {
            double variable = solution.getGeneAt(variableIndex);

            fitness -= variable * variable - A * cos(2 * PI * variable);
        }

        return fitness;
    }

}