package edu.kit.cargame.game.object.obstacle;


import edu.kit.cargame.game.geometry.BoundingBox;
import edu.kit.cargame.game.geometry.Point;
import edu.kit.cargame.game.common.Game;
import edu.kit.cargame.game.common.GameObject;
import edu.kit.cargame.game.playercar.PlayerCar;
import edu.kit.cargame.io.view.gamerenderers.BoundingBoxRenderer;
import edu.kit.cargame.io.view.gamerenderers.SpriteRenderer;

/**
 * This type models an obstacle which does not chang its own position and cant explode.
 */
public class DebrisObstacle extends ImmobileObstacle {
    /**
     * Instantiates a new Immobile obstacle.
     *
     * @param parent   the parent Object
     * @param game     the game in which this obstacle lives
     * @param position the position of the obstacle
     * @param boundingBox the bounding box of the obstacle
     */
    public DebrisObstacle(GameObject parent, Game game, Point position, BoundingBox boundingBox) {
        this(parent, game, position, boundingBox, false);
    }

    private DebrisObstacle(GameObject parent, Game game, Point position, BoundingBox boundingBox, boolean allowCloseCalls) {
        super(parent, game, position, boundingBox, allowCloseCalls);
        setRenderer(new BoundingBoxRenderer(this, new SpriteRenderer(this, "game/obstacles/debris.png",  true)));
    }

    @Override
    protected void handleCollision(PlayerCar playerCar) {
        //do nothing
    }
}
