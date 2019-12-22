package hr.fer.zemris.projekt.gui.configuration;

import static hr.fer.zemris.projekt.gui.util.ResourceLoader.*;

public class SceneConfig {

    public static final String GAME_SCENE_STYLESHEET =
            loadResource(SceneConfig.class, "/style/gameScene.css");

    public static final double GAME_SCENE_WIDTH = 1_200.0;

    public static final double GAME_SCENE_HEIGHT = 1_000.0;

    public static final double PLATFORM_WIDTH = 1_048.0;

    public static final double PLATFORM_HEIGHT = 24.0;

    public static final double PLATFORM_X = (GAME_SCENE_WIDTH - PLATFORM_WIDTH) / 2;

    public static final double PLATFORM_START_Y = 0.05 * GAME_SCENE_HEIGHT;

    public static final double PLAYER_WIDTH = 64.0;

    public static final double PLAYER_HEIGHT = 77.0;

    public static final double PLAYER_START_X = PLATFORM_X + 50;

    public static final double PLAYER_START_Y = PLATFORM_START_Y + PLAYER_HEIGHT;

    public static final double LADDER_WIDTH = 80.0;

    public static final double LADDER_HEIGHT = 170.0;

    public static final int LADDERS_PER_PLATFORM = 3;

    public static final double BARREL_WIDTH = 28.0;

    public static final double BARREL_HEIGHT = 38.0;

}
