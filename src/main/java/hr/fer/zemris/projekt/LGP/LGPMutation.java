package hr.fer.zemris.projekt.LGP;

import java.util.Random;

import hr.fer.zemris.projekt.LGP.lang.EasyLGPInstruction;
import hr.fer.zemris.projekt.algorithm.mutation.Mutation;
import hr.fer.zemris.projekt.algorithm.solution.Solution;

// Mutate each instruction individually
public class LGPMutation implements Mutation<Solution<EasyLGPInstruction>>{

	private Random random = new Random();
	
	private double mutationRate;
	
	public LGPMutation(double mutationRate) {
		if(mutationRate<0 || mutationRate>1) throw new IllegalArgumentException("Mutation rate " + mutationRate + "is not element of [0,1]!");
		this.mutationRate = mutationRate;
	}

	@Override
	public Solution<EasyLGPInstruction> mutate(Solution<EasyLGPInstruction> solutionToMutate) {
		var mutatedSolution = solutionToMutate.copy();
		
		for(int i=0; i<solutionToMutate.getNumberOfGenes(); i++) {
			if(random.nextDouble() < mutationRate)
				solutionToMutate.setGeneAt(i, RandomInstructionUtility.generateRandomLGPInstruction());
		}
		
		return mutatedSolution;
	}

}
