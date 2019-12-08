package hr.fer.zemris.projekt.LGP.lang.instructions;

import java.util.Objects;

import hr.fer.zemris.projekt.LGP.lang.EasyLGPContext;
import hr.fer.zemris.projekt.LGP.lang.EasyLGPInstruction;

public class LdrInstruction implements EasyLGPInstruction {
	
	private long valueRegister;
	private long addressRegister;
	
	public LdrInstruction(long valueRegister, long addressRegister) {
		this.valueRegister = valueRegister;
		this.addressRegister = addressRegister;
	}

	@Override
	public InstructionResult execute(EasyLGPContext context) {
		long memAddress = context.getRegister(addressRegister);
		context.setRegister(valueRegister, context.getMemory(memAddress));
		return new InstructionResult(InstructionResultStatus.CONTINUE);
	}

	@Override
	public int hashCode() {
		return Objects.hash(addressRegister, valueRegister);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof LdrInstruction))
			return false;
		LdrInstruction other = (LdrInstruction) obj;
		return addressRegister == other.addressRegister && valueRegister == other.valueRegister;
	}
	
	@Override
	public String toString() {
		return "LDR " + valueRegister + " [" + addressRegister + "]";
	}

}
