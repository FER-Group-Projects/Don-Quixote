package hr.fer.zemris.projekt.model.elman;

public class ElmanNeuralNetwork {
    private int[] layout;
    private double[] context_layer;

    public ElmanNeuralNetwork(int[] layout){
        this.layout=layout;
        context_layer = new double[layout[1]];
        layout[0]+=context_layer.length;
    }
    //testing the neural network
    public static void main(String[] args) {
        double[] inputs={100,-100};
        double[] weigths={0.5, 0.25,
                        0.5, 0.25,
                        0.5, 0.25,
                        0.5, 0.25,
                        0.5, 0.5
        };

        int[] layout={2,2,1};
        ElmanNeuralNetwork network = new ElmanNeuralNetwork(layout);
        double[] output;
        output=network.calcOutput(inputs,weigths);
        System.out.print("The output is:");
        for(int i=0; i<output.length; i++){
            System.out.println(output[i]);
        }

        output=network.calcOutput(inputs,weigths);
        System.out.print("The output is:");
        for(int i=0; i<output.length; i++){
            System.out.println(output[i]);
        }
    }
    //calculate the output of the neural network
    public double[] calcOutput(double[] inputs, double[] weights ){

        checkArgs(inputs, weights);

        double[] layer_input=new double [inputs.length+context_layer.length];
        double[] layer_output=new double [layout[1]];
        System.arraycopy(inputs,0,layer_input,0,inputs.length);
        System.arraycopy(context_layer,0,layer_input,inputs.length,context_layer.length);
        int weightIndex = 0;

        for(int i=1; i<layout.length; i++){

            for(int j=0; j<layout[i]; j++){
                int bias=0;
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
                System.arraycopy(layer_output,0,context_layer,0,layer_output.length);
            }
            layer_output=new double[layout[i+1]];

        }

        return layer_output.clone();
    }

    public int getFitness(){
        return 0;
    }

    //need to add the transfer fuction, for now it just returns the sum
    private double transfer_function(double sum){
        return sum;
    }

    //check that the args are valid
    private void checkArgs(double[] inputs, double[] weights){
        if (layout[0] != inputs.length+context_layer.length){
            throw new IllegalArgumentException("Invalid number  of input values! Expected: "
                    +layout[0] + " Received " + inputs.length);
        }

        int num = 0;
        for (int i = 1; i < layout.length; i++){
            num += ((layout[i - 1]) * layout[i]);
        }

        if(num!=weights.length){
            throw new IllegalArgumentException("Invalid number  of weights! Expected: "
                    +num+ " Recieved: " + weights.length);
        }
    }

}
