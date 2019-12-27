package hr.fer.zemris.projekt.model.scenes;

import hr.fer.zemris.projekt.model.controller.GameController;
import hr.fer.zemris.projekt.model.controller.impl.GameControllerImpl;
import hr.fer.zemris.projekt.model.controller.impl.GameControllerImpl.GameParameters;
import hr.fer.zemris.projekt.model.objects.Game2DObject;
import hr.fer.zemris.projekt.model.objects.impl.Player;

import java.util.List;
import java.util.stream.Collectors;

public abstract class AbstractSceneGenerator implements SceneGenerator {

    protected GameParameters parameters;
    protected Player player;
    protected List<Game2DObject> gameObjects;

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
