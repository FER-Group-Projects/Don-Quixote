package hr.fer.zemris.projekt.model.objects;

// BoundingBox defined by : 1) x and y coordinate of the upper left vertex of the bounding box (rectangle)
//                          2) height and width of the bounding box (rectangle)
public interface BoundingBox2D {

	double getX();
	
	void setX(double x);
	
	double getY();
	
	void setY(double y);
	
	double getWidth();
	
	void setWidth(double width);

	double getHeight();
	
	void setHeight(double height);
	
	void setLocation(double x, double y);
	
	boolean collidesWith(BoundingBox2D other);
	
	boolean isOnTopOf(BoundingBox2D other);
	
	boolean isAbove(BoundingBox2D other);
	
	boolean isPartOf(BoundingBox2D other);
	
}
