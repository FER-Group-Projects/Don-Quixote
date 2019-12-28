package hr.fer.zemris.projekt.algorithm;

import hr.fer.zemris.projekt.algorithm.differential.crossover.BinomialDifferentialCrossover;
import hr.fer.zemris.projekt.algorithm.differential.mutation.RandomDifferentialMutation;
import hr.fer.zemris.projekt.algorithm.differential.selection.SelectBetterDifferentialSelection;
import hr.fer.zemris.projekt.algorithm.fitness.ElmanFitnessFunction;
import hr.fer.zemris.projekt.algorithm.fitness.GameFitnessFunction;
import hr.fer.zemris.projekt.algorithm.initializer.RandomPopulationInitializer;
import hr.fer.zemris.projekt.algorithm.solution.Solution;
import hr.fer.zemris.projekt.model.controller.PlayerAction;
import hr.fer.zemris.projekt.model.input.impl.RayColliderInputExtractor;
import hr.fer.zemris.projekt.model.scenes.ClimbingScene;

import java.util.List;
import java.util.Scanner;

class DifferentialEvolutionTest {

    public static void main(String[] args) {
        GameFitnessFunction<Solution<Double>> fitnessFunction = new ElmanFitnessFunction(
                List.of(
                        new ClimbingScene(60, 1000, 0.5, 100, 100, 300, 75, 75, 25, 50, 420, 20, 35),
                        new ClimbingScene(60, 1000, 0.5, 100, 100, 300, 75, 75, 25, 50, 420, 20, 35),
                        new ClimbingScene(60, 1000, 0.5, 100, 100, 300, 75, 75, 25, 50, 420, 20, 35)
                ),
                new RayColliderInputExtractor(4), new int[]{8, 5, PlayerAction.values().length});

        OptimizationAlgorithm<Solution<Double>> algorithm =
                new DifferentialEvolution(100, 100000, 269722, new BinomialDifferentialCrossover(0.1),
                        new RandomDifferentialMutation(0.8, 1), new SelectBetterDifferentialSelection<>(),
                        fitnessFunction, new RandomPopulationInitializer(14 * 5 + 6 * 7 + 5, -1, 1));

        Solution<Double> solution = algorithm.run();

        System.out.println(solution.toString());
        System.out.println(fitnessFunction.calculateFitness(solution));
        try {
            Thread.sleep(3_000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Waiting for user input...");
        new Scanner(System.in).nextLine();

        ArtificialPlayerTest.show(fitnessFunction.initializeArtificialPlayer(solution));
//        ArtificialPlayerTest.show(null);
    }

}
