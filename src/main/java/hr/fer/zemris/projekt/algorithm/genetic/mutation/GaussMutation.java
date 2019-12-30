package hr.fer.zemris.projekt.algorithm.genetic.mutation;

import hr.fer.zemris.projekt.algorithm.solution.Solution;

import java.util.Random;

public class GaussMutation implements GeneticMutation<Solution<Double>> {

    private Random random = new Random();

    private final double multiplier;

    public GaussMutation(double multiplier) {
        this.multiplier = multiplier;
    }

    @Override
    public Solution<Double> mutate(Solution<Double> solutionToMutate) {
        for (int i = 0; i < solutionToMutate.getNumberOfGenes(); i++) {
            double gene = solutionToMutate.getGeneAt(i) + multiplier * random.nextGaussian();
            solutionToMutate.setGeneAt(i, gene);
        }

        return solutionToMutate;
    }

}
