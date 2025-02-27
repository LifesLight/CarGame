package edu.kit.cargame.io.view.gamerenderers;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import edu.kit.cargame.game.playercar.CarSprites;
import edu.kit.cargame.game.playercar.UninitialisedCarSprites;
import edu.kit.cargame.game.playercar.PlayerCar;

/**
 * A renderer used for the player car. Based on its state it chooses one of the rotation sprites to render.
 * Also adds a color to the sprite based on the cars state.
 */
public class PlayerCarRenderer extends Renderer {
    private final PlayerCar playerCar;
    private final CarSprites sprites;


    /**
     * Instantiates a new Player car renderer.
     *
     * @param playerCar the player car
     * @param sprites    the sprites
     */
    public PlayerCarRenderer(PlayerCar playerCar, UninitialisedCarSprites sprites) {
        this.playerCar = playerCar;
        this.sprites = CarSprites.fromUninitialised(sprites);
    }


    @Override
    public void render(Batch batch) {
        Sprite currentSprite = getCurrentCarSprite();
        Color color;
        if (playerCar.isInvulnerable() && playerCar.getGame().getCurrentTime() % 10 < 5) {
            color = new Color(0.7f, 0.5f, 0.5f, 1f);
        } else {
            color = new Color(1f, 1f, 1f, 1f);
        }

        if (playerCar.hasBadSteering()) {
            color.mul(new Color(0.6f, 0.4f, 0.2f, 1f));
        }
        currentSprite.setColor(color);

        currentSprite.setBounds(playerCar.getGlobalPosition().x(), playerCar.getGlobalPosition().y() - 5,
            playerCar.getBoundingBox().getWidth() * 1.2f, playerCar.getBoundingBox().getHeight() * 1.2f);
        currentSprite.draw(batch);
    }

    private Sprite getCurrentCarSprite() {
        switch (playerCar.getDirection()) {
            case LEFT_UP:
                return sprites.leftUp();
            case LEFT:
                return sprites.left();
            case AHEAD:
                return sprites.ahead();
            case RIGHT:
                return sprites.right();
            case RIGHT_DOWN:
                return sprites.rightDown();
            default:
                throw new IllegalStateException("Unexpected value: " + playerCar.getDirection());
        }
    }

    @Override
    public void cleanup() {
        sprites.dispose();
    }
}
