package hr.fer.zemris.projekt.LGP.lang;

import java.io.Serializable;

@FunctionalInterface
public interface EasyLGPInstruction extends Serializable {
	
	InstructionResult execute(EasyLGPContext context);
	
	public static class InstructionResult {
		public InstructionResultStatus status;
		public int number;
		
		public InstructionResult(InstructionResultStatus status, int number) {
			this.status = status;
			this.number = number;
		}
		
		public InstructionResult(InstructionResultStatus status) {
			this.status = status;
		}
	}
	
	public static enum InstructionResultStatus {
		CONTINUE,
		HALT,
		JUMP;
	}

}
