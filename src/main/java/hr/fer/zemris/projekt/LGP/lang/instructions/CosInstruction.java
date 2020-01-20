package hr.fer.zemris.projekt.LGP.lang.instructions;

import java.util.Objects;

import hr.fer.zemris.projekt.LGP.lang.EasyLGPContext;
import hr.fer.zemris.projekt.LGP.lang.EasyLGPInstruction;

public class CosInstruction implements EasyLGPInstruction {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6563673020539849134L;
	private int opRegister;
	private int resRegister;
	
	public CosInstruction(int opRegister, int resRegister) {
		this.opRegister = opRegister;
		this.resRegister = resRegister;
	}

	@Override
	public InstructionResult execute(EasyLGPContext context) {
		double op = context.getRegister(opRegister);
		double res = Math.cos(op);
		
		context.setRegister(resRegister, res);
		context.setNegative(res < 0);
		context.setZero(res == 0);
		
		return new InstructionResult(InstructionResultStatus.CONTINUE);
	}

	@Override
	public int hashCode() {
		return Objects.hash(opRegister, resRegister);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof CosInstruction))
			return false;
		CosInstruction other = (CosInstruction) obj;
		return opRegister == other.opRegister && resRegister == other.resRegister;
	}

	@Override
	public String toString() {
		return "SIN " + opRegister + " " + resRegister;
	}

}
