package edu.kit.cargame.game.object.collectable;

import edu.kit.cargame.game.common.Game;
import edu.kit.cargame.game.geometry.BoundingBox;
import edu.kit.cargame.game.geometry.Point;
import edu.kit.cargame.game.common.GameObject;
import edu.kit.cargame.game.object.eyecandy.Animation;
import edu.kit.cargame.game.object.eyecandy.AnimationType;
import edu.kit.cargame.game.playercar.PlayerCar;
import edu.kit.cargame.io.view.gamerenderers.SpriteRenderer;

/**
 * The type CollectableStopwatch is a collectable that slows down time.
 */
public class CollectableStopwatch extends Collectable {
    /**
     * Instantiates a new Collectable.
     *
     * @param parent   the parent Object
     * @param game     the game in which this collectable lives
     * @param position the position of the collectable
     * @param boundingBox the bounding box of the collectable
     */
    public CollectableStopwatch(GameObject parent, Game game, Point position, BoundingBox boundingBox) {
        super(parent, game, position, boundingBox);
        setRenderer(new SpriteRenderer(this, "game/animations/collectables/stop_watch/0000.png"));
    }

    @Override
    public void handleCollision(PlayerCar playerCar) {
        new Animation(this, getGame(), Point.zero(), AnimationType.STOP_WATCH_PICKUP, getBoundingBox());
        getGame().addSlowdown(getGame().getConfig().stopwatchDuration());
    }
}
