package hr.fer.zemris.projekt.model.objects;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class Game2DObject implements BoundingBoxListener, Destroyable {
	
	private BoundingBox position;
	
	private List<Game2DObjectListener> listeners = new ArrayList<>();
	
	public Game2DObject(BoundingBox position) {
		this.position = Objects.requireNonNull(position);
		this.position.addListener(this);
	}

	public BoundingBox getPosition() {
		return position;
	}
	
	public void addListener(Game2DObjectListener listener) {
		listeners.add(Objects.requireNonNull(listener));
	}
	
	public void removeListener(Game2DObjectListener listener) {
		listeners.remove(Objects.requireNonNull(listener));
	}
	
	private void notifyAllListeners() {
		listeners.forEach(l -> l.positionChanged(this));
	}

	@Override
	public void boundingBoxChange(BoundingBox source) {
		notifyAllListeners();
	}
	
	@Override
	public void destroy() {
		position.removeListener(this);
		listeners.forEach(l -> l.objectDestroyed(this));
	}

	@Override
	public String toString() {
		return "Game2DObject [position=" + position + "]";
	}

}
