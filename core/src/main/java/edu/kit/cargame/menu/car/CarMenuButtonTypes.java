package edu.kit.cargame.menu.car;

/**
 * CarMenuButton This enum describes what button / input field the player is hovering.
 */
public enum CarMenuButtonTypes {
    /**
     * The player has selected a car and color, meaning no field is selected.
     */
    NONE,
    /**
     * The player is currently selecting their car.
     */
    CAR,
    /**
     * The player is currently selecting the color of the car.
     */
    COLOR
}
