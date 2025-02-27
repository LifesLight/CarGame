package edu.kit.cargame.game.playercar;

/**
 * Enum for the different directions the player car can move in.
 */
public enum PlayerCarDirection {
    /**
     * Left up player car direction.
     */
    LEFT_UP(0),
    /**
     * Left player car direction.
     */
    LEFT(1),
    /**
     * Ahead player car direction.
     */
    AHEAD(2),
    /**
     * Right player car direction.
     */
    RIGHT(3),
    /**
     * Right down player car direction.
     */
    RIGHT_DOWN(4);

    // The value of what to add to the player sprites path to find the correct sprite
    private final int pathTranslationValue;

    PlayerCarDirection(int pathTranslationValue) {
        this.pathTranslationValue = pathTranslationValue;
    }

    /**
     * To int int.
     *
     * @return the int
     */
    public int toInt() {
        return pathTranslationValue;
    }
}
