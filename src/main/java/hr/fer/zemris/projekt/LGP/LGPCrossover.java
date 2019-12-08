package hr.fer.zemris.projekt.LGP;

import java.util.Random;

import hr.fer.zemris.projekt.LGP.lang.EasyLGPInstruction;
import hr.fer.zemris.projekt.algorithm.crossover.Crossover;
import hr.fer.zemris.projekt.algorithm.solution.Solution;

// Child = FirstParent with mutation (random part of length L is replaced with random part from secondParent of length L)
public class LGPCrossover implements Crossover<Solution<EasyLGPInstruction>> {
	
	private Random random = new Random();

	@Override
	public Solution<EasyLGPInstruction> crossover(Solution<EasyLGPInstruction> firstParent, Solution<EasyLGPInstruction> secondParent) {
		Solution<EasyLGPInstruction> child = firstParent.copy();
		
		int start1 = random.nextInt(firstParent.getNumberOfGenes());		
		int start2 = random.nextInt(secondParent.getNumberOfGenes());
		int crossoverSize = 1 + random.nextInt(Math.min(firstParent.getNumberOfGenes() - start1 + 1, secondParent.getNumberOfGenes() - start2 + 1));
		
		int offset = 0;
		while(offset < crossoverSize) {
			child.setGeneAt(start1 + offset, secondParent.getGeneAt(start2 + offset));
			offset++;
		}
		
		return child;
	}

}
