package hr.fer.zemris.projekt.model.ElmanNetwork;

import java.io.Serializable;
import java.util.Arrays;

public class ElmanNeuralNetwork implements Serializable {
    private int[] layout;
    private double[] contextLayer;

    public ElmanNeuralNetwork(int[] layout){
        this.layout = Arrays.copyOf(layout, layout.length);
        contextLayer = new double[layout[1]];
        this.layout[0]+= contextLayer.length;
    }

    public void setContextLayer(double[] contextLayer) {
        this.contextLayer = Arrays.copyOf(contextLayer, contextLayer.length);
    }

    public double[] getContextLayer() {
        return contextLayer;
    }

    //calculate the output of the neural network
    public double[] calcOutput(double[] inputs, double[] weights ){

        checkArgs(inputs, weights);

        double[] layer_input=new double [inputs.length+ contextLayer.length];
        double[] layer_output=new double [layout[1]];
        System.arraycopy(inputs,0,layer_input,0,inputs.length);
        System.arraycopy(contextLayer,0,layer_input,inputs.length, contextLayer.length);
        int weightIndex = 0;

        for(int i=1; i<layout.length; i++){

            for(int j=0; j<layout[i]; j++){
                double bias=weights[weightIndex];
                weightIndex++;
                
                double sum=0;

                for(int k=0; k<layer_input.length; k++){
                    sum+=weights[weightIndex]*layer_input[k];
                    weightIndex++;
                }

                sum+=bias;
                layer_output[j]=transfer_function(sum);
            }

            layer_input = new double [layer_output.length];
            System.arraycopy(layer_output,0,layer_input,0,layer_output.length);
            if(i==layout.length-1){
                break;
            }

            if(i==1){
                System.arraycopy(layer_output,0, contextLayer,0,layer_output.length);
            }
            layer_output=new double[layout[i+1]];

        }

        return layer_output.clone();
    }

    //need to add the transfer fuction, for now it just returns the sum
    private double transfer_function(double sum){
        return (1/( 1 + Math.pow(Math.E,(-1*sum))));
    }

    //check that the args are valid
    private void checkArgs(double[] inputs, double[] weights){
        if (layout[0] != inputs.length+ contextLayer.length){
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
            num += ((layout[i - 1]+1) * layout[i]);
        }

        return num;
    }

}
