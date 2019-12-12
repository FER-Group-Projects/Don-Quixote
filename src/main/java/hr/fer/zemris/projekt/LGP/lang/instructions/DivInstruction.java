package hr.fer.zemris.projekt.LGP.lang.instructions;

import java.util.Objects;

import hr.fer.zemris.projekt.LGP.lang.EasyLGPContext;
import hr.fer.zemris.projekt.LGP.lang.EasyLGPInstruction;

public class DivInstruction implements EasyLGPInstruction {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -191972937767029223L;
	private long op1Register;
	private long op2Register;
	private long resRegister;
	
	public DivInstruction(long op1Register, long op2Register, long resRegister) {
		this.op1Register = op1Register;
		this.op2Register = op2Register;
		this.resRegister = resRegister;
	}

	@Override
	public InstructionResult execute(EasyLGPContext context) {
		long op1 = context.getRegister(op1Register);
		long op2 = context.getRegister(op2Register);
		long res = 0;
		
		res = op1 / op2;
		
		context.setRegister(resRegister, res);
		context.setNegative(res < 0);
		context.setZero(res == 0);
		
		return new InstructionResult(InstructionResultStatus.CONTINUE);
	}

	@Override
	public int hashCode() {
		return Objects.hash(op1Register, op2Register, resRegister);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof DivInstruction))
			return false;
		DivInstruction other = (DivInstruction) obj;
		return op1Register == other.op1Register && op2Register == other.op2Register && resRegister == other.resRegister;
	}
	
	@Override
	public String toString() {
		return "DIV " + op1Register + " " + op2Register + " " + resRegister;
	}

}