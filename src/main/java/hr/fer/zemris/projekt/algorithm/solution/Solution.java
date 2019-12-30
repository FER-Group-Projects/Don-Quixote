package hr.fer.zemris.projekt.algorithm.solution;

import java.io.Serializable;

public interface Solution<G> extends Serializable {

    double getFitness();
    void setFitness(double newFitness);
    boolean isEvaluated();
    void resetIsEvaluated();

    int getNumberOfGenes();
    G getGeneAt(int index);
    void setGeneAt(int index, G newValue);

    Solution<G> copy();
	

}
