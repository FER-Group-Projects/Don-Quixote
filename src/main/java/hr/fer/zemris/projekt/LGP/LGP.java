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
	private long maxGenerations;
	private double mutationRate;
	
	private FitnessFunction<Solution<EasyLGPInstruction>> fitnessFunction;
	private Crossover<Solution<EasyLGPInstruction>> crossoverOperator;
	private Mutation<Solution<EasyLGPInstruction>> mutationOperator;
	private PopulationInitializer<Solution<EasyLGPInstruction>> populationInitializer;
	
	private double totalPopulationShiftedFitness;
	private double minimalPopulationFitness;
	
	private Comparator<Solution<?>> cmpRev = (s1, s2) -> Double.compare(s2.getFitness(), s1.getFitness());
	private Random random = new Random();

	public LGP(int populationSize, double fitnessThreshold, long maxGenerations, double mutationRate,
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
		
		long iter=0;
		while(iter<maxGenerations && population.get(0).getFitness()<fitnessThreshold) {
			List<Solution<EasyLGPInstruction>> newPopulation = new ArrayList<>();
			
			// Selection, crossover and mutation
			while(newPopulation.size() != populationSize) {
				var parent1 = selection(population);
				var parent2 = selection(population);
				var newSolution = crossoverOperator.crossover(parent1, parent2);
				if(random.nextDouble() < mutationRate)
					newSolution = mutationOperator.mutate(newSolution);
				newPopulation.add(newSolution);
			}
			
			// Fitness calculation
			population = newPopulation;
			populationSort(population);
			iter++;
			System.out.println(population.get(0).getFitness() + " : " + population.get(0).getGeneAt(0));
		}
		
		return population.get(0);
	}
	
	private Solution<EasyLGPInstruction> selection(List<Solution<EasyLGPInstruction>> population) {
		double rnd = random.nextDouble();
		
		double increment = 0;
		for(var solution : population) {
			double shiftedFitness = solution.getFitness() - minimalPopulationFitness;
			double probabilityInterval = shiftedFitness/totalPopulationShiftedFitness + increment;
			if(rnd < probabilityInterval)
				return solution;
			increment+=probabilityInterval;
		}
		
		return null; // Never
	}
	
	private void populationSort(List<Solution<EasyLGPInstruction>> population) {
		double totalShiftedFitness = 0;
		double minValue = Double.MAX_VALUE;
		for(var solution : population) {
			double fitness = fitnessFunction.calculateFitness(solution);
			solution.setFitness(fitness);
			if(fitness < minValue)
				minValue = fitness;
		}
		for(var solution : population) {
			totalShiftedFitness += solution.getFitness() - minValue;
		}
		
		population.sort(cmpRev);
		
		this.totalPopulationShiftedFitness = totalShiftedFitness;
		this.minimalPopulationFitness = minValue;
	}
	
}
