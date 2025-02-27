package edu.kit.cargame.game.object.obstacle;

import java.util.Random;

/**
 * Enum for the different types of moving obstacles.
 */
public enum MovingObstacleType {
    /**
     * The red moving obstacle type.
     */
    PURPLE("purple"),
    /**
     * The gold moving obstacle type.
     */
    GOLD("gold");

    private final String name;

    MovingObstacleType(String name) {
        this.name = name;
    }


    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Get a random moving obstacle type.
     *
     * @param random the random generator
     * @return the random moving obstacle type
     */
    public static MovingObstacleType getRandom(Random random) {
        return values()[random.nextInt(values().length)];
    }

}
