package hr.fer.zemris.projekt.model.raycollider;

/**
 * This class models an immutable vector in a 2D space
 * @author Matija
 *
 */
public class Vector2D {
	
	private final double x;
	private final double y;

	/**
	 * Creates a vector with x y and z components
	 * @param x x component of the vector
	 * @param y y component of the vector
	 */
	public Vector2D(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Returns the norm of the vector
	 * @return  the norm of the vector
	 */
	public double norm() {
		return Math.sqrt(x*x+y*y);
	}
	
	/**
	 * Returns normalized vector so that its norm is 1
	 * @return new vector that has the same direction but norm of 1
	 */
	public Vector2D normalized() {
		double length = norm();
		return new Vector2D(this.x/length, this.y/length);
	}
	
	/**
	 * Returns new vector that is the result of this vector plus other vector
	 * @param other vector to be added to this one
	 * @return new vector that is the result of this vector plus other vector
	 */
	public Vector2D add(Vector2D other) {
		return new Vector2D(this.x+other.x, this.y+other.y);
	}
	
	/**
	 * Returns new vector that is the result of this vector minus other vector
	 * @param other vector to be subtracted to this one
	 * @return new vector that is the result of this vector minus other vector
	 */
	public Vector2D sub(Vector2D other) {
		return new Vector2D(this.x-other.x, this.y-other.y);
	}
	
	/**
	 * Returns a dot product between two vectors (this vector and other)
	 * @param other multiplier
	 * @return dot product between two vectors (this vector and other)
	 */
	public double dot(Vector2D other) {
		return this.x*other.x + this.y*other.y;
	}
	
	/**
	 * Returns an exterior (wedge) product between two vectors (this vector and other)
	 * @param other multiplier
	 * @return exterior (wedge) product between two vectors (this vector and other)
	 */
	public double exterior(Vector2D other) {
		return this.x*other.y - this.y*other.x;
	}
	
	/**
	 * Scales the norm of the vector
	 * @param s scalar with which to sale the norm
	 * @return new vector that has the same direction as this one but norm scaled by s
	 */
	public Vector2D scale(double s) {
		return new Vector2D(this.x*s, this.y*s);
	}
	
	/**
	 * Rotates the vector for a given angle
	 * @param offset rotation angle
	 * @return rotated vector
	 */
	public Vector2D rotate(double angle) {
		double x = this.x * Math.cos(angle) - this.y * Math.sin(angle);
	    double y = this.x * Math.sin(angle) + this.y * Math.cos(angle);
		return new Vector2D(x, y);
	}
	
	/**
	 * Returns cosine of the angle between this vector and other
	 * @param other other vector
	 * @return cosine of the angle between this vector and other
	 */
	public double cosAngle(Vector2D other) {
		return dot(other)/(this.norm()*other.norm());
	}
	
	/**
	 * Returns the x component of the vector
	 * @return the x component of the vector
	 */
	public double getX() {
		return x;
	}
	
	/**
	 * Returns the y component of the vector
	 * @return the y component of the vector
	 */
	public double getY() {
		return y;
	}
	
	/**
	 * Returns components of the vector in a double array with length 3
	 * ( double[] {xcomponent, ycomponent} ).
	 * @return components of the vector as a double array
	 */
	public double[] toArray() {
		return new double[] {x, y};
	}
	
	@Override
	public String toString() {
		return String.format("Vector2D (%.6f, %.6f)", x, y);
	}

}
