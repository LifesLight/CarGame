package edu.kit.cargame.io.view.gamerenderers;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import edu.kit.cargame.game.common.GameObject;
import edu.kit.cargame.game.geometry.Point;
import edu.kit.cargame.game.object.eyecandy.AnimationType;
import edu.kit.cargame.io.view.renderer.Util;

import java.util.List;

/**
 * Renderer for displaying one animation using the time difference from the game time and the attached gameObjects creation time.
 * Kills the object after the animation.
 */
public class SimpleAnimationRenderer extends Renderer {

    private final GameObject gameObject;
    private final List<Texture> animation;
    private final AnimationType animationType;

    /**
     * Instantiates a new Simple animation renderer.
     *
     * @param gameObject    the game object to render
     * @param animationType the type of the animation to render
     */
    public SimpleAnimationRenderer(GameObject gameObject, AnimationType animationType) {
        this.gameObject = gameObject;
        // Check amount of files in the folder:

        this.animation = Util.loadNTextures("game/animations/" + animationType.getName() + "/%04d.png", animationType.getFrameCount(), 0);
        this.animationType = animationType;
        if (!gameObject.getGame().getMuted()) {
            playSound(animationType.getSoundLocation());
        }
    }

    @Override
    public void render(Batch batch) {
        float time = (float) (gameObject.getGame().getCurrentTime() - gameObject.getCreationTime());
        int frame = (int) (time / animationType.getFrameDuration());
        if (frame >= animation.size()) {
            gameObject.kill();
            return;
        }
        Texture texture = animation.get(frame);

        batch.setColor(1, 1, 1, 1);
        Point pos = gameObject.getGlobalPosition().add(animationType.getOffset().scale(animationType.getScale()));
        batch.draw(texture, pos.x(), pos.y(), gameObject.getBoundingBox().getWidth() * animationType.getScale(),
            gameObject.getBoundingBox().getHeight() * animationType.getScale());
    }

}
