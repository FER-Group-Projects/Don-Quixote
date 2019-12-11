package hr.fer.zemris.projekt.algorithm.genetic.crossover;

import java.util.Random;

import hr.fer.zemris.projekt.algorithm.solution.Solution;


public class SinglePointCrossover implements GeneticCrossover<Solution<Double>> {
	
	private Random random;

	@Override
	public Solution<Double> crossover(Solution<Double> firstParent, Solution<Double> secondParent) {
		
		Solution<Double> child = firstParent.copy();
		
		random = new Random();
		int crossPoint = random.nextInt(firstParent.getNumberOfGenes());
		
		for(int i=0;i<firstParent.getNumberOfGenes();++i) {
			
			if(i<crossPoint)
				child.setGeneAt(i, firstParent.getGeneAt(i));
			else
				child.setGeneAt(i, secondParent.getGeneAt(i));
		}
		
		return child;
	}

}
