package hr.fer.zemris.projekt.algorithm.LGP.lang;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import hr.fer.zemris.projekt.algorithm.LGP.lang.EasyLGPInstructionExecutor.InstructionResult;
import hr.fer.zemris.projekt.algorithm.LGP.lang.EasyLGPInstructionExecutor.InstructionResultStatus;

/**
 * 
 * This is an analyzer for EasyLGP language. The language is similar to the
 * ARM assembly with few changes. Language specification : <br>
 *  - Program is an array of L instructions separated by '\n', L>0 <br>
 *  - There are M registers, M>0 <br>
 *  - All registers have initial value of 0 <br>
 *  - All registers hold 64-bit whole number values <br>
 *  - There is a status register which holds 2 flags (N - negative, Z - zero) <br>
 *  - Memory is an array of 64 bit whole number values (initially set to 0) <br>
 *  - Memory address is a 64 bit number (from Long.MIN_VALUE to Long.MAX_VALUE) <br>
 *  - Program instructions are in a separate memory (Harvard architecture) <br>
 *  - Instructions always start with the instruction name and optionally : 
 *    followed by one or more spaces and then by zero or more arguments separated by comma
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
	    <td>LDR Rx, [Ry]</td>
	    <td>Load in register Rx value from the memory at address [Ry]</td>
	  </tr>
	  <tr>
	    <td>STR Rx, [Ry]</td>
	    <td>Store the value from register Rx in the memory at address [Ry]</td>
	  </tr>
	  <tr>
	    <td>MOV Rx, Ry</td>
	    <td>Overwrite value in register Rx with the value held in Ry</td>
	  </tr>
	  <tr>
	    <td>MOV Rx, NUM64</td>
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
	    <td>ADD Rx, Ry, Rz</td>
	    <td>Rx + Ry -> Rz, refresh SR</td>
	  </tr>
	  <tr>
	    <td>SUB Rx, Ry, Rz</td>
	    <td>Rx - Ry -> Rz, refresh SR</td>
	  </tr>
	  <tr>
	    <td>MUL Rx, Ry, Rz</td>
	    <td>Rx * Ry -> Rz, refresh SR</td>
	  </tr>
	  <tr>
	    <td>DIV Rx, Ry, Rz</td>
	    <td>Rx / Ry -> Rz, refresh SR</td>
	  </tr>
	  <tr>
	    <td>MOD Rx, Ry, Rz</td>
	    <td>Rx % Ry -> Rz, refresh SR</td>
	  </tr>
	  <tr>
	    <td>AND Rx, Ry, Rz</td>
	    <td>Rx & Ry -> Rz, refresh SR</td>
	  </tr>
	  <tr>
	    <td>OR Rx, Ry, Rz</td>
	    <td>Rx | Ry -> Rz, refresh SR</td>
	  </tr>
	  <tr>
	    <td>XOR Rx, Ry, Rz</td>
	    <td>Rx XOR Ry -> Rz, refresh SR</td>
	  </tr>
	  <tr>
	    <td>CMP Rx, Ry</td>
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
	
	// Map : Regex -> EasyLGPContextUpdater
	private static Map<String, EasyLGPInstructionExecutor> instructions = new HashMap<>();
	
	static {
		instructions.put("LDR R([0-9]|[1-9][0-9]*), \\[R([0-9]|[1-9][0-9]*)]", (instr, context) -> ldrstr(instr, context));
		instructions.put("STR R([0-9]|[1-9][0-9]*), \\[R([0-9]|[1-9][0-9]*)]", (instr, context) -> ldrstr(instr, context));
		instructions.put("MOV R([0-9]|[1-9][0-9]*), R([0-9]|[1-9][0-9]*)", (instr, context) -> mov(instr, context));
		instructions.put("MOV R([0-9]|[1-9][0-9]*), -?([0-9]|[1-9][0-9]*)", (instr, context) -> movConst(instr, context));
		instructions.put("JP ([0-9]|[1-9][0-9]*)", (instr, context) -> jp(instr, context));
		instructions.put("JP(_N|_NN|_Z|_NZ|_EQ|_NEQ|_LEQ|_GEQ) ([0-9]|[1-9][0-9]*)", (instr, context) -> jpCond(instr, context));
		instructions.put("ADD R([0-9]|[1-9][0-9]*), R([0-9]|[1-9][0-9]*), R([0-9]|[1-9][0-9]*)", (instr, context) -> arithmLogic(instr, context));
		instructions.put("SUB R([0-9]|[1-9][0-9]*), R([0-9]|[1-9][0-9]*), R([0-9]|[1-9][0-9]*)", (instr, context) -> arithmLogic(instr, context));
		instructions.put("MUL R([0-9]|[1-9][0-9]*), R([0-9]|[1-9][0-9]*), R([0-9]|[1-9][0-9]*)", (instr, context) -> arithmLogic(instr, context));
		instructions.put("DIV R([0-9]|[1-9][0-9]*), R([0-9]|[1-9][0-9]*), R([0-9]|[1-9][0-9]*)", (instr, context) -> arithmLogic(instr, context));
		instructions.put("MOD R([0-9]|[1-9][0-9]*), R([0-9]|[1-9][0-9]*), R([0-9]|[1-9][0-9]*)", (instr, context) -> arithmLogic(instr, context));
		instructions.put("AND R([0-9]|[1-9][0-9]*), R([0-9]|[1-9][0-9]*), R([0-9]|[1-9][0-9]*)", (instr, context) -> arithmLogic(instr, context));
		instructions.put("OR R([0-9]|[1-9][0-9]*), R([0-9]|[1-9][0-9]*), R([0-9]|[1-9][0-9]*)", (instr, context) -> arithmLogic(instr, context));
		instructions.put("XOR R([0-9]|[1-9][0-9]*), R([0-9]|[1-9][0-9]*), R([0-9]|[1-9][0-9]*)", (instr, context) -> arithmLogic(instr, context));
		instructions.put("CMP R([0-9]|[1-9][0-9]*), R([0-9]|[1-9][0-9]*)", (instr, context) -> arithmLogic(instr, context));
		instructions.put("HALT", (instr, context) -> halt(instr, context));
	}
	
	public static void execute(String program, EasyLGPContext context) {
		
		String[] lines = program.split("\n");
		
		int pc = 0;
		
		while(pc < lines.length) {
			
			String nextLine = lines[pc];
			
			InstructionResult ir = null;
			for(var entry : instructions.entrySet()) {
				if(!Pattern.matches(entry.getKey(), nextLine)) continue;
				ir = entry.getValue().execute(nextLine, context);
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
			
		}
		
		throw new EasyLGPException("RuntimeException : Instruction on line " + pc + " does not exist!");
		
	}
	
	private static InstructionResult ldrstr(String instr, EasyLGPContext context) {
		return null;
	}
	
	private static InstructionResult mov(String instr, EasyLGPContext context) {
		return null;
	}
	
	private static InstructionResult movConst(String instr, EasyLGPContext context) {
		return null;
	}
	
	private static InstructionResult jp(String instr, EasyLGPContext context) {
		return null;
	}
	
	private static InstructionResult jpCond(String instr, EasyLGPContext context) {
		return null;
	}
	
	private static InstructionResult arithmLogic(String instr, EasyLGPContext context) {
		return null;
	}
	
	private static InstructionResult halt(String instr, EasyLGPContext context) {
		return null;
	}

}
