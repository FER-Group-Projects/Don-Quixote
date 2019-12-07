package hr.fer.zemris.projekt.algorithm.LGP.lang.instructions;

import hr.fer.zemris.projekt.algorithm.LGP.lang.EasyLGPContext;
import hr.fer.zemris.projekt.algorithm.LGP.lang.EasyLGPInstruction;

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

}
