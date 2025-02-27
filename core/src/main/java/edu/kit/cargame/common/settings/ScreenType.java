package edu.kit.cargame.common.settings;

/**
 * An enum that lists all screens available.
 */
public enum ScreenType {
    /**
     * The Start menu. Shown directly after startup
     */
    START,
    /**
     * The mode selection menu. Allows setting a game mode.
     */
    MODE,
    /**
     * The car selection menu. Allows the player/ players to select a car with their color. After this menu,
     * the game should be started
     */
    CAR,
    /**
     * The game itself.
     */
    GAME,
    /**
     * The high score menu.
     */
    HIGH_SCORE,
    /**
     * The pause screen.
     */
    PAUSE

}
