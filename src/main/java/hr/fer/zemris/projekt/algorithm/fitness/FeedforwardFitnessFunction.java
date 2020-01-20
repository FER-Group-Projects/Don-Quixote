package hr.fer.zemris.projekt.algorithm.fitness;

import hr.fer.zemris.projekt.algorithm.player.ArtificialPlayer;
import hr.fer.zemris.projekt.algorithm.solution.DoubleArraySolution;
import hr.fer.zemris.projekt.algorithm.solution.Solution;
import hr.fer.zemris.projekt.feedforwardNeuralNet.NeuralNetwork;
import hr.fer.zemris.projekt.model.ElmanNetwork.ElmanNeuralNetwork;
import hr.fer.zemris.projekt.model.controller.PlayerAction;
import hr.fer.zemris.projekt.model.input.GameInputExtractor;
import hr.fer.zemris.projekt.model.scenes.SceneGenerator;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

public class FeedforwardFitnessFunction extends GameFitnessFunction<Solution<Double>> implements Serializable {

    private final int[] layout;

    public FeedforwardFitnessFunction(List<SceneGenerator> sceneGenerators, GameInputExtractor gameInputExtractor, int[] layout){
        super(sceneGenerators, gameInputExtractor);
        this.layout = Arrays.copyOf(layout, layout.length);
    }

    @Override
    public ArtificialPlayer initializeArtificialPlayer(Solution<Double> solution){
        return new FeedforwardNNPlayer(layout, ((DoubleArraySolution) solution).toArray());
    }

    public static class FeedforwardNNPlayer implements ArtificialPlayer {
        private NeuralNetwork neuralNetwork;
        private double[] weights;

        public FeedforwardNNPlayer(int[] layout, double[] weights) {
            neuralNetwork = new NeuralNetwork(layout);
            this.weights = weights;
        }

        @Override
        public PlayerAction calculateAction(double[] input) {
            double[] output = neuralNetwork.calculateOutput(input, weights);

            int maximumIndex = 0;

            for (int index = 0; index < output.length; index++) {
                if (output[index] > output[maximumIndex]) {
                    maximumIndex = index;
                }
            }

            return PlayerAction.values()[maximumIndex];
        }

    }
}
