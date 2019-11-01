package hr.fer.zemris.projekt.model.controller.impl;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Objects;

import hr.fer.zemris.projekt.model.controller.GameController;
import hr.fer.zemris.projekt.model.controller.GameControllerListener;
import hr.fer.zemris.projekt.model.objects.Game2DObject;
import hr.fer.zemris.projekt.model.objects.impl.Player;

public class GameControllerImpl implements GameController {
	
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
	
	private Player player;
	private List<Game2DObject> objects = new ArrayList<>();
	private GameParameters params;
	
	private double tickDelay;
	private EnumSet<PlayerAction> actions = EnumSet.noneOf(PlayerAction.class);
	
	public GameControllerImpl(Player p, List<Game2DObject> objects, GameParameters parameters) {
		this.player = Objects.requireNonNull(p);
		objects.forEach(o -> {
			if(o instanceof Player) throw new IllegalArgumentException("Collection of other game objects must not contain a player!");
			this.objects.add(o);
		});
		this.params = parameters;
		
		this.tickDelay = 1000.0/params.fps * 1E-3;
	}
	
	public synchronized void setPlayerAction(PlayerAction action) {
		actions.add(action);
	}
	
	public synchronized void unsetPlayerAction(PlayerAction action) {
		actions.remove(action);
	}
	
	public synchronized void tick() {
		
		if(player.getVelocityX()!=0 || player.getVelocityY()!=0) {
			player.setLocation(player.getBoundingBox().getX() + player.getVelocityX()*tickDelay,
							   player.getBoundingBox().getY() + player.getVelocityY()*tickDelay);
		}
		
		if(player.isOnGround()) {
			if(actions.contains(PlayerAction.LEFT) && !actions.contains(PlayerAction.RIGHT)) {
				player.setVelocityX(-params.playerSpeedGround);
			} else if(!actions.contains(PlayerAction.LEFT) && actions.contains(PlayerAction.RIGHT)){
				player.setVelocityX(params.playerSpeedGround);
			} else {
				player.setVelocityX(0);
			}
			
			if(actions.contains(PlayerAction.JUMP)) {
				player.setVelocityY(params.playerJumpSpeed);
				player.setOnGround(false);
			} else {	
				player.setVelocityY(0);
			}
			
		} else {
			player.setVelocityY(player.getVelocityY()-params.gravitationalAcceleration*tickDelay);
		}
	}

	@Override
	public void addListener(GameControllerListener listener) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeListener(GameControllerListener listener) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addGameObject(Game2DObject object) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeGameObject(Game2DObject object) {
		// TODO Auto-generated method stub
		
	}

}
