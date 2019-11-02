package hr.fer.zemris.projekt.model.controller.impl;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import hr.fer.zemris.projekt.model.controller.GameController;
import hr.fer.zemris.projekt.model.controller.GameControllerListener;
import hr.fer.zemris.projekt.model.objects.BoundingBox2D;
import hr.fer.zemris.projekt.model.objects.Game2DObject;
import hr.fer.zemris.projekt.model.objects.MovableGame2DObject;
import hr.fer.zemris.projekt.model.objects.impl.Barrel;
import hr.fer.zemris.projekt.model.objects.impl.BoundingBox2DImpl;
import hr.fer.zemris.projekt.model.objects.impl.Ladder;
import hr.fer.zemris.projekt.model.objects.impl.Platform;
import hr.fer.zemris.projekt.model.objects.impl.Player;

public class GameControllerImpl implements GameController {

	public static class GameParameters {
		private int visibleScreenWidth;
		private int visibleScreenHeight;
		private int fps;
		private double gravitationalAcceleration;
		private double objectLadderProbability;
		
		public GameParameters(int visibleScreenWidth, int visibleScreenHeight, int fps,
				double gravitationalAcceleration, double objectLadderProbability) {
			this.visibleScreenWidth = visibleScreenWidth;
			this.visibleScreenHeight = visibleScreenHeight;
			this.fps = fps;
			this.gravitationalAcceleration = gravitationalAcceleration;
			this.objectLadderProbability = objectLadderProbability;
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
		
		public double getObjectLadderProbability() {
			return objectLadderProbability;
		}
	}
	
	private static class CollisionCollection {
		private Platform p;
		private Ladder l;
		private Barrel b;
	}

	private Player player;
	private List<Game2DObject> objects = new ArrayList<>();
	private GameParameters params;

	private double tickDelay;
	private EnumSet<PlayerAction> actions = EnumSet.noneOf(PlayerAction.class);

	public GameControllerImpl(Player p, List<Game2DObject> objects, GameParameters parameters) {
		this.player = Objects.requireNonNull(p);
		objects.forEach(o -> {
			if (o instanceof Player)
				throw new IllegalArgumentException("Collection of other game objects must not contain a player!");
			this.objects.add(o);
		});
		this.params = parameters;

		this.tickDelay = 1000.0 / params.fps * 1E-3;
	}

	public synchronized void setPlayerAction(PlayerAction action) {
		actions.add(action);
	}

	public synchronized void unsetPlayerAction(PlayerAction action) {
		actions.remove(action);
	}

	public synchronized void tick() {

		if (!player.isAlive())
			return;

		// Move player
		BoundingBox2D newPlayerBB = new BoundingBox2DImpl(
				player.getBoundingBox().getX() + player.getVelocityX() * tickDelay,
				player.getBoundingBox().getY() + player.getVelocityY() * tickDelay, player.getBoundingBox().getWidth(),
				player.getBoundingBox().getHeight());

		// Move other movable objects
		Map<MovableGame2DObject, BoundingBox2D> moveMap = new HashMap<MovableGame2DObject, BoundingBox2D>();
		for (Game2DObject obj : objects) {

			if(!(obj instanceof MovableGame2DObject)) continue;
			MovableGame2DObject m = (MovableGame2DObject) obj;

			BoundingBox2D bb = new BoundingBox2DImpl(m.getBoundingBox().getX(), m.getBoundingBox().getY(),
												     m.getBoundingBox().getWidth(), m.getBoundingBox().getHeight());

			bb.setX(bb.getX() + m.getVelocityX() * tickDelay);
			bb.setY(bb.getY() + m.getVelocityY() * tickDelay);
			moveMap.put(m, bb);

		}
		
		// Check collision : player with other objects
		CollisionCollection collision = checkPlayerCollision(player.getBoundingBox(), newPlayerBB);
		if(!player.isAlive()) return; // Player is dead

		// Check collision for other movable objects
		Map<MovableGame2DObject, CollisionCollection> collisionMap = checkOtherCollision(moveMap);
		
		// Update velocities for player
		updatePlayerVelocity(newPlayerBB, collision);
		
		// Update velocities for other movable objects
		updateOtherVelocity(moveMap, collisionMap);

		// Update location for player
		if (player.getBoundingBox().getX() != newPlayerBB.getX()
				|| player.getBoundingBox().getY() != newPlayerBB.getY())
			player.setLocation(newPlayerBB.getX(), newPlayerBB.getY());
		
		// Update location for other movable objects
		for(var move : moveMap.entrySet()) {			
			if (move.getKey().getBoundingBox().getX() != move.getValue().getX()
					|| move.getKey().getBoundingBox().getY() != move.getValue().getY())
				move.getKey().setLocation(move.getValue().getX(), move.getValue().getY());
		}
		
	}

	private void updateOtherVelocity(Map<MovableGame2DObject, BoundingBox2D> moveMap, Map<MovableGame2DObject, CollisionCollection> collisionMap) {
		for(var move : moveMap.entrySet()) {
			var moveObj = move.getKey();
			if (moveObj.isOnGround()) {
				if (moveObj.getVelocityX() == 0) {
					if(Math.random() < 0.5)
						moveObj.setVelocityX(moveObj.getDefaultSpeedGround());
					else
						moveObj.setVelocityX(-moveObj.getDefaultSpeedGround());
				}

				if (!moveObj.isOnLadders())
					moveObj.setVelocityY(0);
			}

			if (moveObj.isOnLadders()) {
				if(moveObj.isOnGround() && moveObj.getBoundingBox().getY() - moveObj.getBoundingBox().getHeight() > collisionMap.get(moveObj).l.getBoundingBox().getY()
						- collisionMap.get(moveObj).l.getBoundingBox().getHeight() && Math.random() < params.objectLadderProbability) {
					moveObj.setVelocityY(-moveObj.getDefaultSpeedLadder());
				} else {
					moveObj.setVelocityY(0);
				}

				if (!moveObj.isOnGround())
					moveObj.setVelocityX(0);
			}
			
			if (!moveObj.isOnGround() && !moveObj.isOnLadders()) // Free fall
				moveObj.setVelocityY(moveObj.getVelocityY() - params.gravitationalAcceleration * tickDelay);
		}
	}

	private void updatePlayerVelocity(BoundingBox2D newPlayerBB, CollisionCollection collision) {
		if (actions.contains(PlayerAction.JUMP) && !player.isJumping() && player.isOnGround()) {
			player.setVelocityY(player.getDefaultSpeedJump());
			player.setJumping(true);
		}
		
		if (player.isOnGround() && !player.isJumping()) {
			if (actions.contains(PlayerAction.LEFT) && !actions.contains(PlayerAction.RIGHT)) {
				player.setVelocityX(-player.getDefaultSpeedGround());
			} else if (!actions.contains(PlayerAction.LEFT) && actions.contains(PlayerAction.RIGHT)) {
				player.setVelocityX(player.getDefaultSpeedGround());
			} else {
				player.setVelocityX(0);
			}

			if (!player.isOnLadders())
				player.setVelocityY(0);
		}

		if (player.isOnLadders() && !player.isJumping()) {
			if (actions.contains(PlayerAction.UP) && !actions.contains(PlayerAction.DOWN)) {
				player.setVelocityY(player.getDefaultSpeedLadder());
			} else if (!actions.contains(PlayerAction.UP) && actions.contains(PlayerAction.DOWN)) {
				if (newPlayerBB.getY() - newPlayerBB.getHeight() > collision.l.getBoundingBox().getY()
						- collision.l.getBoundingBox().getHeight()) { // If on ladder, you can go down only if you are not on
																	  // the bottom of ladder
					player.setVelocityY(-player.getDefaultSpeedLadder());
				} else {
					player.setVelocityY(0);
				}
			} else {
				player.setVelocityY(0);
			}

			if (!player.isOnGround())
				player.setVelocityX(0);
		}

		if (!player.isOnGround() && !player.isOnLadders() || player.isJumping()) // Free fall
			player.setVelocityY(player.getVelocityY() - params.gravitationalAcceleration * tickDelay);
	}

	private Map<MovableGame2DObject, CollisionCollection> checkOtherCollision(Map<MovableGame2DObject, BoundingBox2D> moveMap) {
		Map<MovableGame2DObject, CollisionCollection> collisionMap = new HashMap<>();
		
		for(var obj : objects) {
			if(obj instanceof MovableGame2DObject) {
				( (MovableGame2DObject) obj ).setOnGround(false);
				( (MovableGame2DObject) obj ).setOnLadders(false);
			}
		}
		
		for (int i = 0; i < objects.size() - 1; i++) {
			for (int j = i + 1; j < objects.size(); j++) {

				MovableGame2DObject obj1;
				Game2DObject obj2;
				if (objects.get(i) instanceof MovableGame2DObject) {
					obj1 = (MovableGame2DObject) objects.get(i);
					obj2 = objects.get(j);
				} else if (objects.get(j) instanceof MovableGame2DObject) {
					obj1 = (MovableGame2DObject) objects.get(j);
					obj2 = objects.get(i);
				} else {
					continue;
				}
				
				if (!obj1.getBoundingBox().collidesWith(obj2.getBoundingBox()))
					continue;
				
				BoundingBox2D bb = moveMap.get(obj1);
				if(!collisionMap.containsKey(obj1))
					collisionMap.put(obj1, new CollisionCollection());

				if (obj2 instanceof Platform) {
					if (obj1.getBoundingBox().getY() - obj1.getBoundingBox().getHeight() >= obj2.getBoundingBox().getY()) {
						obj1.setOnGround(true);
						collisionMap.get(obj1).p = (Platform) obj2;
					}
				} else if (obj2 instanceof Ladder) {
					if (bb.getX() > obj2.getBoundingBox().getX()
							&& bb.getX() < obj2.getBoundingBox().getX() + obj2.getBoundingBox().getWidth()
							&& bb.getX() + bb.getWidth() > obj2.getBoundingBox().getX()
							&& bb.getX() + bb.getWidth() < obj2.getBoundingBox().getX()
									+ obj2.getBoundingBox().getWidth()) {
						obj1.setOnLadders(true);
						collisionMap.get(obj1).l = (Ladder) obj2;
					}
				} else if (obj2 instanceof Barrel) {
					obj1.setVelocityX(-obj1.getVelocityX());
					((Barrel) obj2).setVelocityX(-((Barrel) obj2).getVelocityX());
					collisionMap.get(obj1).b = (Barrel) obj2;
				}

			}
		}
		
		for (var entry : collisionMap.entrySet()) {
			if(entry.getKey().isOnGround() && !entry.getKey().isOnLadders())
				moveMap.get(entry.getKey()).setY(entry.getValue().p.getBoundingBox().getY() + moveMap.get(entry.getKey()).getHeight());
		}
		
		return collisionMap;
	}

	private CollisionCollection checkPlayerCollision(BoundingBox2D oldBBPlayer, BoundingBox2D newBBPlayer) {
		
		player.setOnGround(false);
		player.setOnLadders(false);
		CollisionCollection collision = new CollisionCollection();
		for (Game2DObject g2o : objects) {

			if (!newBBPlayer.collidesWith(g2o.getBoundingBox()))
				continue;

			if (g2o instanceof Platform) {
				if (oldBBPlayer.isAbove(g2o.getBoundingBox())) { // Player is on platform only if it approaches it from the top or was already on
																 // platform
					player.setOnGround(true);
					player.setJumping(false);
					collision.p = (Platform) g2o;
				}
			} else if (g2o instanceof Ladder) {
				if (newBBPlayer.getX() > g2o.getBoundingBox().getX()
						&& newBBPlayer.getX() < g2o.getBoundingBox().getX() + g2o.getBoundingBox().getWidth()
						&& newBBPlayer.getX() + newBBPlayer.getWidth() > g2o.getBoundingBox().getX()
						&& newBBPlayer.getX() + newBBPlayer.getWidth() < g2o.getBoundingBox().getX()
								+ g2o.getBoundingBox().getWidth()) { // Player is on ladder only if his whole
																			// bounding box is within left and right
																			// ladder boundary
					player.setOnLadders(true);
					collision.l = (Ladder) g2o;
				}
			} else if (g2o instanceof Barrel) {
				player.setAlive(false);
				collision.b = (Barrel) g2o;
			}

		}
		
		if(player.isOnGround() && !player.isOnLadders())
			newBBPlayer.setY(collision.p.getBoundingBox().getY() + newBBPlayer.getHeight());
		
		System.out.println(player.isOnGround());
		
		return collision;
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
