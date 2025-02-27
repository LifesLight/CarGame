package edu.kit.cargame.game.object.obstacle;


import edu.kit.cargame.game.geometry.BoundingBox;
import edu.kit.cargame.game.geometry.Point;
import edu.kit.cargame.game.common.Game;
import edu.kit.cargame.game.common.GameObject;
import edu.kit.cargame.io.view.gamerenderers.BoundingBoxRenderer;
import edu.kit.cargame.io.view.gamerenderers.SpriteRenderer;

/**
 * This type models an obstacle which does not chang its own position.
 */
public class ImmobileObstacle extends Obstacle {
    /**
     * Instantiates a new Immobile obstacle.
     *
     * @param parent   the parent Object
     * @param game     the game in which this obstacle lives
     * @param position the position of the obstacle
     * @param boundingBox the bounding box of the obstacle
     */
    public ImmobileObstacle(GameObject parent, Game game, Point position, BoundingBox boundingBox) {
        this(parent, game, position, boundingBox, true);
    }

    /**
     * Instantiates a new Immobile obstacle.
     *
     * @param parent   the parent Object
     * @param game     the game in which this obstacle lives
     * @param position the position of the obstacle
     * @param boundingBox the bounding box of the obstacle
     * @param allowCloseCalls if the obstacle is allowed to be close to other objects
     */
    protected ImmobileObstacle(GameObject parent, Game game, Point position, BoundingBox boundingBox, boolean allowCloseCalls) {
        super(parent, game, position, boundingBox, allowCloseCalls);
        setRenderer(new BoundingBoxRenderer(this, null));
    }

    /**
     * Instantiates a new Immobile obstacle.
     *
     * @param parent the parent Object
     * @param game the game in which this obstacle lives
     * @param position the position of the obstacle
     * @param type the type of the obstacle
     */
    public ImmobileObstacle(GameObject parent, Game game, Point position, ImmobileObstacleType type) {
        super(parent, game, position, type.getBoundingBox(), true);
        setRenderer(new SpriteRenderer(this, "game/obstacles/" + type.getName() + ".png"));
    }
}
