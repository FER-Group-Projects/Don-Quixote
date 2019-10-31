package hr.fer.zemris.projekt.model.objects;

public abstract class MoveableGame2DObject extends Game2DObject {

	private double velocityX;
	private double velocityY;
	
	private boolean isOnGround = false;
	private boolean isOnLadders = false;
	
	public MoveableGame2DObject(BoundingBox position, double velocityX, double velocityY) {
		super(position);
		this.velocityX = velocityX;
		this.velocityY = velocityY;
	}

	public double getVelocityX() {
		return velocityX;
	}

	public void setVelocityX(double velocityX) {
		this.velocityX = velocityX;
	}

	public double getVelocityY() {
		return velocityY;
	}

	public void setVelocityY(double velocityY) {
		this.velocityY = velocityY;
	}
	
	public boolean isOnGround() {
		return isOnGround;
	}
	
	public void setOnGround(boolean isOnGround) {
		this.isOnGround = isOnGround;
	}
	
	public boolean isOnLadders() {
		return isOnLadders;
	}
	
	public void setOnLadders(boolean isOnLadders) {
		this.isOnLadders = isOnLadders;
	}

	@Override
	public String toString() {
		return "MoveableGame2DObject [velocityX=" + velocityX + ", velocityY=" + velocityY + "]";
	}
	
}
