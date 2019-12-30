package hr.fer.zemris.projekt.algorithm;

import hr.fer.zemris.projekt.algorithm.differential.crossover.DifferentialCrossover;
import hr.fer.zemris.projekt.algorithm.differential.mutation.DifferentialMutation;
import hr.fer.zemris.projekt.algorithm.differential.selection.DifferentialSelection;
import hr.fer.zemris.projekt.algorithm.fitness.FitnessFunction;
import hr.fer.zemris.projekt.algorithm.initializer.PopulationInitializer;
import hr.fer.zemris.projekt.algorithm.solution.Solution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class DifferentialEvolution implements OptimizationAlgorithm<Solution<Double>> {

    private final Logger logger = LoggerFactory.getLogger(DifferentialEvolution.class);
    
    private final int populationSize;
    private final int maximumNumberOfIterations;
    private final double fitnessThreshold;
    private final int numberOfIterationsBetweenRefreshes;

    private final DifferentialCrossover<Solution<Double>> crossover;
    private final DifferentialMutation<Solution<Double>> mutation;
    private final DifferentialSelection<Solution<Double>> selection;

    private final FitnessFunction<Solution<Double>> fitnessFunction;
    private final PopulationInitializer<Solution<Double>> populationInitializer;

    public DifferentialEvolution(int populationSize, int maximumNumberOfIterations, double fitnessThreshold, int numberOfIterationsBetweenRefreshes, DifferentialCrossover<Solution<Double>> crossover, DifferentialMutation<Solution<Double>> mutation, DifferentialSelection<Solution<Double>> selection, FitnessFunction<Solution<Double>> fitnessFunction, PopulationInitializer<Solution<Double>> populationInitializer) {
        this.populationSize = populationSize;
        this.maximumNumberOfIterations = maximumNumberOfIterations;
        this.fitnessThreshold = fitnessThreshold;
        this.numberOfIterationsBetweenRefreshes = numberOfIterationsBetweenRefreshes;
        this.crossover = crossover;
        this.mutation = mutation;
        this.selection = selection;
        this.fitnessFunction = fitnessFunction;
        this.populationInitializer = populationInitializer;
    }

    @Override
    public Solution<Double> run() {
        Solution<Double> bestSolution = null;
        List<Solution<Double>> population = populationInitializer.generatePopulation(populationSize);

        for (int iteration = 0; iteration < maximumNumberOfIterations; iteration++) {
            if ((iteration % numberOfIterationsBetweenRefreshes) == 0) {
                if (fitnessFunction.refreshFitness()) {
                    for (Solution<Double> solution : population) {
                        solution.resetIsEvaluated();
                    }

                    if (bestSolution != null) {
                        bestSolution.setFitness(fitnessFunction.calculateFitness(bestSolution));
                    }
                }
            }

            // Evaluate current population
            for (Solution<Double> solution : population) {
                if (!solution.isEvaluated()) {
                    solution.setFitness(fitnessFunction.calculateFitness(solution));
                }
            }

            // Mutation
            List<Solution<Double>> mutantPopulation = mutation.createMutantPopulation(population);

            // Crossover
            List<Solution<Double>> trialPopulation = new ArrayList<>(mutantPopulation.size());

            for (int trialIndex = 0; trialIndex < mutantPopulation.size(); trialIndex++) {
                trialPopulation.add(crossover.crossover(population.get(trialIndex), mutantPopulation.get(trialIndex)));
            }

            // Evaluate trial population
            for (Solution<Double> solution : trialPopulation) {
                if (!solution.isEvaluated()) {
                    solution.setFitness(fitnessFunction.calculateFitness(solution));
                }
            }

            // Select next population
            for (int trialIndex = 0; trialIndex < trialPopulation.size(); trialIndex++) {
                population.set(trialIndex, selection.select(population.get(trialIndex), trialPopulation.get(trialIndex)));
            }

            // Elitism
            Solution<Double> currentBest = population
                    .stream()
                    .max(Comparator.comparingDouble(Solution::getFitness))
                    .get();

            if (bestSolution == null || bestSolution.getFitness() < currentBest.getFitness()) {
                bestSolution = currentBest;
                logger.debug("Iteration {}: found new best solution with fitness {}", iteration, bestSolution.getFitness());

                if (bestSolution.getFitness() >= fitnessThreshold) {
                    if (fitnessFunction.refreshFitness()) {
                        bestSolution.setFitness(fitnessFunction.calculateFitness(bestSolution));

                        if (bestSolution.getFitness() >= fitnessThreshold) {
                            break;
                        }

                        for (Solution<Double> solution : population) {
                            solution.resetIsEvaluated();
                        }
                    }
                    else {
                        break;
                    }
                }
            }
        }
        
        return bestSolution;
    }
    
}
