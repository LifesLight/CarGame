package edu.kit.cargame.io.input.implement;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.controllers.Controllers;
import edu.kit.cargame.io.input.ActionTypes;
import edu.kit.cargame.io.input.UserInput;

/**
 * Controller User input originating from the controller.
 */
public class Controller extends UserInput<ActionTypes> {

    @Override
    public List<ActionTypes> pressed() {
        return justPressed();
    }

    /**
     * Rumble all connected controllers.
     * @param duration in ms
     * @param strength between 0 and 1. 1 being the strongest
     */
    @Override
    public void rumble(int duration, float strength) {
        for (com.badlogic.gdx.controllers.Controller controller : Controllers.getControllers()) {
            controller.startVibration(duration, strength);
        }
    }

    /**
     * Returns a list of buttons pressed.
     * The mapping is as follows:
     * UP: D-Pad up or left stick at least halfway up.
     * DOWN: D-Pad down or left stick at least halfway down.
     * LEFT: D-Pad left or left stick at least halfway left.
     * RIGHT: D-Pad right or left stick at least halfway right.
     * Enter: A-Button.
     * Back: B-Button.
     * Please note that the mapping above is for X-Box controllers and other
     * controllers may have a different mapping,
     * according to their x-input counterpart. For example, switch pro controllers
     * have the A and B buttons reversed.
     *
     * @return A list of buttons pressed. Note that this may include the same button
     *         twice if two buttons with the same mappings are pressed at the same
     *         time.
     */
    public static List<ActionTypes> rawPressed() {
        List<ActionTypes> pressedButtons = new ArrayList<>();

        for (com.badlogic.gdx.controllers.Controller controller : Controllers.getControllers()) {
            // Analog stick input
            if (controller.getAxis(controller.getMapping().axisLeftY) < -0.5f) {
                pressedButtons.add(ActionTypes.UP);
            }
            if (controller.getAxis(controller.getMapping().axisLeftY) > 0.5f) {
                pressedButtons.add(ActionTypes.DOWN);
            }
            if (controller.getAxis(controller.getMapping().axisLeftX) < -0.5f) {
                pressedButtons.add(ActionTypes.LEFT);
            }
            if (controller.getAxis(controller.getMapping().axisLeftX) > 0.5f) {
                pressedButtons.add(ActionTypes.RIGHT);
            }

            // D-pad input
            if (controller.getButton(controller.getMapping().buttonDpadUp)) {
                pressedButtons.add(ActionTypes.UP);
            }
            if (controller.getButton(controller.getMapping().buttonDpadDown)) {
                pressedButtons.add(ActionTypes.DOWN);
            }
            if (controller.getButton(controller.getMapping().buttonDpadLeft)) {
                pressedButtons.add(ActionTypes.LEFT);
            }
            if (controller.getButton(controller.getMapping().buttonDpadRight)) {
                pressedButtons.add(ActionTypes.RIGHT);
            }

            // Other buttons
            if (controller.getButton(controller.getMapping().buttonA)) {
                pressedButtons.add(ActionTypes.ENTER);
            }
            if (controller.getButton(controller.getMapping().buttonB)) {
                pressedButtons.add(ActionTypes.BACK);
            }
        }

        return pressedButtons;
    }

}
