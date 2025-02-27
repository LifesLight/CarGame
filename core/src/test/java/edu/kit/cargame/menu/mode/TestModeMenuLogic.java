package edu.kit.cargame.menu.mode;

import org.junit.jupiter.api.Test;

import edu.kit.cargame.io.input.ActionTypes;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TestModeMenuLogic {
    @Test
    public void simpleSinglePlayer() {
        ModeMenuLogic logic = new ModeMenuLogic();
        logic.consumeKey(ActionTypes.ENTER);
        assertEquals(logic.pollWithReset(), ModeMenuAction.GO_FORWARD_SINGLE_PLAYER);
    }

    @Test
    public void reset(){
        ModeMenuLogic logic = new ModeMenuLogic();
        logic.consumeKey(ActionTypes.ENTER);
        logic.pollWithReset();
        assertEquals(logic.pollWithReset(),ModeMenuAction.NONE);
    }

    @Test
    public void simpleMute() {
        ModeMenuLogic logic = new ModeMenuLogic();
        logic.consumeKey(ActionTypes.DOWN);
        logic.consumeKey(ActionTypes.DOWN);
        logic.consumeKey(ActionTypes.ENTER);
        assertEquals(logic.pollWithReset(), ModeMenuAction.TOGGLE_MUTE);
    }

    @Test
    public void doubleMute() {
        ModeMenuLogic logic = new ModeMenuLogic();
        logic.consumeKey(ActionTypes.DOWN);
        logic.consumeKey(ActionTypes.DOWN);
        logic.consumeKey(ActionTypes.ENTER);
        assertEquals(logic.pollWithReset(), ModeMenuAction.TOGGLE_MUTE);
        logic.consumeKey(ActionTypes.ENTER);
        assertEquals(logic.pollWithReset(), ModeMenuAction.TOGGLE_MUTE);
    }

    @Test
    public void simpleUpDown() {
        ModeMenuLogic logic = new ModeMenuLogic();
        ModeMenuButtonTypes originalButton = logic.getActiveButton();
        logic.consumeKey(ActionTypes.UP);
        logic.consumeKey(ActionTypes.DOWN);
        assertEquals(logic.getActiveButton(), originalButton);
    }

    @Test
    public void simpleDownUp() {
        ModeMenuLogic logic = new ModeMenuLogic();
        ModeMenuButtonTypes originalButton = logic.getActiveButton();
        // we test up down and down up separately as up down wraps, while down up does
        // not.
        logic.consumeKey(ActionTypes.DOWN);
        logic.consumeKey(ActionTypes.UP);
        assertEquals(logic.getActiveButton(), originalButton);
    }

    @Test
    public void simpleBack() {
        ModeMenuLogic logic = new ModeMenuLogic();
        logic.consumeKey(ActionTypes.UP);
        logic.consumeKey(ActionTypes.UP);
        logic.consumeKey(ActionTypes.BACK);

        assertEquals(logic.pollWithReset(), ModeMenuAction.GO_BACKWARDS);
    }
}
