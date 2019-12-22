package hr.fer.zemris.projekt.algorithm.tree_based_GP_implementation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import hr.fer.zemris.projekt.algorithm.OptimizationAlgorithm;
import hr.fer.zemris.projekt.algorithm.fitness.FitnessFunction;

public class TreeAlgorithm implements OptimizationAlgorithm<Tree>{
	private Random random = new Random();
	private TreeCrossover cross = new TreeCrossover();
	private TreeMutation mutation = new TreeMutation();
	private FitnessFunction<Tree> fitnessFunction;
	
	private double reproductionChance; 
	private double mutationChance; 		//sansa mutacije za pojedinacno stablo
	private int populationSize;
	private int terminationFitnessValue;
	private int maxGen = 50;
	
	
	public TreeAlgorithm(int populationSize,int terminationFitnessValue, double reproductionChance, double mutationChance,FitnessFunction<Tree> f) {
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
		
		while((selection.selectFromPopulation(population).getFitness() < terminationFitnessValue) && gen < maxGen) {
			System.out.print("generation " + gen + "	"+"maxFit " +selection.selectFromPopulation(population).getFitness() );
			
			Stream<Tree> stream = population.stream();
			List<Double> fit = stream.map((t) -> t.getFitness()).collect(Collectors.toList());
			System.out.println(fit);
			//test
			gen++;
			List<Tree> newPopulation = new ArrayList<>();
			
			//prenosi u novu populaciju odredjen broj rjesenja sa najvecim fitnessom
			for (int i = 0; i < reproductionChance*populationSize; i++) {
				newPopulation.add(population.get(population.size()-1-i));	
			} 
			population.removeAll(newPopulation);
			//dodaje u novu populaciju crossover od rjesenja s navecim fitnessom
			int selectionSize = population.size();
			for (int i = 0; i < (selectionSize / 2); i++) {
				Tree t1;
				Tree t2;
				
				if (population.size() > 2) {
					int r1 = random.nextInt(population.size());
					t1 = population.get(r1);
					int r2 = random.nextInt(population.size());
					t2 = population.get(r2);
					population.remove(t1);
					population.remove(t2);
				}
				else {
					t1 = population.get(0);
					t2 = population.get(1);
					population.remove(t1);
					population.remove(t2);
				}
				
				newPopulation.add(cross.crossover(t1, t2));
				newPopulation.add(cross.crossover(t2, t1));
				if (population.size() == 0 ) break;
			}
			
			List<Tree> selected = new ArrayList();
			List<Tree> mutated = new ArrayList();
			for (int i = 0; i < newPopulation.size(); i++) {
				if (random.nextDouble() < mutationChance) {
					Tree temp = newPopulation.get(i);
					selected.add(temp);
					mutated.add(mutation.mutate(temp));
			
				}
			}
			
			newPopulation.addAll(mutated);
			newPopulation.removeAll(selected);
			
			population = newPopulation;	
			eval(population);
		}
		System.out.println("solution ");
		System.out.println(selection.selectFromPopulation(population));
		System.out.println(selection.selectFromPopulation(population).getFitness());
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
