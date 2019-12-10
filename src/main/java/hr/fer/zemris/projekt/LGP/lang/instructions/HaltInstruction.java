package hr.fer.zemris.projekt.LGP.lang.instructions;

import java.util.Objects;

import hr.fer.zemris.projekt.LGP.lang.EasyLGPContext;
import hr.fer.zemris.projekt.LGP.lang.EasyLGPInstruction;

public class HaltInstruction implements EasyLGPInstruction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6574043770944133591L;

	@Override
	public InstructionResult execute(EasyLGPContext context) {
		return new InstructionResult(InstructionResultStatus.HALT);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof DivInstruction))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "HALT";
	}

}
