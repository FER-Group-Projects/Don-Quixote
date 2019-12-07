package hr.fer.zemris.projekt.algorithm.LGP.lang.instructions;

import hr.fer.zemris.projekt.algorithm.LGP.lang.EasyLGPContext;
import hr.fer.zemris.projekt.algorithm.LGP.lang.EasyLGPInstruction;

public class StrInstruction implements EasyLGPInstruction {
	
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

}
