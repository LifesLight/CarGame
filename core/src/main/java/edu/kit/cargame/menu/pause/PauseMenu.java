package edu.kit.cargame.menu.pause;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import edu.kit.cargame.io.input.implement.Combined;
import edu.kit.cargame.io.view.renderer.Util;
import edu.kit.cargame.menu.Menu;

import java.util.List;

/**
 * A menu where the player can continue or exit the game, as well as toggle mute.
 */
public class PauseMenu extends Menu<PauseMenuAction> {
    /**
     * Used for animations as a reference time.
     * Loops back to 0 if the animations
     * should restart.
     */
    private int frame = 0;
    private Stage stage;

    private List<Texture> texturesMenu;
    private static final int TEXTURES_MENU_LENGTH = 6;
    private static final String TEXTURES_MENU_POSITION = "pause_menu/menu%d.png";

    private List<Texture> texturesMute;
    private static final int TEXTURES_MUTE_LENGTH = 4;
    private static final String TEXTURES_MUTE_POSITION = "pause_menu/mute%d.png";

    private final PauseMenuLogic logic = new PauseMenuLogic();

    private final Combined inputCombined = new Combined();

    /**
     * Creates a pause menu with given mute state.
     *
     * @param gameMuted whether the game is muted;
     */
    public PauseMenu(boolean gameMuted) {
        logic.setGameMuted(gameMuted);
    }

    /**
     * Sets whether the game is muted.
     *
     * @param gameMuted Whether the game is muted;
     */
    public void setGameMuted(boolean gameMuted) {
        logic.setGameMuted(gameMuted);
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        texturesMenu = Util.loadNTextures(TEXTURES_MENU_POSITION, TEXTURES_MENU_LENGTH);
        texturesMute = Util.loadNTextures(TEXTURES_MUTE_POSITION, TEXTURES_MUTE_LENGTH);

        inputCombined.updateJustPressed();

    }

    @Override
    public void hide() {
    }


    @Override
    public void resize(int width, int height) {
        stage = new Stage(new ScreenViewport());
    }

    @Override
    public void dispose() {
    }

    @Override
    public PauseMenuAction pollAction() {
        return logic.pollWithReset();
    }

    @Override
    protected void processUserInput() {
        PauseMenuButtonTypes lastButton = logic.getActiveButton();
        inputCombined.justPressedFirstExecute(logic::consumeKey);
        // we press the button down if we change it
        if (lastButton != logic.getActiveButton()) {
            frame = 40;
        }
    }

    @Override
    protected Stage generateContent() {
        stage.clear();

        // this shift animates the button to be pressed down
        int shift = frame < 40 ? 0 : 1;
        int shiftMenu = shift * TEXTURES_MENU_LENGTH / 2;

        Table tableMenu = new Table();
        tableMenu.setFillParent(true);
        tableMenu.add(new Image(
            texturesMenu.get(logic.getActiveButton().ordinal()
                + shiftMenu)));
        stage.addActor(tableMenu);

        int shiftMute = logic.getActiveButton() == PauseMenuButtonTypes.MUTE ? shift * TEXTURES_MUTE_LENGTH / 2 : 0;

        Table tableMute = new Table();
        tableMute.setFillParent(true);
        tableMute.add(new Image(texturesMute.get((logic.getGameMuted() ? 1 : 0) + shiftMute)));
        stage.addActor(tableMute);

        frame = (frame + 1) % 80;

        return stage;
    }

}
