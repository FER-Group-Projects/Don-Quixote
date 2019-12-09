package hr.fer.zemris.projekt.LGP;

public class LGPTest {
	
	public static void main(String[] args) {
		
		int numOfRegisters = 5;
		int maxProgramLength = 20;
		long maxAbsMovConstant = 20;
		
		LGP lgp = new LGP(1_000, Double.POSITIVE_INFINITY, 1_000_000, 0.15,
				new ArraySortingFitnessFunction(numOfRegisters, 10000), 
				new LGPCrossover2(maxProgramLength),
				new LGPMutation2(numOfRegisters, maxAbsMovConstant), 
				new LGPPopulationInitializer(maxProgramLength, numOfRegisters, maxAbsMovConstant));
		var solution = lgp.run();
		System.out.println(solution.getFitness());
		System.out.println(solution);
		
	}
	
}
