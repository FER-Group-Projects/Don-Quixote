package hr.fer.zemris.projekt.model.scenes;

import java.util.ArrayList;
import java.util.List;

import hr.fer.zemris.projekt.model.controller.GameController;
import hr.fer.zemris.projekt.model.controller.impl.GameControllerImpl;
import hr.fer.zemris.projekt.model.controller.impl.GameControllerImpl.GameParameters;
import hr.fer.zemris.projekt.model.objects.Game2DObject;
import hr.fer.zemris.projekt.model.objects.impl.Barrel;
import hr.fer.zemris.projekt.model.objects.impl.BoundingBox2DImpl;
import hr.fer.zemris.projekt.model.objects.impl.Ladder;
import hr.fer.zemris.projekt.model.objects.impl.Platform;
import hr.fer.zemris.projekt.model.objects.impl.Player;

// 3 platforms equally spaced apart
// 3 ladders (two on platform 1 and one on platform 2)
// player in the middle of platform 1
// two ladders on platform 1 equally spaced from the center of that platform
// one ladder on platform 2 in the middle of that platform
// two barrels on platform 1, one on platform 2 and two on platform 3
public class ClimbingBarrelScene implements SceneGenerator {
	
	private int tickRatePerSec;
	private double gravitationalAcceleration;
	private double barrelLadderProbability;

	private double playerDefaultSpeedGround;
	private double playerDefaultSpeedLadders;
	private double playerDefaultSpeedJump;
	
	private double otherDefaultSpeedGround;
	private double otherDefaultSpeedLadders;
	
	private double playerWidth;
	private double playerHeight;
	private double platformWidth;
	private double platformHeight;
	private double ladderWidth;
	private double barrelWidth;
	private double barrelHeight;
	
	private double playerX;
	private double playerY;
	private double platform1X;
	private double platform1Y;
	private double platform2X;
	private double platform2Y;
	private double platform3X;
	private double platform3Y;
	private double ladderHeight;
	private double ladder1X;
	private double ladder1Y;
	private double ladder2X;
	private double ladder2Y;
	private double ladder3X;
	private double ladder3Y;
	private double barrel1X;
	private double barrel1Y;
	private double barrel2X;
	private double barrel2Y;
	private double barrel3X;
	private double barrel3Y;
	private double barrel4X;
	private double barrel4Y;
	private double barrel5X;
	private double barrel5Y;

	public ClimbingBarrelScene(int tickRatePerSec, double gravitationalAcceleration, double barrelLadderProbability,
			double playerDefaultSpeedGround, double playerDefaultSpeedLadders, double playerDefaultSpeedJump,
			double otherDefaultSpeedGround, double otherDefaultSpeedLadders, double playerWidth, double playerHeight,
			double platformWidth, double platformHeight, double ladderWidth, double barrelWidth, double barrelHeight) {
		this.tickRatePerSec = tickRatePerSec;
		this.gravitationalAcceleration = gravitationalAcceleration;
		this.barrelLadderProbability = barrelLadderProbability;
		this.playerDefaultSpeedGround = playerDefaultSpeedGround;
		this.playerDefaultSpeedLadders = playerDefaultSpeedLadders;
		this.playerDefaultSpeedJump = playerDefaultSpeedJump;
		this.otherDefaultSpeedGround = otherDefaultSpeedGround;
		this.otherDefaultSpeedLadders = otherDefaultSpeedLadders;
		this.playerWidth = playerWidth;
		this.playerHeight = playerHeight;
		this.platformWidth = platformWidth;
		this.platformHeight = platformHeight;
		this.ladderWidth = ladderWidth;
		this.barrelWidth = barrelWidth;
		this.barrelHeight = barrelHeight;
	
		init();
	}

	private void init() {
		double jumpHalfTime = playerDefaultSpeedJump / gravitationalAcceleration;
		double maxPlayerJumpHeight = playerDefaultSpeedJump * jumpHalfTime - gravitationalAcceleration / 2 * jumpHalfTime * jumpHalfTime;
		double platformDelta = playerHeight + maxPlayerJumpHeight + platformHeight;
		ladderHeight = platformDelta - platformHeight;
		
		platform1X = 10;
		platform1Y = 10 + platformHeight;
		platform2X = 10;
		platform2Y = platform1Y + platformDelta;
		platform3X = 10;
		platform3Y = platform2Y + platformDelta;
		
		playerX = platform1X + platformWidth / 2 - playerWidth / 2;
		playerY = platform1Y + playerHeight;
		
		ladder1X = 10 + platformWidth / 10;
		ladder2X = 10 + platformWidth - platformWidth / 10 - ladderWidth;
		ladder1Y = platform1Y + ladderHeight;
		ladder2Y = ladder1Y;
		ladder3X = 10 + platformWidth / 2 - ladderWidth / 2;
		ladder3Y = platform2Y + ladderHeight;
		
		barrel1X = ladder1X + ladderWidth / 2 - barrelWidth / 2;
		barrel2X = ladder2X + ladderWidth / 2 - barrelWidth / 2;
		barrel1Y = platform1Y + barrelHeight;
		barrel2Y = barrel1Y;
		
		barrel3X = ladder3X + ladderWidth / 2 + platformWidth / 4;
		barrel3Y = platform2Y + barrelHeight;
		
		barrel4X = ladder3X - platformWidth / 12;
		barrel5X = ladder3X + platformWidth / 4;
		barrel4Y = platform3Y + barrelHeight;
		barrel5Y = barrel4Y;
		
	}

	@Override
	public GameController generateScene() {
		Player p = new Player(new BoundingBox2DImpl(playerX, playerY, playerWidth, playerHeight), 0, 0, "Player0");
		
		List<Game2DObject> otherObjects = new ArrayList<>();
		otherObjects.add(new Platform(new BoundingBox2DImpl(platform1X, platform1Y, platformWidth, platformHeight)));
		otherObjects.add(new Platform(new BoundingBox2DImpl(platform2X, platform2Y, platformWidth, platformHeight)));
		otherObjects.add(new Platform(new BoundingBox2DImpl(platform3X, platform3Y, platformWidth, platformHeight)));
		otherObjects.add(new Ladder(new BoundingBox2DImpl(ladder1X, ladder1Y, ladderWidth, ladderHeight)));
		otherObjects.add(new Ladder(new BoundingBox2DImpl(ladder2X, ladder2Y, ladderWidth, ladderHeight)));
		otherObjects.add(new Ladder(new BoundingBox2DImpl(ladder3X, ladder3Y, ladderWidth, ladderHeight)));
		otherObjects.add(new Barrel(new BoundingBox2DImpl(barrel1X, barrel1Y, barrelWidth, barrelHeight), otherDefaultSpeedGround, 0));
		otherObjects.add(new Barrel(new BoundingBox2DImpl(barrel2X, barrel2Y, barrelWidth, barrelHeight), -otherDefaultSpeedGround, 0));
		otherObjects.add(new Barrel(new BoundingBox2DImpl(barrel3X, barrel3Y, barrelWidth, barrelHeight), 0, 0));
		otherObjects.add(new Barrel(new BoundingBox2DImpl(barrel4X, barrel4Y, barrelWidth, barrelHeight), 0, 0));
		otherObjects.add(new Barrel(new BoundingBox2DImpl(barrel5X, barrel5Y, barrelWidth, barrelHeight), 0, 0));
		
		GameParameters parameters = new GameParameters(tickRatePerSec, gravitationalAcceleration, barrelLadderProbability, playerDefaultSpeedGround, playerDefaultSpeedLadders, playerDefaultSpeedJump, otherDefaultSpeedGround, otherDefaultSpeedLadders);
		
		return new GameControllerImpl(p, otherObjects, parameters);
	}

	@Override
	public GameController generateNewScene() {
		return null;
	}

}
