package hr.fer.zemris.projekt.gui;

import hr.fer.zemris.projekt.model.controller.GameController;
import hr.fer.zemris.projekt.model.scenes.ClimbingBarrelScene;
import hr.fer.zemris.projekt.model.scenes.ClimbingScene;
import hr.fer.zemris.projekt.model.serialization.GameControllerSerializer;
import hr.fer.zemris.projekt.model.serialization.JavaGameControllerSerializer;
import hr.fer.zemris.projekt.model.serialization.SerializationException;

import java.nio.file.Paths;

import static hr.fer.zemris.projekt.gui.configuration.GameParemetersConfig.*;
import static hr.fer.zemris.projekt.gui.configuration.SceneConfig.*;

// This class is needed because of JavaFX packaging......
public class Main {
    public static void main(String[] args) {
        GameLauncher.main(args);
        /*GameController gc;
        gc = new ClimbingBarrelScene(
                TICK_RATE_PER_SEC,
                GRAVITATIONAL_ACCELARATION,
                BARREL_LADDER_PROBABILITY,
                PLAYER_DEFAULT_SPEED_GROUND,
                PLAYER_DEFAULT_SPEED_LADDERS,
                PLAYER_DEFAULT_SPEED_JUMP,
                OTHER_DEFAULT_SPEED_GROUND,
                OTHER_DEFAULT_SPEED_LADDERS,
                PLAYER_WIDTH,
                PLAYER_HEIGHT,
                420,
                PLATFORM_HEIGHT,
                LADDER_WIDTH,
                BARREL_WIDTH,
                BARREL_HEIGHT).generateScene();
        GameControllerSerializer s = new JavaGameControllerSerializer();
        try {
            s.serialize(Paths.get("./src/main/resources/scenes/ClimbingBarrelScene.scene"), gc);
        } catch (SerializationException e) {
            e.printStackTrace();
        }*/
    }
}
