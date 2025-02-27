package edu.kit.cargame.game.object.eyecandy;

import edu.kit.cargame.game.geometry.BoundingBox;
import edu.kit.cargame.game.geometry.Point;
import edu.kit.cargame.game.common.Game;
import edu.kit.cargame.game.common.GameObject;
import edu.kit.cargame.io.view.gamerenderers.SpriteRenderer;

/**
 * The type House holds the position of a random House.
 */
public class House extends GameObject {
    /**
     * The Width of a house.
     */
    public static final int WIDTH = 170;
    /**
     * The Height of a house.
     */
    public static final int HEIGHT = 75;


    /**
     * Instantiates a new House.
     *
     * @param parent   the parent Object
     * @param game     the game in which this house lives
     * @param position the position of the house
     * @param type     the type of the house
     */
    public House(GameObject parent, Game game, Point position, BackgroundType type) {
        super(parent, game, position);
        setBoundingBox(new BoundingBox(new Point(WIDTH, HEIGHT)));
        HouseType houseType = type.getRandomHouse(game.getRandom());
        setRenderer(new SpriteRenderer(this, houseType.getFilePath()));
    }
}
