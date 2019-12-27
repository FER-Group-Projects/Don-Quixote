package hr.fer.zemris.projekt.model.objects.impl;

import hr.fer.zemris.projekt.model.objects.BoundingBox2D;
import hr.fer.zemris.projekt.model.objects.MovableGame2DObject;

public class Player extends MovableGame2DObject {
	
	private String name;
	
	private boolean isAlive = true;
	private boolean isJumping = false;

	public Player(BoundingBox2D position, double velocityX, double velocityY, String name) {
		super(position, velocityX, velocityY);
		this.name = name;
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

	@Override
	public String toString() {
		return "Player [name=" + name + ", isAlive=" + isAlive + ", isJumping=" + isJumping + ", toString()="
				+ super.toString() + "]";
	}

	public Player copy() {
		return new Player(getBoundingBox(), getVelocityX(), getVelocityY(), name);
	}

}
