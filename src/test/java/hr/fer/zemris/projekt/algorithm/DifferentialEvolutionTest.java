package hr.fer.zemris.projekt.algorithm;

import hr.fer.zemris.projekt.algorithm.differential.crossover.BinomialDifferentialCrossover;
import hr.fer.zemris.projekt.algorithm.differential.mutation.RandomDifferentialMutation;
import hr.fer.zemris.projekt.algorithm.differential.selection.SelectBetterDifferentialSelection;
import hr.fer.zemris.projekt.algorithm.fitness.RastriginFitnessFunction;
import hr.fer.zemris.projekt.algorithm.initializer.RandomPopulationInitializer;
import hr.fer.zemris.projekt.algorithm.solution.Solution;

class DifferentialEvolutionTest {

    public static void main(String[] args) {
        OptimizationAlgorithm<Solution<Double>> algorithm =
                new DifferentialEvolution(100, 100000, 0, new BinomialDifferentialCrossover(0.1),
                        new RandomDifferentialMutation(0.2, 1), new SelectBetterDifferentialSelection<>(),
                        new RastriginFitnessFunction(100), new RandomPopulationInitializer(100, -5, 5));

        Solution<Double> solution = algorithm.run();

        System.out.println(solution.toString());
    }

}