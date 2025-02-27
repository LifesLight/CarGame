package edu.kit.cargame.common.settings;

import edu.kit.cargame.game.playercar.CarSettings;

/**
 * Contains the information needed to start a game.
 */
public final class GameSettings {
    private final GameMode gameMode;
    private final CarSettings player1;
    private final CarSettings player2;

    private GameSettings(GameMode gameMode, CarSettings player1, CarSettings player2) {
        this.gameMode = gameMode;
        this.player1 = player1;
        this.player2 = player2;
    }

    /**
     * Creates new game setting for single player mode.
     *
     * @param player the car settings for the player
     * @return new game settings for single player mode.
     */
    public static GameSettings createSinglePlayer(CarSettings player) {
        return new GameSettings(GameMode.SINGLE_PLAYER, player, null);
    }

    /**
     * Creates new game setting for two players.
     *
     * @param player1 the car settings for the first player
     * @param player2 the car settings for the second player
     * @return new game settings for multi-player mode.
     */
    public static GameSettings createMultiPlayer(CarSettings player1, CarSettings player2) {
        return new GameSettings(GameMode.MULTI_PLAYER, player1, player2);
    }

    /**
     * Returns whether one or two players play.
     *
     * @return Whether one or two players play.
     */
    public GameMode getGameMode() {
        return gameMode;
    }

    /**
     * Returns the first player.
     *
     * @return The only player in single-player mode is considered the first player.
     */
    public CarSettings getPlayer1() {
        return player1;
    }

    /**
     * Returns the second player. Will throw an IllegalStateException if the
     * settings are for single player
     *
     * @return the second player.
     * @throws IllegalStateException if the settings are for single player
     */
    public CarSettings getPlayer2() {
        if (player2 == null) {
            throw new IllegalStateException("There is no second player in single player mode");
        }
        return player2;
    }
}
