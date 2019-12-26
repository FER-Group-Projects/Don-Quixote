package hr.fer.zemris.projekt.algorithm.fitness;

import hr.fer.zemris.projekt.algorithm.player.ArtificialPlayer;
import hr.fer.zemris.projekt.algorithm.solution.DoubleArraySolution;
import hr.fer.zemris.projekt.algorithm.solution.Solution;
import hr.fer.zemris.projekt.model.ElmanNetwork.ElmanNeuralNetwork;
import hr.fer.zemris.projekt.model.controller.PlayerAction;
import hr.fer.zemris.projekt.model.input.GameInputExtractor;
import hr.fer.zemris.projekt.model.scenes.SceneGenerator;

import java.util.Arrays;
import java.util.List;

public class ElmanFitnessFunction extends GameFitnessFunction<Solution<Double>> {

    private final int[] layout;

    public ElmanFitnessFunction(List<SceneGenerator> sceneGenerators, GameInputExtractor gameInputExtractor, int[] layout) {
        super(sceneGenerators, gameInputExtractor);

        this.layout = Arrays.copyOf(layout, layout.length);
    }

    @Override
    public ArtificialPlayer initializeArtificialPlayer(Solution<Double> solution) {
        double[] weightsAndContextLayer = ((DoubleArraySolution) solution).toArray();

        return new ArtificialPlayer() {
            ElmanNeuralNetwork elmanNeuralNetwork;
            double[] weights;

            {
                elmanNeuralNetwork = new ElmanNeuralNetwork(layout);
                elmanNeuralNetwork.setContextLayer(Arrays.copyOf(weightsAndContextLayer, elmanNeuralNetwork.getContextLayer().length));
                weights = new double[elmanNeuralNetwork.getNumberOfWeights()];

                System.arraycopy(weightsAndContextLayer, elmanNeuralNetwork.getContextLayer().length, weights, 0, weights.length);
            }

            @Override
            public PlayerAction calculateAction(double[] input) {
                double[] output = elmanNeuralNetwork.calcOutput(input, weights);

                int maximumIndex = 0;

                for (int index = 0; index < output.length; index++) {
                    if (output[index] > output[maximumIndex]) {
                        maximumIndex = index;
                    }
                }

                return PlayerAction.values()[maximumIndex];
            }

        };
    }

}
