package hr.fer.zemris.projekt.LGP;

import hr.fer.zemris.projekt.LGP.lang.EasyLGPContext;
import hr.fer.zemris.projekt.LGP.lang.EasyLGPEngine;
import hr.fer.zemris.projekt.LGP.lang.EasyLGPException;
import hr.fer.zemris.projekt.LGP.lang.EasyLGPInstruction;
import hr.fer.zemris.projekt.algorithm.fitness.FitnessFunction;
import hr.fer.zemris.projekt.algorithm.solution.Solution;

// Calculating the value of f(x,y) = abs|x-y| + y^2
public class FunctionCalcFitnessFunction implements FitnessFunction<Solution<EasyLGPInstruction>> {
	
	long numberOfRegisters;
	long maxSteps;

	public FunctionCalcFitnessFunction(long numberOfRegisters, long maxSteps) {
		this.numberOfRegisters = numberOfRegisters;
		this.maxSteps = maxSteps;
	}

	@Override
	public double calculateFitness(Solution<EasyLGPInstruction> solution) {
		
		if(!(solution instanceof LGPSolution)) throw new IllegalArgumentException();
		
		LGPSolution sln = (LGPSolution) solution;
		
		if(sln.getGeneAt(0).toString().equals("HALT")) return Double.NEGATIVE_INFINITY;

		double fitness = 0;
		
		for(int x=-10; x<=10; x++) {
			for(int y=-10; y<=10; y++) {
				long f = f(x, y);
				
				EasyLGPContext context = new EasyLGPContext(numberOfRegisters);
				context.setRegister(0, x);
				context.setRegister(1, y);
				
				try {
					EasyLGPEngine.execute(sln.getInstructions(), context, maxSteps);
				} catch (EasyLGPException | ArithmeticException ex) {
					return Double.NEGATIVE_INFINITY;
				}
				
				if(context.getRegister(2) == f) {
					fitness += 10;
					fitness -= 5;
				}
				
			}
		}
		
		return fitness;
		
	}
	
	private long f(long x, long y) {
		return Math.abs(x-y) + y*y;
	}

}
