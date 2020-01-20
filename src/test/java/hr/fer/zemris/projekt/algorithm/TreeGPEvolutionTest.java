package hr.fer.zemris.projekt.algorithm;

import java.nio.file.Paths;
import java.util.List;

import hr.fer.zemris.projekt.algorithm.fitness.GameFitnessFunction;
import hr.fer.zemris.projekt.algorithm.fitness.TreeGPFitnessFunction;
import hr.fer.zemris.projekt.algorithm.tree_based_GP_implementation.Tree;
import hr.fer.zemris.projekt.algorithm.tree_based_GP_implementation.TreeAlgorithm;
import hr.fer.zemris.projekt.model.input.impl.RayColliderInputExtractor;
import hr.fer.zemris.projekt.model.scenes.ClimbingBarrelScene;
import hr.fer.zemris.projekt.model.scenes.ClimbingScene;
import hr.fer.zemris.projekt.model.serialization.JavaArtificialPlayerSerializer;
import hr.fer.zemris.projekt.model.serialization.SerializationException;

public class TreeGPEvolutionTest {
	
	public static void main(String[] args) {
	
		GameFitnessFunction<Tree> fitnessFunction = new TreeGPFitnessFunction(List.of(
				new ClimbingScene(60, 1000, 1, 100, 100, 300, 75, 75, 25, 50, 420, 20, 35),
				new ClimbingBarrelScene(60, 1000, 1, 100, 100, 300, 75, 75, 25, 50, 420, 20, 35, 20, 20)
                	),
                	new RayColliderInputExtractor(4));
	
		OptimizationAlgorithm<Tree> algorithm = new TreeAlgorithm(3000, 269722, 0.2, 0.01, fitnessFunction);
	
		Tree solution = algorithm.run();

		try {
			new JavaArtificialPlayerSerializer().serialize(Paths.get("player.ff"), fitnessFunction.initializeArtificialPlayer(solution));
		} catch (SerializationException e) {
			e.printStackTrace();
		}

		try {
			Thread.sleep(3_000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		ArtificialPlayerTest.show(fitnessFunction.initializeArtificialPlayer(solution));
	}
}
