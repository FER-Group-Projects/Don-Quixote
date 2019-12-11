package hr.fer.zemris.projekt.algorithm.genetic.selection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;
import hr.fer.zemris.projekt.algorithm.solution.Solution;

public class TournamentSelection<S extends Solution<?>> implements GeneticSelection<S> {
	
	
	private Random random = new Random();

	@Override
	public S selectFromPopulation(Collection<? extends S> population) {
		
		ArrayList<S> list = new ArrayList<S>(population);
		
		int k = random.nextInt(population.size());
		S ind;
		S best = null;
		
		for(int i = 0;i < k; i++) {
			
			ind = list.get(random.nextInt(population.size()));
			
			if(best == null) {
				best = list.get(random.nextInt(population.size()));
			}
			
			if(ind.getFitness()>best.getFitness()) {
				best = ind;
			}
			
		}
		
		return best;
	}

}
