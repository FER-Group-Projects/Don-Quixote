package hr.fer.zemris.projekt.gui.view.sprite;

import hr.fer.zemris.projekt.gui.util.ImageModifier;
import hr.fer.zemris.projekt.model.objects.Game2DObject;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Sprite extends ImageView {

    private final Game2DObject object;

    protected Sprite(String url, Game2DObject object) {
        super(ImageModifier.makeTransparent(new Image(url)));
        this.object = object;
    }

    public Game2DObject getObject() {
        return object;
    }

}
