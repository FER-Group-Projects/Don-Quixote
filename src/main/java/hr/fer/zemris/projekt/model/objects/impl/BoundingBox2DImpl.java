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
	public boolean collidesWith(BoundingBox2D r) {
		double tw = this.width;
		double th = this.height;
		double rw = r.getWidth();
		double rh = r.getHeight();

		double tx = this.x;
		double ty = this.y - th;
		double rx = r.getX();
		double ry = r.getY() - rh;
        rw += rx;
        rh += ry;
        tw += tx;
        th += ty;
        //      overflow || intersect
        return ((rw <= rx || rw >= tx) &&
                (rh <= ry || rh >= ty) &&
                (tw <= tx || tw >= rx) &&
                (th <= ty || th >= ry));
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
	public boolean intersects(BoundingBox2D r) {
		double tw = this.width;
		double th = this.height;
		double rw = r.getWidth();
		double rh = r.getHeight();

		double tx = this.x;
		double ty = this.y - th;
		double rx = r.getX();
		double ry = r.getY() - rh;
        rw += rx;
        rh += ry;
        tw += tx;
        th += ty;
        //      overflow || intersect
        return ((rw < rx || rw > tx) &&
                (rh < ry || rh > ty) &&
                (tw < tx || tw > rx) &&
                (th < ty || th > ry));
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
