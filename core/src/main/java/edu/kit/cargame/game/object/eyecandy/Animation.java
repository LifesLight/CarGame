package edu.kit.cargame.game.object.eyecandy;


import edu.kit.cargame.common.logging.LoggerManagement;
import edu.kit.cargame.game.geometry.BoundingBox;
import edu.kit.cargame.io.view.gamerenderers.Renderer;
import edu.kit.cargame.game.geometry.Point;
import edu.kit.cargame.game.common.Game;
import edu.kit.cargame.game.common.GameObject;
import edu.kit.cargame.io.view.gamerenderers.SimpleAnimationRenderer;

/**
 * The type Animation is a blank GameObject. Its purpose is just to exist at a position, so that its {@link Renderer}
 * can play an Animation there.
 */
public class Animation extends GameObject {
    /**
     * Instantiates a new Animation.
     *
     * @param parent   the parent Object
     * @param game     the game in which this animation lives
     * @param position the position of the animation
     * @param type     the type of the animation
     */
    public Animation(GameObject parent, Game game, Point position, AnimationType type) {
        super(parent, game, position);
        setRenderer(new SimpleAnimationRenderer(this, type));
        setBoundingBox(parent.getBoundingBox());
        if (game.getConfig().rumbleEnabled()) {
            game.getPlayerCar().getUserInput().rumble(type.getRumbleDuration(), type.getRumbleIntensity());
        }
    }

    /**
     * Instantiates a new Animation.
     *
     * @param parent     the parent Object
     * @param game       the game in which this animation lives
     * @param position   the position of the animation
     * @param type       the type of the animation
     * @param boundingBox the bounding box of the animation
     */
    public Animation(GameObject parent, Game game, Point position, AnimationType type, BoundingBox boundingBox) {
        this(parent, game, position, type);
        setBoundingBox(boundingBox);
    }
}
