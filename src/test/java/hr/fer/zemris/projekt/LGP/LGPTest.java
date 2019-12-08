package hr.fer.zemris.projekt.LGP;

public class LGPTest {
	
	public static void main(String[] args) {
		
		LGP lgp = new LGP(500, Double.POSITIVE_INFINITY, 1_000_000, 0.2,
				new ArraySortingFitnessFunction(5, 10000), 
				new LGPCrossover2(50), 
				new LGPMutation2(), 
				new LGPPopulationInitializer(500));
		var solution = lgp.run();
		System.out.println(solution.getFitness());
		
	}
	
}
