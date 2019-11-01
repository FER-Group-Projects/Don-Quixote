package hr.fer.zemris.projekt.model.objects;

public interface BoundingBox {

	double getX();
	
	void setX(double x);
	
	double getY();
	
	void setY(double y);
	
	double getWidth();
	
	void setWidth(double width);

	double getHeight();
	
	void setHeight(double height);
	
	void setLocation(double x, double y);
	
}
