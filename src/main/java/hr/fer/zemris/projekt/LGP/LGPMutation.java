package hr.fer.zemris.projekt.LGP;

import java.util.Random;

import hr.fer.zemris.projekt.LGP.lang.EasyLGPInstruction;
import hr.fer.zemris.projekt.algorithm.mutation.Mutation;
import hr.fer.zemris.projekt.algorithm.solution.Solution;

// Mutate one individual instruction
public class LGPMutation implements Mutation<Solution<EasyLGPInstruction>>{

	private Random random = new Random();
	
	private long numOfRegisters;

	public LGPMutation(long numOfRegisters) {
		this.numOfRegisters = numOfRegisters;
	}

	@Override
	public Solution<EasyLGPInstruction> mutate(Solution<EasyLGPInstruction> solutionToMutate) {
		var mutatedSolution = solutionToMutate.copy();
		
		int instructionIndex = random.nextInt(mutatedSolution.getNumberOfGenes());
		mutatedSolution.setGeneAt(instructionIndex, RandomInstructionUtility.generateRandomLGPInstruction(numOfRegisters));
		
		return mutatedSolution;
	}

}
