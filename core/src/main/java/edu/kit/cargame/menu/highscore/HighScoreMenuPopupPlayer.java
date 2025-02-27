package edu.kit.cargame.menu.highscore;

/**
 * HighScoreMenuPopupPlayer The popup that asks for the user's name may need to
 * signal which player
 * needs to input their name. This enum contains a set of options it may need to
 * show.
 */
public enum HighScoreMenuPopupPlayer {
    /**
     * If in single-player, as only one user exists, no extra field needs to show
     * which player is meant.
     */
    SINGLE,
    /**
     * The first player is meant. P1 should be shown in the popup.
     */
    FIRST,
    /**
     * The second player is meant. P2 should be shown in the popup.
     */
    SECOND
}
