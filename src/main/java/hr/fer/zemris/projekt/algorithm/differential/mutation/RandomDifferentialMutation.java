package hr.fer.zemris.projekt.algorithm.differential.mutation;

import hr.fer.zemris.projekt.algorithm.solution.Solution;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RandomDifferentialMutation extends AbstractDifferentialMutation {

    public RandomDifferentialMutation(double scalingFactor, int numberOfLinearCombinations) {
        super(scalingFactor, numberOfLinearCombinations);
    }

    @Override
    protected List<Solution<Double>> pickLinearCombinations(List<Solution<Double>> population, int currentIndex) {
        List<Solution<Double>> linearCombinations = new ArrayList<>();
        Set<Integer> usedIndexes = new HashSet<>();

        usedIndexes.add(currentIndex);
        addRandomUnusedSolutions(population, linearCombinations, usedIndexes, 2 * numberOfLinearCombinations + 1);

        return linearCombinations;
    }

    @Override
    protected double getScalingFactor() {
        return scalingFactor;
    }
}
