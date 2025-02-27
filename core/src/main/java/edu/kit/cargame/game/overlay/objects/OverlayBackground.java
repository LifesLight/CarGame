package edu.kit.cargame.game.overlay.objects;

import edu.kit.cargame.game.common.Game;
import edu.kit.cargame.game.common.GameObject;
import edu.kit.cargame.game.geometry.BoundingBox;
import edu.kit.cargame.game.geometry.Point;
import edu.kit.cargame.io.view.gamerenderers.SpriteRenderer;

/**
 * A background sprite to be overlaid onto the game screen.
 */
public class OverlayBackground extends GameObject {
    /**
     * Instantiates a new Collectable.
     *
     * @param parent   the parent Object
     * @param game     the game in which this collectable lives
     * @param position the position of the object
     */
    public OverlayBackground(GameObject parent, Game game, Point position) {
        super(parent, game, position);
        setBoundingBox(new BoundingBox(new Point(1200, 100)));
        setRenderer(new SpriteRenderer(this, "game/overlay/background.png"));
        ((SpriteRenderer) getRenderer().get()).setColor(1f, 1f, 1f, 1f);
    }
}
