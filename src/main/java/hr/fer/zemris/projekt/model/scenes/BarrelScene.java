package hr.fer.zemris.projekt.model.scenes;

import java.util.ArrayList;
import java.util.List;

import hr.fer.zemris.projekt.model.controller.GameController;
import hr.fer.zemris.projekt.model.controller.impl.GameControllerImpl;
import hr.fer.zemris.projekt.model.controller.impl.GameControllerImpl.GameParameters;
import hr.fer.zemris.projekt.model.objects.Game2DObject;
import hr.fer.zemris.projekt.model.objects.impl.Barrel;
import hr.fer.zemris.projekt.model.objects.impl.BoundingBox2DImpl;
import hr.fer.zemris.projekt.model.objects.impl.Platform;
import hr.fer.zemris.projekt.model.objects.impl.Player;

// 1 platform, no ladders
// two barrels on platform 1
public class BarrelScene implements SceneGenerator {
	
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
	private double barrelWidth;
	private double barrelHeight;
	
	private double playerX;
	private double playerY;
	private double platform1X;
	private double platform1Y;
	private double barrel1X;
	private double barrel1Y;
	private double barrel2X;
	private double barrel2Y;

	public BarrelScene(int tickRatePerSec, double gravitationalAcceleration, double barrelLadderProbability,
			double playerDefaultSpeedGround, double playerDefaultSpeedLadders, double playerDefaultSpeedJump,
			double otherDefaultSpeedGround, double otherDefaultSpeedLadders, double playerWidth, double playerHeight,
			double platformWidth, double platformHeight, double barrelWidth, double barrelHeight) {
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
		this.barrelWidth = barrelWidth;
		this.barrelHeight = barrelHeight;
	
		init();
	}

	private void init() {		
		platform1X = 10;
		platform1Y = 10 + platformHeight;
		
		playerX = platform1X + platformWidth / 12;
		playerY = platform1Y + playerHeight;
		
		barrel1X = platform1X + platformWidth / 2 - barrelWidth / 2;
		barrel2X = platform1X + 11 * platformWidth / 12;
		barrel1Y = platform1Y + barrelHeight;
		barrel2Y = barrel1Y;
	}

	@Override
	public GameController generateScene() {
		Player p = new Player(new BoundingBox2DImpl(playerX, playerY, playerWidth, playerHeight), 0, 0, "Player0");
		
		List<Game2DObject> otherObjects = new ArrayList<>();
		otherObjects.add(new Platform(new BoundingBox2DImpl(platform1X, platform1Y, platformWidth, platformHeight)));
		otherObjects.add(new Barrel(new BoundingBox2DImpl(barrel1X, barrel1Y, barrelWidth, barrelHeight), -otherDefaultSpeedGround, 0));
		otherObjects.add(new Barrel(new BoundingBox2DImpl(barrel2X, barrel2Y, barrelWidth, barrelHeight), -otherDefaultSpeedGround, 0));
		
		GameParameters parameters = new GameParameters(tickRatePerSec, gravitationalAcceleration, barrelLadderProbability, playerDefaultSpeedGround, playerDefaultSpeedLadders, playerDefaultSpeedJump, otherDefaultSpeedGround, otherDefaultSpeedLadders);
		
		return new GameControllerImpl(p, otherObjects, parameters);
	}

}
