package hr.fer.zemris.projekt.model.scenes;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import hr.fer.zemris.projekt.model.controller.impl.GameControllerImpl.GameParameters;
import hr.fer.zemris.projekt.model.objects.Game2DObject;
import hr.fer.zemris.projekt.model.objects.impl.BoundingBox2DImpl;
import hr.fer.zemris.projekt.model.objects.impl.Ladder;
import hr.fer.zemris.projekt.model.objects.impl.Platform;
import hr.fer.zemris.projekt.model.objects.impl.Player;

// 3 platforms equally spaced apart
// 3 ladders (two on platform 1 and one on platform 2)
// player in the middle of platform 1
// two ladders on platform 1 equally spaced from the center of that platform
// one ladder on platform 2 in the middle of that platform
public class ClimbingScene extends AbstractSceneGenerator {

	private final Random random = new Random();

	private final int minimumNumberOfPlatforms;
	private final int maximumNumberOfPlatforms;

	private double playerWidth;
	private double playerHeight;
	private double platformWidth;
	private double platformHeight;
	private double ladderWidth;

	public ClimbingScene(int tickRatePerSec, double gravitationalAcceleration, double barrelLadderProbability,
			double playerDefaultSpeedGround, double playerDefaultSpeedLadders, double playerDefaultSpeedJump,
			double otherDefaultSpeedGround, double otherDefaultSpeedLadders, double playerWidth, double playerHeight,
			double platformWidth, double platformHeight, double ladderWidth) {
		this(new GameParameters(tickRatePerSec, gravitationalAcceleration, barrelLadderProbability, playerDefaultSpeedGround,
						playerDefaultSpeedLadders, playerDefaultSpeedJump, otherDefaultSpeedGround, otherDefaultSpeedLadders),
				3, 7, playerWidth, playerHeight, platformWidth, platformHeight, ladderWidth);
	}

	public ClimbingScene(GameParameters parameters, int minimumNumberOfPlatforms, int maximumNumberOfPlatforms, double playerWidth, double playerHeight,
						 double platformWidth, double platformHeight, double ladderWidth) {
		this.minimumNumberOfPlatforms = minimumNumberOfPlatforms;
		this.maximumNumberOfPlatforms = maximumNumberOfPlatforms;
		this.parameters = parameters;
		this.playerWidth = playerWidth;
		this.playerHeight = playerHeight;
		this.platformWidth = platformWidth;
		this.platformHeight = platformHeight;
		this.ladderWidth = ladderWidth;
	}

	@Override
	protected void generateGameObjects() {
		double playerDefaultSpeedJump = parameters.getPlayerDefaultSpeedJump();
		double gravitationalAcceleration = parameters.getGravitationalAcceleration();

		double jumpHalfTime = playerDefaultSpeedJump / gravitationalAcceleration;
		double maxPlayerJumpHeight = playerDefaultSpeedJump * jumpHalfTime - gravitationalAcceleration / 2 * jumpHalfTime * jumpHalfTime;
		double platformDelta = playerHeight + maxPlayerJumpHeight + platformHeight;
		double ladderHeight = platformDelta - platformHeight;

		int numberOfPlatforms = random.nextInt(maximumNumberOfPlatforms - minimumNumberOfPlatforms) + minimumNumberOfPlatforms;

		List<Game2DObject> newGameObjects = new ArrayList<>();

		for (int platformIndex = 0; platformIndex < numberOfPlatforms; platformIndex++) {
			double currentPlatformY = (platformIndex + 1) * platformHeight + platformIndex * platformDelta;

			newGameObjects.add(new Platform(new BoundingBox2DImpl(0, currentPlatformY, platformWidth, platformHeight)));

			if (platformIndex != 0) {
				newGameObjects.add(new Ladder(new BoundingBox2DImpl(random.nextDouble() * platformWidth, currentPlatformY - platformHeight, ladderWidth, platformDelta)));
			}
		}

		this.player = new Player(new BoundingBox2DImpl(random.nextDouble() * (platformWidth - playerWidth), platformHeight + playerHeight, playerWidth, playerHeight), 0, 0, "Player0");
		this.gameObjects = newGameObjects;
	}

}
