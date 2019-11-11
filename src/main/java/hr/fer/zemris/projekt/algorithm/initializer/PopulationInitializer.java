package hr.fer.zemris.projekt.algorithm.initializer;

import java.util.ArrayList;
import java.util.List;

public interface PopulationInitializer<S> {

    S generateSolution();

    default List<S> generatePopulation(int numberOfSolutions) {
        List<S> population = new ArrayList<>();

        for (int i = 0; i < numberOfSolutions; i++) {
            population.add(generateSolution());
        }

        return population;
    }

}
