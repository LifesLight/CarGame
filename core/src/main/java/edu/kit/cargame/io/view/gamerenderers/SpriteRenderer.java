package edu.kit.cargame.io.view.gamerenderers;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import edu.kit.cargame.game.common.GameObject;
import edu.kit.cargame.game.geometry.Point;
import edu.kit.cargame.io.view.TextureCache;

/**
 * A renderer which always renders the same texture at the position of the game object.
 */
public class SpriteRenderer extends Renderer {
    private final GameObject gameObject;
    private final Sprite sprite;
    private final boolean fillBoundingBox;

    /**
     * Instantiates a new Sprite renderer.
     *
     * @param gameObject the game object to render
     * @param spritePath the path to the texture
     * @param fillBoundingBox whether the sprite should fill the bounding box of the game object
     */
    public SpriteRenderer(GameObject gameObject, String spritePath, boolean fillBoundingBox) {
        this.gameObject = gameObject;
        this.sprite = new Sprite(TextureCache.getTexture(spritePath));
        this.fillBoundingBox = fillBoundingBox;
    }

    /**
     * Instantiates a new Sprite renderer.
     *
     * @param gameObject the game object to render
     * @param spritePath the path to the texture
     */
    public SpriteRenderer(GameObject gameObject, String spritePath) {
        this(gameObject, spritePath, true);
    }

    /**
     * Sets the color tint of the sprite.
     *
     * @param r the red component
     * @param g the green component
     * @param b the blue component
     * @param a the alpha component
     */
    public void setColor(float r, float g, float b, float a) {
        sprite.setColor(r, g, b, a);
    }

    @Override
    public void render(Batch batch) {
        Point pos = gameObject.getGlobalPosition();
        if (fillBoundingBox) {
            sprite.setBounds(pos.x(), pos.y(), gameObject.getBoundingBox().getWidth(), gameObject.getBoundingBox().getHeight());
            sprite.draw(batch);
        } else {
            sprite.setPosition(pos.x(), pos.y());
            sprite.draw(batch);
        }
    }
}
