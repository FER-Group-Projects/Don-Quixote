package hr.fer.zemris.projekt.feedforwardNeuralNet;

import java.util.ArrayList;

public class NeuralNetwork {

    private InputLayer inputLayer;
    private OutputLayer outputLayer;
    private ArrayList<HiddenLayer> hiddenLayers;
    private int numOfHiddenLayers;
    private int numOfInputNeurons;
    private int numOfOutputNeurons;
    private int[] numOfNeuronsInEachHiddenLayer;

    public NeuralNetwork(int numOfHiddenLayers){
        this.numOfHiddenLayers = numOfHiddenLayers;
    }

    public void initNeuralNetwork(int numOfInputNeurons, int numOfOutputNeurons, int... numOfNeuronsInEachHiddenLayer){
        this.numOfInputNeurons = numOfInputNeurons;
        this.numOfOutputNeurons = numOfOutputNeurons;
        this.numOfNeuronsInEachHiddenLayer = numOfNeuronsInEachHiddenLayer;

        inputLayer = new InputLayer(numOfInputNeurons);
        outputLayer = new OutputLayer(numOfOutputNeurons);
        hiddenLayers = new ArrayList<>();
        for(int i=0; i<numOfHiddenLayers; i++){
            hiddenLayers.add(new HiddenLayer(numOfNeuronsInEachHiddenLayer[i], i));
        }

        generateWeightsRandomly();
    }

    public void setInput(ArrayList<Double> inputs){
        inputLayer.setInputs(inputs);
    }

    public void generateWeightsRandomly(){
        //on hidden layer
        for(int i=0; i<numOfHiddenLayers; i++){
                for(int j=0; j<numOfNeuronsInEachHiddenLayer[i]; j++) {
                    ArrayList<Double> inputWeights = new ArrayList<>();
                    if (i==0) {
                        for (int k=0; k<numOfInputNeurons; k++)
                            inputWeights.add(Math.random());
                    } else {
                        for (int k=0; k<numOfNeuronsInEachHiddenLayer[i-1]; k++)
                            inputWeights.add(Math.random());
                    }
                    hiddenLayers.get(i).getListOfNeurons().get(j).setInputWeights(inputWeights);
                }
        }

        //on output layer
        for(int j=0; j<numOfOutputNeurons; j++){
            ArrayList<Double> inputWeights = new ArrayList<>();
            for(int k=0; k<numOfNeuronsInEachHiddenLayer[numOfHiddenLayers-1]; k++){
                inputWeights.add(Math.random());
            }
            outputLayer.getListOfNeurons().get(j).setInputWeights(inputWeights);
        }
    }

    public void printNetwork(){
        System.out.println(this.toString());
    }

    @Override
    public String toString() {
        String returnValue = "";
        returnValue += inputLayer.toString();
        for(int i=0; i<numOfHiddenLayers; i++){
            returnValue += hiddenLayers.get(i).toString();
        }
        returnValue += outputLayer.toString();
        return returnValue;
    }
}
