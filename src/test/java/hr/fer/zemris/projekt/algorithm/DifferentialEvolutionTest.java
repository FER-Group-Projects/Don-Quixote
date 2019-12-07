package hr.fer.zemris.projekt.algorithm;

import hr.fer.zemris.projekt.algorithm.differential.crossover.ExponentialDifferentialCrossover;
import hr.fer.zemris.projekt.algorithm.differential.mutation.RandomDifferentialMutation;
import hr.fer.zemris.projekt.algorithm.differential.selection.SelectBetterDifferentialSelection;
import hr.fer.zemris.projekt.algorithm.fitness.RastriginFitnessFunction;
import hr.fer.zemris.projekt.algorithm.initializer.RandomPopulationInitializer;
import hr.fer.zemris.projekt.algorithm.solution.Solution;

class DifferentialEvolutionTest {

    public static void main(String[] args) {
        OptimizationAlgorithm<Solution<Double>> algorithm =
                new DifferentialEvolution(100, 100000, 0, new ExponentialDifferentialCrossover(0.9),
                        new RandomDifferentialMutation(2), new SelectBetterDifferentialSelection<>(),
                        new RastriginFitnessFunction(100), new RandomPopulationInitializer(100, -5, 5));

        Solution<Double> solution = algorithm.run();

        System.out.println(solution.toString());
    }

}