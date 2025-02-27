package edu.kit.cargame.game.playercar;

import com.fasterxml.jackson.annotation.JsonValue;
import edu.kit.cargame.game.common.Game;
import edu.kit.cargame.game.common.GameObject;
import edu.kit.cargame.game.geometry.BoundingBox;
import edu.kit.cargame.game.geometry.Point;
import edu.kit.cargame.io.input.ActionTypes;
import edu.kit.cargame.io.input.UserInput;


/**
 * The car types selectable by the player.
 * Each type has five different stats, with each
 * having an integer value between one and five.
 */
public enum CarType {
    /**
     * The default car. It has balanced stats.
     */
    NORMAL("normal car", 3, 3, 2, 3, 3),
    /**
     * A school bus. It is very slow, but has five lives.
     */
    BUS("bus", 2, 1, 1, 5, 5),
    /**
     * A sports car. Very fast and agile, but fragile.
     */
    SPORT("sports car", 5, 5, 3, 2, 2),
    /**
     * A school bus. It is very slow, but has five lives.
     */
    ROCKET("rocket car", 4, 2, 5, 3, 3);

    private static final BoundingBox[] SIZES = {
        new BoundingBox(new Point(50, 35)),
        new BoundingBox(new Point(60, 45)),
        new BoundingBox(new Point(70, 55)),
        new BoundingBox(new Point(50, 38)),
        new BoundingBox(new Point(90, 75))
    };

    private static final float[] BOOSTS = {
        1.5f,
        2.0f,
        2.5f,
        3.0f,
        3.5f
    };

    private static final float[] AGILITIES = {
        .4f,
        .6f,
        .8f,
        .10f,
        .12f
    };

    private static final float[] SPEEDS = {
        .6f,
        .8f,
        1f,
        1.2f,
        1.4f
    };


    private final String name;
    private final int speed;
    private final int agility;
    private final int boost;
    private final int size;
    private final int hp;

    CarType(String name, int speed, int agility, int boost, int size, int hp) {
        this.name = name;
        this.speed = speed;
        this.agility = agility;
        this.boost = boost;
        this.size = size;
        this.hp = hp;
    }

    /**
     * Returns the car object for the given car type.
     *
     * @param parent    The parent object for the car.
     * @param game      The game in which the car lives.
     * @param userInput The user input for the car.
     * @param color     The color of the car.
     * @return The car object.
     */
    public PlayerCar getCar(GameObject parent, Game game, UserInput<ActionTypes> userInput, ColorOptions color) {
        UninitialisedCarSprites sprites = UninitialisedCarSprites.fromStandardPath(name, color);
        return new PlayerCar(parent, game, new Point(20, 200), sprites,
            userInput, getRealSpeed(), hp, getRealAgility(), getRealBoost(), getRealSize());
    }

    private float getRealAgility() {
        return AGILITIES[agility - 1];
    }

    private float getRealSpeed() {
        return SPEEDS[speed - 1];
    }

    private BoundingBox getRealSize() {
        return SIZES[size - 1];
    }

    private float getRealBoost() {
        return BOOSTS[boost - 1];
    }


    /**
     * Returns the name of the car.
     *
     * @return The name of the car.
     */
    @JsonValue
    public String getName() {
        return name;
    }

    /**
     * Returns the speed stat of the car.
     *
     * @return the speed stat of the car
     */
    public int getSpeed() {
        return speed;
    }

    /**
     * Returns the agility stat of the car.
     *
     * @return the agility stat of the car
     */
    public int getAgility() {
        return agility;
    }

    /**
     * Returns the boost stat of the car
     *
     * @return the boost stat of the car
     */
    public int getBoost() {
        return boost;
    }

    /**
     * Returns the size stat of the car.
     *
     * @return the size stat of the car
     */
    public int getSize() {
        return size;
    }

    /**
     * Returns the hp stat of the car.
     *
     * @return the hp stat of the car
     */
    public int getHp() {
        return hp;
    }

    /**
     * Returns a car type matched by name.
     *
     * @param name the name to match against
     * @return the corresponding car type
     * @throws IllegalArgumentException if the name does not match any car type
     */
    public static CarType fromString(String name) {
        for (CarType type : CarType.values()) {
            if (type.name.equalsIgnoreCase(name)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid car type: " + name);
    }

}
