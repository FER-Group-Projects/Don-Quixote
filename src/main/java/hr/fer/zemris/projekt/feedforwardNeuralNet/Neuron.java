package hr.fer.zemris.projekt.feedforwardNeuralNet;

import java.util.ArrayList;

public class Neuron {

    static double minWeight = -1;
    static double maxWeight = 1;

    double[] inputWeights;
    double bias;
    double output = 0;


    // Constructor for hidden and output layers
    public Neuron(double[] inputWeights, double bias){
        this.inputWeights = inputWeights;
        this.bias = bias;
    }

    // Constructor for input layer
    public Neuron(double value){
        this.output = value;
        this.inputWeights = null;
        this.bias = -1;
    }

    // Set min and max weight for all neurons
    public static void setWeightRange(double min, double max){
        minWeight = min;
        maxWeight = max;
    }

    public double[] getInputWeights() {
        return inputWeights;
    }

    public double getOutput() {
        return output;
    }

    public void setInputWeights(double[] inputWeights, double bias) {
        this.inputWeights = inputWeights;
        this.bias = bias;
    }

}
