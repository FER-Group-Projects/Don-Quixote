package hr.fer.zemris.projekt.LGP;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import hr.fer.zemris.projekt.LGP.lang.EasyLGPInstruction;
import hr.fer.zemris.projekt.algorithm.solution.Solution;

public class LGPSolution implements Solution<EasyLGPInstruction> {
	
	private List<EasyLGPInstruction> instructions = new ArrayList<>();
	private double fitness;
	private boolean evaluated;
	
	public LGPSolution() {
		
	}
	
	public List<EasyLGPInstruction> getInstructions() {
		return Collections.unmodifiableList(instructions);
	}
	
	public void setInstructions(List<EasyLGPInstruction> instructions) {
		this.instructions = new ArrayList<EasyLGPInstruction>(instructions);
	}

	@Override
	public double getFitness() {
		if(!evaluated) throw new IllegalStateException("Solution has not been evaluated!");
		return fitness;
	}

	@Override
	public void setFitness(double newFitness) {
		this.fitness = newFitness;
		this.evaluated = true;
	}

	@Override
	public boolean isEvaluated() {
		return evaluated;
	}

	@Override
	public int getNumberOfGenes() {
		return instructions.size();
	}

	@Override
	public EasyLGPInstruction getGeneAt(int index) {
		if(index < 0 || index >= instructions.size())
			throw new IllegalArgumentException();
		return instructions.get(index);
	}

	@Override
	public void setGeneAt(int index, EasyLGPInstruction newValue) {
		if(index < 0 || index > instructions.size())
			throw new IllegalArgumentException();
		
		if(index == instructions.size())
			instructions.add(Objects.requireNonNull(newValue));
		else
			instructions.set(index, Objects.requireNonNull(newValue));
	}

	@Override
	public Solution<EasyLGPInstruction> copy() {
		LGPSolution solution = new LGPSolution();
		solution.fitness = this.fitness;
		solution.instructions.addAll(this.instructions);
		return solution;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(evaluated, fitness, instructions);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof LGPSolution))
			return false;
		LGPSolution other = (LGPSolution) obj;
		return evaluated == other.evaluated
				&& Double.doubleToLongBits(fitness) == Double.doubleToLongBits(other.fitness)
				&& Objects.equals(instructions, other.instructions);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for(var instr : instructions) sb.append(instr.toString() + "\n");
		return sb.toString();
	}

}
