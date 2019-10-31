package hr.fer.zemris.projekt.model.objects;

public class Player extends MoveableGame2DObject {
	
	private String name;
	
	private boolean isAlive = true;

	public Player(BoundingBox position, double velocityX, double velocityY, String name) {
		super(position, velocityX, velocityY);
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public boolean isAlive() {
		return isAlive;
	}

	public void setAlive(boolean alive) {
		this.isAlive = alive;
	}

}
