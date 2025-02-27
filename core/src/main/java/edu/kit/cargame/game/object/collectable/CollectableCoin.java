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
 * The type CollectableCoin is a collectable that increases the player car's speed and how fast it gets score.
 */
public class CollectableCoin extends Collectable {
    /**
     * Instantiates a new Collectable.
     *
     * @param parent   the parent Object
     * @param game     the game in which this collectable lives
     * @param position the position of the collectable
     * @param boundingBox the bounding box of the collectable
     */
    public CollectableCoin(GameObject parent, Game game, Point position, BoundingBox boundingBox) {
        super(parent, game, position, boundingBox);
        setRenderer(new SpriteRenderer(this, "game/animations/collectables/coin/0000.png"));

    }

    @Override
    protected void handleCollision(PlayerCar playerCar) {
        new Animation(this, getGame(), Point.zero(), AnimationType.COIN_PICKUP, getBoundingBox());
        playerCar.addCoin();
    }
}
