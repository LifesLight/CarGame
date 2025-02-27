package edu.kit.cargame.io.input.implement;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import edu.kit.cargame.io.input.ActionTypes;
import edu.kit.cargame.io.input.UserInput;

import java.util.ArrayList;
import java.util.List;

/**
 * Keyboard User input originating from the keyboard.
 */
public class Keyboard extends UserInput<ActionTypes> {

    @Override
    public List<ActionTypes> pressed() {
        return rawPressed();
    }

    /**
     * Returns a list of buttons pressed.
     * The mapping is as follows:
     * - UP: W key and arrow up.
     * - DOWN: S key and arrow down.
     * - LEFT: A key and arrow left.
     * - RIGHT: D key and arrow right.
     * - Enter: Space and Enter.
     * - Back: Escape and Backspace.
     *
     * @return A list of buttons pressed. Note that this may include the same button
     *          twice if two buttons with the same mappings are pressed at the same time
     */
    public static List<ActionTypes> rawPressed() {
        List<ActionTypes> pressedKeys = new ArrayList<>();

        // Directional keys
        if (Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.UP)) {
            pressedKeys.add(ActionTypes.UP);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            pressedKeys.add(ActionTypes.DOWN);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            pressedKeys.add(ActionTypes.LEFT);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            pressedKeys.add(ActionTypes.RIGHT);
        }

        // Enter or space keys
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE) || Gdx.input.isKeyPressed(Input.Keys.ENTER)) {
            pressedKeys.add(ActionTypes.ENTER);
        }

        // Escape key
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE) || Gdx.input.isKeyPressed(Input.Keys.BACKSPACE)) {
            pressedKeys.add(ActionTypes.BACK);
        }

        return pressedKeys;
    }

}
