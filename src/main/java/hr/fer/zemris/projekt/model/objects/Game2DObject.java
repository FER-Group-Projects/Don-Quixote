package hr.fer.zemris.projekt.model.objects;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import hr.fer.zemris.projekt.model.objects.impl.BoundingBoxImpl;

public abstract class Game2DObject implements Destroyable {
	
	private UnmodifiableBoundingBox boundingBox;
	
	private List<Game2DObjectListener> listeners = new ArrayList<>();
	
	public Game2DObject(BoundingBox position) {
		this.boundingBox = new UnmodifiableBoundingBox(new BoundingBoxImpl(position.getX(), position.getY(), position.getWidth(), position.getHeight()));
	}

	public BoundingBox getBoundingBox() {
		return boundingBox;
	}
	
	public void setX(double x) {
		boundingBox.boundingBox.setX(x);
		notifyAllListeners();
	}
	
	public void setY(double y) {
		boundingBox.boundingBox.setY(y);
		notifyAllListeners();
	}
	
	public void setWidth(double w) {
		boundingBox.boundingBox.setWidth(w);
		notifyAllListeners();
	}
	
	public void setHeight(double h) {
		boundingBox.boundingBox.setHeight(h);
		notifyAllListeners();
	}
	
	public void setLocation(double x, double y) {
		boundingBox.boundingBox.setLocation(x, y);
		notifyAllListeners();
	}
	
	public void addListener(Game2DObjectListener listener) {
		listeners.add(Objects.requireNonNull(listener));
	}
	
	public void removeListener(Game2DObjectListener listener) {
		listeners.remove(Objects.requireNonNull(listener));
	}
	
	private void notifyAllListeners() {
		listeners.forEach(l -> l.boundingBoxChanged(this));
	}
	
	@Override
	public void destroy() {
		listeners.forEach(l -> l.objectDestroyed(this));
	}

	@Override
	public String toString() {
		return "Game2DObject [boundingBox=" + boundingBox + "]";
	}
	
	private static class UnmodifiableBoundingBox implements BoundingBox {

		private BoundingBox boundingBox;
		
		public UnmodifiableBoundingBox(BoundingBox boundingBox) {
			this.boundingBox = Objects.requireNonNull(boundingBox);
		}

		@Override
		public double getX() {
			return boundingBox.getX();
		}

		@Override
		public void setX(double x) {
			throw new UnsupportedOperationException();
		}

		@Override
		public double getY() {
			return boundingBox.getY();
		}

		@Override
		public void setY(double y) {
			throw new UnsupportedOperationException();
		}

		@Override
		public double getWidth() {
			return boundingBox.getWidth();
		}

		@Override
		public void setWidth(double width) {
			throw new UnsupportedOperationException();
		}

		@Override
		public double getHeight() {
			return boundingBox.getHeight();
		}

		@Override
		public void setHeight(double height) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void setLocation(double x, double y) {
			throw new UnsupportedOperationException();
		}

		@Override
		public String toString() {
			return boundingBox.toString();
		}
		
	}

}
