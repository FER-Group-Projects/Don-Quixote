package hr.fer.zemris.projekt.gui.assets;

import static hr.fer.zemris.projekt.gui.util.ResourceLoader.*;

/**
 * Simple class which provides system path to certain sprite.
 */
public class Sprites {

    private static final Class<?> spritesClass = Sprites.class;

    public static final String PLAYER_SPRITESHEET =
            loadResource(spritesClass, "/assets/sprites/player_spritesheet.png");

    public static final String BARREL_SPRITESHEET =
            loadResource(spritesClass, "/assets/sprites/barrel_spritesheet.png");

    public static final String LADDER_SPRITE =
            loadResource(spritesClass, "/assets/sprites/ladder.png");

    public static final String PLATFORM_SPRITE =
            loadResource(spritesClass, "/assets/sprites/platform.png");

}
