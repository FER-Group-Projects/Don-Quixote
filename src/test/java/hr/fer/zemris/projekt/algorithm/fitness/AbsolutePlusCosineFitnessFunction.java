package hr.fer.zemris.projekt.algorithm.fitness;

import hr.fer.zemris.projekt.algorithm.solution.Solution;

import static java.lang.Math.abs;
import static java.lang.Math.cos;

// Maximum is f(0) = 0.
//
// Useful for testing basic convergence with a lot of local maxima.
public class AbsolutePlusCosineFitnessFunction implements FitnessFunction<Solution<Double>> {

    @Override
    public double calculateFitness(Solution<Double> solution) {
        if (solution.getNumberOfGenes() != 1) {
            throw new IllegalArgumentException("Wrong number of genes, expected 1, got " + solution.getNumberOfGenes());
        }

        double x = solution.getGeneAt(0);

        return -(abs(x) - 10 * cos(x) + 10);
    }

}