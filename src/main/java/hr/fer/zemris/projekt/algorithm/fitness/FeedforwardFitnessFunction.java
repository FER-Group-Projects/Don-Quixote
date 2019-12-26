package hr.fer.zemris.projekt.algorithm.fitness;

import hr.fer.zemris.projekt.algorithm.player.ArtificialPlayer;
import hr.fer.zemris.projekt.algorithm.solution.DoubleArraySolution;
import hr.fer.zemris.projekt.algorithm.solution.Solution;
import hr.fer.zemris.projekt.feedforwardNeuralNet.NeuralNetwork;
import hr.fer.zemris.projekt.model.controller.PlayerAction;
import hr.fer.zemris.projekt.model.input.GameInputExtractor;
import hr.fer.zemris.projekt.model.scenes.SceneGenerator;

import java.util.Arrays;
import java.util.List;

public class FeedforwardFitnessFunction extends GameFitnessFunction<Solution<Double>> {

    private final int[] layout;

    public FeedforwardFitnessFunction(List<SceneGenerator> sceneGenerators, GameInputExtractor gameInputExtractor, int[] layout){
        super(sceneGenerators, gameInputExtractor);
        this.layout = Arrays.copyOf(layout, layout.length);
    }

    @Override
    public ArtificialPlayer initializeArtificialPlayer(Solution<Double> solution){
        return new ArtificialPlayer() {
            NeuralNetwork feedforwardNN = new NeuralNetwork(layout);
            double[] weights = ((DoubleArraySolution) solution).toArray();

            @Override
            public PlayerAction calculateAction(double[] input) {
                double[] output = feedforwardNN.calculateOutput(input, weights);

                int maxIndex = 0;
                for(int index=0; index<output.length; index++){
                    if(output[index] > output[maxIndex]){
                        maxIndex = index;
                    }
                }

                return PlayerAction.values()[maxIndex];
            }
        };
    }
}
