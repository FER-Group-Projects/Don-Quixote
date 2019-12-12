package hr.fer.zemris.projekt.LGP.lang.instructions;

import java.util.Objects;

import hr.fer.zemris.projekt.LGP.lang.EasyLGPContext;
import hr.fer.zemris.projekt.LGP.lang.EasyLGPInstruction;

public class JpInstruction implements EasyLGPInstruction {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6716482207590808765L;

	public enum Condition {
		NEGATIVE("_N"),
		NON_NEGATIVE("_NN"),
		ZERO("_Z"),
		NON_ZERO("_NZ"),
		EQUALS("_EQ"),
		NOT_EQUALS("_NEQ"),
		LESS_THAN_OR_EQUALS("_LEQ"),
		GREATER_THAN_OR_EQUALS("_GEQ"),
		ALWAYS("");
		
		private String condition;

		private Condition(String condition) {
			this.condition = condition;
		}
		
		@Override
		public String toString() {
			return condition;
		}
	}
	
	private Condition condition;
	private int address;

	public JpInstruction(Condition condition, int address) {
		this.condition = Objects.requireNonNull(condition);
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
							   condition == Condition.GREATER_THAN_OR_EQUALS && (!context.isNegative() || context.isZero()) ||
							   condition == Condition.ALWAYS;
		
		if(conditionMet) {
			return new InstructionResult(InstructionResultStatus.JUMP, address);
		} else {
			return new InstructionResult(InstructionResultStatus.CONTINUE);
		}
	}

	@Override
	public int hashCode() {
		return Objects.hash(address, condition);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof JpInstruction))
			return false;
		JpInstruction other = (JpInstruction) obj;
		return address == other.address && condition == other.condition;
	}
	
	@Override
	public String toString() {
		return "JP" + condition + " " + address;
	}	

}
