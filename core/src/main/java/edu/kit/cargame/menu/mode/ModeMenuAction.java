package edu.kit.cargame.menu.mode;

/**
 * ModeMenuAction hosts a list of actions used by the main loop to determine the
 * actions it should take.
 */
public enum ModeMenuAction {
    /**
     * The player selected the single player button. The CarMenu screen should be
     * opened. The Logic of the CarMenu screen should be reset with the
     * GameMode.SINGLE_PLAYER option.
     */
    GO_FORWARD_SINGLE_PLAYER,
    /**
     * The player selected the multi-player button.
     * The CarMenu screen should be opened.
     * The Logic of the CarMenu screen should be reset with the
     * GameMode.MULTI_PLAYER option.
     */
    GO_FORWARD_MULTI_PLAYER,
    /**
     * The player pressed the mute button. This should enable / disable the sound
     * globally.
     */
    TOGGLE_MUTE,
    /**
     * The player pressed the back button. The StartMenu should be shown.
     */
    GO_BACKWARDS,
    /**
     * The player pressed the exit game button. The game should close back to
     * desktop.
     */
    EXIT_GAME,
    /**
     * Nothing happened. No action needs to be taken.
     */
    NONE
}
