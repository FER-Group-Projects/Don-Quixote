package hr.fer.zemris.projekt.model.objects;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BoundingBox {
	
	private double x;
	private double y;
	private double width;
	private double height;
	
	List<BoundingBoxListener> listeners = new ArrayList<>();
	
	public BoundingBox(double x, double y, double width, double height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	public void addListener(BoundingBoxListener listener) {
		listeners.add(Objects.requireNonNull(listener));
	}
	
	public void removeListener(BoundingBoxListener listener) {
		listeners.remove(Objects.requireNonNull(listener));
	}
	
	private void notifyAllListeners() {
		listeners.forEach(l -> l.bouningBoxChange(this));
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
		notifyAllListeners();
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
		notifyAllListeners();
	}
	
	public void setPosition(double x, double y) {
		this.x = x;
		this.y = y;
		notifyAllListeners();
	}

	public double getWidth() {
		return width;
	}

	public void setWidth(double width) {
		this.width = width;
		notifyAllListeners();
	}

	public double getHeight() {
		return height;
	}

	public void setHeight(double height) {
		this.height = height;
		notifyAllListeners();
	}

	@Override
	public int hashCode() {
		return Objects.hash(height, width, x, y);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof BoundingBox))
			return false;
		BoundingBox other = (BoundingBox) obj;
		return height == other.height && width == other.width && x == other.x && y == other.y;
	}

	@Override
	public String toString() {
		return "BoundingRectangle [x=" + x + ", y=" + y + ", width=" + width + ", height=" + height + "]";
	}

}
