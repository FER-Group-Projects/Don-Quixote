package hr.fer.zemris.projekt.model.input;

import hr.fer.zemris.projekt.model.controller.GameController;

public interface GameInputExtractor {

    int getNumberOfInputs();
    void extractInputs(GameController controller, double[] extractToThis);

    default double[] extractInputs(GameController controller) {
        double[] inputs = new double[getNumberOfInputs()];

        extractInputs(controller, inputs);

        return inputs;
    }

}
