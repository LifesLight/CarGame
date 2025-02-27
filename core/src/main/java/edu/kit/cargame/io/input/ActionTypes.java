package edu.kit.cargame.io.input;

/**
 * ActionTypes An abstraction above the input of a user.
 * As multiple button presses can lead to the same input (e.g., w and arrow up),
 * they are combined
 * into one logical unit.
 */
public enum ActionTypes {
    /**
     * Up-press. Used in menus to select the element above or to drive up.
     */
    UP,
    /**
     * Down-press. Used in menus to select the element below or to drive down.
     */
    DOWN,
    /**
     * Left-press. Only used in menus to select the previous car or color.
     */
    LEFT,
    /**
     * Right-press. Only used in menus to select the next car or color.
     */
    RIGHT,
    /**
     * Enter-press. Used in menus to press a buttons and in game to boost.
     */
    ENTER,
    /**
     * Back-press. Used in menus to go back to a menu or button and in game to pause.
     */
    BACK
}
