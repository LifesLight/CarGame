package edu.kit.cargame.game.object.eyecandy;


import edu.kit.cargame.game.common.Game;
import edu.kit.cargame.game.common.GameObject;
import edu.kit.cargame.game.geometry.BoundingBox;
import edu.kit.cargame.game.geometry.Point;
import edu.kit.cargame.io.view.gamerenderers.SpriteRenderer;

/**
 * This Class represents contains a Background segment, as well as the houses that spawn above the street.
 */
public class BackgroundChunk extends GameObject {

    /**
     * Instantiates a new Chunk.
     *
     * @param parent   the parent
     * @param game     the game
     * @param position the position
     * @param bounds   the bounds
     * @param type     the type of background chunk
     */
    public BackgroundChunk(GameObject parent, Game game, Point position, BoundingBox bounds, BackgroundType type) {
        super(parent, game, position);
        setBoundingBox(bounds);
        setRenderer(new SpriteRenderer(this, type.getSprite()));

        int amountOfHouses = (int) bounds.getWidth() / House.WIDTH;
        for (int i = 0; i < amountOfHouses; i++) {
            new House(this, getGame(),
                new Point(
                    i * (bounds.getWidth() / amountOfHouses),
                    bounds.getHeight() - House.HEIGHT
                ), type);
        }
    }

    @Override
    public void tick(double timeScale) {
        setPosition(getLocalPosition().addX((float) (-5 * timeScale)));
        if (getBoundingBox().topRight().x() < 0) {
            kill();
        }
    }

    @Override
    protected boolean needAllTimescales() {
        return true;
    }
}
