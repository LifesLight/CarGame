package edu.kit.cargame.game.logic.spawning.proxies;

import edu.kit.cargame.common.logging.LoggerManagement;
import edu.kit.cargame.game.geometry.Point;
import edu.kit.cargame.game.object.obstacle.MovingObstacleKeypoint;

import java.util.List;

/**
 * A spawn candidate represents a possible object / collectable to be spawned in the future.
 * Needed as abstraction of a individual GameObject which is not so tightly intertwined into the game tree.
 * This allows for deletion and moving without doing expensive tree updates.
 * Age is how many "chunks" ago this was instantiated, relevant for ghosts
 */
public class SpawnCandidate {
    private Point position;
    private SpawnCandidateType type;
    private final float size;
    private boolean colliding;
    private int age;

    // Only relevant if MovingObstacle
    private List<MovingObstacleKeypoint> keypoints = null;


    /**
     * Create new candidate.
     *
     * @param position the position of the candidate
     * @param type the type of the candidate
     * @param size the size of the candidate
     * @param colliding is the candidate colliding with player car?
     */
    public SpawnCandidate(Point position, SpawnCandidateType type, float size, boolean colliding) {
        this.position = position;
        this.type = type;
        this.size = size;
        this.colliding = colliding;
        this.age = 0;
    }

    /**
     * Moves spawn candidate by offset.
     * Also increments its age.
     *
     * @param original original candidate
     * @param offset offset to move candidate
     */
    public SpawnCandidate(SpawnCandidate original, Point offset) {
        this(original.getPosition().add(offset), SpawnCandidateType.GHOST, original.getSize(), original.getColliding());
        age = original.getAge() + 1;
    }

    /**
     * Returns candidates age.
     *
     * @return candidates age
     */
    public int getAge() {
        return age;
    }

    /**
     * Returns if is colliding.
     *
     * @return is colliding?
     */
    public boolean getColliding() {
        return this.colliding;
    }

    /**
     * Returns size.
     *
     * @return size
     */
    public float getSize() {
        return size;
    }

    /**
     * Returns size
     *
     * @return size
     */
    public Point getPosition() {
        return position;
    }

    /**
     * Sets keypoint's, relevant for moving
     *
     * @param keypoints to add.
     */
    public void setKeypoints(List<MovingObstacleKeypoint> keypoints) {
        this.keypoints = keypoints;
    }

    /**
     * Returns keypoint's
     *
     * @return keypoint's
     */
    public List<MovingObstacleKeypoint> getKeypoints() {
        if (type != SpawnCandidateType.MOVING) {
            LoggerManagement.getLogger().critical("Tried getting MovingObstacleKeypoint's from non moving candidate!");
        }
        return keypoints;
    }

    /**
     * Returns this candidates type.
     *
     * @return the type.
     */
    public SpawnCandidateType getType() {
        return type;
    }
}
