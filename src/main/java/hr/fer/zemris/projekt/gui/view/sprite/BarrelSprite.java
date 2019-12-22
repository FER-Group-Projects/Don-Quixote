package hr.fer.zemris.projekt.gui.view.sprite;

import hr.fer.zemris.projekt.model.objects.BoundingBox2D;
import hr.fer.zemris.projekt.model.objects.impl.Barrel;

public class BarrelSprite extends Sprite {

    private static final int COLUMNS = 8;

    private final double width;
    private final double height;

    // counter for frames
    private int counter;
    private double offsetX;

    public BarrelSprite(String spritesheet, Barrel barrel) {
        super(spritesheet, barrel);
        BoundingBox2D bb = barrel.getBoundingBox();
        width = bb.getWidth();
        height = bb.getHeight();
    }

    private void updateOffsetX() {
        offsetX += width;
    }

    private void resetOffsetX() {
        offsetX = 0;
    }

    private void updateCounter() {
        if (counter == COLUMNS - 1) {
            counter = 0;
            resetOffsetX();
        } else counter++;
    }

    public Frame roll() {
        Frame frame = new Frame(offsetX, 0, width, height);
        updateOffsetX();
        updateCounter();
        return frame;
    }

}
