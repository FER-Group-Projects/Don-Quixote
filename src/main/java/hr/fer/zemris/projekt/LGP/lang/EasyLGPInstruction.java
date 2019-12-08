package hr.fer.zemris.projekt.LGP.lang;

@FunctionalInterface
public interface EasyLGPInstruction {
	
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
