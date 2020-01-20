package hr.fer.zemris.projekt.algorithm;

import hr.fer.zemris.projekt.algorithm.DifferentialEvolution;
import hr.fer.zemris.projekt.algorithm.OptimizationAlgorithm;
import hr.fer.zemris.projekt.algorithm.differential.crossover.BinomialDifferentialCrossover;
import hr.fer.zemris.projekt.algorithm.differential.mutation.RandomDifferentialMutation;
import hr.fer.zemris.projekt.algorithm.differential.mutation.TargetToBestDifferentialMutation;
import hr.fer.zemris.projekt.algorithm.differential.selection.SelectBetterDifferentialSelection;
import hr.fer.zemris.projekt.algorithm.fitness.FeedforwardFitnessFunction;
import hr.fer.zemris.projekt.algorithm.fitness.GameFitnessFunction;
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

public class FFDifferentialEvolutionTest {

    public static void main(String[] args){
        GameFitnessFunction<Solution<Double>> fitnessFunction = new FeedforwardFitnessFunction(
                List.of(
                        new ClimbingScene(60, 1000, 1, 100, 100, 300, 75, 75, 25, 50, 420, 20, 35),
                        new ClimbingBarrelScene(60, 1000, 1, 100, 100, 300, 75, 75, 25, 50, 420, 20, 35, 20, 20)
                ),
                new RayColliderInputExtractor(4), new int[]{8, 10, PlayerAction.values().length});

        OptimizationAlgorithm<Solution<Double>> algorithm =
                new DifferentialEvolution(100, 100000, 215000, new BinomialDifferentialCrossover(0.1),
                        new TargetToBestDifferentialMutation(0.8, 4), new SelectBetterDifferentialSelection<>(),
                        fitnessFunction, new RandomPopulationInitializer(9 * 10 + 11 * 7, -5, 5));

        Solution<Double> solution = algorithm.run();

        try {
            new JavaArtificialPlayerSerializer().serialize(Paths.get("player.ff"), fitnessFunction.initializeArtificialPlayer(solution));
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
//        ArtificialPlayerTest.show(null);
    }

}
