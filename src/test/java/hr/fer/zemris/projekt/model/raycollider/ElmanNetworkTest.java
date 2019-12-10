package hr.fer.zemris.projekt.model.raycollider;

import hr.fer.zemris.projekt.model.ElmanNetwork.ElmanNeuralNetwork;

public class ElmanNetworkTest {

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

}
