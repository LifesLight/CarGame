package edu.kit.cargame.io.view.gamerenderers;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import edu.kit.cargame.game.common.GameObject;
import edu.kit.cargame.io.view.TextureCache;

/**
 * Renderer for displaying effects in the foreground of the game.
 * This includes the slowdown vignette and the game over screen.
 */
public class ForegroundRenderer extends Renderer {

    private final GameObject gameObject;
    private final Sprite vignette;
    private final Sprite gameOver;

    /**
     * Instantiates a new Foreground renderer.
     *
     * @param gameObject the foreground game object used to access the game
     */
    public ForegroundRenderer(GameObject gameObject) {
        this.gameObject = gameObject;
        this.vignette = new Sprite(TextureCache.getTexture("game/box.png"));
        this.gameOver = new Sprite(TextureCache.getTexture("game/gameover.png"));
    }

    @Override
    public void render(Batch batch) {

        if (gameObject.getGame().isSlowdown()) {
            vignette.setColor(0.2f, 0.2f, 0.4f, 0.3f);
            vignette.setBounds(0, 0, gameObject.getGame().getConfig().worldWidth(), gameObject.getGame().getConfig().worldHeight());
            vignette.draw(batch);
        }
        if (gameObject.getGame().isGameOver()) {
            gameOver.setBounds(0, 0, gameObject.getGame().getConfig().worldWidth(), gameObject.getGame().getConfig().worldHeight());
            gameOver.draw(batch);
        }
    }
}
