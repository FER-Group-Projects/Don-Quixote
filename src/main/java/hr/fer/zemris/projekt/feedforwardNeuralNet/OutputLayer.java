package hr.fer.zemris.projekt.feedforwardNeuralNet;

import java.util.ArrayList;

public class OutputLayer extends Layer {

    public OutputLayer(int numOfNeurons) {
        super(numOfNeurons);
    }

    @Override
    public String toString() {
        String returnValue = "\n### Output Layer ###\n";
        for(int i=0; i<getNumOfNeurons(); i++) {
            returnValue += "Neuron "+getListOfNeurons().get(i).getIdNeuron()+"\n";
            returnValue += "Input weights: "+getListOfNeurons().get(i).getInputWeights()+"\n";
            returnValue += "Weighted sum: "+getListOfNeurons().get(i).getWeightedSum()+"\n";
            returnValue += "Output: "+getListOfNeurons().get(i).getOutput()+"\n";
        }
        return returnValue;
    }
}
