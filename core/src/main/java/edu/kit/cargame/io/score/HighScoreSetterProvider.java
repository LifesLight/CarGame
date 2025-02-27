package edu.kit.cargame.io.score;

import edu.kit.cargame.game.playercar.CarType;

/**
 * HighScoreSetterProvider Provides everything the read only HighScoreProvider can,
 * with the addition of being able to add a high score.
 */
public interface HighScoreSetterProvider extends HighScoreProvider {

    /**
     * Checks wether a given score can put into the high score list for a given car
     * type.
     * This is not the case if the list is full and the given score is too low to
     * replace any existing score.
     *
     * @param type  The car type with which the score was achieved.
     * @param score The score which the player achieved.
     * @return true, if the score can be added to the score list.
     */
    boolean checkScoreApplicable(CarType type, int score);

    /**
     * Adds a score to the score list of a given type.
     * This will throw an {@link IllegalArgumentException} if they cannot be
     * inserted
     * (see {@link #checkScoreApplicable(CarType, int)}}).
     *
     * @param type  The car type with which the score was achieved.
     * @param score The score which the player achieved.
     */
    void addScore(CarType type, Score score);
}
