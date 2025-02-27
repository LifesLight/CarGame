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
 * A collectable giving the player car 1 live upon pickup.
 */
public class CollectableHeart extends Collectable {

    /**
     * Instantiates a new Collectable.
     *
     * @param parent   the parent Object
     * @param game     the game in which this collectable lives
     * @param position the position of the collectable
     * @param boundingBox the bounding box of the collectable
     */
    public CollectableHeart(GameObject parent, Game game, Point position, BoundingBox boundingBox) {
        super(parent, game, position, boundingBox);
        setRenderer(new SpriteRenderer(this, "game/animations/collectables/heart/0000.png"));
    }

    @Override
    public void handleCollision(PlayerCar playerCar) {
        new Animation(this, getGame(), Point.zero(), AnimationType.HEART_PICKUP, getBoundingBox());
        playerCar.addLife();
    }
}
