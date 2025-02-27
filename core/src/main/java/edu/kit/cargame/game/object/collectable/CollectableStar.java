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
 * The type CollectableStar is a collectable that makes the player car invincible for a short amount of time.
 */
public class CollectableStar extends Collectable {
    /**
     * Instantiates a new Collectable.
     *
     * @param parent   the parent Object
     * @param game     the game in which this collectable lives
     * @param position the position of the collectable
     * @param boundingBox the bounding box of the collectable
     */
    public CollectableStar(GameObject parent, Game game, Point position, BoundingBox boundingBox) {
        super(parent, game, position, boundingBox);
        setRenderer(new SpriteRenderer(this, "game/animations/collectables/star/0000.png"));

    }

    @Override
    public void handleCollision(PlayerCar playerCar) {
        new Animation(this, getGame(), Point.zero(), AnimationType.STAR_PICKUP, getBoundingBox());
        playerCar.makeInvulnerable(getGame().getConfig().starDuration());
    }
}
