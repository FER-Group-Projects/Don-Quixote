package hr.fer.zemris.projekt.LGP;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import hr.fer.zemris.projekt.LGP.lang.EasyLGPContext;
import hr.fer.zemris.projekt.LGP.lang.EasyLGPEngine;
import hr.fer.zemris.projekt.LGP.lang.EasyLGPException;
import hr.fer.zemris.projekt.LGP.lang.EasyLGPInstruction;
import hr.fer.zemris.projekt.algorithm.fitness.FitnessFunction;
import hr.fer.zemris.projekt.algorithm.solution.Solution;

public class ArraySortingFitnessFunction implements FitnessFunction<Solution<EasyLGPInstruction>> {
	
	long[][] arrays;
	int numberOfRegisters;
	long maxSteps;
	double minimalFitness;

	public ArraySortingFitnessFunction(long[][] arraysToSort, int numberOfRegisters, long maxSteps) {
		this.arrays = Objects.requireNonNull(arraysToSort);
		this.numberOfRegisters = numberOfRegisters;
		this.maxSteps = maxSteps;
		
		this.minimalFitness = 0;
		for(int i=0; i<arrays.length; i++) {
			minimalFitness -= arrays[i].length * (arrays[i].length - 1);
		}
	}

	@Override
	public double calculateFitness(Solution<EasyLGPInstruction> solution) {
		
		if(!(solution instanceof LGPSolution)) throw new IllegalArgumentException();
		
		LGPSolution sln = (LGPSolution) solution;
		
		if(sln.getGeneAt(0).toString().equals("HALT")) return minimalFitness;

		double fitness = 0;
		
		for(long[] array : arrays) {
			int inv = calculateNumberOfInverses(array);
			EasyLGPContext context = new EasyLGPContext(numberOfRegisters);
			setupContext(context, array);
			
			try {
				EasyLGPEngine.execute(sln.getInstructions(), context, maxSteps);
			} catch (EasyLGPException | ArithmeticException ex) {
				return minimalFitness;
			}
			
			long[] arrayAfter = getArrayFromContext(context, array.length);
			if(!isPermutation(arrayAfter, array)) continue;
			int invAfter = calculateNumberOfInverses(arrayAfter);
			fitness += (inv - invAfter) / (double) inv;
		}
		
		return fitness;
		
	}
	
	private boolean isPermutation(long[] arrayAfter, long[] array) {
		
		if(array.length != arrayAfter.length) return false;
		
		Set<Long> arraySet = new HashSet<>();
		Set<Long> arrayAfterSet = new HashSet<>();
		
		for(long l : array) arraySet.add(l);
		for(long l : arrayAfter) arrayAfterSet.add(l);
		
		for(long l : arraySet) {
			if(!arrayAfterSet.remove(l)) return false;
		}
		
		if(arrayAfterSet.size()!=0) return false;
		
		return true;
	}

	private void setupContext(EasyLGPContext context, long[] array) {
		context.setRegister(0, array.length);
		for(int i=1; i<array.length+1; i++) {
			context.setRegister(i, array[i-1]);
		}
	}
	
	private long[] getArrayFromContext(EasyLGPContext context, int length) {
		long[] array = new long[length];
		for(int i=1; i<array.length+1; i++) {
			array[i-1] = (long) context.getRegister(i);
		}
		return array;
	}

	private int calculateNumberOfInverses(long[] array) {
		
		int inverses = 0;
		for(int i=0; i<array.length-1; i++) {
			for(int j=i+1; j<array.length; j++) {
				if(array[i] > array[j]) inverses++;
			}
		}
		return inverses;
		
	}

}
