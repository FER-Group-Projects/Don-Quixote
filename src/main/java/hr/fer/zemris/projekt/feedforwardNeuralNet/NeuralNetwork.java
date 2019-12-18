package hr.fer.zemris.projekt.feedforwardNeuralNet;

import java.util.ArrayList;

public class NeuralNetwork {

    private Layer[] layers;

    public NeuralNetwork(int[] layout){
        layers = new Layer[layout.length];
        layers[0] = null;
        for(int i=1; i<layout.length; i++){
            layers[i] = new Layer(layout[i], layout[i-1]);
        }
    }

    public double[] calculateOutput(double[] inputs){
        layers[0] = new Layer(inputs);
        double[] output = new double[layers[layers.length-1].listOfNeurons.length];
        for(int i=1; i<layers.length; i++){
            for(int j=0; j<layers[i].listOfNeurons.length; j++){
                float sum = 0;
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

    public double sigmoidFunction(double x){
        return (double) (1/(1+Math.pow(Math.E, -x)));
    }

}
