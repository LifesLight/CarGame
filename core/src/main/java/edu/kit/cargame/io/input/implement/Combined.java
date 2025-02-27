package edu.kit.cargame.io.input.implement;

import com.badlogic.gdx.controllers.Controllers;
import edu.kit.cargame.io.input.ActionTypes;
import edu.kit.cargame.io.input.UserInput;

import java.util.List;

/**
 * Combined A wrapper around the controller, keyboard alternative and keyboard input. This combines
 * them into one action. This is useful for the early stages of the start menus
 * as those do not differentiate between inputs.
 */
public class Combined extends Controller {

    @Override
    public List<ActionTypes> pressed() {
        List<ActionTypes> input = Keyboard.rawPressed();
        input.addAll(Controller.rawPressed());
        input.addAll(KeyboardAlternative.rawPressed());
        return input;
    }
}
