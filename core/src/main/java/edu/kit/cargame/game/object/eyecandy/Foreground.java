package edu.kit.cargame.game.object.eyecandy;


import edu.kit.cargame.game.common.Game;
import edu.kit.cargame.game.common.GameObject;
import edu.kit.cargame.game.geometry.Point;
import edu.kit.cargame.io.view.gamerenderers.ForegroundRenderer;

/**
 * The type Foreground is a non-moving, non-collidable GameObject, which just displays a changing Sprite.
 */
public class Foreground extends GameObject {

    /**
     * Instantiates a new Background.
     *
     * @param parent   the parent Object
     * @param game     the game in which this background lives
     * @param position the position of the background
     * @param width    the width of the background
     * @param height   the height of the background
     */
    public Foreground(GameObject parent, Game game, Point position, int width, int height) {
        super(parent, game, position);
        setRenderer(new ForegroundRenderer(this));




    }
}
