package hr.fer.zemris.projekt.model.objects.impl;

import hr.fer.zemris.projekt.model.objects.BoundingBox2D;
import hr.fer.zemris.projekt.model.objects.Game2DObject;

public class Ladder extends Game2DObject {

	public Ladder(BoundingBox2D position) {
		super(position);
	}

	@Override
	public Ladder copy() {
		return new Ladder(getBoundingBox());
	}
}
