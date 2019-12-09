package hr.fer.zemris.projekt.feedforwardNeuralNet;

import java.util.ArrayList;

public class InputLayer extends Layer {

    public InputLayer(int numOfNeurons) {
        super(numOfNeurons);
    }

    public void setInputs(ArrayList<Double> inputs){
        ArrayList<Double> one = new ArrayList<>();
        one.add(1.0);
        for(int i=0; i<getNumOfNeurons(); i++){
            getListOfNeurons().get(i).setInputWeights(one);
            getListOfNeurons().get(i).setWeightedSum(inputs.get(i));
            getListOfNeurons().get(i).setOutput(inputs.get(i));
        }
    }

    @Override
    public String toString() {
        String returnValue = "### Input Layer ###\n";
        for(int i=0; i<getNumOfNeurons(); i++) {
            returnValue += "Neuron "+getListOfNeurons().get(i).getIdNeuron()+"\n";
            returnValue += "Input: "+getListOfNeurons().get(i).getWeightedSum()+"\n";
        }
        return returnValue;
    }
}
