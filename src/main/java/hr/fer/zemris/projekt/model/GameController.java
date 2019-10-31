package hr.fer.zemris.projekt.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import hr.fer.zemris.projekt.model.objects.BoundingBox;
import hr.fer.zemris.projekt.model.objects.Game2DObject;
import hr.fer.zemris.projekt.model.objects.Player;

public class GameController {
	
	public static class GameParameters {
		private int visibleScreenWidth;
		private int visibleScreenHeight;
		private int fps;
		private double gravitationalAcceleration;
		private double playerSpeedGround;
		private double playerSpeedLadders;
		private double playerJumpSpeed;
		private double barrelSpeedGround;
		private double barrelSpeedLadder;
		private double barrelLadderProbability;
		
		public GameParameters(int visibleScreenWidth, int visibleScreenHeight, int fps, double gravitationalAcceleration,
				double playerSpeedGround, double playerSpeedLadders, double playerJumpSpeed, 
				double barrelSpeedGround, double barrelSpeedLadder, double barrelLadderProbability) {
			this.visibleScreenWidth = visibleScreenWidth;
			this.visibleScreenHeight = visibleScreenHeight;
			this.fps = fps;
			this.gravitationalAcceleration = gravitationalAcceleration;
			this.playerSpeedGround = playerSpeedGround;
			this.playerSpeedLadders = playerSpeedLadders;
			this.playerJumpSpeed = playerJumpSpeed;
			this.barrelSpeedGround = barrelSpeedGround;
			this.barrelSpeedLadder = barrelSpeedLadder;
			this.barrelLadderProbability = barrelLadderProbability;
		}

		public int getVisibleScreenWidth() {
			return visibleScreenWidth;
		}

		public int getVisibleScreenHeight() {
			return visibleScreenHeight;
		}

		public int getFps() {
			return fps;
		}

		public double getGravitationalAcceleration() {
			return gravitationalAcceleration;
		}

		public double getPlayerSpeedGround() {
			return playerSpeedGround;
		}

		public double getPlayerSpeedLadders() {
			return playerSpeedLadders;
		}

		public double getPlayerJumpSpeed() {
			return playerJumpSpeed;
		}

		public double getBarrelSpeedGround() {
			return barrelSpeedGround;
		}

		public double getBarrelSpeedLadder() {
			return barrelSpeedLadder;
		}

		public double getBarrelLadderProbability() {
			return barrelLadderProbability;
		}
		
	}	
		
	public enum PlayerAction {
		RIGHT,
		LEFT,
		UP,
		DOWN,
		JUMP;
	}
	
	private Player player;
	private List<Game2DObject> objects = new ArrayList<>();
	private GameParameters params;
	
	private double tickDelay;
	private Map<PlayerAction, Boolean> actions = new HashMap<>();
	
	public GameController(Player p, List<Game2DObject> objects, GameParameters parameters) {
		this.player = Objects.requireNonNull(p);
		objects.forEach(o -> {
			if(o instanceof Player) throw new IllegalArgumentException("Collection of other game objects must not contain a player!");
			this.objects.add(o);
		});
		this.params = parameters;
		
		this.tickDelay = 1000.0/params.fps * 1E-3;
		actions.put(PlayerAction.RIGHT, false);
		actions.put(PlayerAction.LEFT, false);
		actions.put(PlayerAction.UP, false);
		actions.put(PlayerAction.DOWN, false);
		actions.put(PlayerAction.JUMP, false);
	}
	
	public synchronized boolean getPlayerAction(PlayerAction action) {
		return actions.get(Objects.requireNonNull(action));
	}
	
	public synchronized void setPlayerAction(PlayerAction action, boolean value) {
		actions.put(Objects.requireNonNull(action), value);
	}
	
	public synchronized void tick() {
		
		BoundingBox pbb = player.getPosition();
		
		if(player.getVelocityX()!=0 || player.getVelocityY()!=0)
			pbb.setPosition(pbb.getX() + player.getVelocityX()*tickDelay, pbb.getY() + player.getVelocityY()*tickDelay);
		
		if(player.isOnGround()) {
			if(actions.get(PlayerAction.LEFT) && !actions.get(PlayerAction.RIGHT)) {
				player.setVelocityX(-params.playerSpeedGround);
			} else if(!actions.get(PlayerAction.LEFT) && actions.get(PlayerAction.RIGHT)){
				player.setVelocityX(params.playerSpeedGround);
			} else {
				player.setVelocityX(0);
			}
			
			if(actions.get(PlayerAction.JUMP)) {
				player.setVelocityY(params.playerJumpSpeed);
				player.setOnGround(false);
			} else {	
				player.setVelocityY(0);
			}
			
		} else {
			player.setVelocityY(player.getVelocityY()-params.gravitationalAcceleration*tickDelay);
		}
	}

}
