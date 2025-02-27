package edu.kit.cargame.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Colors;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;

/**
 * The main class for the main menu of the car game.
 * As each screen has completely different requirements
 * and different schemes suitable,
 * this class itself only provides the least common denominator.
 * <p>
 * Each class has a similar structure:
 * It always contains a main menu class, which is concerned with drawing the
 * menu and gathering user input, delegating other functionality.
 * This input is then piped to the menu logic class,
 * which keeps state and processes this information.
 * This information is then used by the menu itself, to update the content seen
 * on screen, as well as the game itself.
 * If an action needs to be taken outside the scope of the menu (e.g., changing
 * the screen, exiting the game, etc.).
 * This action resides in an enum, which lists all actions. The only exception
 * to this rule is the start screen,
 * which has not state (detecting any button press is already
 * enough).
 * This decouples the drawing of a given state and the creation of the state itself, allowing them to be tested separately.
 * <p>
 * Other than that, menus may have other classes
 * helping in data storage or rendering of the menu.
 * The menus follow a strict hierarchy, meaning the menu itself cannot access the
 * game and the logic cannot access the game.
 * We use a polling-based approach instead of a callback-based one to detach the execution order from the user input.
 * This has the advantage of a calling trace independent of the user input.
 * <p>
 * As logic and actions have no basic denominator, and we don't wish to implement a comprehensive ui system
 * for the use in only this application, they do not inherit from
 * any class.
 */
public abstract class Menu<ActionType> implements Screen {
    /**
     * The scale with which the graphics are exported.
     * This is needed to calculate the relative position of pixels.
     */
    public static float scale = 10;


    public static final float BANNER_WIDTH = 99 * scale;
    public static final float BANNER_HEIGHT = 94 * scale;

    public static final float LOGO_WIDTH = 99;
    public static final float LOGO_HEIGHT = 41;

    public static final float BIG_LOGO_WIDTH = 99 * scale;
    public static final float BIG_LOGO_HEIGHT = 41 * scale;

    /**
     * Overwrites the complete screen with the background color.
     * This should be called before anything else editing the frame buffer.
     */
    public static void renderSetBackgroundColor() {
        Color color = Colors.get("background");
        Gdx.gl.glClearColor(color.r, color.g, color.b, color.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    /**
     * Returns the action that should be taken according to the logic of the menu, but which are outside the scope
     * of the menu. E.g., if the player presses back, the active menu should be switched to the previous. However,
     * switching menus is outside the scope of a specific menu.
     * @return The action that should be taken according to the logic of the menu, but which are outside the scope
     * of the menu.
     */
    public abstract ActionType pollAction();
    @Override
    public void show() {
    }

    /**
     * Renders the screen by:
     * <ul>
     * <li> Processing the user input.</li>
     * <li> setting the background color.</li>
     * <li> drawing the stage provided by {@link #generateContent()}.</li>
     * </ul>
     *
     * This should not be overridden further.
     */
    @Override
    public void render(float delta) {

        processUserInput();

        renderSetBackgroundColor();

        Stage stage = generateContent();

        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }


    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null); // Clear input when hidden
    }


    /**
     * Processes the user input according to the menus needs.
     */
    protected abstract void processUserInput();
    /**
     * Generates a stage which will be drawn on render.
     * @return The stage to be drawn.
     */
    protected abstract Stage generateContent();
}
