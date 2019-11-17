package hr.fer.zemris.projekt.algorithm.solution;

import java.util.Arrays;

public class DoubleArraySolution implements Solution<Double> {

    private double[] genes;
    private double fitness;
    private boolean isEvaluated;

    public DoubleArraySolution(int numberOfGenes) {
        this.genes = new double[numberOfGenes];
    }

    public DoubleArraySolution(double[] genes) {
        this.genes = Arrays.copyOf(genes, genes.length);
    }

    @Override
    public double getFitness() {
        if (!isEvaluated) {
            throw new IllegalStateException("Solution has not been evaluated.");
        }

        return fitness;
    }

    @Override
    public void setFitness(double newFitness) {
        fitness = newFitness;
        isEvaluated = true;
    }

    @Override
    public boolean isEvaluated() {
        return isEvaluated;
    }

    @Override
    public int getNumberOfGenes() {
        return genes.length;
    }

    @Override
    public Double getGeneAt(int index) {
        return genes[index];
    }

    @Override
    public void setGeneAt(int index, Double newValue) {
        genes[index] = newValue;
        isEvaluated = false;
    }

    @Override
    public Solution<Double> copy() {
        Solution<Double> copy = new DoubleArraySolution(genes);

        copy.setFitness(getFitness());

        return copy;
    }

}
