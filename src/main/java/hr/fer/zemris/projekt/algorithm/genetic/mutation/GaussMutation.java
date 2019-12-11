package hr.fer.zemris.projekt.algorithm.genetic.mutation;

import java.util.Random;

import hr.fer.zemris.projekt.algorithm.solution.Solution;

public class GaussMutation implements GeneticMutation<Solution<Double>> {
	
	private Random random = new Random();
	private double gene;

	@Override
	public Solution<Double> mutate(Solution<Double> solutionToMutate) {
		
		for (int i = 0; i < solutionToMutate.getNumberOfGenes(); i++) {
			
			gene = solutionToMutate.getGeneAt(i) + random.nextGaussian();
			solutionToMutate.setGeneAt(i, gene);

		}
		
		return solutionToMutate;
	}

}
