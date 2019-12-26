package hr.fer.zemris.projekt.algorithm.tree_based_GP_implementation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
			case "%":
				return (compute(left,input) % compute(right,input));
			case "sin+":
				return (Math.sin(compute(left,input)) + Math.sin(compute(right,input)));
			case "sin-":
				return (Math.sin(compute(left,input)) - Math.sin(compute(right,input)));
			case "cos+":
				return (Math.cos(compute(left,input)) + Math.cos(compute(right,input)));
			case "cos-":
				return (Math.cos(compute(left,input)) - Math.cos(compute(right,input)));
			case "tan+":
				return (Math.tan(compute(left,input)) + Math.tan(compute(right,input)));
			case "tan-":
				return (Math.tan(compute(left,input)) - Math.tan(compute(right,input)));
			default:
				return(0.0);
			}	
		}
	}	
	
	public static PlayerAction calculatePlayerAction(double value) {
		//kod koji mapira izlaz compute funkcije u akciju
		
		double doubleVal = 6*(Math.sin(value)+1)/2.0;
		
		int index = (int)Math.floor(doubleVal);
		List<PlayerAction> lista = new ArrayList<PlayerAction>(Arrays.asList(PlayerAction.values()));
		return lista.get(index);
		
	}

}
