package hr.fer.zemris.projekt.algorithm.tree_based_GP_implementation;

import hr.fer.zemris.projekt.algorithm.mutation.Mutation;

public class TreeMutation implements Mutation<Tree>{
	
	public static double deletionChance = 0.5;
	@Override
	public Tree mutate(Tree solutionToMutate) {
		RandomNodes r = new RandomNodes(solutionToMutate.head);
		Node copy = solutionToMutate.head.copy();
		
		r.setRandomBranch(copy,new TreeInitializer().generateSolution().head);
		return new Tree(copy);
	}

}
