package hr.fer.zemris.projekt.LGP;

import java.util.Random;

import hr.fer.zemris.projekt.LGP.lang.EasyLGPInstruction;
import hr.fer.zemris.projekt.algorithm.initializer.PopulationInitializer;
import hr.fer.zemris.projekt.algorithm.solution.Solution;

public class LGPPopulationInitializer implements PopulationInitializer<Solution<EasyLGPInstruction>> {

	private int maxLength;
	private long numOfRegisters;
	
	private Random random = new Random();
	
	public LGPPopulationInitializer(int maxLength, long numOfRegisters) {
		this.maxLength = maxLength;
		this.numOfRegisters = numOfRegisters;
	}

	@Override
	public LGPSolution generateSolution() {
		int length = random.nextInt(maxLength) + 1;
		
		LGPSolution solution = new LGPSolution();
		for(int i=0; i<length; i++) {
			var instr = RandomInstructionUtility.generateRandomLGPInstruction(numOfRegisters);
			solution.setGeneAt(i, instr);
		}
		
		return solution;
	}

}
