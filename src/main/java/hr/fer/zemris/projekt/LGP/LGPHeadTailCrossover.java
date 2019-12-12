package hr.fer.zemris.projekt.LGP;

import java.util.Random;

import hr.fer.zemris.projekt.LGP.lang.EasyLGPInstruction;
import hr.fer.zemris.projekt.algorithm.crossover.Crossover;
import hr.fer.zemris.projekt.algorithm.solution.Solution;

// Child = random part of the firstParent + random part of the secondParent
public class LGPHeadTailCrossover implements Crossover<Solution<EasyLGPInstruction>> {
	
	private Random random = new Random();
	
	private int maxChildLength;

	public LGPHeadTailCrossover(int maxChildLength) {
		this.maxChildLength = maxChildLength;
	}

	@Override
	public Solution<EasyLGPInstruction> crossover(Solution<EasyLGPInstruction> firstParent, Solution<EasyLGPInstruction> secondParent) {
		LGPSolution child = new LGPSolution();
		
		int start1 = random.nextInt(firstParent.getNumberOfGenes());		
		int size1 = 1 + random.nextInt(Math.min(maxChildLength, firstParent.getNumberOfGenes() - start1));
		
		int start2 = random.nextInt(secondParent.getNumberOfGenes());
		int size2 = size1 == maxChildLength ? 0 : 1 + random.nextInt(Math.min(maxChildLength - size1, secondParent.getNumberOfGenes() - start2));
		
		int i = 0;
		int offset = 0;
		while(offset < size1) {
			child.setGeneAt(i, firstParent.getGeneAt(start1 + offset));
			offset++;
			i++;
		}
		
		offset = 0;
		while(offset < size2) {
			child.setGeneAt(i, secondParent.getGeneAt(start2 + offset));
			offset++;
			i++;
		}
		
		return child;
	}

}
