package hr.fer.zemris.projekt.LGP.lang;

import java.util.List;

import hr.fer.zemris.projekt.LGP.lang.EasyLGPInstruction.InstructionResult;
import hr.fer.zemris.projekt.LGP.lang.EasyLGPInstruction.InstructionResultStatus;

/**
 * 
 * This is an engine for EasyLGP language. The language is similar to the
 * ARM assembly with few changes. Language specification : <br>
 *  - Program is an array of L instructions separated by '\n', L>0 <br>
 *  - There are M registers, M>0 <br>
 *  - All registers have initial value of 0 <br>
 *  - All registers hold 64-bit double values <br>
 *  - There is a status register which holds 2 flags (N - negative, Z - zero) <br>
 *  - Program instructions are in a separate memory (Harvard architecture) <br>
 *  - Instructions always start with the instruction name and optionally : 
 *    followed by one space and then by zero or more arguments each separated by one space
 *  <br>
 *  Rx - register Rx (R0, R1, R2...) <br>
 *  [Rx] - value in the register Rx <br>
	<table border="1">
	  <caption>Instruction set</caption>
	  <tr>
	    <th>Instruction</th>
	    <th>Description</th>
	  </tr>
	  <tr>
	    <td>MOV Rx Ry</td>
	    <td>Overwrite value in register Rx with the value held in Ry</td>
	  </tr>
	  <tr>
	    <td>JP NUM64UNSIGN</td>
	    <td>Overwrites program counter (PC) with the value NUM64UNSIGN (unsigned 64 bit whole number constant)</td>
	  </tr>
	  <tr>
	    <td>JP{condition} NUM64UNSIGN</td>
	    <td>Overwrites program counter (PC) with the value NUM64UNSIGN (unsigned 64 bit whole number constant) only if the condition is met<br>
	        {condition}: _N (negative), _NN (non-negative), _Z (zero), _NZ (non-zero), _EQ (equals), _NEQ (not equals), _LEQ (less than or equals), _GEQ (greater than or equals)</td>
	  </tr>
	  <tr>
	    <td>ADD Rx Ry Rz</td>
	    <td>Rx + Ry -> Rz, refresh SR</td>
	  </tr>
	  <tr>
	    <td>SUB Rx Ry Rz</td>
	    <td>Rx - Ry -> Rz, refresh SR</td>
	  </tr>
	  <tr>
	    <td>MUL Rx Ry Rz</td>
	    <td>Rx * Ry -> Rz, refresh SR</td>
	  </tr>
	  <tr>
	    <td>DIV Rx Ry Rz</td>
	    <td>Rx / Ry -> Rz, refresh SR</td>
	  </tr>
	  <tr>
	    <td>MOD Rx Ry Rz</td>
	    <td>Rx % Ry -> Rz, refresh SR</td>
	  </tr>
	  <tr>
	    <td>CMP Rx Ry</td>
	    <td>Rx - Ry, refresh SR</td>
	  </tr>
	  <tr>
	    <td>HALT</td>
	    <td>Stops the program</td>
	  </tr>
	</table>
 *	
 * <br>
 * Jump conditions : (condition -> flags) <br>
 * _N -> N=1 <br>
 * _NN -> N=0 <br>
 * _Z -> Z=1 <br>
 * _NZ -> Z=0 <br>
 * _EQ -> Z=1 <br>
 * _NEQ -> Z=0 <br>
 * _LEQ -> Z=1 OR N=1 <br>
 * _GEQ -> Z=1 OR N=0 <br>
 * 
 * @author Matija
 *
 */
public class EasyLGPEngine {
	
	public static void execute(List<EasyLGPInstruction> program, EasyLGPContext context, long maxSteps) {
		
		long step = 0;
		int pc = 0;
		
		while(pc < program.size()) {
			
			if(step==maxSteps) throw new EasyLGPException("MaxSteps has been reached!");
			
			EasyLGPInstruction next = program.get(pc);
			
			InstructionResult ir = next.execute(context);
			
			if(ir==null) {
				throw new EasyLGPException("Syntax error on line " + pc + " : " + next.toString());
			} else if(ir.status == InstructionResultStatus.JUMP){
				pc = ir.number;
			} else if(ir.status == InstructionResultStatus.HALT) {
				return;
			} else {
				pc++;
			}
			
			step++;
			
		}
		
		throw new EasyLGPException("RuntimeException : Instruction on line " + pc + " does not exist!");
		
	}

}
