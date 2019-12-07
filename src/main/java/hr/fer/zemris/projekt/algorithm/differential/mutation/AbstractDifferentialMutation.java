package hr.fer.zemris.projekt.algorithm.differential.mutation;

import hr.fer.zemris.projekt.algorithm.solution.Solution;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

public abstract class AbstractDifferentialMutation implements DifferentialMutation<Solution<Double>> {

    protected final Random random = new Random();

    protected final double scalingFactor;
    protected final int numberOfLinearCombinations;

    public AbstractDifferentialMutation(double scalingFactor, int numberOfLinearCombinations) {
        this.scalingFactor = scalingFactor;
        this.numberOfLinearCombinations = numberOfLinearCombinations;
    }

    @Override
    public List<Solution<Double>> createMutantPopulation(List<Solution<Double>> population) {
        List<Solution<Double>> mutantPopulation = new ArrayList<>(population.size());
        int numberOfComponents = population.get(0).getNumberOfGenes();

        for (int mutantIndex = 0; mutantIndex < population.size(); mutantIndex++) {
            List<Solution<Double>> linearCombinations = pickLinearCombinations(population, mutantIndex);

            Solution<Double> baseVector = linearCombinations.get(0);
            Solution<Double> mutantVector = baseVector.copy();

            for (int componentIndex = 0; componentIndex < numberOfComponents; componentIndex++) {
                double mutantComponent = baseVector.getGeneAt(componentIndex);

                for (int linearCombinationIndex = 1; linearCombinationIndex < linearCombinations.size(); linearCombinationIndex++) {
                    // Even indexes should be subtracted and odd indexes should be added
                    mutantComponent += ((linearCombinationIndex % 2) == 1 ? 1 : -1) *
                            getScalingFactor() * linearCombinations.get(linearCombinationIndex).getGeneAt(componentIndex);
                }

                mutantVector.setGeneAt(componentIndex, mutantComponent);
            }

            mutantPopulation.add(mutantVector);
        }

        return mutantPopulation;
    }

    protected abstract List<Solution<Double>> pickLinearCombinations(List<Solution<Double>> population, int currentIndex);
    protected abstract double getScalingFactor();

    protected int indexOfBestSolution(List<Solution<Double>> population) {
        double bestFitness = 0;
        int bestIndex = -1;

        for (int solutionIndex = 0; solutionIndex < population.size(); solutionIndex++) {
            if (bestIndex == -1 || bestFitness < population.get(solutionIndex).getFitness()) {
                bestFitness = population.get(solutionIndex).getFitness();
                bestIndex = solutionIndex;
            }
        }

        return bestIndex;
    }

    protected void addRandomUnusedSolutions(List<Solution<Double>> population, List<Solution<Double>> linearCombinations, Set<Integer> usedIndexes, int numberOfIndexesToAdd) {
        int whenToStop = usedIndexes.size() + numberOfIndexesToAdd;

        while (usedIndexes.size() < whenToStop) {
            int randomIndex = random.nextInt(population.size());

            if (usedIndexes.add(randomIndex)) {
                linearCombinations.add(population.get(randomIndex));
            }
        }
    }

}
