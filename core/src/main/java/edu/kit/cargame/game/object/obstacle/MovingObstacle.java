package edu.kit.cargame.game.object.obstacle;


import edu.kit.cargame.game.geometry.BoundingBox;
import edu.kit.cargame.game.geometry.Point;
import edu.kit.cargame.game.common.Game;
import edu.kit.cargame.game.common.GameObject;
import edu.kit.cargame.game.logic.spawning.Pathfinding;
import edu.kit.cargame.io.view.gamerenderers.MovingObstacleRenderer;

import java.util.ArrayList;
import java.util.List;

/**
 * This type models an obstacle which changes its own position.
 */
public class MovingObstacle extends Obstacle {

    /**
     * How fast the car moves.
     */
    public static final float SPEED = 4.0f;

    private List<MovingObstacleKeypoint> keypoints = new ArrayList<>();

    private int keypointIndex = 0;
    private float verticalVelocity = 0;

    /**
     * Instantiates a new Moving obstacle.
     *
     * @param parent      the parent Object
     * @param game        the game in which this obstacle lives
     * @param position    the position of the obstacle
     * @param boundingBox the bounding box of the obstacle
     * @param type        the type of the obstacle
     */
    public MovingObstacle(GameObject parent, Game game, Point position, BoundingBox boundingBox, MovingObstacleType type) {
        super(parent, game, position, boundingBox);

        setRenderer(new MovingObstacleRenderer(this, type.getName()));

    }

    /**
     * Instantiates a new Moving obstacle.
     *
     * @param parent      the parent Object
     * @param game        the game in which this obstacle lives
     * @param position    the position of the obstacle
     * @param boundingBox the bounding box
     */
    public MovingObstacle(GameObject parent, Game game, Point position, BoundingBox boundingBox) {
        this(parent, game, position, boundingBox, MovingObstacleType.getRandom(game.getRandom()));
    }


    @Override
    public void tick(double timeScale) {
        setPosition(getLocalPosition().addX((float) (SPEED * timeScale * -1)));

        float currentX = getLocalPosition().x();

        if (keypoints.isEmpty()) {
            return;
        }

        if (keypointIndex >= keypoints.size() - 1) {
            MovingObstacleKeypoint lastKeypoint = keypoints.getLast();
            setPosition(new Point(currentX, lastKeypoint.yTarget()));
            verticalVelocity = 0;
            return;
        }

        MovingObstacleKeypoint currentKeypoint = keypoints.get(keypointIndex);
        MovingObstacleKeypoint nextKeypoint = keypoints.get(keypointIndex + 1);

        while (keypointIndex < keypoints.size() - 1 && currentX <= nextKeypoint.eventX()) {
            keypointIndex++;
            currentKeypoint = keypoints.get(keypointIndex);
            if (keypointIndex < keypoints.size() - 1) {
                nextKeypoint = keypoints.get(keypointIndex + 1);
            }
        }

        if (keypointIndex >= keypoints.size() - 1) {
            setPosition(new Point(currentX, currentKeypoint.yTarget()));
            verticalVelocity = 0;
            return;
        }

        float distanceToNext = currentX - nextKeypoint.eventX();

        float interpolatedY;
        if (distanceToNext > Pathfinding.INTERPOLATION_DURATION) {
            interpolatedY = currentKeypoint.yTarget();
            verticalVelocity = 0;
        } else {
            float t = (Pathfinding.INTERPOLATION_DURATION - distanceToNext) / Pathfinding.INTERPOLATION_DURATION;
            interpolatedY = (1 - t) * currentKeypoint.yTarget() + t * nextKeypoint.yTarget();
            verticalVelocity = (nextKeypoint.yTarget() - currentKeypoint.yTarget()) / Pathfinding.INTERPOLATION_DURATION;
        }

        setPosition(new Point(currentX, interpolatedY));
    }


    /**
     * Sets keypoints the moving obstacles follows.
     *
     * @param keypoints the keypoints
     */
    public void setKeypoints(List<MovingObstacleKeypoint> keypoints) {
        this.keypoints = keypoints;
    }

    /**
     * Gets vertical velocity.
     *
     * @return the vertical velocity
     */
    public float getVerticalVelocity() {
        return verticalVelocity;
    }

    /**
     * Gets max velocity.
     *
     * @return the max velocity
     */
    public float getMaxVelocity() {
        return 5;
    }

    /**
     * Checks if the car is blinking in a certain direction.
     *
     * @param upwards to check if the car is blinking upwards
     * @return true if the car is blinking in the given direction
     */
    public boolean isBlinking(boolean upwards) {
        if (keypointIndex >= keypoints.size() - 1) {
            return false;
        }

        MovingObstacleKeypoint nextKeypoint = keypoints.get(keypointIndex + 1);
        float distanceToTurn = getLocalPosition().x() - nextKeypoint.eventX();

        if (distanceToTurn > getGame().getConfig().movingObstacleBlinkerDistance()) {
            return false;
        }

        float verticalDifference = nextKeypoint.yTarget() - getLocalPosition().y();
        return upwards ? verticalDifference > 5 : verticalDifference < -5;
    }

    /**
     * Checks if the car is blinking upwards.
     *
     * @return true if the car is blinking upwards
     */
    public boolean isBlinkingUp() {
        return isBlinking(true);
    }

    /**
     * Checks if the car is blinking downwards.
     *
     * @return true if the car is blinking downwards
     */
    public boolean isBlinkingDown() {
        return isBlinking(false);
    }
}
