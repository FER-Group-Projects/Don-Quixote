package hr.fer.zemris.projekt.algorithm.LGP.lang;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class EasyLGPEngineTest {

	@Test
	void testAddInRegisters() {
		EasyLGPContext context = new EasyLGPContext(5);
		
		String program = "MOV R0 5\nMOV R1 5\nADD R0 R1 R0\nHALT";
		EasyLGPEngine.execute(program, context);
		
		assertEquals(10, context.getRegister(0));
		assertEquals(5, context.getRegister(1));
		assertEquals(0, context.getRegister(2));
		assertEquals(0, context.getRegister(3));
		assertEquals(0, context.getRegister(4));
	}
	
	@Test
	void testLTandGT() {
		EasyLGPContext context = new EasyLGPContext(5);
		context.setMemory(0, 150);
		context.setMemory(1, 420);
		
		String program = "MOV R2 0\nLDR R0 [R2]\nMOV R2 1\nLDR R1 [R2]\n"
				+ "CMP R0 R1\nJP_EQ 8\nJP_GEQ 10\nJP_LEQ 12\n"
				+ "MOV R3 0\nJP 13\nMOV R3 1\nJP 13\nMOV R3 -1\nMOV R2 2\nSTR R3 [R2]\nHALT";
		EasyLGPEngine.execute(program, context);
		
		assertEquals(-1, context.getMemory(2));
	}
	
	@Test
	void testLTandGT2() {
		EasyLGPContext context = new EasyLGPContext(5);
		context.setMemory(0, 420);
		context.setMemory(1, 150);
		
		String program = "MOV R2 0\nLDR R0 [R2]\nMOV R2 1\nLDR R1 [R2]\n"
				+ "CMP R0 R1\nJP_EQ 8\nJP_GEQ 10\nJP_LEQ 12\n"
				+ "MOV R3 0\nJP 13\nMOV R3 1\nJP 13\nMOV R3 -1\nMOV R2 2\nSTR R3 [R2]\nHALT";
		EasyLGPEngine.execute(program, context);
		
		assertEquals(1, context.getMemory(2));
	}
	
	@Test
	void testLTandGT3() {
		EasyLGPContext context = new EasyLGPContext(5);
		context.setMemory(0, 150);
		context.setMemory(1, 150);
		
		String program = "MOV R2 0\nLDR R0 [R2]\nMOV R2 1\nLDR R1 [R2]\n"
				+ "CMP R0 R1\nJP_EQ 8\nJP_GEQ 10\nJP_LEQ 12\n"
				+ "MOV R3 0\nJP 13\nMOV R3 1\nJP 13\nMOV R3 -1\nMOV R2 2\nSTR R3 [R2]\nHALT";
		EasyLGPEngine.execute(program, context);
		
		assertEquals(0, context.getMemory(2));
	}
	
	@Test
	void testMultiplyInMemoryByHand() {
		EasyLGPContext context = new EasyLGPContext(5);
		context.setMemory(0, 25);
		context.setMemory(1, 5);
		
		String program = "MOV R2 0\nLDR R0 [R2]\nMOV R2 1\nLDR R1 [R2]\n"
				+ "ADD R3 R0 R3\nSUB R1 R2 R1\nJP_NZ 4\nMOV R2 2\nSTR R3 [R2]\nHALT";
		EasyLGPEngine.execute(program, context);
		
		assertEquals(125, context.getMemory(2));
	}

}
