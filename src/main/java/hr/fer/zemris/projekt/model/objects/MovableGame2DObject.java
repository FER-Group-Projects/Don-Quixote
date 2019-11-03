package hr.fer.zemris.projekt.model.objects;

public abstract class MovableGame2DObject extends Game2DObject {

	private double velocityX;
	private double velocityY;
	
	private double defaultSpeedGround;
	private double defaultSpeedLadder;
	
	private boolean isOnGround = false;
	private boolean isOnLadders = false;
	private boolean isAboveLadders = false;
	private boolean isInGround = false;
	
	public MovableGame2DObject(BoundingBox2D position, double velocityX, double velocityY, double defaultSpeedGround,
			double defaultSpeedLadder) {
		super(position);
		this.velocityX = velocityX;
		this.velocityY = velocityY;
		this.defaultSpeedGround = defaultSpeedGround;
		this.defaultSpeedLadder = defaultSpeedLadder;
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
	
	public boolean isAboveLadders() {
		return isAboveLadders;
	}
	
	public void setAboveLadders(boolean isAboveLadders) {
		this.isAboveLadders = isAboveLadders;
	}
	
	public boolean isInGround() {
		return isInGround;
	}
	
	public void setInGround(boolean isInGround) {
		this.isInGround = isInGround;
	}
	
	public double getDefaultSpeedGround() {
		return defaultSpeedGround;
	}
	
	public void setDefaultSpeedGround(double defaultSpeedGround) {
		this.defaultSpeedGround = defaultSpeedGround;
	}
	
	public double getDefaultSpeedLadder() {
		return defaultSpeedLadder;
	}
	
	public void setDefaultSpeedLadder(double defaultSpeedLadder) {
		this.defaultSpeedLadder = defaultSpeedLadder;
	}

	@Override
	public String toString() {
		return "MoveableGame2DObject [velocityX=" + velocityX + ", velocityY=" + velocityY + "]";
	}
	
}
