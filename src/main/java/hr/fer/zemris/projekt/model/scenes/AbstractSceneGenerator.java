package hr.fer.zemris.projekt.model.scenes;

import hr.fer.zemris.projekt.model.controller.GameController;
import hr.fer.zemris.projekt.model.controller.impl.GameControllerImpl;
import hr.fer.zemris.projekt.model.controller.impl.GameControllerImpl.GameParameters;
import hr.fer.zemris.projekt.model.objects.Game2DObject;
import hr.fer.zemris.projekt.model.objects.impl.Player;

import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;

public abstract class AbstractSceneGenerator implements SceneGenerator {

    protected final Random random = new Random();

    protected GameParameters parameters;
    protected Player player;
    protected List<Game2DObject> gameObjects;

    protected double playerWidth;
    protected double playerHeight;
    protected double platformWidth;
    protected double platformHeight;
    protected double ladderWidth;
    protected double barrelWidth;
    protected double barrelHeight;

    public AbstractSceneGenerator(GameParameters parameters, double playerWidth, double playerHeight, double platformWidth,
                                  double platformHeight, double ladderWidth, double barrelWidth, double barrelHeight) {
        this.parameters = Objects.requireNonNull(parameters, "Game parameters cannot be null.");
        this.playerWidth = playerWidth;
        this.playerHeight = playerHeight;
        this.platformWidth = platformWidth;
        this.platformHeight = platformHeight;
        this.ladderWidth = ladderWidth;
        this.barrelWidth = barrelWidth;
        this.barrelHeight = barrelHeight;
    }

    @Override
    public GameController generateScene() {
        if (gameObjects == null) generateGameObjects();

        Player playerCopy = player.copy();
        List<Game2DObject> copyOfGameObjects = gameObjects
                .stream()
                .map(Game2DObject::copy)
                .collect(Collectors.toList());

        return new GameControllerImpl(playerCopy, copyOfGameObjects, parameters);
    }

    @Override
    public GameController generateNewScene() {
        generateGameObjects();

        return generateScene();
    }

    protected abstract void generateGameObjects();

}
