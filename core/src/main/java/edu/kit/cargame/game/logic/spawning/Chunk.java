package edu.kit.cargame.game.logic.spawning;

import edu.kit.cargame.game.common.Game;
import edu.kit.cargame.game.common.GameObject;
import edu.kit.cargame.game.geometry.BoundingBox;
import edu.kit.cargame.game.geometry.LineSegment;
import edu.kit.cargame.game.geometry.Point;
import edu.kit.cargame.game.logic.spawning.proxies.SpawnCandidate;
import edu.kit.cargame.game.object.collectable.CollectableType;
import edu.kit.cargame.game.object.obstacle.ImmobileObstacle;
import edu.kit.cargame.game.object.obstacle.ImmobileObstacleType;
import edu.kit.cargame.game.object.obstacle.MovingObstacle;
import edu.kit.cargame.io.view.gamerenderers.LineRenderer;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Chunk is a Game Object with the purpose of holding other Game Objects. It is invisible, but its children
 * (i.e. Obstacles and Collectables) are rendered. It makes simulating movement of the player car easier, as the chunk
 * can be moved towards the player car, instead of having to move all Objects individually.
 */
public class Chunk extends GameObject {
    /**
     * The speed with which the chunk moves towards the player car.
     */
    public static final float SPEED = 5.0f;

    private List<Point> linePoints;
    private List<SpawnCandidate> spawnCandidates;
    private final float width;

    /**
     * Instantiates a new Chunk.
     *
     * @param parent   the parent
     * @param game     the game
     * @param position the position
     * @param width    the width of the chunk
     */
    public Chunk(GameObject parent, Game game, Point position, float width) {
        super(parent, game, position);
        this.width = width;
        if (game.getConfig().bestPathVisible()) {
            setRenderer(new LineRenderer(this));
        }
    }

    /**
     * Sets the line points.
     *
     * @param linePoints the line points
     */
    public void setLinePoints(List<Point> linePoints) {
        this.linePoints = linePoints;
    }

    /**
     * Spawns the candidates at the given positions.
     * The type of the candidate determines the type of the object that is spawned.
     *
     * @param spawnCandidates the spawn candidates
     */
    public void spawnCandidates(List<SpawnCandidate> spawnCandidates) {
        this.spawnCandidates = spawnCandidates;

        for (SpawnCandidate candidate : spawnCandidates) {
            switch (candidate.getType()) {
                case GHOST:
                    // new DebugObstacle(this, getGame(), candidate.getPosition(), new BoundingBox(new Point(50, 50)), "HI");
                    break;
                case MOVING:
                    MovingObstacle moving = new MovingObstacle(this, getGame(), candidate.getPosition(), new BoundingBox(new Point(50, 50)));
                    moving.setKeypoints(candidate.getKeypoints());
                    break;
                case IMMOBILE:
                    new ImmobileObstacle(this, getGame(), candidate.getPosition(), ImmobileObstacleType.randomType(getGame().getRandom()));
                    break;
                case COLLECTABLE:
                    CollectableType.spawnRandomCollectable(this, getGame(), candidate.getPosition(), getGame().getRandom());
                    break;
            }
        }
    }

    @Override
    public void addChild(GameObject child) {
        super.addChild(child);
    }

    @Override
    public void tick(double timeScale) {
        setPosition(getLocalPosition().addX((float) (-SPEED * timeScale)));
        if ((getGlobalPosition().x() + width) < 0) {
            kill();
        }
    }

    @Override
    protected boolean needAllTimescales() {
        return true;
    }

    /**
     * Gets the line points.
     * Used for rendering the best path.
     *
     * @return the line points
     */
    public List<Point> getLinePoints() {
        return linePoints;
    }

    /**
     * Gets the spawn candidates.
     *
     * @return the spawn candidates
     */
    public List<SpawnCandidate> getSpawnCandidates() {
        return spawnCandidates;
    }

    /**
     * Gets the line segments of the best path.
     * Used for spawning
     *
     * @return the line segments
     */
    public List<LineSegment> getLineSegments() {
        List<LineSegment> lineSegments = new ArrayList<>();
        Point lastStart = getLinePoints().getFirst();
        for (int i = 1; i < getLinePoints().size(); i++) {
            LineSegment lineSegment = new LineSegment(lastStart, getLinePoints().get(i));
            lineSegments.add(lineSegment);
            lastStart = getLinePoints().get(i);
            //LoggerManagement.getLogger().debug(String.format("Line Segment %d: + %s", i, lineSegment));
        }
        return lineSegments;
    }
}
