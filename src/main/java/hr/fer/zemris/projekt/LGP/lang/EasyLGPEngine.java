package hr.fer.zemris.projekt.LGP.lang;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.regex.Pattern;

import hr.fer.zemris.projekt.LGP.lang.EasyLGPInstruction.InstructionResult;
import hr.fer.zemris.projekt.LGP.lang.EasyLGPInstruction.InstructionResultStatus;

/**
 * 
 * This is an engine for EasyLGP language. The language is similar to the
 * ARM assembly with few changes. Language specification : <br>
 *  - Program is an array of L instructions separated by '\n', L>0 <br>
 *  - There are M registers, M>0 <br>
 *  - All registers have initial value of 0 <br>
 *  - All registers hold 64-bit whole number values <br>
 *  - There is a status register which holds 2 flags (N - negative, Z - zero) <br>
 *  - Memory is an array of 64 bit whole number values (initially set to 0) <br>
 *  - Memory address is a 64 bit number (from -Long.MAX_VALUE-1 to Long.MAX_VALUE) <br>
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
	    <td>LDR Rx [Ry]</td>
	    <td>Load in register Rx value from the memory at address [Ry]</td>
	  </tr>
	  <tr>
	    <td>STR Rx [Ry]</td>
	    <td>Store the value from register Rx in the memory at address [Ry]</td>
	  </tr>
	  <tr>
	    <td>MOV Rx Ry</td>
	    <td>Overwrite value in register Rx with the value held in Ry</td>
	  </tr>
	  <tr>
	    <td>MOV Rx NUM64</td>
	    <td>Overwrite value in register Rx with the value NUM64 (64 bit whole number constant)</td>
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
	    <td>AND Rx Ry Rz</td>
	    <td>Rx & Ry -> Rz, refresh SR</td>
	  </tr>
	  <tr>
	    <td>OR Rx Ry Rz</td>
	    <td>Rx | Ry -> Rz, refresh SR</td>
	  </tr>
	  <tr>
	    <td>XOR Rx Ry Rz</td>
	    <td>Rx XOR Ry -> Rz, refresh SR</td>
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

	// Map : Regex -> (String instruction, EasyLGPContext, IntructionResult)
	private static Map<String, BiFunction<String, EasyLGPContext, EasyLGPInstruction.InstructionResult>> instructions = new HashMap<>();
	
	static {
		instructions.put("LDR R([0-9]|[1-9][0-9]*) \\[R([0-9]|[1-9][0-9]*)]", (instr, context) -> ldrstr(instr, context));
		instructions.put("STR R([0-9]|[1-9][0-9]*) \\[R([0-9]|[1-9][0-9]*)]", (instr, context) -> ldrstr(instr, context));
		instructions.put("MOV R([0-9]|[1-9][0-9]*) R([0-9]|[1-9][0-9]*)", (instr, context) -> mov(instr, context));
		instructions.put("MOV R([0-9]|[1-9][0-9]*) -?([0-9]|[1-9][0-9]*)", (instr, context) -> movConst(instr, context));
		instructions.put("JP ([0-9]|[1-9][0-9]*)", (instr, context) -> jp(instr, context));
		instructions.put("JP_(N|NN|Z|NZ|EQ|NEQ|LEQ|GEQ) ([0-9]|[1-9][0-9]*)", (instr, context) -> jpCond(instr, context));
		instructions.put("(ADD|SUB|MUL|DIV|MOD|AND|OR|XOR) R([0-9]|[1-9][0-9]*) R([0-9]|[1-9][0-9]*) R([0-9]|[1-9][0-9]*)", (instr, context) -> arithmLogic(instr, context));
		instructions.put("CMP R([0-9]|[1-9][0-9]*) R([0-9]|[1-9][0-9]*)", (instr, context) -> cmp(instr, context));
		instructions.put("HALT", (instr, context) -> halt(instr, context));
	}
	
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
	
	public static void execute(String program, EasyLGPContext context, long maxSteps) {
		
		String[] lines = program.split("\n");
		
		long step = 0;
		int pc = 0;
		
		while(pc < lines.length) {
			
			if(step==maxSteps) throw new EasyLGPException("MaxSteps has been reached!");
			
			String nextLine = lines[pc].trim();
			
			InstructionResult ir = null;
			for(var entry : instructions.entrySet()) {
				if(!Pattern.matches(entry.getKey(), nextLine)) continue;
				ir = entry.getValue().apply(nextLine, context);
				break;
			}
			
			if(ir==null) {
				throw new EasyLGPException("Syntax error on line " + pc + " : " + nextLine);
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
	
	private static InstructionResult ldrstr(String instr, EasyLGPContext context) {
		String[] s = instr.split("\\s+");
		long valueReg = Long.parseLong(s[1].substring(1));
		long addressReg = Long.parseLong(s[2].substring(2, s[2].length()-1));
		long memAddress = context.getRegister(addressReg);
		
		if(s[0].equals("LDR")) {
			context.setRegister(valueReg, context.getMemory(memAddress));
		} else {
			context.setMemory(memAddress, context.getRegister(valueReg));
		}
		
		return new InstructionResult(InstructionResultStatus.CONTINUE);
	}
	
	private static InstructionResult mov(String instr, EasyLGPContext context) {
		String[] s = instr.split("\\s+");
		long regTo = Long.parseLong(s[1].substring(1));
		long regFrom = Long.parseLong(s[2].substring(1));
		
		context.setRegister(regTo, context.getRegister(regFrom));
		
		return new InstructionResult(InstructionResultStatus.CONTINUE);
	}
	
	private static InstructionResult movConst(String instr, EasyLGPContext context) {
		String[] s = instr.split("\\s+");
		long regTo = Long.parseLong(s[1].substring(1));
		long constant = Long.parseLong(s[2]);
		
		context.setRegister(regTo, constant);
		
		return new InstructionResult(InstructionResultStatus.CONTINUE);
	}
	
	private static InstructionResult jp(String instr, EasyLGPContext context) {
		String[] s = instr.split("\\s+");
		int address = Integer.parseInt(s[1]);
		
		return new InstructionResult(InstructionResultStatus.JUMP, address);
	}
	
	private static InstructionResult jpCond(String instr, EasyLGPContext context) {
		String[] s = instr.split("\\s+");
		String condition = s[0].substring(3);
		int address = Integer.parseInt(s[1]);
		
		boolean conditionMet = condition.equals("N") && context.isNegative() ||
							   condition.equals("NN") && !context.isNegative() ||
							   condition.equals("Z") && context.isZero() ||
							   condition.equals("NZ") && !context.isZero() ||
							   condition.equals("EQ") && context.isZero() ||
							   condition.equals("NEQ") && !context.isZero() ||
							   condition.equals("LEQ") && (context.isNegative() || context.isZero()) ||
							   condition.equals("GEQ") && (!context.isNegative() || context.isZero());
		
		if(conditionMet) {
			return new InstructionResult(InstructionResultStatus.JUMP, address);
		} else {
			return new InstructionResult(InstructionResultStatus.CONTINUE);
		}
	}
	
	private static InstructionResult arithmLogic(String instr, EasyLGPContext context) {
		String[] s = instr.split("\\s+");
		long op1Reg = Long.parseLong(s[1].substring(1));
		long op2Reg = Long.parseLong(s[2].substring(1));
		long resReg = Long.parseLong(s[3].substring(1));
		
		long op1 = context.getRegister(op1Reg);
		long op2 = context.getRegister(op2Reg);
		long res = 0;
		
		if(s[0].equals("ADD")) {
			res = op1 + op2;
		} else if(s[0].equals("SUB")) {
			res = op1 - op2;
		} else if(s[0].equals("MUL")) {
			res = op1 * op2;
		} else if(s[0].equals("DIV")) {
			res = op1 / op2;
		} else if(s[0].equals("MOD")) {
			res = op1 % op2;
		} else if(s[0].equals("AND")) {
			res = op1 & op2;
		} else if(s[0].equals("OR")) {
			res = op1 | op2;
		} else if(s[0].equals("XOR")) {
			res = op1 ^ op2;
		}
		
		context.setRegister(resReg, res);
		context.setNegative(res < 0);
		context.setZero(res == 0);
		
		return new InstructionResult(InstructionResultStatus.CONTINUE);
	}
	
	private static InstructionResult cmp(String instr, EasyLGPContext context) {
		String[] s = instr.split("\\s+");
		long op1Reg = Long.parseLong(s[1].substring(1));
		long op2Reg = Long.parseLong(s[2].substring(1));
		
		long op1 = context.getRegister(op1Reg);
		long op2 = context.getRegister(op2Reg);
		long res = op1 - op2;
		
		context.setNegative(res < 0);
		context.setZero(res == 0);
		
		return new InstructionResult(InstructionResultStatus.CONTINUE);
	}
	
	private static InstructionResult halt(String instr, EasyLGPContext context) {
		return new InstructionResult(InstructionResultStatus.HALT);
	}

}
