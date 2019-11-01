package hr.fer.zemris.projekt.model.objects.impl;

import hr.fer.zemris.projekt.model.objects.BoundingBox;

public class BoundingBoxImpl implements BoundingBox {
	
	private double x;
	private double y;
	private double width;
	private double height;
	
	public BoundingBoxImpl(double x, double y, double width, double height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	public double getX() {
		return x;
	}
	
	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}
	
	public void setY(double y) {
		this.y = y;
	}
	
	public double getWidth() {
		return width;
	}
	
	public void setWidth(double width) {
		this.width = width;
	}

	public double getHeight() {
		return height;
	}
	
	public void setHeight(double height) {
		this.height = height;
	}
	
	public void setLocation(double x, double y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public String toString() {
		return "BoundingRectangle [x=" + x + ", y=" + y + ", width=" + width + ", height=" + height + "]";
	}

}
