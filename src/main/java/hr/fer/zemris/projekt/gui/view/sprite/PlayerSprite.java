package hr.fer.zemris.projekt.gui.view.sprite;

import hr.fer.zemris.projekt.model.objects.BoundingBox2D;
import hr.fer.zemris.projekt.model.objects.impl.Player;

public class PlayerSprite extends Sprite {

    private static final int COLUMNS = 10;

    private static final int IDLE = 0;
    private static final int RUN = 1;
    private static final int JUMP = 2;

    private final double width;
    private final double height;

    private boolean idle;
    private boolean run;
    private boolean jump;

    // counter for frames
    private int counter;
    private double offsetX;
    private double offsetY;

    public PlayerSprite(String spritesheet, Player player) {
        super(spritesheet, player);
        BoundingBox2D bb = player.getBoundingBox();
        width = bb.getWidth();
        height = bb.getHeight();
    }

    private void setOffsetY(double offsetY) {
        this.offsetY = offsetY;
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

    private Frame getNextFrame(double x, double y, double width, double height) {
        Frame frame = new Frame(x, y, width, height);
        updateOffsetX();
        updateCounter();
        return frame;
    }

    public Frame idle() {
        run = jump = false;
        if (!idle) {
            idle = true;
            setOffsetY(IDLE * height);
        }
        return getNextFrame(0, 0, width, height);
    }

    public Frame run() {
        idle = jump = false;
        if (!run) {
            run = true;
            setOffsetY(RUN * height);
        }
        return getNextFrame(offsetX, offsetY, width, height);
    }

    public Frame jump() {
        idle = run = false;
        if (!jump) {
            jump = true;
            setOffsetY(JUMP * height);
        }
        return getNextFrame(offsetX, offsetY, width, height);
    }

}
