package edu.kit.cargame.game.playercar;

import com.badlogic.gdx.graphics.g2d.Sprite;
import edu.kit.cargame.io.view.TextureCache;

/**
 * Record holding the sprites of a car type.
 * The sprites are for moving up, down and normal.
 *
 * @param left      the sprite of the car when it is moving left
 * @param leftUp    the sprite of the car when it is moving left and up
 * @param right     the sprite of the car when it is moving right
 * @param rightDown the sprite of the car when it is moving down and right
 * @param ahead     the sprite of the car when it is moving ahead
 */
public record CarSprites(Sprite left, Sprite leftUp, Sprite right, Sprite rightDown, Sprite ahead) {

    /**
     * Initialises the sprites by loading them from disk.
     *
     * @param uninitialised The uninitialised version of this object.
     * @return The initialised sprites.
     */
    public static CarSprites fromUninitialised(UninitialisedCarSprites uninitialised) {
        return new CarSprites(
            new Sprite(TextureCache.getTexture(uninitialised.leftPath())),
            new Sprite(TextureCache.getTexture(uninitialised.leftUpPath())),
            new Sprite(TextureCache.getTexture(uninitialised.rightPath())),
            new Sprite(TextureCache.getTexture(uninitialised.rightDownPath())),
            new Sprite(TextureCache.getTexture(uninitialised.aheadPath()))
        );

    }

    /**
     * Disposes the textures of the sprites.
     */
    public void dispose() {
        // Do not dispose cached textures
    }
}
