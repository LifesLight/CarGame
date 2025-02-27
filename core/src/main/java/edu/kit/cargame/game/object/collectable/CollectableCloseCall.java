package edu.kit.cargame.game.object.collectable;

import edu.kit.cargame.game.common.Game;
import edu.kit.cargame.game.common.GameObject;
import edu.kit.cargame.game.geometry.BoundingBox;
import edu.kit.cargame.game.geometry.Point;
import edu.kit.cargame.game.object.eyecandy.Animation;
import edu.kit.cargame.game.object.eyecandy.AnimationType;
import edu.kit.cargame.game.playercar.PlayerCar;

/**
 * The type CollectableCloseCall describes a collectable that gives the player a small amount of score as a reward for
 * driving very close to an obstacle without hitting it.
 */
public class CollectableCloseCall extends Collectable {
    // how many ticks the player cant hit anything so the close call counts
    private static final int CLOSE_CALL_DELAY_TICKS = 10;
    private int startLives;

    /**
     * Instantiates a new Collectable.
     *
     * @param parent      the parent Object
     * @param game        the game in which this collectable lives
     * @param position    the position of the collectable
     * @param boundingBox the bounding box of the collectable
     */
    public CollectableCloseCall(GameObject parent, Game game, Point position, BoundingBox boundingBox) {
        super(parent, game, position, boundingBox);
    }

    @Override
    protected void handleCollision(PlayerCar playerCar) {
        negateScore(playerCar);
        // this would make close calls only happen during slow motion
        // if(!getGame().isSlowdown()) {return;}

        if (playerCar.isInvulnerable()) {
            return;
        }
        startLives = playerCar.getLives();
        getGame().addScheduledEvent(CLOSE_CALL_DELAY_TICKS, this::activateCloseCall);
        kill();
    }

    private void activateCloseCall() {
        PlayerCar playerCar = getGame().getPlayerCar();
        if (startLives > playerCar.getLives()) {
            return;
        }
        new Animation(playerCar, getGame(), Point.zero(), AnimationType.CLOSE_CALL);
        playerCar.addScore(getGame().getConfig().closeCallPoints());
        kill();
    }

    @Override
    protected void pickup() {
        //cant pickup
    }
}
