package hr.fer.zemris.projekt.LGP;

import java.util.Random;

import hr.fer.zemris.projekt.LGP.lang.EasyLGPInstruction;
import hr.fer.zemris.projekt.algorithm.mutation.Mutation;
import hr.fer.zemris.projekt.algorithm.solution.Solution;

// Mutate one individual instruction
public class LGPInstructionMutation implements Mutation<Solution<EasyLGPInstruction>>{

	private Random random = new Random();
	
	private int numOfRegisters;
	private double maxAbsMovConstant;

	public LGPInstructionMutation(int numOfRegisters, double maxAbsMovConstant) {
		this.numOfRegisters = numOfRegisters;
		this.maxAbsMovConstant = maxAbsMovConstant;
	}

	@Override
	public Solution<EasyLGPInstruction> mutate(Solution<EasyLGPInstruction> solutionToMutate) {
		var mutatedSolution = solutionToMutate.copy();
		
		int instructionIndex = random.nextInt(mutatedSolution.getNumberOfGenes());
		mutatedSolution.setGeneAt(instructionIndex, RandomInstructionUtility.generateRandomLGPInstruction(numOfRegisters, solutionToMutate.getNumberOfGenes(), maxAbsMovConstant));
		
		return mutatedSolution;
	}

}
