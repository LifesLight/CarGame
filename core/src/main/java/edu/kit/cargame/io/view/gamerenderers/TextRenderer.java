package edu.kit.cargame.io.view.gamerenderers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import edu.kit.cargame.game.common.GameObject;
import edu.kit.cargame.game.geometry.Point;

/**
 * A renderer which renders a game object's debug text at its position.
 * This is useful for debugging purposes.
 */
public class TextRenderer extends Renderer {
    private final GameObject gameObject;
    private final BitmapFont font;
    private final Renderer wrappedRenderer;

    /**
     * Instantiates a new Text renderer.
     *
     * @param gameObject the game object to render
     * @param wrappedRenderer the renderer to wrap and render before the text
     */
    public TextRenderer(GameObject gameObject, Renderer wrappedRenderer) {
        this.gameObject = gameObject;
        font = new BitmapFont(Gdx.files.internal("default.fnt"), false);
        this.wrappedRenderer = wrappedRenderer;
    }

    @Override
    public void render(Batch batch) {
        if (wrappedRenderer != null) {
            wrappedRenderer.render(batch);
        }
        Point pos = gameObject.getGlobalPosition();
        font.draw(batch, gameObject.getDebugText(), pos.x(), pos.y());
    }


}
