package edu.kit.cargame.io.input.implement;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import edu.kit.cargame.io.input.ActionTypes;
import edu.kit.cargame.io.input.UserInput;

import java.util.ArrayList;
import java.util.List;

/**
 * Alternative keyboard input for mocking a second input device.
 */
public class KeyboardAlternative extends UserInput<ActionTypes> {

    @Override
    public List<ActionTypes> pressed() {
        return rawPressed();
    }

    /**
     * Returns a list of buttons pressed.
     * The mapping is as follows:
     * - UP: I
     * - DOWN: K
     * - LEFT: J
     * - RIGHT: L
     * - Enter: P
     * - Back: O
     *
     * @return A list of buttons pressed. Note that this may include the same button
     *      twice if two buttons with the same mappings are pressed at the same time.
     */
    public static List<ActionTypes> rawPressed() {
        List<ActionTypes> pressedKeys = new ArrayList<>();

        // Directional keys
        if (Gdx.input.isKeyPressed(Input.Keys.I)) {
            pressedKeys.add(ActionTypes.UP);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.K)) {
            pressedKeys.add(ActionTypes.DOWN);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.J)) {
            pressedKeys.add(ActionTypes.LEFT);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.L)) {
            pressedKeys.add(ActionTypes.RIGHT);
        }

        // Enter or space keys
        if (Gdx.input.isKeyPressed(Input.Keys.P)) {
            pressedKeys.add(ActionTypes.ENTER);
        }

        // Escape key
        if (Gdx.input.isKeyPressed(Input.Keys.O)) {
            pressedKeys.add(ActionTypes.BACK);
        }

        return pressedKeys;
    }

}
