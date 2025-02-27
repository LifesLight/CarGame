package edu.kit.cargame.menu.car;

/**
 * CarMenuAction The actions sent by the car selection menu logic.
 * Those are going back to the mode selection menu and going forward, starting the game.
 */
public enum CarMenuAction {
    /**
     * A player has canceled their selection.
     * The game should go back to the previous menu
     */
    BACK,
    /**
     * All players selected a car and color. The game can start.
     */
    START_GAME,
    /**
     * No action needing intervention happened.
     */
    NONE
}
