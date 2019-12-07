package hr.fer.zemris.projekt.algorithm.LGP.lang.instructions;

import hr.fer.zemris.projekt.algorithm.LGP.lang.EasyLGPContext;
import hr.fer.zemris.projekt.algorithm.LGP.lang.EasyLGPInstruction;

public class HaltInstruction implements EasyLGPInstruction {

	@Override
	public InstructionResult execute(EasyLGPContext context) {
		return new InstructionResult(InstructionResultStatus.HALT);
	}

}
