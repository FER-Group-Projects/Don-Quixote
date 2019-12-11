package hr.fer.zemris.projekt.algorithm.tree_based_GP_implementation;

import java.util.Random;

public class RandomNodes {
	private int count1;
	private int count2;
	Node head;
	Node gennode;
	private int randomNum1;
	private int randomNum2;
	private final Random random = new Random();
	
	public RandomNodes(Node head) {
		this.head = head;
		count1 = -1;
		count2 = -1;
		randomNum1 = random.nextInt(Tree.nodeSize(head));
		randomNum2 = random.nextInt(Tree.nodeSize(head));
	}
	
	public void getRandomBranch(Node head) {
		if (!head.isTerminal()) {
			count1++;
		
			if (count1 == randomNum1) gennode = head;
			getRandomBranch(head.getLeft());
			getRandomBranch(head.getRight());
		}		
	}
	
	public void setRandomBranch(Node head,Node branch) {
		if (!head.isTerminal) {
			count2++;
			
			if (count2 == randomNum2) {
				if (random.nextDouble() > 0.5) head.setLeft(branch);
				else {
					head.setRight(branch);
				}
			}
			setRandomBranch(head.getLeft(),branch);
			setRandomBranch(head.getRight(),branch);		
		}
	}	
}
