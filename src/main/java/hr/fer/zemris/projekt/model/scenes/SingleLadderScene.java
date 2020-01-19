package hr.fer.zemris.projekt.model.scenes;

import hr.fer.zemris.projekt.model.controller.GameController;
import hr.fer.zemris.projekt.model.controller.impl.GameControllerImpl;
import hr.fer.zemris.projekt.model.controller.impl.GameControllerImpl.GameParameters;
import hr.fer.zemris.projekt.model.objects.Game2DObject;
import hr.fer.zemris.projekt.model.objects.impl.BoundingBox2DImpl;
import hr.fer.zemris.projekt.model.objects.impl.Ladder;
import hr.fer.zemris.projekt.model.objects.impl.Platform;
import hr.fer.zemris.projekt.model.objects.impl.Player;

import java.util.ArrayList;
import java.util.List;

import static hr.fer.zemris.projekt.gui.configuration.SceneConfig.*;
import static hr.fer.zemris.projekt.gui.configuration.SceneConfig.LADDER_HEIGHT;

public class SingleLadderScene implements SceneGenerator {

    private final GameParameters parameters;

    public SingleLadderScene(GameParameters parameters) {
        this.parameters = parameters;
    }

    @Override
    public GameController generateScene() {
        List<Game2DObject> objects = new ArrayList<>();
        int numberOfPlatforms = 0;

        double offsetY = PLATFORM_START_Y;
        while (offsetY <= 0.94 * GAME_SCENE_HEIGHT) {
            Platform platform = new Platform(new BoundingBox2DImpl(
                    PLATFORM_X, offsetY,
                    PLATFORM_WIDTH, PLATFORM_HEIGHT)
            );
            objects.add(platform);
            offsetY += LADDER_HEIGHT;
            numberOfPlatforms++;
        }

        offsetY = PLATFORM_START_Y + LADDER_HEIGHT;

        double lowerBound = PLATFORM_X;
        double upperBound = PLATFORM_X + PLATFORM_WIDTH - LADDER_WIDTH;

        for (int i = 0; i < numberOfPlatforms - 1; i++) {
            double randomX = lowerBound + (upperBound - lowerBound) * (1 - i / (numberOfPlatforms - 2.0));
            Ladder ladder = new Ladder(new BoundingBox2DImpl(
                    randomX, offsetY,
                    LADDER_WIDTH, LADDER_HEIGHT)
            );
            objects.add(ladder);

            offsetY += LADDER_HEIGHT;
        }

        Player player = new Player(new BoundingBox2DImpl(PLAYER_START_X, PLAYER_START_Y, PLAYER_WIDTH, PLAYER_HEIGHT), 0, 0, "Player");

        return new GameControllerImpl(player, objects, parameters);
    }

}
