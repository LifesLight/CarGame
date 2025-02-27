package edu.kit.cargame.menu.highscore;

/**
 * HighScoreMenuAction A set of actions dispatched by the high score menu that
 * need processing by the caller.
 */
public enum HighScoreMenuAction {
    /**
     * The player pressed the restart button, which should launch him back to a new
     * game with the same settings as before.
     */
    PLAY_AGAIN,
    /**
     * The player pressed the back to menu button, which should launch him back to
     * the mode selection screen.
     */
    RESET,
    /**
     * There is no action to take.
     */
    NONE
}
