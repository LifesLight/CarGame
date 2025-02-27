package edu.kit.cargame.io.view.gamerenderers;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import edu.kit.cargame.game.object.obstacle.MovingObstacle;
import edu.kit.cargame.game.playercar.MovingCarSprites;

/**
 * A renderer for moving obstacles. It checks the state of the obstacle and chooses the right sprite to render.
 */
public class MovingObstacleRenderer extends Renderer {
    private final MovingObstacle movingObstacle;
    private final MovingCarSprites sprites;

    private static final float UPPER_THRESHOLD = 0.5f;
    private static final float LOWER_THRESHOLD = 0.1f;

    /**
     * Instantiates a new Moving obstacle renderer.
     *
     * @param movingObstacle the moving obstacle
     * @param name           the name to use for the sprites
     */
    public MovingObstacleRenderer(MovingObstacle movingObstacle, String name) {
        this.movingObstacle = movingObstacle;
        this.sprites = MovingCarSprites.fromName(name);
    }

    @Override
    public void render(Batch batch) {
        // render the player car based on its state
        float relativeYVelocity = movingObstacle.getVerticalVelocity() / movingObstacle.getMaxVelocity();
        Sprite currentSprite = getAngledSprite(relativeYVelocity);
        currentSprite.setBounds(movingObstacle.getGlobalPosition().x(), movingObstacle.getGlobalPosition().y(),
            movingObstacle.getBoundingBox().getWidth() * 1.2f, movingObstacle.getBoundingBox().getHeight() * 1.2f);
        currentSprite.draw(batch);
    }

    private Sprite getAngledSprite(float relativeYVelocity) {
        Sprite currentSprite;
        if (Math.abs(relativeYVelocity) > UPPER_THRESHOLD) {
            if (relativeYVelocity > 0) {
                currentSprite = sprites.rightUp();
            } else {
                currentSprite = sprites.leftDown();
            }
        } else if (Math.abs(relativeYVelocity) > LOWER_THRESHOLD) {
            if (relativeYVelocity > 0) {
                currentSprite = sprites.right();
            } else {
                currentSprite = sprites.left();
            }
        } else {
            if (movingObstacle.isBlinkingUp()) {
                currentSprite = sprites.blinkingRight();
            } else if (movingObstacle.isBlinkingDown()) {
                currentSprite = sprites.blinkingLeft();
            } else {
                currentSprite = sprites.ahead();
            }
            // add indicator effect
            if (movingObstacle.getGame().getCurrentTime() % 8 < 4) {
                currentSprite = sprites.ahead();
            }

        }
        return currentSprite;
    }
}
