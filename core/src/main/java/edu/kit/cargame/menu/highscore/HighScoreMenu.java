package edu.kit.cargame.menu.highscore;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import edu.kit.cargame.io.input.ActionTypes;
import edu.kit.cargame.io.input.UserInput;
import edu.kit.cargame.io.input.implement.Combined;
import edu.kit.cargame.io.input.implement.Keyboard;
import edu.kit.cargame.io.input.implement.KeyboardAndController;
import edu.kit.cargame.io.input.implement.Text;
import edu.kit.cargame.io.score.GameOutcome;
import edu.kit.cargame.io.score.HighScoreSetterProvider;
import edu.kit.cargame.io.view.Scale;
import edu.kit.cargame.io.view.renderer.TextRenderer;
import edu.kit.cargame.io.view.renderer.Util;
import edu.kit.cargame.menu.Menu;

import java.util.ArrayList;
import java.util.List;

/**
 * StartMenu The start menu is shown on startup.
 * It just contains the game logo as well as the text "Press Any Key".
 * The polling method provided for the game returns true as soon as any key was
 * pressed.
 */
public class HighScoreMenu extends Menu<HighScoreMenuAction> {
    private Stage stage;

    private List<Texture> texturesMenu = new ArrayList<>();
    private List<Texture> texturesPopup = new ArrayList<>();
    private final UserInput<ActionTypes> userInput = new KeyboardAndController();
    private final Text userTextInput = new Text();

    private TextRenderer textRendererNormal;
    private TextRenderer textRendererLarge;
    private final HighScoreSetterProvider highScore;
    private HighScoreMenuLogic logic;

    /**
     * Creates a new high score menu with given high score provider.
     *
     * @param highScore The high score provider with write access.
     */
    public HighScoreMenu(HighScoreSetterProvider highScore) {
        this.highScore = highScore;

    }

    @Override
    public void show() {
        generateStages();
        loadTextures();
        userInput.updateJustPressed();
        userTextInput.updateJustPressed();
    }

    private void generateStages() {
        // full hd
        if (Gdx.graphics.getWidth() <= 1920 || Gdx.graphics.getHeight() <= 1080) {
            stage = new Stage(new FitViewport(1920, 1080));
        } else {
            stage = new Stage(new ScreenViewport());
        }
        textRendererNormal = new TextRenderer(stage, Scale.NORMAL);
        textRendererLarge = new TextRenderer(stage, Scale.LARGE);

    }

    /**
     * Sets the game outcome and resets the menu logic back to start.
     * This should be called before this screen is switched to.
     *
     * @param outcome The game outcome of the last game.
     */
    public void setGameOutcomeAndReset(GameOutcome outcome) {
        logic = new HighScoreMenuLogic(outcome, highScore);
    }

    private void loadTextures() {
        texturesMenu = Util.loadNTextures("high_score_menu/menu%d.png", 3);
        texturesPopup = Util.loadNTextures("high_score_menu/popup%d.png", 3);
    }

    @Override
    public void resize(int width, int height) {
        generateStages();
        //stage = new Stage(new ScreenViewport());
    }

    @Override
    public void dispose() {
    }

    @Override
    protected void processUserInput() {
        userInput.justPressedFirstExecute(logic::consumeInput);
        userTextInput.justPressedFirstExecute(logic::consumeTextInput);
    }

    @Override
    public HighScoreMenuAction pollAction() {
        return logic.pollWithReset();
    }

    @Override
    protected Stage generateContent() {
        stage.clear();
        int currentMenu = logic.getButton().ordinal();
        drawTextureRelative(texturesMenu.get(currentMenu), -BANNER_WIDTH / 2, -BANNER_HEIGHT / 2);
        int offsetLeft = 0;
        for (String text : logic.getPlayerScores()) {
            drawScoreText(text, scoreTextLeftOffsetX(), offsetLeft);
            offsetLeft += 1;
        }

        offsetLeft += 1;
        for (String text : logic.getHighScores()) {
            drawScoreText(text, scoreTextLeftOffsetX(), offsetLeft);
            offsetLeft += 1;
        }

        int offsetRight = 0;
        for (String text : logic.getCarHighScores()) {
            drawScoreText(text, scoreTextRightOffsetX(), offsetRight);
            offsetRight += 1;
        }

        if (logic.getButton() == HighScoreMenuButtons.POPUP) {
            drawTextureRelative(texturesPopup.get(logic.getPopup().ordinal()), -BANNER_WIDTH / 2, -BANNER_HEIGHT / 2);
            textRendererLarge.drawText(logic.getPopupInput(), popupTextX(), popupTextY());

        }

        return stage;
    }

    private float popupTextX() {
        return stage.getWidth() / 2 - BANNER_WIDTH / 2 + 23 * Scale.LARGE.getScale();
    }

    private float popupTextY() {
        return stage.getHeight() / 2 - BANNER_HEIGHT / 2 + 50 * Scale.LARGE.getScale();
    }

    private void drawScoreText(String text, float startX, int position) {
        textRendererNormal.drawText(text, startX, scoreTextOffsetY() - position * 6 * Scale.NORMAL.getScale());
    }

    private float scoreTextLeftOffsetX() {
        return stage.getWidth() / 2 - BANNER_WIDTH / 2 + 10 * Scale.LARGE.getScale();
    }

    private float scoreTextRightOffsetX() {
        return stage.getWidth() / 2 - BANNER_WIDTH / 2 + 54 * Scale.LARGE.getScale();
    }

    private float scoreTextOffsetY() {
        return stage.getHeight() / 2 - BANNER_HEIGHT / 2 + 39 * Scale.LARGE.getScale();
    }

    private void drawTextureRelative(Texture texture, float x, float y) {
        Image image = new Image(texture);
        image.setPosition(stage.getWidth() / 2f + x, stage.getHeight() / 2f + y);
        stage.addActor(image);
    }

}
