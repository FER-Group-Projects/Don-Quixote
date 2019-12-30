package hr.fer.zemris.projekt.feedforwardNeuralNet;

public class NeuralNetwork {
    private int[] layout;

    public NeuralNetwork(int[] layout) {
        this.layout = layout;
    }

    public double[] calculateOutput(double[] inputs, double[] weights) {
        checkArgs(inputs, weights);

        double[] layer_input = new double[inputs.length];
        System.arraycopy(inputs, 0, layer_input, 0, inputs.length);
        double[] layer_output = new double[layout[1]];
        int weightIndex = 0;

        for (int i = 1; i < layout.length; i++) {
            for (int j = 0; j < layout[i]; j++) {
                double bias = weights[weightIndex];
                weightIndex++;

                double sum = 0;
                for (int k = 0; k < layer_input.length; k++) {
                    sum += weights[weightIndex] * layer_input[k];
                    weightIndex++;
                }
                sum += bias;
                layer_output[j] = sigmoidFunction(sum);
            }

            if (i == layout.length - 1) {
                break;
            }

            layer_input = new double[layer_output.length];
            System.arraycopy(layer_output, 0, layer_input, 0, layer_output.length);
            layer_output = new double[layout[i + 1]];
        }

        return layer_output;
    }

    //check that the args are valid
    private void checkArgs(double[] inputs, double[] weights) {
        if (layout[0] != inputs.length) {
            throw new IllegalArgumentException("Invalid number  of input values! Expected: "
                    + layout[0] + " Received " + inputs.length);
        }

        int num = getNumberOfWeights();

        if (num != weights.length) {
            throw new IllegalArgumentException("Invalid number  of weights! Expected: "
                    + num + " Recieved: " + weights.length);
        }
    }

    public int getNumberOfWeights() {
        int num = 0;
        for (int i = 1; i < layout.length; i++) {
            num += ((layout[i - 1] + 1) * layout[i]);
        }
        return num;
    }

    public double getRandomDouble(double min, double max) {
        return (Math.random() * ((max - min) + 1)) + min;
    }

    public double sigmoidFunction(double x) {
        return (double) (1 / (1 + Math.pow(Math.E, -x)));
    }

}
