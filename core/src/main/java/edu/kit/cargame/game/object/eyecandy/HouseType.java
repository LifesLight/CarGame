package edu.kit.cargame.game.object.eyecandy;

/**
 * The type HouseType holds the different types of houses.
 */
public enum HouseType {
    SAND1("desert"),
    SAND2("desert2"),
    HOUSE1("house1"),
    HOUSE2("house2"),
    HOUSE3("house3"),
    FOREST1("trees1"),
    FOREST2("trees2"),
    FOREST3("trees3");

    private static final String HOUSE_PATH = "game/background/house/%s.png";
    private final String name;

    HouseType(String name) {
        this.name = name;
    }

    /**
     * Gets file path of the texture for the house.
     *
     * @return the file path
     */
    public String getFilePath() {
        return HOUSE_PATH.formatted(name);
    }
}
