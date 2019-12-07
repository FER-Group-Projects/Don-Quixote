package hr.fer.zemris.projekt.algorithm;

import hr.fer.zemris.projekt.algorithm.differential.crossover.ExponentialDifferentialCrossover;
import hr.fer.zemris.projekt.algorithm.differential.mutation.RandomDifferentialMutation;
import hr.fer.zemris.projekt.algorithm.differential.selection.SelectBetterDifferentialSelection;
import hr.fer.zemris.projekt.algorithm.fitness.RastriginFitnessFunction;
import hr.fer.zemris.projekt.algorithm.initializer.PopulationInitializer;
import hr.fer.zemris.projekt.algorithm.solution.DoubleArraySolution;
import hr.fer.zemris.projekt.algorithm.solution.Solution;

import java.util.Random;

class DifferentialEvolutionTest {

    public static void main(String[] args) {
        OptimizationAlgorithm<Solution<Double>> algorithm =
                new DifferentialEvolution(100, 100000, 0, new ExponentialDifferentialCrossover(0.9),
                        new RandomDifferentialMutation(2), new SelectBetterDifferentialSelection<>(),
                        new RastriginFitnessFunction(100), new RandomPopulationInitializer(100));

        Solution<Double> solution = algorithm.run();

        System.out.println(solution.toString());
    }

    private static class RandomPopulationInitializer implements PopulationInitializer<Solution<Double>> {

        private final int numberOfDimensions;

        private final Random random = new Random();

        private RandomPopulationInitializer(int numberOfDimensions) {
            this.numberOfDimensions = numberOfDimensions;
        }

        @Override
        public Solution<Double> generateSolution() {
            double[] solution = new double[numberOfDimensions];

            for (int dimensionIndex = 0; dimensionIndex < numberOfDimensions; dimensionIndex++) {
                solution[dimensionIndex] = (random.nextDouble() - 0.5) * 10;
            }

            return new DoubleArraySolution(solution);
        }

    }
}