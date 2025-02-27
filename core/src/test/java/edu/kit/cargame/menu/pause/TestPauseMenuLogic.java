package edu.kit.cargame.menu.pause;

import org.junit.jupiter.api.Test;

import edu.kit.cargame.io.input.ActionTypes;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TestPauseMenuLogic {
    @Test
    public void simpleSinglePlayer() {
        PauseMenuLogic logic = new PauseMenuLogic();
        logic.consumeKey(ActionTypes.ENTER);
        assertEquals(logic.pollWithReset(), PauseMenuAction.CONTINUE);
    }

    @Test
    public void reset(){
        PauseMenuLogic logic = new PauseMenuLogic();
        logic.consumeKey(ActionTypes.ENTER);
        logic.pollWithReset();
        assertEquals(logic.pollWithReset(),PauseMenuAction.NONE);
    }

    @Test
    public void simpleMute() {
        PauseMenuLogic logic = new PauseMenuLogic();
        logic.consumeKey(ActionTypes.DOWN);
        logic.consumeKey(ActionTypes.ENTER);
        assertEquals(logic.pollWithReset(), PauseMenuAction.TOGGLE_MUTE);
    }

    @Test
    public void doubleMute() {
        PauseMenuLogic logic = new PauseMenuLogic();
        logic.consumeKey(ActionTypes.DOWN);
        logic.consumeKey(ActionTypes.ENTER);
        assertEquals(logic.pollWithReset(), PauseMenuAction.TOGGLE_MUTE);
        logic.consumeKey(ActionTypes.ENTER);
        assertEquals(logic.pollWithReset(), PauseMenuAction.TOGGLE_MUTE);
    }

    @Test
    public void simpleUpDown() {
        PauseMenuLogic logic = new PauseMenuLogic();
        PauseMenuButtonTypes originalButton = logic.getActiveButton();
        logic.consumeKey(ActionTypes.UP);
        logic.consumeKey(ActionTypes.DOWN);
        assertEquals(logic.getActiveButton(), originalButton);
    }

    @Test
    public void simpleDownUp() {
        PauseMenuLogic logic = new PauseMenuLogic();
        PauseMenuButtonTypes originalButton = logic.getActiveButton();
        // we test up down and down up separately as up down wraps, while down up does
        // not.
        logic.consumeKey(ActionTypes.DOWN);
        logic.consumeKey(ActionTypes.UP);
        assertEquals(logic.getActiveButton(), originalButton);
    }

    @Test
    public void simpleBack() {
        PauseMenuLogic logic = new PauseMenuLogic();
        logic.consumeKey(ActionTypes.UP);
        logic.consumeKey(ActionTypes.UP);
        logic.consumeKey(ActionTypes.BACK);

        assertEquals(logic.pollWithReset(), PauseMenuAction.CONTINUE);
    }
}
