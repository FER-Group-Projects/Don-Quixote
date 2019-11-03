package hr.fer.zemris.projekt.model.objects.impl;

import hr.fer.zemris.projekt.model.objects.BoundingBox2D;

public class BoundingBox2DImpl implements BoundingBox2D {
	
	// x and y coordinate of the upper left vertex of the bounding box (rectangle)
	private double x;
	private double y;
	private double width;
	private double height;
	
	public BoundingBox2DImpl(double x, double y, double width, double height) {
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
	public boolean collidesWith(BoundingBox2D other) {
		
		double thisXLeft = this.x;
		double thisXRight = this.x + this.width - 1;
		double thisYUp = this.y;
		double thisYDown = this.y - this.height - 1;
		
		double otherXLeft = other.getX();
		double otherXRight = other.getX() + other.getWidth() - 1;
		double otherYUp = other.getY();
		double otherYDown = other.getY() - other.getHeight() - 1;
		
		if (thisXLeft <= otherXRight && thisXRight >= otherXLeft &&
			     thisYUp >= otherYDown && thisYDown <= otherYUp)
		return true;
		
		return false;
		
	}
	
	@Override
	public boolean isOnTopOf(BoundingBox2D other) {
		return (other.getY() == this.y - this.height) && (other.getX() + other.getWidth() >= this.x) && (other.getX() <= this.x);
	}
	
	@Override
	public boolean isAbove(BoundingBox2D other) {
		return (other.getY() <= this.y - this.height);
	}
	
	@Override
	public boolean isPartOf(BoundingBox2D other) {
		return (this.y <= other.getY()) && 
				(this.x >= other.getX()) &&
				(this.y - this.height >= other.getY() - other.getHeight()) &&
				(this.x + this.width <= other.getX() + other.getWidth());
	}
	
	@Override
	public boolean isBetweenVerticalBoundariesOf(BoundingBox2D other) {
		return (this.x > other.getX()) && (this.x < other.getX() + other.getWidth()) &&
				(this.x + this.width > other.getX()) && (this.x + this.width < other.getX() + other.getWidth());
	}

	@Override
	public String toString() {
		return "BoundingRectangle [x=" + x + ", y=" + y + ", width=" + width + ", height=" + height + "]";
	}

}
