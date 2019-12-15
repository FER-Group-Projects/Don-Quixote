package hr.fer.zemris.projekt.algorithm.tree_based_GP_implementation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import hr.fer.zemris.projekt.algorithm.OptimizationAlgorithm;
import hr.fer.zemris.projekt.algorithm.fitness.FitnessFunction;

public class TreeAlgorithm implements OptimizationAlgorithm<Tree>{
	private Random random = new Random();
	private TreeCrossover cross = new TreeCrossover();
	private TreeMutation mutation = new TreeMutation();
	private FitnessFunction<Tree> fitnessFunction;
	private double crossOverChance; 	//mnozenjem sa velicinom populacije daje broj stabala odabranih za crossover
	private double reproductionChance; //crossOverChance + reproducationChance = 1.0 
	private double mutationChance; 		//sansa mutacije za pojedinacno stablo
	private int populationSize;
	private int terminationFitnessValue;
	private int randomSubsetSize = 50;
	private int maxGen = 50;
	
	@SuppressWarnings("unchecked")
	public TreeAlgorithm(int populationSize,int terminationFitnessValue, int reproductionChance, int mutationChance,FitnessFunction<Tree> f) {
		this.populationSize = populationSize;
		this.terminationFitnessValue = terminationFitnessValue;
		this.reproductionChance = reproductionChance;
		this.mutationChance = mutationChance;
		this.fitnessFunction = f;
	}
	
	@Override
	public Tree run() {
		int gen = 0;
		TreeSelection selection = new TreeSelection();
		List<Tree> population = new TreeInitializer().generatePopulation(populationSize);
		eval(population);
		
		while(selection.selectFromPopulation(population).getFitness() < terminationFitnessValue | gen > maxGen) {
			gen++;
			List<Tree> newPopulation = new ArrayList<>();
			
			//prenosi u novu populaciju odredjen broj rjesenja sa najvecim fitnessom
			for (int i = populationSize-1,j = 0; i >= reproductionChance*populationSize; i--,j++) {
				newPopulation.add(population.get(i));	
				population.remove(j);
			} 
			
			//dodaje u novu populaciju crossover od rjesenja s navecim fitnessom
			int selectionSize = population.size();
			for (int i = 0; i < (selectionSize / 2); i++) {
				int r1 = random.nextInt(population.size());
				Tree t1 = population.get(r1);
				population.remove(r1);
				int r2 = random.nextInt(population.size());
				Tree t2 = population.get(r2);
				population.remove(r2);
				
				newPopulation.add(cross.crossover(t1, t2));
				newPopulation.add(cross.crossover(t2, t1));
			}
			
			for (Tree t : newPopulation) {
				if (random.nextDouble() > mutationChance) {
					newPopulation.add(mutation.mutate(t));
					newPopulation.remove(t);
				}
			}
			
			population = newPopulation;	
			eval(population);
		}
		
		return selection.selectFromPopulation(population);
	}
		
	
	public void eval(List<Tree> population) {
		for (Tree t : population) {
			if (!t.isEvaluated()) {
				t.setFitness(fitnessFunction.calculateFitness(t));
				t.setIsEvaluated(true);
			}
		}
		Collections.sort(population); //uzlazni fitnessi
	}
	
}
