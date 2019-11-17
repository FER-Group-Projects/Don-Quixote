package hr.fer.zemris.projekt.algorithm.initializer;

import hr.fer.zemris.projekt.algorithm.solution.Solution;

import java.util.ArrayList;
import java.util.List;

public interface PopulationInitializer<S extends Solution<?>> {

    S generateSolution();

    default List<S> generatePopulation(int numberOfSolutions) {
        List<S> population = new ArrayList<>();

        for (int i = 0; i < numberOfSolutions; i++) {
            population.add(generateSolution());
        }

        return population;
    }

}
