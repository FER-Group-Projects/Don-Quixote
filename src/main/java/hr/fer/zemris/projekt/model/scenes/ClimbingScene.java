package hr.fer.zemris.projekt.model.scenes;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import hr.fer.zemris.projekt.model.controller.impl.GameControllerImpl.GameParameters;
import hr.fer.zemris.projekt.model.objects.BoundingBox2D;
import hr.fer.zemris.projekt.model.objects.Game2DObject;
import hr.fer.zemris.projekt.model.objects.impl.BoundingBox2DImpl;
import hr.fer.zemris.projekt.model.objects.impl.Ladder;
import hr.fer.zemris.projekt.model.objects.impl.Platform;
import hr.fer.zemris.projekt.model.objects.impl.Player;

public class ClimbingScene extends AbstractSceneGenerator {

	private final int minimumNumberOfPlatforms;
	private final int maximumNumberOfPlatforms;

	private final int minimumNumberOfLadders;
	private final int maximumNumberOfLadders;

	public ClimbingScene(int tickRatePerSec, double gravitationalAcceleration, double barrelLadderProbability,
			double playerDefaultSpeedGround, double playerDefaultSpeedLadders, double playerDefaultSpeedJump,
			double otherDefaultSpeedGround, double otherDefaultSpeedLadders, double playerWidth, double playerHeight,
			double platformWidth, double platformHeight, double ladderWidth) {
		this(new GameParameters(tickRatePerSec, gravitationalAcceleration, barrelLadderProbability, playerDefaultSpeedGround,
						playerDefaultSpeedLadders, playerDefaultSpeedJump, otherDefaultSpeedGround, otherDefaultSpeedLadders),
				3, 7, 1, 3, platformWidth, platformHeight, ladderWidth, playerWidth, playerHeight);
	}

	public ClimbingScene(GameParameters parameters, int minimumNumberOfPlatforms, int maximumNumberOfPlatforms,
						 int minimumNumberOfLadders, int maximumNumberOfLadders, double platformWidth, double platformHeight,
						 double ladderWidth, double playerWidth, double playerHeight) {
		super(parameters, playerWidth, playerHeight, platformWidth, platformHeight, ladderWidth, -1, -1);

		this.minimumNumberOfPlatforms = minimumNumberOfPlatforms;
		this.maximumNumberOfPlatforms = maximumNumberOfPlatforms;
		this.minimumNumberOfLadders = minimumNumberOfLadders;
		this.maximumNumberOfLadders = maximumNumberOfLadders;
	}

	@Override
	protected void generateGameObjects() {
		double playerDefaultSpeedJump = parameters.getPlayerDefaultSpeedJump();
		double gravitationalAcceleration = parameters.getGravitationalAcceleration();

		double jumpHalfTime = playerDefaultSpeedJump / gravitationalAcceleration;
		double maxPlayerJumpHeight = playerDefaultSpeedJump * jumpHalfTime - gravitationalAcceleration / 2 * jumpHalfTime * jumpHalfTime;
		double platformDelta = playerHeight + maxPlayerJumpHeight + platformHeight;

		int numberOfPlatforms = random.nextInt(maximumNumberOfPlatforms - minimumNumberOfPlatforms) + minimumNumberOfPlatforms;

		List<Game2DObject> newGameObjects = new ArrayList<>();

		for (int platformIndex = 0; platformIndex < numberOfPlatforms; platformIndex++) {
			double currentPlatformY = (platformIndex + 1) * platformHeight + platformIndex * platformDelta;

			newGameObjects.add(new Platform(new BoundingBox2DImpl(0, currentPlatformY, platformWidth, platformHeight)));

			if (platformIndex != 0) {
				int numberOfLadders = random.nextInt(maximumNumberOfLadders - minimumNumberOfLadders) + minimumNumberOfLadders;

				// Create N non intersecting ladders between current and previous platform
				for (int ladderIndex = 0; ladderIndex < numberOfLadders; ladderIndex++) {
					BoundingBox2D ladderBB = new BoundingBox2DImpl(random.nextDouble() * (platformWidth - ladderWidth), currentPlatformY - platformHeight, ladderWidth, platformDelta);
					boolean collidesExisting = false;

					for (Game2DObject gameObject : newGameObjects) {
						if (gameObject.getBoundingBox().intersects(ladderBB)) {
							collidesExisting = true;
							break;
						}
					}

					if (collidesExisting) {
						--ladderIndex;
						continue;
					}

					newGameObjects.add(new Ladder(ladderBB));
				}
			}
		}

		this.player = new Player(new BoundingBox2DImpl(random.nextDouble() * (platformWidth - playerWidth), platformHeight + playerHeight, playerWidth, playerHeight), 0, 0, "Player0");
		this.gameObjects = newGameObjects;
	}

}
