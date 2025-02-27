package edu.kit.cargame.game.playercar;

/**
 * The information about the player car.
 *
 * @param carType the specific car type for the player
 * @param color    the color of that car
 */
public record CarSettings(CarType carType, ColorOptions color) { }
