package edu.kit.cargame.io.config;

import edu.kit.cargame.game.common.Game;

/**
 * The type MetaData holds Metadata that is given to {@link Game} on initialisation.
 * @param height the height of the game window
 * @param width  the width of the game window
 */
public record MetaData(int height, int width) {
}
