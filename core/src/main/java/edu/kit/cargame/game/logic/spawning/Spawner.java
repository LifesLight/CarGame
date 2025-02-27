package edu.kit.cargame.game.logic.spawning;

import edu.kit.cargame.common.logging.LoggerManagement;
import edu.kit.cargame.game.common.Game;
import edu.kit.cargame.game.common.GameObject;
import edu.kit.cargame.game.geometry.BoundingBox;
import edu.kit.cargame.game.geometry.LineSegment;
import edu.kit.cargame.game.geometry.Point;
import edu.kit.cargame.game.logic.spawning.proxies.SpawnCandidate;
import edu.kit.cargame.game.logic.spawning.proxies.SpawnCandidateType;
import edu.kit.cargame.game.object.obstacle.ImmobileObstacleType;
import edu.kit.cargame.io.config.Config;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * The type Spawner is an invisible Game Object, which generates new, random {@link Chunk} Objects.
 */
public class Spawner extends GameObject {
    /**
     * How many chunks can exist at once
     * Increasing this will improve performance but may break pathfinding!
     */
    private static final int NUM_CHUNKS_ON_SCREEN = 3;
    private static final int NUM_CHUNKS_BUFFERED = 2;
    private static final int NUM_CHUNKS = NUM_CHUNKS_ON_SCREEN + NUM_CHUNKS_BUFFERED;

    private static final float TOP_DEADZONE = 155;
    private static final float BOT_DEADZONE = 0;

    /**
     * The real "collision" radius of spawn candidates.
     * These are only really used without PRUNING_FACTOR in moving car collisions
     */
    private float immovableRadius;
    private float collectableRadius;
    private float movingRadius;
    private float largestRadius;

    /**
     * How much "buffer" must be between two spawning obstacles for them not to be pruned
     * Relevant because we want finer control over moving car collisions!
     */
    private static final float PRUNING_FACTOR = 1.25f;
    private static final float PRUNING_FACTOR_MOVING = 2.0f;

    private static final int NUM_LANES = 3;

    /**
     * This might need to be increased if moving cars drive slower!
     */
    private static final int GHOST_BUFFER_SIZE = 2;

    private final float chunkWidth;
    private final float chunkHeight;

    private final float newChunkSpawnX;
    private final float newChunkSpawnY;

    private final BoundingBox pointSamplingBounds;

    private Chunk lastChunk = null;

    private final Config config;


    private void configureRadia() {
        movingRadius = 20;
        collectableRadius = 30;

        for (ImmobileObstacleType type : ImmobileObstacleType.values()) {
            float radius = Math.max(type.getBoundingBox().getHeight(), type.getBoundingBox().getWidth()) / 2;
            immovableRadius = Math.max(radius, immovableRadius);
        }

        largestRadius = Math.max(Math.max(immovableRadius, collectableRadius), movingRadius);
    }

    /**
     * Instantiates a new Spawner.
     *
     * @param parent   the parent Object
     * @param game     the game in which this spawner lives
     * @param position the position of the spawner
     */
    public Spawner(GameObject parent, Game game, Point position) {
        super(parent, game, position);

        configureRadia();

        config = game.getConfig();
        float width = config.worldWidth();
        float height = config.worldHeight() - TOP_DEADZONE - BOT_DEADZONE;

        setBoundingBox(new BoundingBox(new Point(width, height)));

        chunkWidth = width / NUM_CHUNKS_ON_SCREEN;
        chunkHeight = height + largestRadius;

        newChunkSpawnX = (NUM_CHUNKS - 1) * chunkWidth;
        newChunkSpawnY = BOT_DEADZONE;

        pointSamplingBounds = new BoundingBox(new Point(chunkWidth, chunkHeight));

        for (int i = 0; i < NUM_CHUNKS; i++) {
            Chunk chunk = createChunk(true);
            chunk.setPosition(new Point(i * chunkWidth, newChunkSpawnY));

            lastChunk = chunk;
        }
    }


    private Chunk createChunk(boolean empty) {
        Chunk chunk = new Chunk(this, getGame(), new Point(newChunkSpawnX, newChunkSpawnY), chunkWidth);

        List<SpawnCandidate> spawnCandidates = new ArrayList<>();
        addGhosts(spawnCandidates);

        addImmobileCandidates(spawnCandidates, config.obstacleAmount());

        extendLine(chunk);
        spawnCandidates = pruneByLine(spawnCandidates, chunk);

        int score = getGame().getScore();
        double scoreFactor = (score > 0) ? Math.pow(score, config.collectableChanceDecayRate()) : 1;
        double expectedCollectables = (config.obstacleAmount() * config.collectableChance()) / scoreFactor;
        int collectableCount = (int) Math.floor(expectedCollectables + getGame().getRandom().nextDouble());

        addCollectableCandidates(spawnCandidates, collectableCount);

        spawnCandidates = pruneByRadius(spawnCandidates, false, PRUNING_FACTOR);

        int movingCarCount = (int) (config.obstacleAmount() * config.movingObstacleFactor());
        addMovingCandidates(spawnCandidates, movingCarCount);

        spawnCandidates = Pathfinding.configureKeypoints(spawnCandidates, NUM_LANES, chunkHeight, getGame(), chunk, this);

        if (empty) {
            spawnCandidates = new ArrayList<>();
        }

        chunk.spawnCandidates(spawnCandidates);

        lastChunk = chunk;
        return chunk;
    }


    private static List<SpawnCandidate> pruneByRadius(List<SpawnCandidate> points, boolean ignoreY, float pruningFactor) {
        List<SpawnCandidate> accepted = new ArrayList<>(points.size());

        // First, add all ghosts (they cannot be pruned).
        for (SpawnCandidate candidate : points) {
            if (candidate.getType() == SpawnCandidateType.GHOST) {
                accepted.add(candidate);
            }
        }

        // Then, process non-ghost candidates in order.
        for (SpawnCandidate candidate : points) {
            if (candidate.getType() != SpawnCandidateType.GHOST) {
                boolean tooClose = false;
                // Check against all already accepted candidates.
                for (SpawnCandidate acceptedCandidate : accepted) {
                    float dist;

                    if (ignoreY) {
                        dist = Math.abs(candidate.getPosition().x() - acceptedCandidate.getPosition().x());
                    } else {
                        dist = candidate.getPosition().distance(acceptedCandidate.getPosition());
                    }

                    if (dist < (candidate.getSize() + acceptedCandidate.getSize()) * pruningFactor) {
                        tooClose = true;
                        break;
                    }
                }
                // Only add if no accepted candidate is too close.
                if (!tooClose) {
                    accepted.add(candidate);
                }
            }
        }

        return accepted;
    }


    private void addGhosts(List<SpawnCandidate> candidates) {
        if (lastChunk == null) {
            return;
        }

        Point offsetPoint = new Point(chunkWidth * -1, 0);
        for (SpawnCandidate candidate : lastChunk.getSpawnCandidates()) {
            // We ignore moving here since they "wont be there anymore"
            if (candidate.getType() == SpawnCandidateType.MOVING || candidate.getAge() > GHOST_BUFFER_SIZE) {
                continue;
            }
            candidates.add(new SpawnCandidate(candidate, offsetPoint));
        }
    }

    private List<SpawnCandidate> pruneByLine(Collection<SpawnCandidate> candidates, Chunk chunk) {
        List<LineSegment> lineSegments = chunk.getLineSegments();

        List<SpawnCandidate> prunedCandidates = new ArrayList<>();
        for (SpawnCandidate candidate : candidates) {
            boolean valid = true;
            for (LineSegment lineSegment : lineSegments) {
                float distance = lineSegment.distance(candidate.getPosition());
                if (distance < candidate.getSize() + config.alwaysPossibleLineWidth()) {
                    valid = false;
                    break;
                }
            }
            if (valid) {
                prunedCandidates.add(candidate);
            }
        }

        return prunedCandidates;
    }

    private void extendLine(Chunk chunk) {
        List<Point> lineExtension = new ArrayList<>();
        if (lastChunk != null) {
            Point lastPoint = lastChunk.getLinePoints().getLast();
            lineExtension.add(new Point(0, lastPoint.y()));
        }

        float stepSize = chunkWidth / (config.alwaysPossibleLineFrequency() - 1);

        for (int i = 1; i < config.alwaysPossibleLineFrequency(); i++) {
            Point linePoint = new Point(stepSize * i, (getGame().getRandom().nextFloat(chunkHeight)) + BOT_DEADZONE);
            lineExtension.add(linePoint);
        }

        chunk.setLinePoints(lineExtension);
    }

    private void addImmobileCandidates(Collection<SpawnCandidate> candidates, int amount) {
        Collection<Point> points = PointTools.samplePoints(pointSamplingBounds, amount, NUM_LANES,
            config.spawningScatterFactor(), getGame().getRandom());
        for (Point point : points) {
            SpawnCandidate spawnCandidate = new SpawnCandidate(point, SpawnCandidateType.IMMOBILE, immovableRadius, true);
            candidates.add(spawnCandidate);
        }
    }

    private void addMovingCandidates(Collection<SpawnCandidate> candidates, int amount) {
        Collection<Point> points = PointTools.samplePoints(pointSamplingBounds, amount, NUM_LANES, 0, getGame().getRandom());

        List<SpawnCandidate> movingCandidates = new ArrayList<>();

        for (Point point : points) {
            SpawnCandidate spawnCandidate = new SpawnCandidate(point, SpawnCandidateType.MOVING, movingRadius, true);
            movingCandidates.add(spawnCandidate);
        }

        movingCandidates = pruneByRadius(movingCandidates, true, PRUNING_FACTOR_MOVING);

        candidates.addAll(movingCandidates);
    }

    private void addCollectableCandidates(List<SpawnCandidate> spawnCandidates, int amount) {
        Collection<Point> points = PointTools.samplePoints(pointSamplingBounds, amount, NUM_LANES,
            config.spawningScatterFactor(), getGame().getRandom());
        for (Point point : points) {
            spawnCandidates.add(new SpawnCandidate(point, SpawnCandidateType.COLLECTABLE, collectableRadius, false));
        }
    }

    /**
     * Returns the y value of the line at global x.
     *
     * @param globalX global x coordinate value to sample
     * @return global y value of line at x
     */
    public float sampleLineY(float globalX) {
        for (GameObject chunk : getChildren()) {
            if (!(chunk instanceof Chunk child)) {
                LoggerManagement.getLogger().critical("Non Chunk is child of Spawner?!");
                return Float.POSITIVE_INFINITY;
            }

            Point globalPosition = child.getGlobalPosition();

            float localX = globalX - globalPosition.x();

            if (localX < 0 || localX > chunkWidth) {
                continue;
            }

            for (LineSegment lineSegment : child.getLineSegments()) {
                Point p1 = lineSegment.start();
                Point p2 = lineSegment.end();

                float segMinX = p1.x();
                float segMaxX = p2.x();

                if (localX >= segMinX && localX <= segMaxX) {
                    if (segMaxX == segMinX) {
                        return (p1.y() + p2.y()) / 2.0f;
                    }

                    float t;
                    float y;
                    if (p1.x() <= p2.x()) {
                        t = (localX - p1.x()) / (p2.x() - p1.x());
                        y = p1.y() + t * (p2.y() - p1.y());
                    } else {
                        t = (localX - p2.x()) / (p1.x() - p2.x());
                        y = p2.y() + t * (p1.y() - p2.y());
                    }
                    return y;
                }
            }
        }

        return Float.POSITIVE_INFINITY;
    }

    /**
     * Creates a new chunk once the oldest chunk kills itself.
     */
    @Override
    public void tick(double timeScale) {
        if (getChildren().size() < NUM_CHUNKS) {
            createChunk(false);
        }
    }

    @Override
    public boolean needAllTimescales() {
        return true;
    }
}
