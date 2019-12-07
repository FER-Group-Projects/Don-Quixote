package hr.fer.zemris.projekt.algorithm.differential.mutation;

import hr.fer.zemris.projekt.algorithm.solution.Solution;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BestDifferentialMutation extends AbstractDifferentialMutation {

    public BestDifferentialMutation(double scalingFactor, int numberOfLinearCombinations) {
        super(scalingFactor, numberOfLinearCombinations);
    }

    @Override
    protected List<Solution<Double>> pickLinearCombinations(List<Solution<Double>> population, int currentIndex) {
        int indexOfBestSolution = indexOfBestSolution(population);
        List<Solution<Double>> linearCombinations = new ArrayList<>();
        Set<Integer> usedIndexes = new HashSet<>();

        usedIndexes.add(indexOfBestSolution);
        linearCombinations.add(population.get(indexOfBestSolution));
        addRandomUnusedSolutions(population, linearCombinations, usedIndexes, 2 * numberOfLinearCombinations);

        return linearCombinations;
    }

    @Override
    protected double getScalingFactor() {
        return scalingFactor + 0.001 * (random.nextDouble() - 0.5);
    }

}
