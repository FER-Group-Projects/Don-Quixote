package hr.fer.zemris.projekt.model.objects;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

import hr.fer.zemris.projekt.model.objects.impl.BoundingBox2DImpl;

public abstract class Game2DObject implements Destroyable {
	
	private UnmodifiableBoundingBox boundingBox;

	private List<Game2DObjectListener> listeners = new CopyOnWriteArrayList<>();
	
	public Game2DObject(BoundingBox2D position) {
		this.boundingBox = new UnmodifiableBoundingBox(new BoundingBox2DImpl(position.getX(), position.getY(), position.getWidth(), position.getHeight()));
	}

	public BoundingBox2D getBoundingBox() {
		return boundingBox;
	}
	
	public void setX(double x) {
		boundingBox.boundingBox.setX(x);
		notifyAllListenersChanged();
	}
	
	public void setY(double y) {
		boundingBox.boundingBox.setY(y);
		notifyAllListenersChanged();
	}
	
	public void setWidth(double w) {
		boundingBox.boundingBox.setWidth(w);
		notifyAllListenersChanged();
	}
	
	public void setHeight(double h) {
		boundingBox.boundingBox.setHeight(h);
		notifyAllListenersChanged();
	}
	
	public void setLocation(double x, double y) {
		boundingBox.boundingBox.setLocation(x, y);
		notifyAllListenersChanged();
	}
	
	public void addListener(Game2DObjectListener listener) {
		listeners.add(Objects.requireNonNull(listener));
	}
	
	public void removeListener(Game2DObjectListener listener) {
		listeners.remove(Objects.requireNonNull(listener));
	}
	
	private void notifyAllListenersChanged() {
		listeners.forEach(l -> l.boundingBoxChanged(this));
	}
	
	private  void notifyAllListenersDestroyed() {
		listeners.forEach(l -> l.objectDestroyed(this));
	}
	
	@Override
	public void destroy() {
		notifyAllListenersDestroyed();
	}

	@Override
	public String toString() {
		return "Game2DObject [boundingBox=" + boundingBox + "]";
	}
	
	private static class UnmodifiableBoundingBox implements BoundingBox2D {

		private BoundingBox2D boundingBox;
		
		public UnmodifiableBoundingBox(BoundingBox2D boundingBox) {
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
		public boolean containsPoint(double x, double y) {
			return boundingBox.containsPoint(x, y);
		}
		
		@Override
		public boolean collidesWith(BoundingBox2D other) {
			return boundingBox.collidesWith(other);
		}
		
		@Override
		public boolean isOnTopOf(BoundingBox2D other) {
			return boundingBox.isOnTopOf(other);
		}
		
		@Override
		public boolean isAbove(BoundingBox2D other) {
			return boundingBox.isAbove(other);
		}
		
		@Override
		public boolean isPartOf(BoundingBox2D other) {
			return boundingBox.isPartOf(other);
		}
		
		@Override
		public boolean intersects(BoundingBox2D other) {
			return boundingBox.intersects(other);
		}
		
		@Override
		public boolean isBetweenVerticalBoundariesOf(BoundingBox2D other) {
			return boundingBox.isBetweenVerticalBoundariesOf(other);
		}

		@Override
		public String toString() {
			return boundingBox.toString();
		}

	}

}
