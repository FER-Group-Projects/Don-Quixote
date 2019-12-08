package hr.fer.zemris.projekt.LGP;

import java.util.Random;

import hr.fer.zemris.projekt.LGP.lang.EasyLGPInstruction;
import hr.fer.zemris.projekt.LGP.lang.instructions.AddInstruction;
import hr.fer.zemris.projekt.LGP.lang.instructions.AndInstruction;
import hr.fer.zemris.projekt.LGP.lang.instructions.CmpInstruction;
import hr.fer.zemris.projekt.LGP.lang.instructions.DivInstruction;
import hr.fer.zemris.projekt.LGP.lang.instructions.HaltInstruction;
import hr.fer.zemris.projekt.LGP.lang.instructions.JpInstruction;
import hr.fer.zemris.projekt.LGP.lang.instructions.LdrInstruction;
import hr.fer.zemris.projekt.LGP.lang.instructions.ModInstruction;
import hr.fer.zemris.projekt.LGP.lang.instructions.MovConstInstruction;
import hr.fer.zemris.projekt.LGP.lang.instructions.MovInstruction;
import hr.fer.zemris.projekt.LGP.lang.instructions.MulInstruction;
import hr.fer.zemris.projekt.LGP.lang.instructions.OrInstruction;
import hr.fer.zemris.projekt.LGP.lang.instructions.StrInstruction;
import hr.fer.zemris.projekt.LGP.lang.instructions.SubInstruction;
import hr.fer.zemris.projekt.LGP.lang.instructions.XorInstruction;
import hr.fer.zemris.projekt.LGP.lang.instructions.JpInstruction.Condition;

public class RandomInstructionUtility {
	
	private static Random random = new Random();
	
	public static EasyLGPInstruction generateRandomLGPInstruction() {
		
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
			case 13 : return new JpInstruction(generateRandomJpCondition(), random.nextInt(Integer.MAX_VALUE));
			case 14 : return new HaltInstruction();
			default : return null; // Never
		
		}
		
	}

	private static long generateRandomRegNumber() {
		return (long) (random.nextDouble() * Long.MAX_VALUE);
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
