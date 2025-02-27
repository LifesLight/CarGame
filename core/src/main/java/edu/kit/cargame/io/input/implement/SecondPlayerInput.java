package edu.kit.cargame.io.input.implement;

import edu.kit.cargame.io.input.ActionTypes;

import java.util.List;

/**
 * Combined A wrapper around the controller and alternative keyboard input for player 2.
 */
public class SecondPlayerInput extends Controller {

    @Override
    public List<ActionTypes> pressed() {
        List<ActionTypes> input = KeyboardAlternative.rawPressed();
        input.addAll(Controller.rawPressed());
        return input;
    }
}
