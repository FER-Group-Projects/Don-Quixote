package hr.fer.zemris.projekt.algorithm.fitness;

public interface FitnessFunction<T> {

    double calculateFitness(T solution);

}
