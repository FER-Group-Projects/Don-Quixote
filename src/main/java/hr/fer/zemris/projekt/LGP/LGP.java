package hr.fer.zemris.projekt.LGP;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

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
	private int maxGenerations;
	private double mutationRate;
	
	private FitnessFunction<Solution<EasyLGPInstruction>> fitnessFunction;
	private Crossover<Solution<EasyLGPInstruction>> crossoverOperator;
	private Mutation<Solution<EasyLGPInstruction>> mutationOperator;
	private PopulationInitializer<Solution<EasyLGPInstruction>> populationInitializer;
	
	private Comparator<Solution<?>> cmpRev = (s1, s2) -> Double.compare(s2.getFitness(), s1.getFitness());
	private Random random = new Random();

	public LGP(int populationSize, double fitnessThreshold, int maxGenerations, double mutationRate,
			FitnessFunction<Solution<EasyLGPInstruction>> fitnessFunction,
			Crossover<Solution<EasyLGPInstruction>> crossoverOperator,
			Mutation<Solution<EasyLGPInstruction>> mutationOperator,
			PopulationInitializer<Solution<EasyLGPInstruction>> populationInitializer) {
		this.populationSize = populationSize;
		this.fitnessThreshold = fitnessThreshold;
		this.maxGenerations = maxGenerations;
		this.mutationRate = mutationRate;
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
		while(iter<maxGenerations && population.get(0).getFitness()<fitnessThreshold) {
			List<Solution<EasyLGPInstruction>> newPopulation = new ArrayList<>();
			
			// Selection, crossover and mutation
	outer:	for(int i=1; i<population.size(); i++) {
				for(int j=0; j<i; j++) {
					var newSolution = crossoverOperator.crossover(population.get(j), population.get(i));
					if(random.nextDouble() < mutationRate)
						newSolution = mutationOperator.mutate(newSolution);
					newPopulation.add(newSolution);
					if(newPopulation.size()==populationSize) break outer;
				}
			}
			
			// Fitness calculation
			population = newPopulation;
			populationSort(population);
			iter++;
			System.out.println(population.get(0).getFitness() + " : " + population.get(0).getGeneAt(0));
		}
		
		return population.get(0);
	}
	
	private void populationSort(List<Solution<EasyLGPInstruction>> population) {
		for(var solution : population) solution.setFitness(fitnessFunction.calculateFitness(solution));
		population.sort(cmpRev);
	}
	
}
