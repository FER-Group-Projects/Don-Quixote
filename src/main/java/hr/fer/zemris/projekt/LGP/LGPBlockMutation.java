package hr.fer.zemris.projekt.LGP;

import java.util.Random;

import hr.fer.zemris.projekt.LGP.lang.EasyLGPInstruction;
import hr.fer.zemris.projekt.algorithm.mutation.Mutation;
import hr.fer.zemris.projekt.algorithm.solution.Solution;

// Mutate block of instructions
public class LGPBlockMutation implements Mutation<Solution<EasyLGPInstruction>>{

	private Random random = new Random();

	private long numOfRegisters;
	private long maxAbsMovConstant;

	public LGPBlockMutation(long numOfRegisters, long maxAbsMovConstant) {
		this.numOfRegisters = numOfRegisters;
		this.maxAbsMovConstant = maxAbsMovConstant;
	}
	
	@Override
	public Solution<EasyLGPInstruction> mutate(Solution<EasyLGPInstruction> solutionToMutate) {
		var mutatedSolution = solutionToMutate.copy();
		
		int blockStart = random.nextInt(mutatedSolution.getNumberOfGenes());
		int blockEnd = blockStart + random.nextInt(mutatedSolution.getNumberOfGenes() - blockStart);
		
		for(int i=blockStart; i<=blockEnd; i++) {
			mutatedSolution.setGeneAt(i, RandomInstructionUtility.generateRandomLGPInstruction(numOfRegisters, solutionToMutate.getNumberOfGenes(), maxAbsMovConstant));
		}
		
		return mutatedSolution;
	}

}
