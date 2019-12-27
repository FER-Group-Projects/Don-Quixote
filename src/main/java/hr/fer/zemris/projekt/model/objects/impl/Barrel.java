package hr.fer.zemris.projekt.model.objects.impl;

import hr.fer.zemris.projekt.model.objects.BoundingBox2D;
import hr.fer.zemris.projekt.model.objects.Game2DObject;
import hr.fer.zemris.projekt.model.objects.MovableGame2DObject;

public class Barrel extends MovableGame2DObject {

	public Barrel(BoundingBox2D position, double velocityX, double velocityY) {
		super(position, velocityX, velocityY);
	}

	@Override
	public Game2DObject copy() {
		return new Barrel(getBoundingBox(), getVelocityX(), getVelocityY());
	}
}
