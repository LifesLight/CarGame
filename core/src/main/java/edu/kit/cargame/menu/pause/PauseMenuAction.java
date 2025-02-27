package edu.kit.cargame.menu.pause;

/**
 * A list of actions that may be dispatched by the pause menu.
 */
public enum PauseMenuAction {
    /**
     * Continue with the game.
     */
    CONTINUE,
    /**
     * The player pressed the mute button. This should enable / disable the sound
     * globally.
     */
    TOGGLE_MUTE,
    /**
     * Go back to the mode seleciton menu.
     */
    TO_MODE,
    /**
     * Nothing happened. No action needs to be taken.
     */
    NONE
}
