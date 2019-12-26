package hr.fer.zemris.projekt.algorithm.fitness;

import hr.fer.zemris.projekt.algorithm.player.ArtificialPlayer;
import hr.fer.zemris.projekt.algorithm.player.ClimbNearestLadderPlayer;
import hr.fer.zemris.projekt.algorithm.solution.DoubleArraySolution;
import hr.fer.zemris.projekt.algorithm.solution.Solution;
import hr.fer.zemris.projekt.model.input.impl.RayColliderInputExtractor;
import hr.fer.zemris.projekt.model.scenes.ClimbingScene;

import java.util.List;

public class ClimbNearestLadderGameFitnessFunctionTest extends GameFitnessFunction<Solution<Double>> {

    private static final int playerWidth = 25, playerHeight = 50;

    public ClimbNearestLadderGameFitnessFunctionTest() {
        super(List.of(new ClimbingScene(60, 1000, 0.5, 100, 100, 300, 75, 75, playerWidth, playerHeight, 420, 20, 35)),
                new RayColliderInputExtractor(4));
    }

    @Override
    public ArtificialPlayer initializeArtificialPlayer(Solution<Double> solution) {
        return new ClimbNearestLadderPlayer();
    }

    public static void main(String[] args) {
        System.out.println(new ClimbNearestLadderGameFitnessFunctionTest().calculateFitness(new DoubleArraySolution(new double[0])));
    }

}