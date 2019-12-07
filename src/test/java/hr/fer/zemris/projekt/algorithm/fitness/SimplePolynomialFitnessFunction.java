package hr.fer.zemris.projekt.algorithm.fitness;

import hr.fer.zemris.projekt.algorithm.solution.Solution;

// Maximum is f(10, 30) = 0.
//
// Useful for testing basic convergence.
public class SimplePolynomialFitnessFunction implements FitnessFunction<Solution<Double>> {

    @Override
    public double calculateFitness(Solution<Double> solution) {
        if (solution.getNumberOfGenes() != 2) {
            throw new IllegalArgumentException("Wrong number of genes, expected 2, got " + solution.getNumberOfGenes());
        }

        double x1 = solution.getGeneAt(0);
        double x2 = solution.getGeneAt(1);

        return -((x1 - 10) * (x1 - 10) + (x2 - 30) * (x2 - 30));
    }

}