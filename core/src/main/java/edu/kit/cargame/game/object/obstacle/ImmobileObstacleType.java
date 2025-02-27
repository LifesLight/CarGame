package edu.kit.cargame.game.object.obstacle;

import edu.kit.cargame.game.geometry.BoundingBox;
import edu.kit.cargame.game.geometry.Point;

import java.util.Random;

/**
 * Enum for the different types of immobile obstacles.
 */
public enum ImmobileObstacleType {
    /**
     * The Barrier.
     */
    BARRIER("barrier", new BoundingBox(new Point(26, 58))),
    /**
     * The Bin.
     */
    BIN("bin", new BoundingBox(new Point(52, 39))),
    /**
     * The Broken down car purple.
     */
    BROKEN_DOWN_CAR_PURPLE("broken_down_car_purple", new BoundingBox(new Point(60, 49))),
    /**
     * The Broken down car red.
     */
    BROKEN_DOWN_CAR_RED("broken_down_car_red", new BoundingBox(new Point(60, 49))),
    /**
     * The Broken down car turquoise.
     */
    BROKEN_DOWN_CAR_TURQUOISE("broken_down_car_turquois", new BoundingBox(new Point(60, 49))),
    /**
     * The Cones.
     */
    CONES("cones", new BoundingBox(new Point(25, 62)));

    private final String name;
    private final BoundingBox boundingBox;

    ImmobileObstacleType(String name, BoundingBox boundingBox) {
        this.name = name;
        this.boundingBox = boundingBox;
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
     * Gets bounding box.
     *
     * @return the bounding box
     */
    public BoundingBox getBoundingBox() {
        return boundingBox;
    }

    /**
     * Get a random immobile obstacle type.
     *
     * @param random the random generator
     * @return the immobile obstacle type
     */
    public static ImmobileObstacleType randomType(Random random) {
        return values()[(random.nextInt(values().length))];
    }
}
