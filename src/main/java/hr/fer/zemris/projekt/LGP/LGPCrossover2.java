package hr.fer.zemris.projekt.LGP;

import java.util.Random;

import hr.fer.zemris.projekt.LGP.lang.EasyLGPInstruction;
import hr.fer.zemris.projekt.algorithm.crossover.Crossover;
import hr.fer.zemris.projekt.algorithm.solution.Solution;

// Child = random part of the firstParent + random part of the secondParent
public class LGPCrossover2 implements Crossover<Solution<EasyLGPInstruction>> {
	
	private Random random = new Random();
	
	private int maxChildLength;

	public LGPCrossover2(int maxChildLength) {
		this.maxChildLength = maxChildLength;
	}

	@Override
	public Solution<EasyLGPInstruction> crossover(Solution<EasyLGPInstruction> firstParent, Solution<EasyLGPInstruction> secondParent) {
		LGPSolution child = new LGPSolution();
		
		int start1 = random.nextInt(firstParent.getNumberOfGenes());		
		int end1 = start1 + random.nextInt(firstParent.getNumberOfGenes() - start1);
		
		int start2 = random.nextInt(secondParent.getNumberOfGenes());
		int end2 = start2 + random.nextInt(Math.min(secondParent.getNumberOfGenes(), maxChildLength) - start2);
		
		int i = 0;
		int ptr = start1;
		while(ptr <= end1) {
			child.setGeneAt(i, firstParent.getGeneAt(ptr));
			ptr++;
			i++;
		}
		
		ptr = start2;
		while(ptr <= end2) {
			child.setGeneAt(i, secondParent.getGeneAt(ptr));
			ptr++;
			i++;
		}
		
		return child;
	}

}
