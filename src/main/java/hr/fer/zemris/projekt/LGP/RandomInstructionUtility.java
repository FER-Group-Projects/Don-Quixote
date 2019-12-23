package hr.fer.zemris.projekt.LGP;

import java.util.Random;

import hr.fer.zemris.projekt.LGP.lang.EasyLGPInstruction;
import hr.fer.zemris.projekt.LGP.lang.instructions.AddInstruction;
import hr.fer.zemris.projekt.LGP.lang.instructions.CmpInstruction;
import hr.fer.zemris.projekt.LGP.lang.instructions.DivInstruction;
import hr.fer.zemris.projekt.LGP.lang.instructions.HaltInstruction;
import hr.fer.zemris.projekt.LGP.lang.instructions.JpInstruction;
import hr.fer.zemris.projekt.LGP.lang.instructions.ModInstruction;
import hr.fer.zemris.projekt.LGP.lang.instructions.MovConstInstruction;
import hr.fer.zemris.projekt.LGP.lang.instructions.MovInstruction;
import hr.fer.zemris.projekt.LGP.lang.instructions.MulInstruction;
import hr.fer.zemris.projekt.LGP.lang.instructions.SubInstruction;
import hr.fer.zemris.projekt.LGP.lang.instructions.JpInstruction.Condition;

public class RandomInstructionUtility {
	
	private static Random random = new Random();
	
	public static EasyLGPInstruction generateRandomLGPInstruction(int numOfRegisters, int programLength, double maxAbsMovConstant) {
		
		int rnd = random.nextInt(9);
		
		switch(rnd) {
		
			case 0 : return new AddInstruction(generateRandomRegNumber(numOfRegisters), generateRandomRegNumber(numOfRegisters), generateRandomRegNumber(numOfRegisters));
			case 1 : return new SubInstruction(generateRandomRegNumber(numOfRegisters), generateRandomRegNumber(numOfRegisters), generateRandomRegNumber(numOfRegisters));
			case 2 : return new MulInstruction(generateRandomRegNumber(numOfRegisters), generateRandomRegNumber(numOfRegisters), generateRandomRegNumber(numOfRegisters));
			case 3 : return new DivInstruction(generateRandomRegNumber(numOfRegisters), generateRandomRegNumber(numOfRegisters), generateRandomRegNumber(numOfRegisters));
			case 4 : return new ModInstruction(generateRandomRegNumber(numOfRegisters), generateRandomRegNumber(numOfRegisters), generateRandomRegNumber(numOfRegisters));
			case 5 : return new CmpInstruction(generateRandomRegNumber(numOfRegisters), generateRandomRegNumber(numOfRegisters));
			case 6 : return new JpInstruction(generateRandomJpCondition(), random.nextInt(programLength));
			case 7 : return new HaltInstruction();
			case 8 : return new MovInstruction(generateRandomRegNumber(numOfRegisters), generateRandomRegNumber(numOfRegisters));
			case 9 : return new MovConstInstruction(generateRandomRegNumber(numOfRegisters), getRandomSign() * (long) (random.nextDouble() * maxAbsMovConstant));
			default : return null; // Never
		
		}
		
	}
	
	private static int getRandomSign() {
		return random.nextDouble() < 0.5 ? 1 : -1;
	}

	private static int generateRandomRegNumber(int numOfRegisters) {
		return (int) (random.nextDouble() * numOfRegisters);
	}

	private static Condition generateRandomJpCondition() {
		
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
