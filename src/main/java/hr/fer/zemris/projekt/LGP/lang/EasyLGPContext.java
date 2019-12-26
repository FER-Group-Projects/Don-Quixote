package hr.fer.zemris.projekt.LGP.lang;

import java.util.HashMap;
import java.util.Map;

public class EasyLGPContext {
	
	private int numberOfRegisters;
	
	// Map : registerNumber -> registerValue
	private Map<Integer, Double> registers = new HashMap<>();
	
	// Status register (SR)
	boolean negative = false;
	boolean zero = false;
	
	public EasyLGPContext(int numberOfRegisters) {
		this.numberOfRegisters = numberOfRegisters;
	}
	
	public void setRegister(int regNumber, double value) {
		if(regNumber < 0 || regNumber >= numberOfRegisters) throw new EasyLGPException("Register number " + regNumber + " out of range for length " + numberOfRegisters);
	
		registers.put(regNumber, value);
	}
	
	public double getRegister(int regNumber) {
		if(regNumber < 0 || regNumber >= numberOfRegisters) throw new EasyLGPException("Register number " + regNumber + " out of range for length " + numberOfRegisters);
	
		return registers.getOrDefault(regNumber, 0D);
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

	public long getNumberOfRegisters() {
		return numberOfRegisters;
	}
	
	public void clear() {
		registers.clear();
		negative = false;
		zero = false;
	}
	
}
