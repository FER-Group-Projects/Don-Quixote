package hr.fer.zemris.projekt.model;

import hr.fer.zemris.projekt.feedforwardNeuralNet.NeuralNetwork;

public class NeuralNetworkTest {

    public static void  main(String[] args){

        int[] layout = {3, 8, 6, 6, 2};

        NeuralNetwork neuralNetwork = new NeuralNetwork(layout);
        int numOfWeights = neuralNetwork.getNumberOfWeights();
        double[] weights = new double[numOfWeights];
        for(int i=0; i<numOfWeights; i++){
            weights[i] = neuralNetwork.getRandomDouble(-1, 1);
        }
        double[] input = {8.8, 12.234, 3.2};

        double[] output = neuralNetwork.calculateOutput(input, weights);
        for(int i=0; i<output.length; i++){
            System.out.println("Output " + i + " = " + output[i]);
        }

    }
}
