package hr.fer.zemris.projekt.algorithm;

import java.util.List;

import hr.fer.zemris.projekt.algorithm.fitness.FitnessFunction;
import hr.fer.zemris.projekt.algorithm.genetic.crossover.GeneticCrossover;
import hr.fer.zemris.projekt.algorithm.genetic.crossover.SinglePointCrossover;
import hr.fer.zemris.projekt.algorithm.genetic.mutation.GaussMutation;
import hr.fer.zemris.projekt.algorithm.genetic.mutation.GeneticMutation;
import hr.fer.zemris.projekt.algorithm.genetic.selection.GeneticSelection;
import hr.fer.zemris.projekt.algorithm.genetic.selection.TournamentSelection;
import hr.fer.zemris.projekt.algorithm.initializer.PopulationInitializer;
import hr.fer.zemris.projekt.algorithm.solution.Solution;

public class GeneticAlgorithm implements OptimizationAlgorithm<Solution<Double>> {
	
	
	private final int populationSize;
	private final int maxIterations;
	private final double fitnessThreshold;
	private final GeneticCrossover<Solution<Double>> crossover;
	private final GeneticMutation<Solution<Double>> mutation;
	private final GeneticSelection<Solution<Double>> selection;
	private final FitnessFunction<Solution<Double>> fitness;
	PopulationInitializer<Solution<Double>> initializer;

	
	
	public GeneticAlgorithm(int populationSize, int maxIterations, double fitnessThreshold, SinglePointCrossover crossover, GaussMutation mutation, TournamentSelection<Solution<Double>> selection
			                ,FitnessFunction<Solution<Double>> fitness, PopulationInitializer<Solution<Double>> initializer) {
		
		this.populationSize = populationSize;
		this.maxIterations = maxIterations;
		this.fitnessThreshold = fitnessThreshold;
		this.crossover = crossover;
		this.mutation = mutation;
		this.selection = selection;
		this.fitness = fitness;
		this.initializer = initializer;

		
	}
	

	@Override
	public Solution<Double> run() {
		
		Solution<Double> bestSolution = null;
		List<Solution<Double>> population = initializer.generatePopulation(populationSize);
		
		for (int i = 0; i < maxIterations; i++) {
			
			for (Solution<Double> solution : population) {
				if(!solution.isEvaluated()) {
					solution.setFitness(fitness.calculateFitness(solution));
				}
				if(bestSolution == null || solution.getFitness() > bestSolution.getFitness()) {
					bestSolution = solution;
				}    
			}
			
			if (bestSolution.getFitness()>fitnessThreshold) {
			    System.out.println("Iteration: " + i + "   Fitnes " + bestSolution.getFitness());

				break;
				
			}	
			
			List<Solution<Double>> newPopulation = initializer.generatePopulation(populationSize);
			
			for (int iteration = 0; iteration < populationSize; iteration++) {
				Solution<Double> firstParent = selection.selectFromPopulation(population);
				Solution<Double> secondParent = selection.selectFromPopulation(population);
				
				Solution<Double> child = crossover.crossover(firstParent, secondParent);
				Solution<Double> mutatedChild = mutation.mutate(child);

				newPopulation.add(mutatedChild);
			}
			
		    System.out.println("Iteration: " + i + "   Fitnes " + bestSolution.getFitness());
			population = newPopulation;
			
		}
		
		return bestSolution;
	}

}
