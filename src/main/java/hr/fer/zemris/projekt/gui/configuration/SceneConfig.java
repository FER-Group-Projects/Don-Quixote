package hr.fer.zemris.projekt.gui.configuration;

import static hr.fer.zemris.projekt.gui.util.ResourceLoader.*;

public class SceneConfig {

    public static final String MENU_SCENE_STYLESHEET =
            loadResource(SceneConfig.class, "/style/menuScene.css");

    public static final String GAME_SCENE_STYLESHEET =
            loadResource(SceneConfig.class, "/style/gameScene.css");

    public static final double GAME_SCENE_WIDTH = 1_200.0;

    public static final double GAME_SCENE_HEIGHT = 1_000.0;

    public static final double PLATFORM_WIDTH = 1_048.0;

    public static final double PLATFORM_HEIGHT = 24.0;

    public static final double PLATFORM_X = (GAME_SCENE_WIDTH - PLATFORM_WIDTH) / 2;

    public static final double PLATFORM_START_Y = PLATFORM_HEIGHT;

    public static final double PLAYER_WIDTH = 49.0;

    public static final double PLAYER_HEIGHT = 72.33;

    public static final double PLAYER_START_X = PLATFORM_X + 50;

    public static final double PLAYER_START_Y = PLATFORM_START_Y + PLAYER_HEIGHT;

    public static final double LADDER_WIDTH = 55.0;

    public static final double LADDER_HEIGHT = 170.0;

    public static final int LADDERS_PER_PLATFORM = 3;

    public static final double BARREL_WIDTH = 40.0;

    public static final double BARREL_HEIGHT = 32.0;

}
