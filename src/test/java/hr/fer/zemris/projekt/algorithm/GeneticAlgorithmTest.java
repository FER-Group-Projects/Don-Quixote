package hr.fer.zemris.projekt.algorithm;

import hr.fer.zemris.projekt.algorithm.fitness.ElmanFitnessFunction;
import hr.fer.zemris.projekt.algorithm.fitness.GameFitnessFunction;
import hr.fer.zemris.projekt.algorithm.fitness.RastriginFitnessFunction;
import hr.fer.zemris.projekt.algorithm.fitness.SimplePolynomialFitnessFunction;
import hr.fer.zemris.projekt.algorithm.genetic.crossover.BlendCrossover;
import hr.fer.zemris.projekt.algorithm.genetic.crossover.SinglePointCrossover;
import hr.fer.zemris.projekt.algorithm.genetic.mutation.GaussMutation;
import hr.fer.zemris.projekt.algorithm.genetic.selection.TournamentSelection;
import hr.fer.zemris.projekt.algorithm.initializer.RandomPopulationInitializer;
import hr.fer.zemris.projekt.algorithm.solution.Solution;
import hr.fer.zemris.projekt.model.controller.PlayerAction;
import hr.fer.zemris.projekt.model.input.impl.RayColliderInputExtractor;
import hr.fer.zemris.projekt.model.scenes.ClimbingScene;

import java.util.List;

public class GeneticAlgorithmTest {

	public static void main(String[] args) {
		GameFitnessFunction<Solution<Double>> fitnessFunction = new ElmanFitnessFunction(
				List.of(
						new ClimbingScene(60, 1000, 0.5, 100, 100, 300, 75, 75, 25, 50, 420, 20, 35)
				),
				new RayColliderInputExtractor(4), new int[]{8, 5, 5, PlayerAction.values().length});

		OptimizationAlgorithm<Solution<Double>> algorithm =
                new GeneticAlgorithm(100, 100000, 269722, new BlendCrossover<>(0.5),
                        new GaussMutation(0.1), new TournamentSelection<>(5),
                        fitnessFunction, new RandomPopulationInitializer(14 * 5 + 6 * 5 + 6 * 7 + 5, -1, 1));

		Solution<Double> solution = algorithm.run();

		System.out.println(solution.toString());
		System.out.println(fitnessFunction.calculateFitness(solution));
		try {
			Thread.sleep(3_000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		ArtificialPlayerTest.show(fitnessFunction.initializeArtificialPlayer(solution));
	}

}
