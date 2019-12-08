package hr.fer.zemris.projekt.LGP;

import java.util.Random;

import hr.fer.zemris.projekt.LGP.lang.EasyLGPInstruction;
import hr.fer.zemris.projekt.LGP.lang.instructions.*;
import hr.fer.zemris.projekt.LGP.lang.instructions.JpInstruction.Condition;
import hr.fer.zemris.projekt.algorithm.initializer.PopulationInitializer;
import hr.fer.zemris.projekt.algorithm.solution.Solution;

public class LGPPopulationInitializer implements PopulationInitializer<Solution<EasyLGPInstruction>> {

	private int maxLength;
	private long numberOfRegisters;
	
	private Random random = new Random();
	
	public LGPPopulationInitializer(int maxLength, long numberOfRegisters) {
		this.maxLength = maxLength;
		this.numberOfRegisters = numberOfRegisters;
	}

	@Override
	public LGPSolution generateSolution() {
		int length = random.nextInt(maxLength) + 1;
		
		LGPSolution solution = new LGPSolution();
		for(int i=0; i<length; i++) {
			solution.setGeneAt(i, generateRandomLGPInstruction(length));
		}
		
		return solution;
	}

	private EasyLGPInstruction generateRandomLGPInstruction(int programLength) {
		
		int rnd = random.nextInt(15);
		
		switch(rnd) {
		
			case 0 : return new LdrInstruction(generateRandomRegNumber(), generateRandomRegNumber());
			case 1 : return new StrInstruction(generateRandomRegNumber(), generateRandomRegNumber());
			case 2 : return new MovInstruction(generateRandomRegNumber(), generateRandomRegNumber());
			case 3 : return new MovConstInstruction(generateRandomRegNumber(), random.nextLong());
			case 4 : return new AddInstruction(generateRandomRegNumber(), generateRandomRegNumber(), generateRandomRegNumber());
			case 5 : return new SubInstruction(generateRandomRegNumber(), generateRandomRegNumber(), generateRandomRegNumber());
			case 6 : return new MulInstruction(generateRandomRegNumber(), generateRandomRegNumber(), generateRandomRegNumber());
			case 7 : return new DivInstruction(generateRandomRegNumber(), generateRandomRegNumber(), generateRandomRegNumber());
			case 8 : return new ModInstruction(generateRandomRegNumber(), generateRandomRegNumber(), generateRandomRegNumber());
			case 9 : return new AndInstruction(generateRandomRegNumber(), generateRandomRegNumber(), generateRandomRegNumber());
			case 10 : return new OrInstruction(generateRandomRegNumber(), generateRandomRegNumber(), generateRandomRegNumber());
			case 11 : return new XorInstruction(generateRandomRegNumber(), generateRandomRegNumber(), generateRandomRegNumber());
			case 12 : return new CmpInstruction(generateRandomRegNumber(), generateRandomRegNumber());
			case 13 : return new JpInstruction(generateRandomJpCondition(), random.nextInt(programLength));
			case 14 : return new HaltInstruction();
			default : return null; // Never
		
		}
		
	}

	private long generateRandomRegNumber() {
		return (long) (random.nextDouble() * numberOfRegisters);
	}

	private Condition generateRandomJpCondition() {
		
		int rnd = random.nextInt(9);
		
		switch(rnd) {
			case 0: return Condition.ALWAYS;
			case 1: return Condition.NEGATIVE;
			case 2: return Condition.NON_NEGATIVE;
			case 3: return Condition.ZERO;
			case 4: return Condition.NON_ZERO;
			case 5: return Condition.EQUALS;
			case 6: return Condition.NOT_EQUALS;
			case 7: return Condition.LESS_THAN_OR_EQUALS;
			case 8: return Condition.GREATER_THAN_OR_EQUALS;
			default: return null; // Never
		}
		
	}

}
