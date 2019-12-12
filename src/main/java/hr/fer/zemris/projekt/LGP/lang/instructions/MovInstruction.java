package hr.fer.zemris.projekt.LGP.lang.instructions;

import java.util.Objects;

import hr.fer.zemris.projekt.LGP.lang.EasyLGPContext;
import hr.fer.zemris.projekt.LGP.lang.EasyLGPInstruction;

public class MovInstruction implements EasyLGPInstruction {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 58964316451867475L;
	private long regTo;
	private long regFrom;
	
	public MovInstruction(long regTo, long regFrom) {
		this.regTo = regTo;
		this.regFrom = regFrom;
	}

	@Override
	public InstructionResult execute(EasyLGPContext context) {
		context.setRegister(regTo, context.getRegister(regFrom));
		return new InstructionResult(InstructionResultStatus.CONTINUE);
	}

	@Override
	public int hashCode() {
		return Objects.hash(regFrom, regTo);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof MovInstruction))
			return false;
		MovInstruction other = (MovInstruction) obj;
		return regFrom == other.regFrom && regTo == other.regTo;
	}
	
	@Override
	public String toString() {
		return "MOV " + regTo + " " + regFrom;
	}

}
