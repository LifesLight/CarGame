package edu.kit.cargame.game.playercar;

import com.badlogic.gdx.graphics.g2d.Sprite;
import edu.kit.cargame.io.view.TextureCache;

/**
 * Record holding the sprites of a moving obstacle.
 *
 * @param left      the sprite of the car when it is moving left
 * @param leftDown    the sprite of the car when it is moving left and up
 * @param right     the sprite of the car when it is moving right
 * @param rightUp the sprite of the car when it is moving down and right
 * @param ahead     the sprite of the car when it is moving ahead
 * @param blinkingRight the sprite of the car when it is blinking to the right
 * @param blinkingLeft the sprite of the car when it is blinking to the left
 */
public record MovingCarSprites(Sprite left, Sprite leftDown, Sprite right,
                               Sprite rightUp, Sprite ahead, Sprite blinkingRight, Sprite blinkingLeft) {

    /**
     * Initialises the sprites by loading them from disk.
     *
     * @param name the name of the car
     * @return The initialized sprites.
     */
    public static MovingCarSprites fromName(String name) {
        String basePath = "game/obstacles/enemy_car/" + name + "/";
        return new MovingCarSprites(
            new Sprite(TextureCache.getTexture(basePath + "3.png")),
            new Sprite(TextureCache.getTexture(basePath + "4.png")),
            new Sprite(TextureCache.getTexture(basePath + "1.png")),
            new Sprite(TextureCache.getTexture(basePath + "0.png")),
            new Sprite(TextureCache.getTexture(basePath + "2.png")),
            new Sprite(TextureCache.getTexture(basePath + "right.png")),
            new Sprite(TextureCache.getTexture(basePath + "left.png"))
        );

    }

    /**
     * Disposes the textures of the sprites.
     */
    public void dispose() {
        // Do not dispose cached textures
    }
}
