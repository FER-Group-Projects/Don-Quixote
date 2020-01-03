package hr.fer.zemris.projekt.gui.assets;

import static hr.fer.zemris.projekt.gui.util.ResourceLoader.*;

/**
 * Simple class which provides system path to certain sprite.
 */
public class Audios {

    private static final Class<?> audiosClass = Audios.class;

    public static final String WALKING_SOUND =
            loadResource(audiosClass, "/assets/audio/walking.wav");

}
