package hr.fer.zemris.projekt.feedforwardNeuralNet;

public class HiddenLayer extends Layer {

    private int idHiddenLayer;

    public HiddenLayer(int numOfNeurons, int idHiddenLayer) {
        super(numOfNeurons);
        this.idHiddenLayer = idHiddenLayer;
    }

    @Override
    public String toString() {
        String returnValue = "\n### Hidden Layer "+idHiddenLayer+" ###\n";
        for(int i=0; i<getNumOfNeurons(); i++) {
            returnValue += "Neuron "+getListOfNeurons().get(i).getIdNeuron()+"\n";
            returnValue += "Input weights: "+getListOfNeurons().get(i).getInputWeights()+"\n";
            returnValue += "Weighted sum: "+getListOfNeurons().get(i).getWeightedSum()+"\n";
            returnValue += "Output: "+getListOfNeurons().get(i).getOutput()+"\n";
        }
        return returnValue;
    }
}
