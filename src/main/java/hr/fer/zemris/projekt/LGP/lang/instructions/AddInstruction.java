package hr.fer.zemris.projekt.LGP.lang.instructions;

import java.util.Objects;

import hr.fer.zemris.projekt.LGP.lang.EasyLGPContext;
import hr.fer.zemris.projekt.LGP.lang.EasyLGPInstruction;

public class AddInstruction implements EasyLGPInstruction {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6563673020539849134L;
	private int op1Register;
	private int op2Register;
	private int resRegister;
	
	public AddInstruction(int op1Register, int op2Register, int resRegister) {
		this.op1Register = op1Register;
		this.op2Register = op2Register;
		this.resRegister = resRegister;
	}

	@Override
	public InstructionResult execute(EasyLGPContext context) {
		double op1 = context.getRegister(op1Register);
		double op2 = context.getRegister(op2Register);
		double res = 0;
		
		res = op1 + op2;
		
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
		if (!(obj instanceof AddInstruction))
			return false;
		AddInstruction other = (AddInstruction) obj;
		return op1Register == other.op1Register && op2Register == other.op2Register && resRegister == other.resRegister;
	}

	@Override
	public String toString() {
		return "ADD " + op1Register + " " + op2Register + " " + resRegister;
	}

}
