package edu.kit.cargame.game.logic.spawning;

import edu.kit.cargame.common.logging.LoggerManagement;
import edu.kit.cargame.game.common.Game;
import edu.kit.cargame.game.geometry.Point;
import edu.kit.cargame.game.logic.spawning.proxies.SpawnCandidate;
import edu.kit.cargame.game.logic.spawning.proxies.SpawnCandidateType;
import edu.kit.cargame.game.object.obstacle.MovingObstacle;
import edu.kit.cargame.game.object.obstacle.MovingObstacleKeypoint;

import java.util.ArrayList;
import java.util.List;

/**
 * Pathfinding class, pre computes paths for moving obstacles.
 */
public final class Pathfinding {
    private static final int LOOKAHEAD_STEPS = 4;
    private static final int ESTIMATION_FACTOR = 1;

    private static final float LINE_OFFSET = 0;
    private static final float LINE_SIZE = 80;

    private static final float PATHFINDING_BUFFER_FACTOR = 1.05f;

    /**
     * How long cars switch lane for.
     */
    public static final int INTERPOLATION_DURATION = 80;
    private static final float INTERPOLATION_STRICTNESS = 0.2f;

    private Pathfinding() {
        // Utility
    }

    private static boolean collidesInFuture(List<SpawnCandidate> candidates, float carSpeed, Point origin, float carSize) {
        Point offsetAmount = new Point(-1 * carSpeed * ESTIMATION_FACTOR, origin.y());

        Point scanningPoint = new Point(origin.x(), origin.y());
        for (int i = 0; i < LOOKAHEAD_STEPS; i++) {
            for (SpawnCandidate candidate : candidates) {
                float distance = candidate.getPosition().distance(scanningPoint);
                if (distance < candidate.getSize() + carSize) {
                    return true;
                }
            }

            scanningPoint = scanningPoint.add(offsetAmount);
        }

        return false;
    }

    /**
     *
     */
    private static boolean isLaneFree(List<SpawnCandidate> candidates, float carSpeed, Point origin, float yOffset, float carSize) {
        Point offsetOrigin = new Point(origin.x(), origin.y() + yOffset);
        Point offsetAmount = new Point(-1 * carSpeed * ESTIMATION_FACTOR, origin.y());

        Point scanningPoint = new Point(offsetOrigin.x(), offsetOrigin.y());
        for (int i = 0; i < LOOKAHEAD_STEPS; i++) {
            for (SpawnCandidate candidate : candidates) {
                if (candidate.getPosition().distance(scanningPoint) < candidate.getSize() + carSize) {
                    return false;
                }
            }

            scanningPoint = scanningPoint.add(offsetAmount);
        }

        return true;
    }


    /**
     * TODO ALEX
     *
     * @param spawnCandidates
     * @param laneCount
     * @param height
     * @param game
     * @param chunk
     * @param spawner
     * @return
     */
    public static List<SpawnCandidate> configureKeypoints(List<SpawnCandidate> spawnCandidates, int laneCount, float height,
                                                          Game game, Chunk chunk, Spawner spawner) {
        float randomSwapChance = game.getConfig().movingCarRandomLaneSwapChance();

        float moveAmount = height / laneCount;

        // First filter Moving
        List<SpawnCandidate> moving = new ArrayList<>();
        List<SpawnCandidate> collidables = new ArrayList<>();
        List<SpawnCandidate> other = new ArrayList<>();

        for (SpawnCandidate spawnCandidate : spawnCandidates) {
            if (spawnCandidate.getType() == SpawnCandidateType.MOVING) {
                moving.add(spawnCandidate);
            } else if (spawnCandidate.getColliding()) {
                collidables.add(spawnCandidate);
            } else {
                other.add(spawnCandidate);
            }
        }

        // We try adding Moving obstacles via greeedy pathfinding

        List<SpawnCandidate> configuredMoving = new ArrayList<>();

        int statsTotalAttempts = moving.size();
        int statsSucceeded = 0;
        int statsFailedInterpolation = 0;
        int statsFailedNoPath = 0;

        for (SpawnCandidate spawnCandidate : moving) {
            Point movingPosition = new Point(spawnCandidate.getPosition().x(), spawnCandidate.getPosition().y());
            List<MovingObstacleKeypoint> keypoints = new ArrayList<>();

            List<SpawnCandidate> otherWithLineGhost = new ArrayList<>(collidables);

            float playeCarX = game.getPlayerCar().getGlobalPosition().x();
            float carpos = spawnCandidate.getPosition().add(chunk.getGlobalPosition()).x();
            float t = (carpos - playeCarX) / (MovingObstacle.SPEED + Chunk.SPEED);

            float collisionX = carpos - (t * MovingObstacle.SPEED); // Line moves left at speed 5?
            float collisionY = spawner.sampleLineY(collisionX);

            float localCollisionX = collisionX - chunk.getGlobalPosition().x() + LINE_OFFSET;

            if (collisionY != Float.POSITIVE_INFINITY) {
                SpawnCandidate lineCollisionGhost = new SpawnCandidate(new Point(localCollisionX, collisionY), SpawnCandidateType.GHOST, LINE_SIZE, true);
                otherWithLineGhost.add(lineCollisionGhost);
            }

            keypoints.add(new MovingObstacleKeypoint(movingPosition.x(), movingPosition.y()));

            boolean failed = false;
            int lastSwap = 100000;

            int stepsToCompute = (int) (t * PATHFINDING_BUFFER_FACTOR);

            for (int i = 0; i < stepsToCompute; i++) {
                // Random lane swap attempt

                boolean cantSwap = lastSwap < INTERPOLATION_DURATION * INTERPOLATION_STRICTNESS;

                boolean mustSwap = collidesInFuture(otherWithLineGhost, MovingObstacle.SPEED, movingPosition, spawnCandidate.getSize());

                if (mustSwap && cantSwap) {
                    failed = true;
                    statsFailedInterpolation++;
                    break;
                }

                boolean wantSwap = !cantSwap && game.getRandom().nextFloat() < randomSwapChance;

                if (wantSwap || mustSwap) {
                    float directionMult = game.getRandom().nextFloat() < 0.5 ? 1 : -1;
                    directionMult *= moveAmount;

                    boolean success = false;
                    for (int d = 0; d < 2; d++) {
                        // Check if already at edge
                        // testing up
                        if (directionMult > 0 && movingPosition.y() + directionMult > height) {
                            break;
                        }

                        //testing down
                        if (directionMult < 0 && movingPosition.y() + directionMult < 0) {
                            break;
                        }

                        // Check if free
                        if (isLaneFree(otherWithLineGhost, MovingObstacle.SPEED, movingPosition, directionMult, spawnCandidate.getSize())) {
                            keypoints.add(new MovingObstacleKeypoint(movingPosition.x(), movingPosition.y() + directionMult));
                            movingPosition = new Point(movingPosition.x(), movingPosition.y() + directionMult);
                            success = true;
                            lastSwap = 0;
                            break;
                        }

                        directionMult *= -1;
                    }

                    if (!success && mustSwap) {
                        failed = true;
                        statsFailedNoPath++;
                        break;
                    }
                } else {
                    lastSwap++;
                }

                movingPosition = movingPosition.add(new Point(-1 * MovingObstacle.SPEED, 0));
            }

            if (!failed) {
                spawnCandidate.setKeypoints(keypoints);
                configuredMoving.add(spawnCandidate);
                statsSucceeded++;
            }
        }

        configuredMoving.addAll(other);
        configuredMoving.addAll(collidables);

        LoggerManagement.getLogger().debug(String.format("Attempts %d: Interpolation: %d, NoPath: %d, Success: %d",
            statsTotalAttempts,
            statsFailedInterpolation,
            statsFailedNoPath,
            statsSucceeded));

        return configuredMoving;
    }
}
