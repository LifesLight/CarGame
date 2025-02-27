package edu.kit.cargame.game.object.obstacle;


import edu.kit.cargame.game.geometry.BoundingBox;
import edu.kit.cargame.game.geometry.Point;
import edu.kit.cargame.game.common.Game;
import edu.kit.cargame.game.common.GameObject;
import edu.kit.cargame.io.view.gamerenderers.BoundingBoxRenderer;
import edu.kit.cargame.io.view.gamerenderers.SpriteRenderer;
import edu.kit.cargame.io.view.gamerenderers.TextRenderer;

/**
 * This type models an obstacle which does not change its own position.
 * This class is used for debugging purposes.
 */
public class DebugObstacle extends Obstacle {

    private String debugInfo;
    /**
     * Instantiates a new Immobile obstacle.
     *
     * @param parent   the parent Object
     * @param game     the game in which this obstacle lives
     * @param position the position of the obstacle
     * @param boundingBox the bounding box of the obstacle
     * @param debug the debug info to display
     */
    public DebugObstacle(GameObject parent, Game game, Point position, BoundingBox boundingBox, String debug) {
        this(parent, game, position, boundingBox, true);
        setRenderer(new TextRenderer(this, null));
        debugInfo = debug;
    }

    @Override
    public String getDebugText() {
        return debugInfo;
    }

    private DebugObstacle(GameObject parent, Game game, Point position, BoundingBox boundingBox, boolean allowCloseCalls) {
        super(parent, game, position, boundingBox, allowCloseCalls);
        setRenderer(new BoundingBoxRenderer(this, null));
    }

    /**
     * Instantiates a new Debug obstacle.
     *
     * @param parent   the parent Object
     * @param game     the game in which this obstacle lives
     * @param position the position of the obstacle
     * @param type the type of the obstacle
     */
    public DebugObstacle(GameObject parent, Game game, Point position, ImmobileObstacleType type) {
        super(parent, game, position, type.getBoundingBox(), true);
        setRenderer(new SpriteRenderer(this, "game/obstacles/" + type.getName() + ".png"));
    }
}
