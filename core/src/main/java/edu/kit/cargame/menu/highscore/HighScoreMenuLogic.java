package edu.kit.cargame.menu.highscore;

import edu.kit.cargame.common.settings.GameMode;
import edu.kit.cargame.game.playercar.CarType;
import edu.kit.cargame.io.score.GameOutcome;
import edu.kit.cargame.io.score.HighScoreSetterProvider;
import edu.kit.cargame.io.score.Score;
import edu.kit.cargame.io.input.ActionTypes;
import edu.kit.cargame.io.input.TextTypes;

import java.util.ArrayList;
import java.util.List;

/**
 * HighScoreMenuLogic The logic of the high score menu.
 * The primary task is to:
 * <ul>
 * <li> Determine whether one or multiple users achieved a new high score</li>
 * <li> If so, process the {@link TextTypes} for the users name.</li>
 * <li> Then give the player the option to select between restarting the game or
 * going back to the start menu.</li>
 * </ul>
 * This also includes processing the high score data to text format
 * representable by the menu.
 */
public class HighScoreMenuLogic {
    private static final String SCORE_FORMAT = " %s %d";
    private final GameOutcome outcome;
    private final HighScoreSetterProvider highScoreProvider;

    private String popupInput = "";
    private HighScoreMenuButtons button;
    private HighScoreMenuPopupPlayer popup;
    private HighScoreMenuAction action = HighScoreMenuAction.NONE;

    private State state;

    /**
     * The maximum length for the username.
     */
    public static final int MAX_NAME_LENGTH = 8;

    /**
     * Creates a new high score menu logic with given outcome and high score provider.
     *
     * @param outcome           The outcome off the game.
     * @param highScoreProvider The high score provider with write access.
     */
    public HighScoreMenuLogic(GameOutcome outcome, HighScoreSetterProvider highScoreProvider) {
        this.outcome = outcome;
        this.highScoreProvider = highScoreProvider;
        handleFirstPlayerState();
    }

    /**
     * Updates the menu state according to the input. Practically, this means:
     * If the popup is shown, it directly appends the typed characters to the name.
     * In this, back deletes the last character and enter continues to the next
     * state.
     * The next state is either going to the restart button or showing the popup
     * menu for the second player.
     *
     * @param action The user input. If in popup mode, enter will accept the string
     *               and back will delete the last character.
     *               In the normal view, left and right will switch between the
     *               buttons
     *               and Enter will set the action, depending on the current menu.
     */
    public void consumeInput(ActionTypes action) {
        switch (state) {
            case Buttons:
                handleButtonInput(action);
                break;
            case FirstPlayerPopup:
                handleFirstPlayerPopup(action);
                break;
            case SecondPlayerPopup:
                handleSecondPlayerPopup(action);
                break;
            default:
                break;
        }
    }

    /**
     * Consumes the text user input.
     * Does nothing if the current button is not
     * popup.
     * If a popup is shown, the key press will be translated to its meaning and
     * added onto the popup input,
     * as long as this would not increase the name length above
     * {@link #MAX_NAME_LENGTH}.
     *
     * @param input The text user input.
     * @see #getPopupInput()
     */
    public void consumeTextInput(TextTypes input) {
        if (button != HighScoreMenuButtons.POPUP || popupInput.length() >= MAX_NAME_LENGTH) {
            return;
        }

        popupInput = popupInput + input.toChar();
    }

    /**
     * Returns the text that the input field of the popup should contain.
     *
     * @return The text that the input field of the popup should contain.
     * @throws IllegalStateException If the popup is currently not shown.
     */
    public String getPopupInput() {
        if (popupInput == null) {
            throw new IllegalStateException("The popup is currently not shown");
        }
        return popupInput;
    }

    /**
     * Returns the active button.
     *
     * @return The active button.
     */
    public HighScoreMenuButtons getButton() {
        return button;
    }

    /**
     * Returns the popup player.
     *
     * @return The popup player.
     * @throws IllegalStateException If the popup is currently not shown.
     */
    public HighScoreMenuPopupPlayer getPopup() {
        if (popup == null) {
            throw new IllegalStateException("The popup is currently not shown");
        }
        return popup;
    }

    /**
     * Polls the current action. It also resets this action back to NONE.
     *
     * @return the current action.
     */
    public HighScoreMenuAction pollWithReset() {
        HighScoreMenuAction tmp = action;
        action = HighScoreMenuAction.NONE;
        return tmp;
    }

    /**
     * Returns the score of the players represented in the following format:
     * P(Player number if in multiplayer): (Score)
     *
     * @return The score of the players represented in the following format:
     * P(Player number if in multiplayer): (Score)
     */
    public List<String> getPlayerScores() {
        List<String> text = new ArrayList<>();
        text.add("Player score:");
        if (outcome.getMode() == GameMode.SINGLE_PLAYER) {
            text.add(formatScore(" P", outcome.getScoreP1()));
        } else {
            text.add(formatScore(" P1", outcome.getScoreP1()));
            text.add(formatScore(" P2", outcome.getScoreP2()));
        }
        return text;
    }

    /**
     * Returns the high scores represented in the following format:
     * Name of player achieving the score: Score
     *
     * @return The high scores represented in the following format:
     * Name of player achieving the score: Score
     */
    public List<String> getHighScores() {
        List<String> text = new ArrayList<>();
        text.add("High-scores:");
        List<Score> highScores = highScoreProvider.getHighestScores();
        for (Score score : highScores) {
            text.add(formatScore(score.name(), score.score()));
        }
        return text;
    }

    /**
     * Returns the high scores for each car represented in the following format:
     * Car Name: Score
     *
     * @return The high scores for each car represented in the following format:
     * Car Name: Score
     */
    public List<String> getCarHighScores() {
        List<String> text = new ArrayList<>();
        text.add("car High-scores:");
        for (CarType carType : CarType.values()) {
            highScoreProvider.getHighScore(carType).ifPresent(score -> text.add(formatScore(carType.name(), score.score())));
        }
        return text;
    }

    private String formatScore(String name, int score) {
        return String.format(SCORE_FORMAT, name, score);

    }

    private boolean firstPlayerHasHighScore() {
        return highScoreProvider.checkScoreApplicable(outcome.getCarTypeP1(), outcome.getScoreP1());
    }

    private boolean secondPlayerHasHighScore() {
        return highScoreProvider.checkScoreApplicable(outcome.getCarTypeP2(), outcome.getScoreP2());
    }

    private void handleFirstPlayerState() {
        if (firstPlayerHasHighScore()) {
            button = HighScoreMenuButtons.POPUP;
            popup = switch (outcome.getMode()) {
                case MULTI_PLAYER:
                    yield HighScoreMenuPopupPlayer.FIRST;
                case SINGLE_PLAYER:
                    yield HighScoreMenuPopupPlayer.SINGLE;
            };
            state = State.FirstPlayerPopup;
        } else {
            handleSecondPlayerState();
        }
    }

    private void handleSecondPlayerState() {
        popupInput = "";
        if (outcome.getMode() == GameMode.MULTI_PLAYER && secondPlayerHasHighScore()) {
            state = State.SecondPlayerPopup;
            button = HighScoreMenuButtons.POPUP;
            popup = HighScoreMenuPopupPlayer.SECOND;
        } else {
            setStateToButtons();
        }
    }

    private void setStateToButtons() {
        state = State.Buttons;
        button = HighScoreMenuButtons.RESTART;
        popup = null;
    }

    private enum State {
        FirstPlayerPopup,
        SecondPlayerPopup,
        Buttons
    }

    private void handleButtonInput(ActionTypes input) {
        switch (input) {
            case ENTER:
                switch (button) {
                    case BACK:
                        action = HighScoreMenuAction.RESET;
                        break;
                    case RESTART:
                        action = HighScoreMenuAction.PLAY_AGAIN;
                        break;
                    default:
                        throw new IllegalStateException(
                            "Internal logic error: Button was accepted, even though no button is currently pressed");
                }
                break;
            case LEFT:
            case RIGHT:
                switch (button) {
                    case BACK:
                        button = HighScoreMenuButtons.RESTART;
                        break;
                    case RESTART:
                        button = HighScoreMenuButtons.BACK;
                        break;
                    default:
                        throw new IllegalStateException(
                            "Internal logic error: Button should be changed, even though no button is currently pressed");
                }
                break;
            default:
                break;
        }
    }

    private void handleFirstPlayerPopup(ActionTypes action) {
        switch (action) {
            case BACK:
                if (!popupInput.isEmpty()) {
                    popupInput = popupInput.substring(0, popupInput.length() - 1);
                }
                break;
            case ENTER:
                if (!popupInput.isEmpty()) {
                    highScoreProvider.addScore(outcome.getCarTypeP1(),
                        new Score(popupInput, outcome.getScoreP1()));
                }
                handleSecondPlayerState();
                break;
            default:
                break;
        }

    }

    private void handleSecondPlayerPopup(ActionTypes action) {
        switch (action) {
            case BACK:
                if (!popupInput.isEmpty()) {
                    popupInput = popupInput.substring(0, popupInput.length() - 1);
                }
                break;
            case ENTER:
                if (!popupInput.isEmpty()) {
                    highScoreProvider.addScore(outcome.getCarTypeP2(),
                        new Score(popupInput, outcome.getScoreP2()));
                }
                setStateToButtons();
                break;
            default:
                break;
        }
    }
}
