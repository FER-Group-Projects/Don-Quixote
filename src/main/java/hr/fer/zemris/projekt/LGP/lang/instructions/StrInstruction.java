package hr.fer.zemris.projekt.LGP.lang.instructions;

import java.util.Objects;

import hr.fer.zemris.projekt.LGP.lang.EasyLGPContext;
import hr.fer.zemris.projekt.LGP.lang.EasyLGPInstruction;

public class StrInstruction implements EasyLGPInstruction {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1169732344294483078L;
	private long valueRegister;
	private long addressRegister;
	
	public StrInstruction(long valueRegister, long addressRegister) {
		this.valueRegister = valueRegister;
		this.addressRegister = addressRegister;
	}

	@Override
	public InstructionResult execute(EasyLGPContext context) {
		long memAddress = context.getRegister(addressRegister);
		context.setMemory(memAddress, context.getRegister(valueRegister));
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
		if (!(obj instanceof StrInstruction))
			return false;
		StrInstruction other = (StrInstruction) obj;
		return addressRegister == other.addressRegister && valueRegister == other.valueRegister;
	}
	
	@Override
	public String toString() {
		return "STR " + valueRegister + " [" + addressRegister + "]";
	}

}
