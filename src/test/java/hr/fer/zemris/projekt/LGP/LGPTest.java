package hr.fer.zemris.projekt.LGP;

public class LGPTest {
	
	public static void main(String[] args) {
		
		int numOfRegisters = 20;
		int maxProgramLength = 0;
		long maxAbsMovConstant = 0;
		
		LGP lgp = new LGP(1_000, 3, Long.MAX_VALUE, 0.25,
				new LGPPopulationInitializer(maxProgramLength, numOfRegisters, maxAbsMovConstant),
				new LGPBlockCrossover(),
				new LGPBlockMutation(numOfRegisters, maxAbsMovConstant),
				new ArraySortingFitnessFunction(new long[][] {
					{10, 9, 8, 7, 6, 5, 4, 3, 2, 1},
					{5, 7, 3, 4, 10, 2, 1, 7, 6, 6},
					{15, 17, 18, 20, 11, 7, 21, 3, 22, 15}
				},numOfRegisters, 5000));
		var solution = lgp.run();
		System.out.println(solution.getFitness());
		System.out.println(solution);
		
	}
	
}
