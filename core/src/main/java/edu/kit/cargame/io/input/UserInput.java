package edu.kit.cargame.io.input;

import java.util.ArrayList;
import java.util.List;

/**
 * UserInput A provider of user input mapped to Type.
 * The only function that needs to be implemented is pollPressed, to get the
 * current buttons pressed.
 * The other functions are convenience wrappers around it for common use
 * cases.
 * As functions in these class are called as often as the stage is rendered,
 * their performance is critical.
 * For this reason, we use Lists instead of Sets for the input and
 * manually deal with removing duplicates
 * ourselves. This is much more efficient as those lists have at most around 10
 * entries and even adding a new element,
 * where we need to check each other entry to determine whether it is a
 * duplicate, it is still much more efficient than calculating a hash.
 *
 * @param <T> The type of user input.
 *
 */
public abstract class UserInput<T> {
    private List<T> lastKeys = new ArrayList<>();

    /**
     * Returns a list of buttons that just where pressed.
     * To achieve this, it looks at the list of buttons pressed last call determine
     * every new input.
     * Note this means that if this method was not called last time, the
     * information provided by this method may not be correct.
     * This is, for example, the case if the polling resides in a screen to which just
     * was changed to. In this case, the last keys need to be updated if the screen
     * changes, for which the updateJustPressed method may be used.
     *
     * @return A list of buttons that just where pressed.
     */
    public List<T> justPressed() {
        List<T> currentKeys = pressed();
        List<T> newJustPressed = currentKeys.stream()
                .filter(key -> !lastKeys.contains(key))
                .toList();
        lastKeys = currentKeys;
        return newJustPressed;
    }

    /**
     * Starts vibrating this input.
     * @param duration in ms
     * @param strength between 0 and 1. 1 being the strongest
     */
    public void rumble(int duration, float strength) {

    }

    /**
     * This is a convenience wrapper around {@link #justPressed()}.
     * It returns the first button just pressed, instead of a list of buttons.
     * This is useful for applications where multiple button presses at once can be discarded, e.g., in the menu.
     * @return The first button just pressed.
     */
    public T justPressedFirst() {
        List<T> justPressed = justPressed();
        if (justPressed.isEmpty()) {
            return null;
        }
        return justPressed.getFirst();
    }

    /**
     * Returns a list of buttons currently pressed.
     *
     * @return A list of buttons currently pressed.
     */
    public abstract List<T> pressed();

    /**
     * A wrapper around {@link #justPressedFirst()} which takes a function which is called, if the action
     * is not null.
     * @param action The action consumer.
     */
    public void justPressedFirstExecute(java.util.function.Consumer<T> action) {
        T type = justPressedFirst();
        if (type != null) {
            action.accept(type);
        }
    }

    /**
     * Updates the list of buttons that are currently held down. For information why
     * to call this, see the documentation of {@link #justPressed()}.
     */
    public void updateJustPressed() {
        lastKeys = pressed();
    }
}
