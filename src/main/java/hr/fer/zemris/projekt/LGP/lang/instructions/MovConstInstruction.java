package hr.fer.zemris.projekt.LGP.lang.instructions;

import java.util.Objects;

import hr.fer.zemris.projekt.LGP.lang.EasyLGPContext;
import hr.fer.zemris.projekt.LGP.lang.EasyLGPInstruction;

public class MovConstInstruction implements EasyLGPInstruction {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5472710173686017486L;
	private long regTo;
	private long constant;
	
	public MovConstInstruction(long regTo, long constant) {
		this.regTo = regTo;
		this.constant = constant;
	}

	@Override
	public InstructionResult execute(EasyLGPContext context) {
		context.setRegister(regTo, constant);
		return new InstructionResult(InstructionResultStatus.CONTINUE);
	}

	@Override
	public int hashCode() {
		return Objects.hash(constant, regTo);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof MovConstInstruction))
			return false;
		MovConstInstruction other = (MovConstInstruction) obj;
		return constant == other.constant && regTo == other.regTo;
	}
	
	@Override
	public String toString() {
		return "MOV " + regTo + " " + constant;
	}

}
