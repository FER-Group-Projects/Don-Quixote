package hr.fer.zemris.projekt.model.test;

import java.util.Arrays;

import hr.fer.zemris.projekt.algorithm.tree_based_GP_implementation.Tree;
import hr.fer.zemris.projekt.algorithm.tree_based_GP_implementation.TreeCrossover;
import hr.fer.zemris.projekt.algorithm.tree_based_GP_implementation.TreeEngine;
import hr.fer.zemris.projekt.algorithm.tree_based_GP_implementation.TreeInitializer;
import hr.fer.zemris.projekt.algorithm.tree_based_GP_implementation.TreeMutation;

public class treeTest {  
	
	
	public static void main(String[] args) {
		

		double[] l = {5.5,1.2,2.7,0.0};
		Tree solution1 = new TreeInitializer().generateSolution();
		Tree solution2 = new TreeInitializer().generateSolution();
		System.out.println("tree 1 \n" + solution1);
		System.out.println("tree 2 \n" + solution2);
		
		Tree solution3 = new TreeCrossover().crossover(solution1, solution2);

		System.out.println("result tree (branch)tree2--->(branch)tree1 \n" + solution3);
		System.out.println("input list = " + Arrays.toString(l));
		System.out.println("computation of new tree for list = " + TreeEngine.compute(solution3, l));
		System.out.println("mutant new tree \n");
		Tree mutation = new TreeMutation().mutate(solution3);
		System.out.println(mutation);
		
	}

}
