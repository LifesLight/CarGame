package edu.kit.cargame.game.object.collectable;


import edu.kit.cargame.game.common.CollidingGameObject;
import edu.kit.cargame.game.common.Game;
import edu.kit.cargame.game.common.GameObject;
import edu.kit.cargame.game.geometry.BoundingBox;
import edu.kit.cargame.game.geometry.Point;
import edu.kit.cargame.game.playercar.PlayerCar;

/**
 * The type Collectable describes a generic Item which the player can pick up.
 */
public abstract class Collectable extends CollidingGameObject {
    private static final int SCORE_FOR_COLLECTABLE = 1000;

    /**
     * Instantiates a new Collectable.
     *
     * @param parent      the parent Object
     * @param game        the game in which this collectable lives
     * @param position    the position of the collectable
     * @param boundingBox the bounding box of the collectable
     */
    protected Collectable(GameObject parent, Game game, Point position, BoundingBox boundingBox) {
        super(parent, game, position, boundingBox);
    }

    @Override
    public final void handleCollision(CollidingGameObject other) {
        if (other instanceof PlayerCar playerCar) {
            handleCollision(playerCar);
            playerCar.addScore(SCORE_FOR_COLLECTABLE);
        }

        if (other.destroysCollectables()) {
            pickup();
        }
    }

    /**
     * Subtracts the Collectable pickup score from the player
     *
     * @param playerCar the player car
     */
    protected void negateScore(PlayerCar playerCar) {
        playerCar.addScore(-SCORE_FOR_COLLECTABLE);
    }

    /**
     * This function is called when the collectable is collected.
     */
    protected void pickup() {
        kill();
    }

    /**
     * This function is called when the collectable collides with a player car.
     *
     * @param playerCar the player car
     */
    protected abstract void handleCollision(PlayerCar playerCar);

}
