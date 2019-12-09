package hr.fer.zemris.projekt.feedforwardNeuralNet;

import java.util.ArrayList;

public class NeuralNetworkTest {

    public static void  main(String[] args){

        ArrayList<Double> inputs = new ArrayList<>();
        inputs.add(2.4);
        inputs.add(3.2);
        inputs.add(3.5);
        inputs.add(2.7);


        NeuralNetwork neuralNetwork = new NeuralNetwork(2);
        neuralNetwork.initNeuralNetwork(4, 4, 6, 6);
        neuralNetwork.setInput(inputs);
        neuralNetwork.printNetwork();

    }
}
