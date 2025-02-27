package edu.kit.cargame.game.object.obstacle;

import edu.kit.cargame.game.common.Game;
import edu.kit.cargame.game.common.GameObject;
import edu.kit.cargame.game.geometry.BoundingBox;
import edu.kit.cargame.game.geometry.Point;
import edu.kit.cargame.game.playercar.PlayerCar;

/**
 * The type Invisible Obstacle is an invisible Box which is placed at the top and bottom of the road.
 * It removes a life from the player when they try to  leave the road.
 */

public class InvisibleObstacle extends Obstacle {

    private final boolean top;

    /**
     * Instantiate a new invisible Obstacle.
     *
     * @param parent      the parent Object
     * @param game        the game in which this obstacle lives
     * @param position    the position of the obstacle
     * @param boundingBox the bounding box of the obstacle
     * @param top         if the obstacle is at the top or bottom of the road
     */
    public InvisibleObstacle(GameObject parent, Game game, Point position, BoundingBox boundingBox, boolean top) {
        super(parent, game, position, boundingBox, false);
        this.top = top;
        //setRenderer(new BoundingBoxRenderer(this, null));
    }

    @Override
    protected void handleCollision(PlayerCar playerCar) {
        //playerCar.removeLife();

        playerCar.setVelocity(0);

        if (top) {
            playerCar.setPosition(new Point(playerCar.getLocalPosition().x(), getLocalPosition().y() - playerCar.getBoundingBox().getHeight()));
        } else {
            playerCar.setPosition(new Point(playerCar.getLocalPosition().x(), getLocalPosition().y() + getBoundingBox().getHeight()));
        }
        //playerCar.setPosition(new Point(playerCar.getLocalPosition().x(), (float) getGame().getConfig().worldHeight() /2 - 70));
    }

}
