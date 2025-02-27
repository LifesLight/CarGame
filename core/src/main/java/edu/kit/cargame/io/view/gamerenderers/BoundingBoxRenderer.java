package edu.kit.cargame.io.view.gamerenderers;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import edu.kit.cargame.game.common.GameObject;
import edu.kit.cargame.game.geometry.BoundingBox;
import edu.kit.cargame.game.geometry.Point;

/**
 * A renderer used for rendering the bounding box after the normal rendering pass.
 * This is useful for debugging purposes.
 */
public class BoundingBoxRenderer extends Renderer {
    private final GameObject gameObject;
    private final Sprite bbox;
    private final Sprite box;
    private final Renderer wrappedRenderer;

    /**
     * Instantiates a new bounding Box renderer.
     * First draws the wrapped renderer and then the bounding box.
     *
     * @param gameObject      the game object to render the bounding box of
     * @param wrappedRenderer the renderer to wrap
     */
    public BoundingBoxRenderer(GameObject gameObject, Renderer wrappedRenderer) {
        this.gameObject = gameObject;
        this.bbox = new Sprite(new Texture("game/bbox.png"));
        this.box = new Sprite(new Texture("game/box.png"));
        this.wrappedRenderer = wrappedRenderer;
    }

    @Override
    public void render(Batch batch) {
        if (wrappedRenderer != null) {
            wrappedRenderer.render(batch);
        }
        BoundingBox boundingBox = gameObject.getGlobalBoundingBox();
        Point position = boundingBox.bottomLeft();
        bbox.setBounds(position.x(), position.y(), boundingBox.getWidth(), boundingBox.getHeight());
        bbox.draw(batch);
        box.setPosition(gameObject.getGlobalPosition().x() - box.getWidth() / 2, gameObject.getGlobalPosition().y() - box.getHeight() / 2);
        box.draw(batch);
    }
}
