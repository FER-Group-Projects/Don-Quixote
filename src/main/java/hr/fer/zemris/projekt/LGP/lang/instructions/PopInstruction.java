package hr.fer.zemris.projekt.LGP.lang.instructions;

import java.util.Objects;

import hr.fer.zemris.projekt.LGP.lang.EasyLGPContext;
import hr.fer.zemris.projekt.LGP.lang.EasyLGPInstruction;

public class PopInstruction implements EasyLGPInstruction {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6563673020539849134L;
	private int reg;

	public PopInstruction(int reg) {
		this.reg = reg;
	}

	@Override
	public InstructionResult execute(EasyLGPContext context) {
		context.setRegister(reg, context.pop());
		
		return new InstructionResult(InstructionResultStatus.CONTINUE);
	}

	@Override
	public int hashCode() {
		return Objects.hash(reg);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof PopInstruction))
			return false;
		PopInstruction other = (PopInstruction) obj;
		return reg == other.reg;
	}

	@Override
	public String toString() {
		return "POP " + reg;
	}

}
