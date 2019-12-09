package hr.fer.zemris.projekt.feedforwardNeuralNet;

import java.util.ArrayList;

public class Neuron {

    private ArrayList<Double> inputWeights;
    private double weightedSum;
    private double output;
    private int idNeuron;

    public Neuron(int idNeuron){
        this.idNeuron = idNeuron;
    }

    public void activationFunction(){
    }

    public ArrayList<Double> getInputWeights() {
        return inputWeights;
    }

    public void setInputWeights(ArrayList<Double> inputWeights) {
        this.inputWeights = inputWeights;
    }

    public double getWeightedSum() {
        return weightedSum;
    }

    public void setWeightedSum(double weightedSum) {
        this.weightedSum = weightedSum;
    }

    public double getOutput() {
        return output;
    }

    public void setOutput(double output) {
        this.output = output;
    }

    public int getIdNeuron() {
        return idNeuron;
    }



}
