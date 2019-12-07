package hr.fer.zemris.projekt.algorithm.LGP.lang.instructions;

import hr.fer.zemris.projekt.algorithm.LGP.lang.EasyLGPContext;
import hr.fer.zemris.projekt.algorithm.LGP.lang.EasyLGPInstruction;

public class MovInstruction implements EasyLGPInstruction {
	
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

}
