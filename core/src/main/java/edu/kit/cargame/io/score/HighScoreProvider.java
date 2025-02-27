package edu.kit.cargame.io.score;

import java.util.List;
import java.util.Optional;

import edu.kit.cargame.game.playercar.CarType;

/**
 * HighScoreProvider
 * Provides the ability to read the highest scores archived, both on a per car
 * and total basis.
 */
public interface HighScoreProvider {
    /**
     * Returns the highest score achieved with a given car type. Returns
     * Optional.empty if no score was ever achieved with that car.
     *
     * @param type The car type for which the highest score should be returned.
     * @return The highest score achieved with a given car type. Returns
     *         Optional.empty if no score was ever achieved with that car.
     */
    Optional<Score> getHighScore(CarType type);

    /**
     * Returns a list of the highest scores achieved, no matter with which car.
     * @return A list of the highest scores achieved, no matter with which car.
     */
    List<Score> getHighestScores();
}
