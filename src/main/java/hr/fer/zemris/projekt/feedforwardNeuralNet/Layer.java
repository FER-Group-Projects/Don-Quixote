package hr.fer.zemris.projekt.feedforwardNeuralNet;

import java.util.ArrayList;

public class Layer {

    Neuron[] listOfNeurons;

    // Constructor for hidden and output layer
    public Layer(int numOfNeurons, int inNeurons){
        this.listOfNeurons = new Neuron[numOfNeurons];
        for(int i=0; i<numOfNeurons; i++){
            double[] inputWeights = new double[inNeurons];
            for(int j=0; j<inNeurons; j++){
                inputWeights[j] = getRandomDouble(Neuron.minWeight, Neuron.maxWeight);
            }
            listOfNeurons[i] = new Neuron(inputWeights, getRandomDouble(0, 1));
        }
    }

    // Constructor for input layer
    public Layer(double[] input){
        this.listOfNeurons = new Neuron[input.length];
        for(int i=0; i<input.length; i++){
            this.listOfNeurons[i] = new Neuron(input[i]);
        }
    }

    public double getRandomDouble(double min, double max){
        return (Math.random()*((max-min)+1))+min;
    }

}
