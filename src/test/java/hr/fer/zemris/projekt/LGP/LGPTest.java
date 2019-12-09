package hr.fer.zemris.projekt.LGP;

public class LGPTest {
	
	public static void main(String[] args) {
		
		int numOfRegisters = 5;
		LGP lgp = new LGP(1000, Double.POSITIVE_INFINITY, 1_000_000, 0.1,
				new ArraySortingFitnessFunction(numOfRegisters, 10000), 
				new LGPCrossover2(30),
				new LGPMutation2(numOfRegisters), 
				new LGPPopulationInitializer(30, numOfRegisters));
		var solution = lgp.run();
		System.out.println(solution.getFitness());
		System.out.println(solution);
		
	}
	
}
