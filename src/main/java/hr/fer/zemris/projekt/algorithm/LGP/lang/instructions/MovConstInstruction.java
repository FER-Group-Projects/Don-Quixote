package hr.fer.zemris.projekt.algorithm.LGP.lang.instructions;

import hr.fer.zemris.projekt.algorithm.LGP.lang.EasyLGPContext;
import hr.fer.zemris.projekt.algorithm.LGP.lang.EasyLGPInstruction;

public class MovConstInstruction implements EasyLGPInstruction {
	
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

}
