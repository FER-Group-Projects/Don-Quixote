package hr.fer.zemris.projekt.algorithm;

import hr.fer.zemris.projekt.algorithm.fitness.*;
import hr.fer.zemris.projekt.algorithm.genetic.crossover.BlendCrossover;
import hr.fer.zemris.projekt.algorithm.genetic.mutation.GaussMutation;
import hr.fer.zemris.projekt.algorithm.genetic.selection.TournamentSelection;
import hr.fer.zemris.projekt.algorithm.initializer.RandomPopulationInitializer;
import hr.fer.zemris.projekt.algorithm.solution.Solution;
import hr.fer.zemris.projekt.model.controller.PlayerAction;
import hr.fer.zemris.projekt.model.input.impl.RayColliderInputExtractor;
import hr.fer.zemris.projekt.model.scenes.ClimbingBarrelScene;
import hr.fer.zemris.projekt.model.scenes.ClimbingScene;
import hr.fer.zemris.projekt.model.serialization.JavaArtificialPlayerSerializer;
import hr.fer.zemris.projekt.model.serialization.SerializationException;

import java.nio.file.Paths;
import java.util.List;

public class GeneticAlgorithmTest {

	public static void main(String[] args) {
		GameFitnessFunction<Solution<Double>> fitnessFunction = new FeedforwardFitnessFunction(
				List.of(
						new ClimbingScene(60, 1000, 1, 100, 100, 300, 75, 75, 25, 50, 420, 20, 35),
						new ClimbingBarrelScene(60, 1000, 1, 100, 100, 300, 75, 75, 25, 50, 420, 20, 35, 20, 20)
				),
				new RayColliderInputExtractor(4), new int[]{8, 10, PlayerAction.values().length});

		OptimizationAlgorithm<Solution<Double>> algorithm =
                new GeneticAlgorithm<Solution<Double>>(100, 100000, 269722, new BlendCrossover<>(0.5),
                        new GaussMutation(0.1), new TournamentSelection<>(5),
                        fitnessFunction, new RandomPopulationInitializer(9 * 10 + 11 * 7, -1, 1));

		Solution<Double> solution = algorithm.run();

		try {
			new JavaArtificialPlayerSerializer().serialize(Paths.get("player_ga.ff"), fitnessFunction.initializeArtificialPlayer(solution));
		} catch (SerializationException e) {
			e.printStackTrace();
		}

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
