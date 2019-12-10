package hr.fer.zemris.projekt.LGP;

public class LGPTest {
	
	public static void main(String[] args) {
		
		int numOfRegisters = 10;
		int maxProgramLength = 30;
		long maxAbsMovConstant = Long.MAX_VALUE;
		
		LGP lgp = new LGP(1_000, 1_000, Long.MAX_VALUE, 0.2,
				new FunctionCalcFitnessFunction(numOfRegisters, 10000), 
				new LGPCrossover(),
				new LGPMutation(numOfRegisters, maxAbsMovConstant), 
				new LGPPopulationInitializer(maxProgramLength, numOfRegisters, maxAbsMovConstant));
		var solution = lgp.run();
		System.out.println(solution.getFitness());
		System.out.println(solution);
		
	}
	
}
