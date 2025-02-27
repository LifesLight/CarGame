package edu.kit.cargame.game.logic.spawning.proxies;

import edu.kit.cargame.common.logging.LoggerManagement;
import edu.kit.cargame.game.geometry.Point;
import edu.kit.cargame.game.object.obstacle.MovingObstacleKeypoint;

import java.util.List;
//TODO ALEX JAVA DOCS
public class SpawnCandidate {
    private Point position;
    private SpawnCandidateType type;
    private final float size;
    private boolean colliding;
    private int age;

    // Only relevant if MovingObstacle
    private List<MovingObstacleKeypoint> keypoints = null;


    public SpawnCandidate(Point position, SpawnCandidateType type, float size, boolean colliding) {
        this.position = position;
        this.type = type;
        this.size = size;
        this.colliding = colliding;
        this.age = 0;
    }

    public SpawnCandidate(SpawnCandidate original, Point offset) {
        this(original.getPosition().add(offset), SpawnCandidateType.GHOST, original.getSize(), original.getColliding());
        age = original.getAge() + 1;
    }

    public int getAge() {
        return age;
    }

    public boolean getColliding() {
        return this.colliding;
    }

    public float getSize() {
        return size;
    }

    public Point getPosition() {
        return position;
    }

    public void setKeypoints(List<MovingObstacleKeypoint> keypoints) {
        this.keypoints = keypoints;
    }

    public List<MovingObstacleKeypoint> getKeypoints() {
        if (type != SpawnCandidateType.MOVING) {
            LoggerManagement.getLogger().critical("Tried getting MovingObstacleKeypoint's from non moving candidate!");
        }
        return keypoints;
    }

    public SpawnCandidateType getType() {
        return type;
    }
}
