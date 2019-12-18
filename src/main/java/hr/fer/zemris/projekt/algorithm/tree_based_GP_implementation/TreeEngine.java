package hr.fer.zemris.projekt.algorithm.tree_based_GP_implementation;

import hr.fer.zemris.projekt.model.controller.PlayerAction;

public class TreeEngine { 
	
	//prima input, prolazi kroz stablo i izvrsava operaicje nad inputom
	//ovdje dodavati funkcionalnost novih operacija
	public static double compute(Tree t, double[] input) {
		
		if (t.head.isTerminal()) return input[Integer.parseInt(t.head.getValue())];
		else {
			Tree left = new Tree(t.head.getLeft());
			Tree right = new Tree(t.head.getRight());
			
			switch(t.head.getValue()) {
			case "+":
				return(compute(left,input) + compute(right,input));
				
			case "-":
				return (compute(left,input) - compute(right,input)); 
			case "*":
				return (compute(left,input) * compute(right,input));
			
			default:
				return(0.0);
			}	
		}
	}	
	
	public static PlayerAction calculatePlayerAction(double value) {
		//kod koji mapira izlaz compute funkcije u akciju
		return PlayerAction.DOWN;
		
	}

}
