package edu.kit.cargame.io.view.renderer;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import edu.kit.cargame.io.view.Scale;

/**
 * The TextRenderer is responsible for rendering text on a stage.
 * It supports rendering digits and alphabetic characters.
 */
public class TextRenderer {
    private float scale = 5;
    private static final int DEFAULT_WIDTH = 3;
    private final Stage stage;
    private List<Texture> texturesNumbers = new ArrayList<>();
    private List<Texture> texturesAlphabet = new ArrayList<>();
    private List<Texture> texturesSpecial = new ArrayList<>();
    /**
     * A regex which searches for characters unsupported by the text renderer.
     */
    public static final String UNSUPORTED_CHARACTERS = "[^a-zA-Z0-9-:]";

    /**
     * Create a new text renderer.
     *
     * @param stage The stage on which {@link #drawText(String, float, float)} will
     *              draw.
     * @param scale The scale of the text.
     */
    public TextRenderer(Stage stage, Scale scale) {
        this.stage = stage;
        this.scale = scale.getScale();
        texturesNumbers = Util.loadNTextures("digits/%s/d%d.png", 10, scale);
        texturesAlphabet = Util.loadNTextures("characters/%s/c%d.png", 26, scale);
        texturesSpecial = Util.loadNTextures("special_characters/%s/s%d.png", 2, scale);
    }

    /**
     * Draw the given text onto the stage at given location.
     *
     * @param text The text that should be drawn.
     * @param x    the horizontal position in pixels. The given pixel will be the
     *             left most row.
     * @param y    the vertical position in pixels. The given pixel will be the
     *             bottom most row.
     * @return The width in pixels of the text.
     * @throws IllegalStateException if the stage is not set.
     */
    public int drawText(String text, float x, float y) {
        if (stage == null) {
            throw new IllegalStateException("Stage is not set, only batch method is supported then");
        }
        int currentPosition = 0;

        for (char c : text.toCharArray()) {
            drawCharacter(c, currentPosition, x, y);
            currentPosition += charWidth(c) + 1;
        }
        return currentPosition;
    }

    /**
     * Draw the given text onto the batch at given location.
     *
     * @param text  The text that should be drawn.
     * @param x     the horizontal position in pixels. The given pixel will be the
     *              left most row.
     * @param y     the vertical position in pixels. The given pixel will be the
     *              bottom most row.
     * @param batch The batch on which the text should be drawn.
     * @return The width in pixels of the text.
     */
    public int drawText(String text, float x, float y, Batch batch) {
        int currentPosition = 0;

        for (char c : text.toCharArray()) {
            drawCharacter(c, currentPosition, x, y, batch);
            currentPosition += charWidth(c) + 1;
        }
        return currentPosition;
    }

    private Texture charToTexture(char ch) {
        if (ch == ' ') {
            return null;
        }
        switch (ch) {
            case ' ':
                return null;
            case '-':
                return texturesSpecial.get(1);
            case ':':
                return texturesSpecial.get(0);

        }
        if (Character.isDigit(ch)) {
            return texturesNumbers.get(Character.getNumericValue(ch));
        }
        if (Character.isAlphabetic(ch)) {
            return texturesAlphabet.get(Character.toUpperCase(ch) - 'A');
        }
        throw new IllegalStateException("Character is unsuported");
    }

    private void drawCharacter(char ch, int position, float x, float y) {
        Texture texture = charToTexture(ch);
        // texture == null -> space
        if (texture == null) {
            return;
        }
        Image image = new Image(texture);

        image.setPosition(x + scale * position, y);
        stage.addActor(image);
    }

    private void drawCharacter(char ch, int position, float x, float y, Batch batch) {
        Texture texture = charToTexture(ch);
        // texture == null -> space
        if (texture == null) {
            return;
        }
        batch.draw(texture, x + scale * position, y, texture.getWidth(), texture.getHeight());
    }

    private int charWidth(char ch) {
        return switch (Character.toUpperCase(ch)) {
            case 'M':
            case 'W':
                yield 5;
            default:
                yield DEFAULT_WIDTH;
        };
    }

}
