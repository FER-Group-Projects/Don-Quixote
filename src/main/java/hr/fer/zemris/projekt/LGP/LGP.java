package hr.fer.zemris.projekt.LGP;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import hr.fer.zemris.projekt.LGP.lang.EasyLGPInstruction;
import hr.fer.zemris.projekt.algorithm.OptimizationAlgorithm;
import hr.fer.zemris.projekt.algorithm.crossover.Crossover;
import hr.fer.zemris.projekt.algorithm.fitness.FitnessFunction;
import hr.fer.zemris.projekt.algorithm.initializer.PopulationInitializer;
import hr.fer.zemris.projekt.algorithm.mutation.Mutation;
import hr.fer.zemris.projekt.algorithm.solution.Solution;

public class LGP implements OptimizationAlgorithm<Solution<EasyLGPInstruction>> {
	
	private int populationSize;
	private double fitnessThreshold;
	private int maxIterations;
	
	private FitnessFunction<Solution<EasyLGPInstruction>> fitnessFunction;
	private Crossover<Solution<EasyLGPInstruction>> crossoverOperator;
	private Mutation<Solution<EasyLGPInstruction>> mutationOperator;
	private PopulationInitializer<Solution<EasyLGPInstruction>> populationInitializer;
	
	private Comparator<Solution<?>> cmpRev = (s1, s2) -> Double.compare(s2.getFitness(), s1.getFitness());

	public LGP(int populationSize, double fitnessThreshold, int maxIterations,
			FitnessFunction<Solution<EasyLGPInstruction>> fitnessFunction,
			Crossover<Solution<EasyLGPInstruction>> crossoverOperator,
			Mutation<Solution<EasyLGPInstruction>> mutationOperator,
			PopulationInitializer<Solution<EasyLGPInstruction>> populationInitializer) {
		this.populationSize = populationSize;
		this.fitnessThreshold = fitnessThreshold;
		this.maxIterations = maxIterations;
		this.fitnessFunction = fitnessFunction;
		this.crossoverOperator = crossoverOperator;
		this.mutationOperator = mutationOperator;
		this.populationInitializer = populationInitializer;
	}

	@Override
	public Solution<EasyLGPInstruction> run() {
		// Initialization
		List<Solution<EasyLGPInstruction>> population = populationInitializer.generatePopulation(populationSize);
		populationSort(population);
		
		int iter=0;
		while(iter<maxIterations && population.get(0).getFitness()<fitnessThreshold) {
			List<Solution<EasyLGPInstruction>> newPopulation = new ArrayList<>();
			
			// Selection and crossover
	outer:	for(int i=1; i<population.size(); i++) {
				for(int j=0; j<i; j++) {
					newPopulation.add(crossoverOperator.crossover(population.get(j), population.get(i)));
					if(newPopulation.size()==populationSize) break outer;
				}
			}
			
			// Mutation
			for(var sln : newPopulation) {
				mutationOperator.mutate(sln);
			}
			
			// Fitness calculation
			population = newPopulation;
			populationSort(population);
		}
		
		return population.get(0);
	}
	
	private void populationSort(List<Solution<EasyLGPInstruction>> population) {
		for(var solution : population) solution.setFitness(fitnessFunction.calculateFitness(solution));
		population.sort(cmpRev);
	}
	
}
