package hr.fer.zemris.projekt.algorithm.LGP.lang;

import java.util.HashMap;
import java.util.Map;

public class EasyLGPContext {
	
	private long numberOfRegisters;
	
	// Map : registerNumber -> registerValue
	private Map<Long, Long> registers = new HashMap<>();
	// Map : memorySlotAddress -> memorySlotValue
	private Map<Long, Long> memory = new HashMap<>();
	
	// Status register (SR)
	boolean negative = false;
	boolean zero = false;
	
	public EasyLGPContext(long numberOfRegisters) {
		this.numberOfRegisters = numberOfRegisters;
	}
	
	public void setRegister(long regNumber, long value) {
		if(regNumber < 0 || regNumber >= numberOfRegisters) throw new EasyLGPException("Register number " + regNumber + " out of range for length " + numberOfRegisters);
	
		registers.put(regNumber, value);
	}
	
	public long getRegister(long regNumber) {
		if(regNumber < 0 || regNumber >= numberOfRegisters) throw new EasyLGPException("Register number " + regNumber + " out of range for length " + numberOfRegisters);
	
		return registers.getOrDefault(regNumber, 0L);
	}
	
	public void setMemory(long memAddress, long value) {
		memory.put(memAddress, value);
	}
	
	public long getMemory(long memAddress) {
		return memory.getOrDefault(memAddress, 0L);
	}
	
	public boolean isNegative() {
		return negative;
	}
	
	public void setNegative(boolean negative) {
		this.negative = negative;
	}
	
	public boolean isZero() {
		return zero;
	}

	public void setZero(boolean zero) {
		this.zero = zero;
	}
	
}
