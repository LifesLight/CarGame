package edu.kit.cargame.io.input.implement;

import edu.kit.cargame.io.input.ActionTypes;

import java.util.List;

/**
 * Combined A wrapper around the controller and keyboard input. This combines
 * them into one action. This is useful for the high-score menu where we dont want to have keyboard alternative combined as well.
 */
public class KeyboardAndController extends Controller {

    @Override
    public List<ActionTypes> pressed() {
        List<ActionTypes> input = Keyboard.rawPressed();
        input.addAll(Controller.rawPressed());
        return input;
    }
}
