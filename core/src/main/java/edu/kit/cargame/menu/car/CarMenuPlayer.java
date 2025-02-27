package edu.kit.cargame.menu.car;

import edu.kit.cargame.game.playercar.CarType;
import edu.kit.cargame.game.playercar.ColorOptions;

/**
 * CarMenuPlayer This class contains every information linked to a single
 * player.
 * This includes its car and color selected, as well as which field he is
 * editing.
 */
public class CarMenuPlayer {
    private CarType car;
    private ColorOptions color;
    private CarMenuButtonTypes button;

    /**
     * Creates a new player with all given information.
     *
     * @param car    The car type the player has selected.
     * @param color  The color type the player has selected.
     * @param button The button the player is editing.
     */
    public CarMenuPlayer(CarType car, ColorOptions color, CarMenuButtonTypes button) {
        this.car = car;
        this.color = color;
        this.button = button;
    }

    /**
     * Returns the car the player has selected.
     *
     * @return The car the player has selected.
     */
    public CarType getCar() {
        return car;
    }

    /**
     * Updates the car the player has selected.
     *
     * @param car The car the player has selected.
     */
    public void setCar(CarType car) {
        this.car = car;
    }

    /**
     * Returns the color the player has selected for their car.
     *
     * @return The color the player has selected for their car.
     */
    public ColorOptions getColor() {
        return color;
    }

    /**
     * Updates the color the player selected for their car.
     *
     * @param color The color the player selected for their car.
     */
    public void setColor(ColorOptions color) {
        this.color = color;
    }

    /**
     * Returns the button currently selected by the player.
     *
     * @return The button currently selected by the player.
     */
    public CarMenuButtonTypes getButton() {
        return button;
    }

    /**
     * Updates the button currently selected by the player.
     *
     * @param button The button currently selected by the player.
     */
    public void setButton(CarMenuButtonTypes button) {
        this.button = button;
    }
}
