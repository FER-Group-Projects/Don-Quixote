package hr.fer.zemris.projekt.model.objects.impl;

import hr.fer.zemris.projekt.model.objects.BoundingBox2D;
import hr.fer.zemris.projekt.model.objects.Game2DObject;

public class Platform extends Game2DObject {

	public Platform(BoundingBox2D position) {
		super(position);
	}

	@Override
	public Platform copy() {
		return new Platform(getBoundingBox());
	}
}
