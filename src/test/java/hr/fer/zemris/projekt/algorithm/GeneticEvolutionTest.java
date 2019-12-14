package hr.fer.zemris.projekt.algorithm;

import hr.fer.zemris.projekt.algorithm.fitness.RastriginFitnessFunction;
import hr.fer.zemris.projekt.algorithm.fitness.SimplePolynomialFitnessFunction;
import hr.fer.zemris.projekt.algorithm.genetic.crossover.SinglePointCrossover;
import hr.fer.zemris.projekt.algorithm.genetic.mutation.GaussMutation;
import hr.fer.zemris.projekt.algorithm.genetic.selection.TournamentSelection;
import hr.fer.zemris.projekt.algorithm.initializer.RandomPopulationInitializer;
import hr.fer.zemris.projekt.algorithm.solution.Solution;

public class GeneticEvolutionTest {

	public static void main(String[] args) {
		OptimizationAlgorithm<Solution<Double>> algorithm =
                new GeneticAlgorithm(100, 1000, 0, new SinglePointCrossover(),
                        new GaussMutation(), new TournamentSelection<>(),
                        new SimplePolynomialFitnessFunction(), new RandomPopulationInitializer(2, -5, 5));

        Solution<Double> solution = algorithm.run();

        System.out.println(solution.toString());


	}

}
