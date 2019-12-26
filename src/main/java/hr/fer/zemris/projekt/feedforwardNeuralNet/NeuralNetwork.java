package hr.fer.zemris.projekt.feedforwardNeuralNet;

import java.util.ArrayList;
import java.util.Arrays;

public class NeuralNetwork {

    private Layer[] layers;
    private int[] layout;

    public NeuralNetwork(int[] layout){
        this.layout = layout;
        layers = new Layer[layout.length];
        layers[0] = null;
        for(int i=1; i<layout.length; i++){
            layers[i] = new Layer(layout[i], layout[i-1]);
        }
    }

    public double[] calculateOutput(double[] inputs, double[] weights){
        checkArgs(inputs, weights);
        layers[0] = new Layer(inputs);

        setWeights(weights);

        double[] output = new double[layers[layers.length-1].listOfNeurons.length];
        for(int i=1; i<layers.length; i++){
            for(int j=0; j<layers[i].listOfNeurons.length; j++){
                double sum = 0;
                for(int k=0; k<layers[i-1].listOfNeurons.length; k++){
                    sum += layers[i-1].listOfNeurons[k].output * layers[i].listOfNeurons[j].inputWeights[k];
                }
                sum += layers[i].listOfNeurons[j].bias;
                layers[i].listOfNeurons[j].output = sigmoidFunction(sum);
                if(i == layers.length-1){
                    output[j] = layers[i].listOfNeurons[j].output;
                }
            }
        }
        return output;
    }

    public void setWeights(double[] allWeights){
        int index = 0;

        for(int layer=1; layer<layers.length; layer++){
            int numOfWeights = layers[layer-1].listOfNeurons.length;
            for(int neuron=0; neuron<layers[layer].listOfNeurons.length; neuron++){
                double[] weights = Arrays.copyOfRange(allWeights, index, index + numOfWeights);
                index += numOfWeights + 1;
                layers[layer].listOfNeurons[neuron].setInputWeights(weights, weights[numOfWeights - 1]);
            }
        }
    }

    //check that the args are valid
    private void checkArgs(double[] inputs, double[] weights){
        if (layout[0] != inputs.length){
            throw new IllegalArgumentException("Invalid number  of input values! Expected: "
                    +layout[0] + " Received " + inputs.length);
        }

        int num = getNumberOfWeights();

        if(num!=weights.length){
            throw new IllegalArgumentException("Invalid number  of weights! Expected: "
                    +num+ " Recieved: " + weights.length);
        }
    }

    public int getNumberOfWeights() {
        int num = 0;
        for (int i = 1; i < layout.length; i++){
            num += ((layout[i - 1] + 1) * layout[i]);
        }
        return num;
    }


    public double sigmoidFunction(double x){
        return (double) (1/(1+Math.pow(Math.E, -x)));
    }

}
