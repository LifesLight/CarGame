package edu.kit.cargame.menu.start;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import edu.kit.cargame.io.input.UserInput;
import edu.kit.cargame.io.input.ActionTypes;
import edu.kit.cargame.io.input.implement.SecondPlayerInput;
import edu.kit.cargame.menu.Menu;

import com.badlogic.gdx.graphics.Texture;

/**
 * The start menu is shown on startup.
 * It just contains the game logo as well as the text "Press Any Key".
 * Its action is set to go forward if any key is pressed on keyboard or
 * controller.
 */
public class StartMenu extends Menu<StartMenuAction> {
    private int frame = 0;
    // As this screen is very simple, we can just use two stages and just render one
    // of them.
    // This cuts out any rendering logic, effectively just setting the output
    // buffer.
    private Stage stage1; // contains the menu without button pressed.
    private Stage stage2; // contains the menu with button pressed.
    private Texture image1;
    private boolean shouldChangeScreen = false; // Tracks whether we should change to next screen

    private final UserInput<ActionTypes> inputController = new SecondPlayerInput();

    @Override
    public void show() {
        createStages();

        inputController.updateJustPressed();
    }

    private void createStages() {
        image1 = new Texture("logo/start-screen1.png");
        Texture image2 = new Texture("logo/start-screen2.png");

        stage1 = new Stage(new ScreenViewport());
        stage2 = new Stage(new ScreenViewport());
        fillStage(stage1, image1);
        fillStage(stage2, image2);
    }

    @Override
    public void resize(int width, int height) {
        createStages();
    }

    @Override
    public void dispose() {
        stage1.dispose();
        stage2.dispose();
        image1.dispose();
    }

    @Override
    protected void processUserInput() {
        boolean keyOrMicePressed = anyKeyJustPressed() ||
        // this is done to support controller
                inputController.justPressedFirst() != null
            || Gdx.input.isButtonPressed(Input.Buttons.LEFT)
            || Gdx.input.isButtonPressed(Input.Buttons.RIGHT);
        if (keyOrMicePressed) {
            shouldChangeScreen = true;
        }
    }

    @Override
    public StartMenuAction pollAction() {
        StartMenuAction action = shouldChangeScreen ? StartMenuAction.GO_FORWARD : StartMenuAction.NONE;
        shouldChangeScreen = false;
        return action;
    }

    @Override
    protected Stage generateContent() {
        Stage stageToDraw;
        if (frame < 40) {
            stageToDraw = stage1;
        } else {
            stageToDraw = stage2;
        }
        frame = (frame + 1) % 80;
        return stageToDraw;
    }

    private void fillStage(Stage stage, Texture image) {
        stage.clear();
        Table table = new Table();
        table.setFillParent(true);
        table.add(new Image(image));
        stage.addActor(table);
    }

    private boolean anyKeyJustPressed() {
        for (int i = 0; i < 256; i++) { // Loop through possible key codes
            if (Gdx.input.isKeyJustPressed(i)) {
                return true;
            }
        }
        return false;
    }
}
