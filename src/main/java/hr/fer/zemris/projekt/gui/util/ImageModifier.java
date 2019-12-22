package hr.fer.zemris.projekt.gui.util;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;

/**
 * Simple utility class which provides {@link #makeTransparent(Image)} method.
 */
public class ImageModifier {

    private static final int TOLERANCE_TRESHOLD = 0xCF;

    /**
     * Makes provided <i>input</i> image transparent i.e. white background color is removed.
     *
     * @param input input image.
     * @return transparent image.
     */
    public static Image makeTransparent(Image input) {
        final int width = (int) input.getWidth();
        final int height = (int) input.getHeight();
        WritableImage output = new WritableImage(width, height);
        PixelReader reader = input.getPixelReader();
        PixelWriter writer = output.getPixelWriter();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int argb = reader.getArgb(x, y);

                int r = (argb >> 16) & 0xFF;
                int g = (argb >> 8) & 0xFF;
                int b = argb & 0xFF;

                if (r >= TOLERANCE_TRESHOLD && g >= TOLERANCE_TRESHOLD && b >= TOLERANCE_TRESHOLD) {
                    argb &= 0x00FFFFFF;
                }

                writer.setArgb(x, y, argb);
            }
        }
        return output;
    }
}
