package edu.kit.cargame.menu.highscore;

import org.junit.jupiter.api.Test;

import edu.kit.cargame.common.settings.GameMode;
import edu.kit.cargame.game.playercar.CarType;
import edu.kit.cargame.io.input.ActionTypes;
import edu.kit.cargame.io.input.TextTypes;
import edu.kit.cargame.io.score.GameOutcome;
import edu.kit.cargame.io.score.HighScoreSetterProvider;
import edu.kit.cargame.io.score.ScoreList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TestHighScoreMenuLogic {
    private static final int SINGLE_PLAYER_SCORE = 1000;
    private GameOutcome createSinglePlayerOutcome(){
        return GameOutcome.createSinglePlayerResult(CarType.NORMAL,SINGLE_PLAYER_SCORE);
    }

    private HighScoreSetterProvider createHighScoreProvider(){
        return new ScoreList();
    }
    private HighScoreMenuLogic createSinglePlayerLogic(){
        return new HighScoreMenuLogic(createSinglePlayerOutcome(),createHighScoreProvider());
    }

    @Test
    void singlePlayerFormat(){
        HighScoreMenuLogic logic = createSinglePlayerLogic();
        logic.consumeTextInput(TextTypes.A);
        logic.consumeInput(ActionTypes.ENTER);
        assertTrue(logic.getPlayerScores().get(1).contains(String.valueOf(SINGLE_PLAYER_SCORE)));
    }

    @Test
    void buttons(){
        HighScoreMenuLogic logic = createSinglePlayerLogic();
        assertEquals(HighScoreMenuAction.NONE,logic.pollWithReset());
        logic.consumeInput(ActionTypes.ENTER);
        logic.consumeInput(ActionTypes.ENTER);
        if (logic.getButton() == HighScoreMenuButtons.RESTART) {
            assertEquals(HighScoreMenuAction.PLAY_AGAIN,logic.pollWithReset());
        }else{
            assertEquals(HighScoreMenuAction.RESET,logic.pollWithReset());
        }
        HighScoreMenuButtons button = logic.getButton();
        logic.consumeInput(ActionTypes.LEFT);
        assertTrue(!button.equals(logic.getButton()));

    }
}
