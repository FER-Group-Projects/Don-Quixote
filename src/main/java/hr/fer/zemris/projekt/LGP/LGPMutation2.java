package hr.fer.zemris.projekt.LGP;

import java.util.Random;

import hr.fer.zemris.projekt.LGP.lang.EasyLGPInstruction;
import hr.fer.zemris.projekt.algorithm.mutation.Mutation;
import hr.fer.zemris.projekt.algorithm.solution.Solution;

// Mutate block of instructions
public class LGPMutation2 implements Mutation<Solution<EasyLGPInstruction>>{

	private Random random = new Random();
	
	private double mutationRate;
	
	public LGPMutation2(double mutationRate) {
		if(mutationRate<0 || mutationRate>1) throw new IllegalArgumentException("Mutation rate " + mutationRate + "is not element of [0,1]!");
		this.mutationRate = mutationRate;
	}

	@Override
	public Solution<EasyLGPInstruction> mutate(Solution<EasyLGPInstruction> solutionToMutate) {
		if(random.nextDouble() >= mutationRate) return solutionToMutate;
		var mutatedSolution = solutionToMutate.copy();
		
		int blockStart = random.nextInt(mutatedSolution.getNumberOfGenes());
		int blockEnd = blockStart + random.nextInt(mutatedSolution.getNumberOfGenes() - blockStart);
		
		for(int i=blockStart; i<=blockEnd; i++) {
			mutatedSolution.setGeneAt(i, RandomInstructionUtility.generateRandomLGPInstruction());
		}
		
		return mutatedSolution;
	}

}
