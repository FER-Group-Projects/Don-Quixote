package hr.fer.zemris.projekt.model.objects.impl;

import hr.fer.zemris.projekt.model.objects.BoundingBox2D;
import hr.fer.zemris.projekt.model.objects.MovableGame2DObject;

public class Player extends MovableGame2DObject {
	
	private String name;
	private double defaultSpeedJump;
	
	private boolean isAlive = true;
	private boolean isJumping = false;

	public Player(BoundingBox2D position, double velocityX, double velocityY, double defaultSpeedGround,
			double defaultSpeedLadder, String name, double defaultSpeedJump) {
		super(position, velocityX, velocityY, defaultSpeedGround, defaultSpeedLadder);
		this.name = name;
		this.defaultSpeedJump = defaultSpeedJump;
	}

	public String getName() {
		return name;
	}

	public boolean isAlive() {
		return isAlive;
	}

	public void setAlive(boolean alive) {
		this.isAlive = alive;
	}
	
	public boolean isJumping() {
		return isJumping;
	}
	
	public void setJumping(boolean isJumping) {
		this.isJumping = isJumping;
	}
	
	public double getDefaultSpeedJump() {
		return defaultSpeedJump;
	}
	
	public void setDefaultSpeedJump(double defaultSpeedJump) {
		this.defaultSpeedJump = defaultSpeedJump;
	}

}
