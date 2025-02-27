package edu.kit.cargame.game.object.obstacle;

/**
 * Stores info for Moving car what to do when.
 * @param eventX what x coordinate the event takes place
 * @param yTarget the target y coordinate
 */
public record MovingObstacleKeypoint(float eventX, float yTarget) {
}
