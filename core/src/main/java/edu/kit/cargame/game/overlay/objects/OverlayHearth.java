package edu.kit.cargame.game.overlay.objects;

import edu.kit.cargame.game.common.Game;
import edu.kit.cargame.game.common.GameObject;
import edu.kit.cargame.game.geometry.BoundingBox;
import edu.kit.cargame.game.geometry.Point;
import edu.kit.cargame.io.view.gamerenderers.SpriteRenderer;

/**
 * A hearth sprite to be overlaid onto the game screen.
 */
public class OverlayHearth extends GameObject {
    /**
     * Instantiates a new Overlay Hearth.
     *
     * @param parent   the parent Object
     * @param game     the game in which this collectable lives
     * @param position the position of the object
     */
    public OverlayHearth(GameObject parent, Game game, Point position) {
        super(parent, game, position);
        setBoundingBox(new BoundingBox(new Point(50, 50)));
        setRenderer(new SpriteRenderer(this, "game/animations/collectables/heart/0000.png"));
    }
}
