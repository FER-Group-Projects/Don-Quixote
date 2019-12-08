package hr.fer.zemris.projekt.LGP.lang.instructions;

import java.util.Objects;

import hr.fer.zemris.projekt.LGP.lang.EasyLGPContext;
import hr.fer.zemris.projekt.LGP.lang.EasyLGPInstruction;

public class CmpInstruction implements EasyLGPInstruction {
	
	private long op1Register;
	private long op2Register;
	
	public CmpInstruction(long op1Register, long op2Register) {
		this.op1Register = op1Register;
		this.op2Register = op2Register;
	}

	@Override
	public InstructionResult execute(EasyLGPContext context) {
		long op1 = context.getRegister(op1Register);
		long op2 = context.getRegister(op2Register);
		long res = 0;
		
		res = op1 - op2;
		
		context.setNegative(res < 0);
		context.setZero(res == 0);
		
		return new InstructionResult(InstructionResultStatus.CONTINUE);
	}

	@Override
	public int hashCode() {
		return Objects.hash(op1Register, op2Register);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof CmpInstruction))
			return false;
		CmpInstruction other = (CmpInstruction) obj;
		return op1Register == other.op1Register && op2Register == other.op2Register;
	}

	@Override
	public String toString() {
		return "CMP " + op1Register + " " + op2Register;
	}

}
