package edu.kit.cargame.menu.pause;

import edu.kit.cargame.io.input.ActionTypes;

/**
 * The pause menus logic. Handles the key presses of the user and the actions needed to be taken.
 */
public class PauseMenuLogic {
    private PauseMenuButtonTypes activeButton;
    private PauseMenuAction nextAction;
    private boolean gameMuted;

    /**
     * Creates a new pause menu logic.
     */
    public PauseMenuLogic() {
        reset();
    }

    /**
     * Returns the button type.
     * @return The button type.
     */
    public PauseMenuButtonTypes getActiveButton() {
        return activeButton;
    }

    /**
     * Updates the menu state according to the input. Practically, this means:
     * <ul>
     * <li>Go up/down if up/down are pressed (in terms of which button is selected).</li>
     * <li>Go back to the game if back is pressed.</li>
     * <li>Update the PauseMenuAction if enter is pressed, according to which button</li>
     * is selected.
     * </ul>
     * <p>
     * This screen is controllable by both users, if in multiplayer pause
     *
     * @param keypress The input event the logic should consider.
     */
    public void consumeKey(ActionTypes keypress) {
        switch (keypress) {
            case UP:
                handleUp();
                break;
            case DOWN:
                handleDown();
                break;
            case BACK:
                handleBack();
                break;
            case ENTER:
                handleEnter();
                break;
            default:
                break;

        }

    }

    /**
     * Polls the current action. It also resets this action back to NONE.
     *
     * @return the current action.
     */
    public PauseMenuAction pollWithReset() {
        PauseMenuAction tmp = nextAction;
        nextAction = PauseMenuAction.NONE;
        return tmp;
    }


    /**
     * Returns whether the game is muted.
     *
     * @return Whether the game is muted;
     */
    public boolean getGameMuted() {
        return gameMuted;
    }

    /**
     * Sets whether the game is muted.
     *
     * @param gameMuted Whether the game is muted;
     */
    public void setGameMuted(boolean gameMuted) {
        this.gameMuted = gameMuted;
    }

    private void reset() {
        activeButton = PauseMenuButtonTypes.CONTINUE;
        nextAction = PauseMenuAction.NONE;
    }

    private void handleUp() {
        PauseMenuButtonTypes[] values = PauseMenuButtonTypes.values();
        int currentIndex = activeButton.ordinal();
        activeButton = values[(currentIndex - 1 + values.length) % values.length];
    }

    private void handleDown() {
        PauseMenuButtonTypes[] values = PauseMenuButtonTypes.values();
        int currentIndex = activeButton.ordinal();
        activeButton = values[(currentIndex + 1) % values.length];
    }

    private void handleEnter() {
        // currently, each button is directly mapped to its action at the same place.
        nextAction = PauseMenuAction.values()[activeButton.ordinal()];
    }

    private void handleBack() {
        nextAction = PauseMenuAction.CONTINUE;
    }
}
