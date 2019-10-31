package hr.fer.zemris.projekt.model.objects;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class Game2DObject implements Destroyable {
	
	private BoundingBox position;
	
	private List<Game2DObjectListener> listeners = new ArrayList<>();
	
	public Game2DObject(BoundingBox position) {
		this.position = new BoundingBox(position.getX(), position.getY(), position.getWidth(), position.getHeight());
	}

	public BoundingBox getPosition() {
		return new BoundingBox(position.getX(), position.getY(), position.getWidth(), position.getHeight());
	}
	
	public void setX(double x) {
		position.setX(x);
		notifyAllListeners();
	}
	
	public void setY(double y) {
		position.setY(y);
		notifyAllListeners();
	}
	
	public void setWidth(double w) {
		position.setWidth(w);
		notifyAllListeners();
	}
	
	public void setHeight(double h) {
		position.setHeight(h);
		notifyAllListeners();
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
	public void destroy() {
		listeners.forEach(l -> l.objectDestroyed(this));
	}

	@Override
	public String toString() {
		return "Game2DObject [position=" + position + "]";
	}

}
