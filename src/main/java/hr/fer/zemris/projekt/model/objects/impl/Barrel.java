package hr.fer.zemris.projekt.model.objects.impl;

import hr.fer.zemris.projekt.model.objects.BoundingBox;
import hr.fer.zemris.projekt.model.objects.MoveableGame2DObject;

public class Barrel extends MoveableGame2DObject {
	
	public Barrel(BoundingBox position, double velocityX, double velocityY) {
		super(position, velocityX, velocityY);
	}
	
}
