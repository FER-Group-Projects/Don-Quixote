package hr.fer.zemris.projekt.algorithm.LGP.lang.instructions;

import hr.fer.zemris.projekt.algorithm.LGP.lang.EasyLGPContext;
import hr.fer.zemris.projekt.algorithm.LGP.lang.EasyLGPInstruction;

public class XorInstruction implements EasyLGPInstruction {
	
	private long op1Register;
	private long op2Register;
	private long resRegister;
	
	public XorInstruction(long op1Register, long op2Register, long resRegister) {
		this.op1Register = op1Register;
		this.op2Register = op2Register;
		this.resRegister = resRegister;
	}

	@Override
	public InstructionResult execute(EasyLGPContext context) {
		long op1 = context.getRegister(op1Register);
		long op2 = context.getRegister(op2Register);
		long res = 0;
		
		res = op1 ^ op2;
		
		context.setRegister(resRegister, res);
		context.setNegative(res < 0);
		context.setZero(res == 0);
		
		return new InstructionResult(InstructionResultStatus.CONTINUE);
	}

}
