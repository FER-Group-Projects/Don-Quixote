package hr.fer.zemris.projekt.algorithm.LGP.lang.instructions;

import hr.fer.zemris.projekt.algorithm.LGP.lang.EasyLGPContext;
import hr.fer.zemris.projekt.algorithm.LGP.lang.EasyLGPInstruction;

public class JpInstruction implements EasyLGPInstruction {
	
	public enum Condition {
		NEGATIVE,
		NON_NEGATIVE,
		ZERO,
		NON_ZERO,
		EQUALS,
		NOT_EQUALS,
		LESS_THAN_OR_EQUALS,
		GREATER_THAN_OR_EQUALS,
		ALWAYS;
	}
	
	private Condition condition;
	private int address;

	public JpInstruction(Condition condition, int address) {
		this.condition = condition;
		this.address = address;
	}

	@Override
	public InstructionResult execute(EasyLGPContext context) {		
		boolean conditionMet = condition == Condition.NEGATIVE && context.isNegative() ||
							   condition == Condition.NON_NEGATIVE && !context.isNegative() ||
							   condition == Condition.ZERO && context.isZero() ||
							   condition == Condition.NON_ZERO && !context.isZero() ||
							   condition == Condition.EQUALS && context.isZero() ||
							   condition == Condition.NOT_EQUALS && !context.isZero() ||
							   condition == Condition.LESS_THAN_OR_EQUALS && (context.isNegative() || context.isZero()) ||
							   condition == Condition.GREATER_THAN_OR_EQUALS && (!context.isNegative() || context.isZero());
		
		if(conditionMet) {
			return new InstructionResult(InstructionResultStatus.JUMP, address);
		} else {
			return new InstructionResult(InstructionResultStatus.CONTINUE);
		}
	}

}
