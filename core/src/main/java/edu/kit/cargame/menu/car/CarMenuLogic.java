package edu.kit.cargame.menu.car;

import edu.kit.cargame.io.input.ActionTypes;
import edu.kit.cargame.game.playercar.CarSettings;
import edu.kit.cargame.game.playercar.CarType;
import edu.kit.cargame.game.playercar.ColorOptions;
import edu.kit.cargame.common.settings.GameMode;
import edu.kit.cargame.common.settings.GameSettings;

/**
 * CarMenuLogic The car menu logic handles the user input for the car menu.
 * This includes keeping track of what car and color each player has selected
 * and whether an action occurred which needs intervention by the game (e.g.,
 * players finished, game can start).
 */
public class CarMenuLogic {
    private GameMode mode;
    private CarMenuAction action = CarMenuAction.NONE;
    /**
     * Frames till the start game action will be set.
     * If -1, deactivate the counter.
     */
    private float timeTillStart = -1;

    /**
     * The time it takes between the players being ready and the game starting.
     */
    public static final float START_TIMER_DURATION = 3;

    private CarMenuPlayer player1;
    private CarMenuPlayer player2;

    /**
     * This resets the car menu logic to its default state.
     *
     * @param mode The game mode, determining whether one menu or two menus need to
     *             be drawn.
     */
    public void resetToMode(GameMode mode) {
        action = CarMenuAction.NONE;
        this.mode = mode;
        timeTillStart = -1;
        player1 = new CarMenuPlayer(
                CarType.NORMAL,
                ColorOptions.BLUE,
                CarMenuButtonTypes.CAR);
        if (mode == GameMode.MULTI_PLAYER) {
            player2 = new CarMenuPlayer(
                    CarType.NORMAL,
                    ColorOptions.BLUE,
                    CarMenuButtonTypes.CAR);
        } else {
            player2 = null;
        }

    }

    /**
     * Returns all information regarding the first player.
     *
     * @return All information regarding the first player.
     */
    public CarMenuPlayer getPlayer1() {
        return player1;
    }

    /**
     * Returns all information regarding the second player.
     * If there is no second player, an IllegalStateException will be thrown.
     *
     * @return All information regarding the second player.
     * @throws IllegalStateException if there is no second player.
     */
    public CarMenuPlayer getPlayer2() {
        if (mode == GameMode.SINGLE_PLAYER) {
            throw new IllegalStateException("there is no player 2 in single player");
        }
        return player2;
    }

    /**
     * Returns the game mode set during the last reset.
     *
     * @return The game mode set during the last reset.
     */
    public GameMode getMode() {
        return mode;
    }

    /**
     * Returns The time till the game starts.
     * This will be negative (-1 to be exact) if at least one player is not ready.
     *
     * @return The time till the game starts.
     */
    public float getTimeTillStart() {
        return timeTillStart;
    }

    /**
     * Polls the current action. It also resets this action back to NONE.
     *
     * @return the current action.
     */
    public CarMenuAction pollWithReset() {
        CarMenuAction tmp = action;
        action = CarMenuAction.NONE;
        return tmp;
    }

    /**
     * Updates the menu state according to the input. Practically, this means:
     * <ul>
     *   <li>Go up/down if up/down are pressed (changes the selected button).</li>
     *   <li>Go back to the start menu if the back button is pressed.</li>
     *   <li>Update the ModeMenuAction if the enter button is pressed, based on the selected button.</li>
     * </ul>
     * <p>
     * This menu will process the input as a keyboard input, meaning it will
     *  control player 1.
     *
     * @param keypress The input event the logic should consider.
     */
    public void consumeKeyboard(ActionTypes keypress) {
        consumeKey(keypress, player1);
    }

    /**
     * Updates the menu state according to the input. Practically, this means:
     * <ul>
     *   <li>Go up/down if up/down are pressed (changes the selected button).</li>
     *   <li>Go back to the start menu if the back button is pressed.</li>
     *   <li>Update the ModeMenuAction if the enter button is pressed, based on the selected button.</li>
     * </ul>
     * <p>
     * This menu processes input as a controller input, meaning it will control:
     * <ul>
     *   <li>Player 1 in single-player mode.</li>
     *   <li>Player 2 in multi-player mode.</li>
     * </ul>
     *
     * @param keypress The input event the logic should consider.
     */

    public void consumeController(ActionTypes keypress) {
        consumeKey(keypress, controllerPlayer());
    }


    /**
     * Tests, whether all conditions are met to start the next game.
     * This should be polled once per render.
     * If the game is ready, the {@link CarMenuAction#START_GAME} action will be set to let the game know
     * it should start.
     *
     * @param delta the time since last called. this is needed to calculate how much
     *              time is left till the game starts
     */
    public void testStartGame(float delta) {
        boolean startGame = (player1.getButton() == CarMenuButtonTypes.NONE)
                && (mode == GameMode.SINGLE_PLAYER || player2.getButton() == CarMenuButtonTypes.NONE);
        if (startGame) {
            if (timeTillStart < 0) {
                timeTillStart = Math.max(START_TIMER_DURATION - delta,0);
            } else {
                timeTillStart = Math.max(timeTillStart - delta, 0);
            }
        } else {
            timeTillStart = -1;
        }
        if (timeTillStart == 0) {
            action = CarMenuAction.START_GAME;
        }
    }

    /**
     * Generates the game settings based on the current selection of each player.
     *
     * @return The game settings generated.
     */
    public GameSettings generateGameSettings() {
        return switch (mode) {
            case SINGLE_PLAYER:
                yield GameSettings.createSinglePlayer(new CarSettings(player1.getCar(), player1.getColor()));
            case MULTI_PLAYER:
                yield GameSettings.createMultiPlayer(new CarSettings(player1.getCar(), player1.getColor()),
                        new CarSettings(player2.getCar(), player2.getColor()));
        };
    }

    private void consumeKey(ActionTypes keypress, CarMenuPlayer player) {
        switch (keypress) {
            case UP:
            case DOWN:
                handleDownUp(player);
                break;
            case LEFT:
                handleLeft(player);
                break;
            case RIGHT:
                handleRight(player);
                break;
            case BACK:
                handleBack(player);
                break;
            case ENTER:
                handleEnter(player);
                break;
            default:
                break;

        }
    }

    private CarMenuPlayer controllerPlayer() {
        return switch (mode) {
            case MULTI_PLAYER:
                yield player2;
            case SINGLE_PLAYER:
                yield player1;
        };
    }

    private void handleBack(CarMenuPlayer player) {
        switch (player.getButton()) {
            case CAR:
                action = CarMenuAction.BACK;
                break;
            case COLOR:
                player.setButton(CarMenuButtonTypes.CAR);
                break;
            case NONE:
                player.setButton(CarMenuButtonTypes.COLOR);
                break;
            default:
                break;
        }
    }

    private void handleEnter(CarMenuPlayer player) {
        if (player.getButton() == CarMenuButtonTypes.CAR) {
            player.setButton(CarMenuButtonTypes.COLOR);
        } else {
            player.setButton(CarMenuButtonTypes.NONE);
        }
    }

    private void handleRight(CarMenuPlayer player) {
        int currentIndex;
        switch (player.getButton()) {
            case CAR:
                CarType[] carTypes = CarType.values();
                currentIndex = player.getCar().ordinal();
                player.setCar(carTypes[(currentIndex + 1 + carTypes.length) % carTypes.length]);
                break;
            case COLOR:
                ColorOptions[] colors = ColorOptions.values();
                currentIndex = player.getColor().ordinal();
                player.setColor(colors[(currentIndex + 1 + colors.length) % colors.length]);
                break;
            case NONE:
                break;
        }
    }

    private void handleLeft(CarMenuPlayer player) {
        int currentIndex;
        switch (player.getButton()) {
            case CAR:
                CarType[] carTypes = CarType.values();
                currentIndex = player.getCar().ordinal();
                player.setCar(carTypes[(currentIndex - 1 + carTypes.length) % carTypes.length]);
                break;
            case COLOR:
                ColorOptions[] colors = ColorOptions.values();
                currentIndex = player.getColor().ordinal();
                player.setColor(colors[(currentIndex - 1 + colors.length) % colors.length]);
                break;
            case NONE:
                break;
        }
    }

    private void handleDownUp(CarMenuPlayer player) {
        switch (player.getButton()) {
            case CAR:
                player.setButton(CarMenuButtonTypes.COLOR);
                break;
            case COLOR:
                player.setButton(CarMenuButtonTypes.CAR);
                break;
            case NONE:
                break;
        }
    }

}
