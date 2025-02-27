package edu.kit.cargame.io.score;

import edu.kit.cargame.game.playercar.CarType;
import edu.kit.cargame.common.settings.GameMode;

/**
 * GameOutcome Holds the result of a game.
 * This information is then used by the High Score menu to show
 * the scores of the players and to determine whether it presents
 * a new high score.
 *
 */
public final class GameOutcome {
    private final GameMode mode;
    private final CarType carTypeP1;

    private final CarType carTypeP2;
    private final int scoreP1;
    private final int scoreP2;

    private GameOutcome(GameMode mode, CarType carTypeP1, CarType carTypeP2, int scoreP1, int scoreP2) {
        this.mode = mode;
        this.carTypeP1 = carTypeP1;
        this.carTypeP2 = carTypeP2;
        this.scoreP1 = scoreP1;
        this.scoreP2 = scoreP2;
    }

    /**
     * Creates a new outcome of a single player game with a given car type and
     * achieved
     * score.
     *
     * @param carType The car type used by the only player.
     * @param score   The score achieved by the only player.
     * @return A new outcome of a single player game with a given car type and
     *         achieved
     *         score.
     */
    public static GameOutcome createSinglePlayerResult(CarType carType, int score) {
        return new GameOutcome(GameMode.SINGLE_PLAYER, carType, null, score, -1);
    }

    /**
     * Creates a new outcome of a multi-player game with given car types and
     * achieved
     * scores.
     *
     * @param carTypeP1 The car type used by the first.
     * @param carTypeP2 The car type used by the second.
     * @param scoreP1   The score achieved by the first player.
     * @param scoreP2   The score achieved by the second player.
     * @return A new outcome of a multi-player game with given car types and
     *         achieved
     *         scores.
     */
    public static GameOutcome createMultiPlayerResult(CarType carTypeP1, CarType carTypeP2, int scoreP1, int scoreP2) {
        return new GameOutcome(GameMode.MULTI_PLAYER, carTypeP1, carTypeP2, scoreP1, scoreP2);
    }

    /**
     * Returns one if the first player won or 2 if the second player won.
     * Throws an
     * IllegalStateException if the game mode was single-player.
     *
     * @return One if the first player won or 2 if the second player won.
     * @throws IllegalStateException if the game mode was single-player.
     */
    public int returnWinner() {
        if (mode == GameMode.SINGLE_PLAYER) {
            throw new IllegalStateException("there is no winner in single player mode");
        }
        return scoreP1 > scoreP2 ? 1 : 2;
    }

    /**
     * Returns the game mode of the game.
     *
     * @return The game mode of the game.
     */
    public GameMode getMode() {
        return mode;
    }

    /**
     * Returns the car type of the first / only player.
     *
     * @return The car type of the first / only player.
     */
    public CarType getCarTypeP1() {
        return carTypeP1;
    }

    /**
     * Returns the car type of the second player. Throws an
     * IllegalStateException if the game mode was single-player.
     *
     * @return The car type of the second player. Throws an
     * @throws IllegalStateException if the game mode was single-player.
     */
    public CarType getCarTypeP2() {
        if (mode == GameMode.SINGLE_PLAYER) {
            throw new IllegalStateException("there is no second player in single player mode");
        }
        return carTypeP2;
    }

    /**
     * Returns the score of the first / only player.
     *
     * @return The score of the first / only player.
     */
    public int getScoreP1() {
        return scoreP1;
    }

    /**
     * Returns the score of the second player. Throws an
     * @throws IllegalStateException if the game mode was single-player.
     *
     * @return The score of the second player. Throws an
     *         IllegalStateException if the game mode was single-player.
     */
    public int getScoreP2() {
        if (mode == GameMode.SINGLE_PLAYER) {
            throw new IllegalStateException("there is no second player in single player mode");
        }
        return scoreP2;
    }
}
