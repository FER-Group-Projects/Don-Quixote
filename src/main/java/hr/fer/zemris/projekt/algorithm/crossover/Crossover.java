package hr.fer.zemris.projekt.algorithm.crossover;

public interface Crossover<T> {

    T crossover(T firstParent, T secondParent);

}
