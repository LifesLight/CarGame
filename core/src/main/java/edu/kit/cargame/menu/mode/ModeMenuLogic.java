package edu.kit.cargame.menu.mode;

import com.badlogic.gdx.Gdx;

import edu.kit.cargame.io.input.ActionTypes;

/**
 * ModeMenuLogic The logic of the ModeMenu. The two primary methods are
 * 1. consumeKey: called in each render cycle. Used to make the menu
 * interactive.
 * 2. pollWithReset: polls the action resulting from the user input. It also
 * resets the action.
 */
public class ModeMenuLogic {
    private ModeMenuButtonTypes activeButton;
    private ModeMenuAction nextAction;
    private boolean gameMuted;

    /**
     * Creates a new mode menu logic.
     */
    public ModeMenuLogic() {
        reset();
    }

    /**
     * Returns the button type.
     * @return The button type.
     */
    public ModeMenuButtonTypes getActiveButton() {
        return activeButton;
    }

    /**
     * Updates the menu state according to the input. Practically, this means:
     * 1. Go up/down if up/down are pressed (in terms of which button is selected).
     * 2. Go back to the start menu if back is pressed.
     * 3. Update the ModeMenuAction if enter is pressed, according to which button
     * is selected.
     * <p>
     * This screen is controllable by both users, if in multiplayer mode
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
    public ModeMenuAction pollWithReset() {
        ModeMenuAction tmp = nextAction;
        nextAction = ModeMenuAction.NONE;
        return tmp;
    }

    /**
     * Resets the logic to its default values.
     */
    public void reset() {
        activeButton = ModeMenuButtonTypes.SINGLE_PLAYER;
        nextAction = ModeMenuAction.NONE;
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

    private void handleUp() {
        ModeMenuButtonTypes[] values = ModeMenuButtonTypes.values();
        int currentIndex = activeButton.ordinal();
        activeButton = values[(currentIndex - 1 + values.length) % values.length];
    }

    private void handleDown() {
        ModeMenuButtonTypes[] values = ModeMenuButtonTypes.values();
        int currentIndex = activeButton.ordinal();
        activeButton = values[(currentIndex + 1) % values.length];
    }

    private void handleEnter() {
        switch (activeButton) {
            case EXIT:
                nextAction = ModeMenuAction.EXIT_GAME;
                break;
            case MULTI_PLAYER:
                nextAction = ModeMenuAction.GO_FORWARD_MULTI_PLAYER;
                break;
            case MUTE:
                nextAction = ModeMenuAction.TOGGLE_MUTE;
                break;
            case SINGLE_PLAYER:
                nextAction = ModeMenuAction.GO_FORWARD_SINGLE_PLAYER;
                break;
            default:
                break;
        }
    }

    private void handleBack() {
        nextAction = ModeMenuAction.GO_BACKWARDS;
    }
}
