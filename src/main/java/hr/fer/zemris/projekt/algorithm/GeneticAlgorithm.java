package hr.fer.zemris.projekt.algorithm;

import hr.fer.zemris.projekt.algorithm.fitness.FitnessFunction;
import hr.fer.zemris.projekt.algorithm.genetic.crossover.GeneticCrossover;
import hr.fer.zemris.projekt.algorithm.genetic.mutation.GeneticMutation;
import hr.fer.zemris.projekt.algorithm.genetic.selection.GeneticSelection;
import hr.fer.zemris.projekt.algorithm.initializer.PopulationInitializer;
import hr.fer.zemris.projekt.algorithm.solution.Solution;

import java.util.ArrayList;
import java.util.List;

public class GeneticAlgorithm<S extends Solution<?>> implements OptimizationAlgorithm<S> {


    private final int populationSize;
    private final int maxIterations;
    private final double fitnessThreshold;
    private final GeneticCrossover<S> crossover;
    private final GeneticMutation<S> mutation;
    private final GeneticSelection<S> selection;
    private final FitnessFunction<S> fitness;
    PopulationInitializer<S> initializer;


    public GeneticAlgorithm(int populationSize, int maxIterations, double fitnessThreshold, GeneticCrossover<S> crossover, GeneticMutation<S> mutation, GeneticSelection<S> selection,
                            FitnessFunction<S> fitness, PopulationInitializer<S> initializer) {
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
    public S run() {

        S bestSolution = null;
        List<S> population = initializer.generatePopulation(populationSize);

        for (int i = 0; i < maxIterations; i++) {

            for (S solution : population) {
                if (!solution.isEvaluated()) {
                    solution.setFitness(fitness.calculateFitness(solution));
                }
                if (bestSolution == null || solution.getFitness() > bestSolution.getFitness()) {
                    bestSolution = solution;
					System.out.println("Iteration: " + i + "   Fitness " + bestSolution.getFitness());
				}
            }

            if (bestSolution.getFitness() > fitnessThreshold) {
                System.out.println("Iteration: " + i + "   Fitness " + bestSolution.getFitness());

                break;

            }

            List<S> newPopulation = new ArrayList<>();

            for (int iteration = 0; iteration < populationSize; iteration++) {
                S firstParent = selection.selectFromPopulation(population);
                S secondParent = selection.selectFromPopulation(population);

                S child = crossover.crossover(firstParent, secondParent);
                S mutatedChild = mutation.mutate(child);

                newPopulation.add(mutatedChild);
            }

            population = newPopulation;

        }

        return bestSolution;
    }

}
