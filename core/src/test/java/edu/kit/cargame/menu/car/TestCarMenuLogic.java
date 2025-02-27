package edu.kit.cargame.menu.car;

import org.junit.jupiter.api.Test;

import edu.kit.cargame.common.settings.GameMode;
import edu.kit.cargame.io.input.ActionTypes;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TestCarMenuLogic {
    @Test
    void simpleSinglePlayer() {
        CarMenuLogic logic = new CarMenuLogic();
        logic.resetToMode(GameMode.SINGLE_PLAYER);
        assertEquals(
                logic.getPlayer1().getButton(), CarMenuButtonTypes.CAR);
        logic.consumeKeyboard(ActionTypes.ENTER);
        assertEquals(
                logic.getPlayer1().getButton(), CarMenuButtonTypes.COLOR);
    }

    @Test
    void simpleSinglePlayerController() {
        CarMenuLogic logic = new CarMenuLogic();
        logic.resetToMode(GameMode.SINGLE_PLAYER);
        logic.consumeController(ActionTypes.ENTER);
        assertEquals(
                logic.getPlayer1().getButton(), CarMenuButtonTypes.COLOR);
    }

    @Test
    void simpleMultiPlayer() {
        CarMenuLogic logic = new CarMenuLogic();
        logic.resetToMode(GameMode.MULTI_PLAYER);

        assertEquals(
                logic.getPlayer1().getButton(), CarMenuButtonTypes.CAR);
        assertEquals(
                logic.getPlayer2().getButton(), CarMenuButtonTypes.CAR);

        logic.consumeKeyboard(ActionTypes.ENTER);
        assertEquals(
                logic.getPlayer1().getButton(), CarMenuButtonTypes.COLOR);

        logic.consumeController(ActionTypes.ENTER);
        assertEquals(
                logic.getPlayer2().getButton(), CarMenuButtonTypes.COLOR);
    }

    @Test
    void startGameSinglePlayer() {
        CarMenuLogic logic = new CarMenuLogic();
        logic.resetToMode(GameMode.SINGLE_PLAYER);
        // select car and color
        logic.consumeKeyboard(ActionTypes.ENTER);
        logic.consumeKeyboard(ActionTypes.ENTER);
        logic.consumeKeyboard(ActionTypes.ENTER);
        logic.testStartGame(10000);// 10 sec should be enough to start;
        logic.testStartGame(10000);// 10 sec should be enough to start;
        assertEquals(logic.pollWithReset(), CarMenuAction.START_GAME);
    }

    @Test
    void singlePlayerCarSelection(){
        CarMenuLogic logic = new CarMenuLogic();
        logic.resetToMode(GameMode.SINGLE_PLAYER);

        assertEquals(0,logic.getPlayer1().getCar().ordinal());
        logic.consumeKeyboard(ActionTypes.RIGHT);
        assertEquals(1,logic.getPlayer1().getCar().ordinal());
    }
}
