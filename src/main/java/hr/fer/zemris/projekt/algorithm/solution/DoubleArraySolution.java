package hr.fer.zemris.projekt.algorithm.solution;

import java.util.Arrays;
import java.util.OptionalDouble;

public class DoubleArraySolution implements Solution<Double> {

    private double[] genes;
    private OptionalDouble fitness;

    public DoubleArraySolution(int numberOfGenes) {
        this.genes = new double[numberOfGenes];
        this.fitness = OptionalDouble.empty();
    }

    public DoubleArraySolution(double[] genes) {
        this.genes = Arrays.copyOf(genes, genes.length);
        this.fitness = OptionalDouble.empty();
    }

    @Override
    public double getFitness() {
        return fitness.getAsDouble();
    }

    @Override
    public void setFitness(double newFitness) {
        fitness = OptionalDouble.of(newFitness);
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
        fitness = OptionalDouble.empty();
    }

    @Override
    public Solution<Double> copy() {
        Solution<Double> copy = new DoubleArraySolution(genes);

        copy.setFitness(getFitness());

        return copy;
    }

}
