package hr.fer.zemris.projekt.algorithm.solution;

public interface Solution<G> {

    double getFitness();
    void setFitness(double newFitness);
    boolean isEvaluated();

    int getNumberOfGenes();
    G getGeneAt(int index);
    void setGeneAt(int index, G newValue);

    Solution<G> copy();

}
