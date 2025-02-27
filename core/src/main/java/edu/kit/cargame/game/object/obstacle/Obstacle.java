package edu.kit.cargame.game.object.obstacle;

import edu.kit.cargame.game.common.CollidingGameObject;
import edu.kit.cargame.game.common.Game;
import edu.kit.cargame.game.common.GameObject;
import edu.kit.cargame.game.geometry.BoundingBox;
import edu.kit.cargame.game.geometry.Point;
import edu.kit.cargame.game.object.collectable.CollectableCloseCall;
import edu.kit.cargame.game.object.eyecandy.Animation;
import edu.kit.cargame.game.object.eyecandy.AnimationType;
import edu.kit.cargame.game.playercar.PlayerCar;

/**
 * The type Obstacle models all GameObjects which remove a life from the player car, upon colliding with it.
 */
public abstract class Obstacle extends CollidingGameObject {
    /**
     * Instantiates a new Obstacle.
     *
     * @param parent      the parent Object
     * @param game        the game in which this obstacle lives
     * @param position    the position of the obstacle
     * @param boundingBox the bounding box of the obstacle
     */
    protected Obstacle(GameObject parent, Game game, Point position, BoundingBox boundingBox) {
        this(parent, game, position, boundingBox, true);

    }

    /**
     * Instantiates a new Obstacle.
     *
     * @param parent          the parent Object
     * @param game            the game in which this obstacle lives
     * @param position        the position of the obstacle
     * @param boundingBox     the bounding box of the obstacle
     * @param allowCloseCalls whether close calls should be allowed
     */
    protected Obstacle(GameObject parent, Game game, Point position, BoundingBox boundingBox, boolean allowCloseCalls) {
        super(parent, game, position, boundingBox);

        float closeCallDistance = getGame().getConfig().closeCallDistance();

        if (allowCloseCalls) {
            //create the larger bounding box, used to determine 'close calls'
            new CollectableCloseCall(this, game, new Point(-closeCallDistance, -closeCallDistance),
                getBoundingBox().enlarge(2 * closeCallDistance));

        }
    }

    @Override
    public final void handleCollision(CollidingGameObject other) {
        if (other instanceof PlayerCar playerCar) {
            handleCollision(playerCar);
        }
    }


    @Override
    public boolean destroysCollectables() {
        return true;
    }


    @Override
    public boolean needAllTimescales() {
        return true;
    }

    /**
     * Handle collision with the player car.
     *
     * @param playerCar the player car
     */
    protected void handleCollision(PlayerCar playerCar) {
        playerCar.removeLife();

        //new DebrisObstacle(this, getGame(), Point.zero(), getBoundingBox());
        new Animation(this, getGame(), Point.zero(), AnimationType.EXPLODE, new BoundingBox(new Point(100, 100)));
        kill();
    }

}
