package hr.fer.zemris.projekt.algorithm.differential.mutation;

import hr.fer.zemris.projekt.algorithm.solution.Solution;

import java.util.*;

public class RandomDifferentialMutation implements DifferentialMutation<Solution<Double>> {

    private final Random random = new Random();

    private final double scalingFactor;

    public RandomDifferentialMutation(double scalingFactor) {
        this.scalingFactor = scalingFactor;
    }

    @Override
    public List<Solution<Double>> createMutantPopulation(List<Solution<Double>> population) {
        List<Solution<Double>> mutantPopulation = new ArrayList<>(population.size());
        int numberOfComponents = population.get(0).getNumberOfGenes();

        for (int mutantIndex = 0; mutantIndex < population.size(); mutantIndex++) {
            Set<Integer> randomIndexes = new HashSet<>();

            randomIndexes.add(mutantIndex);

            while (randomIndexes.size() < 4) {
                randomIndexes.add(random.nextInt(population.size()));
            }

            randomIndexes.remove(mutantIndex);

            List<Integer> listOfRandomIndexes = new ArrayList<>(randomIndexes);
            Solution<Double> mutantVector = population.get(mutantIndex).copy();

            for (int componentIndex = 0; componentIndex < numberOfComponents; componentIndex++) {
                mutantVector.setGeneAt(componentIndex,
                        population.get(listOfRandomIndexes.get(0)).getGeneAt(componentIndex) +
                                scalingFactor * (population.get(listOfRandomIndexes.get(1)).getGeneAt(componentIndex) -
                                population.get(listOfRandomIndexes.get(2)).getGeneAt(componentIndex)));
            }

            mutantPopulation.add(mutantVector);
        }

        return mutantPopulation;
    }

}
