package hr.fer.zemris.projekt.model.controller.impl;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

import hr.fer.zemris.projekt.model.controller.GameController;
import hr.fer.zemris.projekt.model.controller.GameControllerListener;
import hr.fer.zemris.projekt.model.controller.PlayerAction;
import hr.fer.zemris.projekt.model.objects.BoundingBox2D;
import hr.fer.zemris.projekt.model.objects.Game2DObject;
import hr.fer.zemris.projekt.model.objects.Game2DObjectListener;
import hr.fer.zemris.projekt.model.objects.MovableGame2DObject;
import hr.fer.zemris.projekt.model.objects.impl.Barrel;
import hr.fer.zemris.projekt.model.objects.impl.BoundingBox2DImpl;
import hr.fer.zemris.projekt.model.objects.impl.Ladder;
import hr.fer.zemris.projekt.model.objects.impl.Platform;
import hr.fer.zemris.projekt.model.objects.impl.Player;

public class GameControllerImpl implements GameController, Game2DObjectListener {
	
	private Random random = new Random(System.currentTimeMillis());

	private List<GameControllerListener> listeners = new CopyOnWriteArrayList<>();
	
	private Player player;
	private List<Game2DObject> objects = new LinkedList<>();
	private GameParameters params;

	private double tickDelay;
	private EnumSet<PlayerAction> actions = EnumSet.noneOf(PlayerAction.class);

	public GameControllerImpl(Player p, List<Game2DObject> otherObjects, GameParameters parameters) {
		this.player = Objects.requireNonNull(p);
		this.player.addListener(this);
		otherObjects.forEach(o -> {
			if (o instanceof Player)
				throw new IllegalArgumentException("Collection of other game objects must not contain a player!");
			this.objects.add(o);
			o.addListener(this);
		});
		this.params = parameters;

		this.tickDelay = 1000.0 / params.tickRatePerSec * 1E-3;
	}
	
	@Override
	public List<Game2DObject> getGameObjects() {
		var objs = new ArrayList<>(objects);
		objs.add(player);
		return objs;
	}
	
	public GameParameters getGameParameters() {
		return params;
	}

	@Override
	public synchronized void setPlayerAction(PlayerAction action) {
		actions.add(action);
		listeners.forEach(l -> l.playerActionStateChanged(action, true));
	}

	@Override
	public synchronized void unsetPlayerAction(PlayerAction action) {
		actions.remove(action);
		listeners.forEach(l -> l.playerActionStateChanged(action, false));
	}

	@Override
	public void addListener(GameControllerListener listener) {
		listeners.add(Objects.requireNonNull(listener));
	}

	@Override
	public void removeListener(GameControllerListener listener) {
		listeners.remove(Objects.requireNonNull(listener));
	}

	@Override
	public synchronized void addGameObject(Game2DObject object) {
		if(object instanceof Player) throw new IllegalArgumentException("Player cannot be added as object!");
		
		objects.add(Objects.requireNonNull(object));
		object.addListener(this);
		listeners.forEach(l -> l.gameObjectAdded(object));
	}

	@Override
	public synchronized void removeGameObject(Game2DObject object) {
		objects.remove(Objects.requireNonNull(object));
		object.removeListener(this);
		listeners.forEach(l -> l.gameObjectRemoved(object));
	}
	
	@Override
	public void boundingBoxChanged(Game2DObject source) {
		listeners.forEach(l -> l.gameObjectChanged(source));
	}

	@Override
	public void objectDestroyed(Game2DObject source) {
		listeners.forEach(l -> l.gameObjectDestroyed(source));
		removeGameObject(source);
	}
	
	public synchronized void tick() {

		if (!player.isAlive())
			return;

		// Move player
		BoundingBox2D newPlayerBB = new BoundingBox2DImpl(
				player.getBoundingBox().getX() + player.getVelocityX() * tickDelay,
				player.getBoundingBox().getY() + player.getVelocityY() * tickDelay, player.getBoundingBox().getWidth(),
				player.getBoundingBox().getHeight());

		// Move other movable objects and check for every barrel if it goes down in next tick
		Map<MovableGame2DObject, BoundingBox2D> moveMap = new HashMap<MovableGame2DObject, BoundingBox2D>();
		Set<MovableGame2DObject> goDownObjects = new HashSet<>();
		for (Game2DObject obj : objects) {

			if (!(obj instanceof MovableGame2DObject))
				continue;
			MovableGame2DObject m = (MovableGame2DObject) obj;

			BoundingBox2D bb = new BoundingBox2DImpl(m.getBoundingBox().getX(), m.getBoundingBox().getY(),
					m.getBoundingBox().getWidth(), m.getBoundingBox().getHeight());

			bb.setX(bb.getX() + m.getVelocityX() * tickDelay);
			bb.setY(bb.getY() + m.getVelocityY() * tickDelay);
			moveMap.put(m, bb);
			
			if(m instanceof Barrel && !m.isAboveLadders() && random.nextDouble() < params.barrelLadderProbability) {
				goDownObjects.add((Barrel) m);
			}

		}

		// Check collision : player with other objects
		CollisionCollection collision = checkPlayerCollision(player.getBoundingBox(), newPlayerBB);
		if (!player.isAlive()) {
			player.destroy();
			return; // Player is dead
		}

		// Check collision for other movable objects
		Map<MovableGame2DObject, CollisionCollection> collisionMap = checkOtherCollision(moveMap, goDownObjects);

		// Update velocities for player
		updatePlayerVelocity(newPlayerBB, collision);

		// Update velocities for other movable objects
		updateOtherVelocity(moveMap, collisionMap, goDownObjects);

		// Update location for player
		if (player.getBoundingBox().getX() != newPlayerBB.getX()
				|| player.getBoundingBox().getY() != newPlayerBB.getY()) {
			player.setLocation(newPlayerBB.getX(), newPlayerBB.getY());
		}

		// Update location for other movable objects
		for (var move : moveMap.entrySet()) {
			if (move.getKey().getBoundingBox().getX() != move.getValue().getX()
					|| move.getKey().getBoundingBox().getY() != move.getValue().getY()) {
				move.getKey().setLocation(move.getValue().getX(), move.getValue().getY());
			}
		}
		
		listeners.forEach(l -> l.tickPerformed());

	}

	private CollisionCollection checkPlayerCollision(BoundingBox2D oldBBPlayer, BoundingBox2D newBBPlayer) {

		player.setOnGround(false);
		player.setOnLadders(false);
		player.setAboveLadders(false);
		player.setInGround(false);
		CollisionCollection collision = new CollisionCollection();
		
		for (Game2DObject g2o : objects) {

			if (!newBBPlayer.collidesWith(g2o.getBoundingBox()))
				continue;

			if (g2o instanceof Platform) {
				if (oldBBPlayer.isAbove(g2o.getBoundingBox()) && !oldBBPlayer.isOnTopOf(g2o.getBoundingBox()) || newBBPlayer.isOnTopOf(g2o.getBoundingBox())) { // Player is on platform only if it approaches it from the top or was already on platform
					player.setOnGround(true);
					player.setJumping(false);
				} else if (newBBPlayer.intersects(g2o.getBoundingBox())){										// Else, player is (in 3D space) hanging on the edge
					player.setInGround(true);
				} // else it is a corner touch
				collision.p = (Platform) g2o ; 
			} else if (g2o instanceof Ladder) {
				if (newBBPlayer.isBetweenVerticalBoundariesOf(g2o.getBoundingBox())) { // Player is on ladder only if his whole bounding box is within left and right ladder boundary
					player.setOnLadders(true);
					collision.l = (Ladder) g2o;
				}
			} else if (g2o instanceof Barrel) {
				player.setAlive(false);
				collision.b = (Barrel) g2o;
			}

		}

		if (player.isOnGround()) {
			// Checking if he is above ladders
			for(var obj : objects) {
				if(!(obj instanceof Ladder)) continue;
				
				Ladder l = (Ladder) obj;
				if(newBBPlayer.isBetweenVerticalBoundariesOf(l.getBoundingBox()) && l.getBoundingBox().getY() == collision.p.getBoundingBox().getY() - collision.p.getBoundingBox().getHeight()) {
					player.setAboveLadders(true);
					break;
				}
			}
			
			// Location correction
			if(!(player.isAboveLadders() && (actions.contains(PlayerAction.UP) || actions.contains(PlayerAction.DOWN))))
				newBBPlayer.setY(collision.p.getBoundingBox().getY() + newBBPlayer.getHeight());
		}
		
		return collision;
	}
	
	private Map<MovableGame2DObject, CollisionCollection> checkOtherCollision(
			Map<MovableGame2DObject, BoundingBox2D> moveMap,
			Set<MovableGame2DObject> goDownObjects) {
		Map<MovableGame2DObject, CollisionCollection> collisionMap = new HashMap<>();

		for (var obj : objects) {
			if (obj instanceof MovableGame2DObject) {
				((MovableGame2DObject) obj).setOnGround(false);
				((MovableGame2DObject) obj).setOnLadders(false);
				((MovableGame2DObject) obj).setAboveLadders(false);
				((MovableGame2DObject) obj).setInGround(false);
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

				BoundingBox2D bb = moveMap.get(obj1);
				BoundingBox2D bb2 = obj2 instanceof MovableGame2DObject ? moveMap.get(obj2) : obj2.getBoundingBox();
				
				if (!bb.collidesWith(bb2))
					continue;

				if (!collisionMap.containsKey(obj1))
					collisionMap.put(obj1, new CollisionCollection());

				if (obj2 instanceof Platform) {
					if (obj1.getBoundingBox().isAbove(obj2.getBoundingBox()) && !obj1.getBoundingBox().isOnTopOf(obj2.getBoundingBox()) || bb.isOnTopOf(obj2.getBoundingBox())) {
						obj1.setOnGround(true);
						collisionMap.get(obj1).p = (Platform) obj2 ; 
					} else if (bb.intersects(obj2.getBoundingBox())){
						obj1.setInGround(true);
						collisionMap.get(obj1).p = (Platform) obj2 ; 
					}
				} else if (obj2 instanceof Ladder) {
					if (bb.isBetweenVerticalBoundariesOf(obj2.getBoundingBox())) {
						obj1.setOnLadders(true);
						collisionMap.get(obj1).l = (Ladder) obj2;
					}
				} else if (obj2 instanceof Barrel) {
					if(bb.getY() == bb2.getY()) {
						obj1.setVelocityX(-obj1.getVelocityX());
						((Barrel) obj2).setVelocityX(-((Barrel) obj2).getVelocityX());
						collisionMap.get(obj1).b = (Barrel) obj2;
						if(obj1.getVelocityX() < 0) bb.setX(bb2.getX() - bb.getWidth() - 1);
						else bb2.setX(bb.getX() - bb2.getWidth() - 1);
					}
				}

			}
		}

		for (var entry : collisionMap.entrySet()) {
			if (entry.getKey().isOnGround()) {	
				// Checking if he is above ladders
				for(var obj : objects) {
					if(!(obj instanceof Ladder)) continue;
					
					Ladder l = (Ladder) obj;
					if(moveMap.get(entry.getKey()).isBetweenVerticalBoundariesOf(l.getBoundingBox()) && l.getBoundingBox().getY() == collisionMap.get(entry.getKey()).p.getBoundingBox().getY() - collisionMap.get(entry.getKey()).p.getBoundingBox().getHeight()) {
						entry.getKey().setAboveLadders(true);
						break;
					}
				}
				
				// Location correction
				if(!( entry.getKey().isAboveLadders() && goDownObjects.contains(entry.getKey()) ))
					moveMap.get(entry.getKey()).setY(collisionMap.get(entry.getKey()).p.getBoundingBox().getY() + moveMap.get(entry.getKey()).getHeight());
			}
		}

		return collisionMap;
	}
	
	private void updatePlayerVelocity(BoundingBox2D newPlayerBB, CollisionCollection collision) {
		// Jump
		if (actions.contains(PlayerAction.JUMP) && !player.isJumping() && player.isOnGround()) {
			player.setVelocityY(params.playerDefaultSpeedJump);
			player.setJumping(true);
		}

		// Left and right movement
		if (player.isOnGround()) {
			if (actions.contains(PlayerAction.LEFT) && !actions.contains(PlayerAction.RIGHT)) {
				player.setVelocityX(-params.playerDefaultSpeedGround);
			} else if (!actions.contains(PlayerAction.LEFT) && actions.contains(PlayerAction.RIGHT)) {
				player.setVelocityX(params.playerDefaultSpeedGround);
			} else {
				player.setVelocityX(0);
			}
			
			if (!player.isOnLadders() && !player.isJumping())
				player.setVelocityY(0);
		}

		// Up and down movement
		if ((player.isOnLadders() || player.isInGround() || player.isAboveLadders()) && !player.isJumping()) {
			if (actions.contains(PlayerAction.UP) && !actions.contains(PlayerAction.DOWN)) {
				player.setVelocityY(params.playerDefaultSpeedLadders);
			} else if (!actions.contains(PlayerAction.UP) && actions.contains(PlayerAction.DOWN)) {
				player.setVelocityY(-params.playerDefaultSpeedLadders);
			} else {
				player.setVelocityY(0);
			}
			
			if(!player.isOnGround())
				player.setVelocityX(0);
		}
		
		// Player cannot go through the platform if he is on ladders, but there is no ladders below him
		if(player.isOnGround() && !player.isAboveLadders() && player.getVelocityY() < 0) {
			player.setVelocityY(0);
		}
		
		// Free fall
		if (!player.isOnGround() && !player.isOnLadders() && !player.isInGround() || player.isJumping()) 
			player.setVelocityY(player.getVelocityY() - params.gravitationalAcceleration * tickDelay);
	}
	
	private void updateOtherVelocity(Map<MovableGame2DObject, BoundingBox2D> moveMap,
			Map<MovableGame2DObject, CollisionCollection> collisionMap,
			Set<MovableGame2DObject> goDownObjects) {
		for (var move : moveMap.entrySet()) {
			var moveObj = move.getKey();
			
			if (moveObj.isOnGround()) {
				if (moveObj.getVelocityX() == 0) {
					if (random.nextDouble() < 0.5)
						moveObj.setVelocityX(params.otherDefaultSpeedGround);
					else
						moveObj.setVelocityX(-params.otherDefaultSpeedGround);
				}
				
				moveObj.setVelocityY(0);
			}

			if (moveObj.isOnLadders() || moveObj.isInGround() || moveObj.isAboveLadders() && goDownObjects.contains(moveObj)) {
				moveObj.setVelocityY(-params.otherDefaultSpeedLadders);

				if (!moveObj.isOnGround())
					moveObj.setVelocityX(0);
			}

			if(moveObj.isOnGround() && !moveObj.isAboveLadders() && moveObj.getVelocityY() < 0) {
				moveObj.setVelocityY(0);
			}
			
			if (!moveObj.isOnGround() && !moveObj.isOnLadders() && !moveObj.isInGround()) // Free fall
				moveObj.setVelocityY(moveObj.getVelocityY() - params.gravitationalAcceleration * tickDelay);
		}
	}
	
	public static class GameParameters {
		private int tickRatePerSec;
		private double gravitationalAcceleration;
		private double barrelLadderProbability;

		private double playerDefaultSpeedGround;
		private double playerDefaultSpeedLadders;
		private double playerDefaultSpeedJump;
		
		private double otherDefaultSpeedGround;
		private double otherDefaultSpeedLadders;
		
		public GameParameters(int tickRatePerSec, double gravitationalAcceleration, double barrelLadderProbability,
				double playerDefaultSpeedGround, double playerDefaultSpeedLadders, double playerDefaultSpeedJump,
				double otherDefaultSpeedGround, double otherDefaultSpeedLadders) {
			this.tickRatePerSec = tickRatePerSec;
			this.gravitationalAcceleration = gravitationalAcceleration;
			this.barrelLadderProbability = barrelLadderProbability;
			this.playerDefaultSpeedGround = playerDefaultSpeedGround;
			this.playerDefaultSpeedLadders = playerDefaultSpeedLadders;
			this.playerDefaultSpeedJump = playerDefaultSpeedJump;
			this.otherDefaultSpeedGround = otherDefaultSpeedGround;
			this.otherDefaultSpeedLadders = otherDefaultSpeedLadders;
		}

		public int getTickRatePerSec() {
			return tickRatePerSec;
		}

		public double getGravitationalAcceleration() {
			return gravitationalAcceleration;
		}

		public double getBarrelLadderProbability() {
			return barrelLadderProbability;
		}

		public double getPlayerDefaultSpeedGround() {
			return playerDefaultSpeedGround;
		}

		public double getPlayerDefaultSpeedLadders() {
			return playerDefaultSpeedLadders;
		}

		public double getPlayerDefaultSpeedJump() {
			return playerDefaultSpeedJump;
		}

		public double getOtherDefaultSpeedGround() {
			return otherDefaultSpeedGround;
		}

		public double getOtherDefaultSpeedLadders() {
			return otherDefaultSpeedLadders;
		}

		@Override
		public String toString() {
			return "GameParameters [tickRatePerSec=" + tickRatePerSec + ", gravitationalAcceleration="
					+ gravitationalAcceleration + ", barrelLadderProbability=" + barrelLadderProbability
					+ ", playerDefaultSpeedGround=" + playerDefaultSpeedGround + ", playerDefaultSpeedLadders="
					+ playerDefaultSpeedLadders + ", playerDefaultSpeedJump=" + playerDefaultSpeedJump
					+ ", otherDefaultSpeedGround=" + otherDefaultSpeedGround + ", otherDefaultSpeedLadders="
					+ otherDefaultSpeedLadders + "]";
		}
	}

	private static class CollisionCollection {
		private Platform p;
		private Ladder l;
		private Barrel b;
	}

}
