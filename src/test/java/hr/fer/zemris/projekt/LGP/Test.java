package hr.fer.zemris.projekt.LGP;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Test {
	
	public static void main(String[] args) {
		
		List<Double> fitness = new ArrayList<>();
		fitness.add(-100D);
		fitness.add(-150D);
		fitness.add(0D);
		fitness.add(50D);
		fitness.add(100D);
		fitness.add(100D);
		fitness.add(150D);
		
		double shift = Math.abs(Collections.min(fitness)) + 1;
		double shiftFitnessSum = 0;
		for(var f : fitness) {
			shiftFitnessSum += f + shift;
		}
		
		double rnd = Math.random();
		double increment = 0;
		for(int i=0; i<fitness.size(); i++) {
			double shiftedFitness = fitness.get(i) + shift;
			increment += shiftedFitness/shiftFitnessSum;
			if(rnd < increment) {
				System.out.println(i);
				break;
			}
		}
		
		System.out.println(increment);
		
	}

}
