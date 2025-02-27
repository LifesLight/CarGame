package edu.kit.cargame.game.object.eyecandy;

import java.util.Random;

/**
 * The type BackgroundType is an Enum that represents the different types of Backgrounds that can be used in the game.
 */
public enum BackgroundType {
    GRASS("game/background/street/street_grass.png", new HouseType[]{HouseType.HOUSE1, HouseType.HOUSE2, HouseType.HOUSE3}),
    DESERT("game/background/street/street_sand.png", new HouseType[]{HouseType.SAND1, HouseType.SAND2}),
    FOREST("game/background/street/street_forest.png", new HouseType[]{HouseType.FOREST1, HouseType.FOREST2, HouseType.FOREST3}),
    SNOW("game/background/street/street_snowy.png", new HouseType[]{HouseType.HOUSE1, HouseType.HOUSE2, HouseType.HOUSE3});

    private final String sprite;
    private final HouseType[] possibleHouses;

    BackgroundType(String sprite, HouseType[] possibleHouses) {
        this.sprite = sprite;
        this.possibleHouses = possibleHouses;
    }

    /**
     * Gets the sprite.
     *
     * @return the sprite
     */
    public String getSprite() {
        return sprite;
    }

    /**
     * Gets a random BackgroundType.
     *
     * @param random the random generator to use
     * @return the random
     */
    public static BackgroundType getRandom(Random random) {
        return values()[random.nextInt(values().length)];
    }

    /**
     * Gets a random HouseType.
     *
     * @param random the random generator to use
     * @return the random
     */
    public HouseType getRandomHouse(Random random) {
        return possibleHouses[random.nextInt(possibleHouses.length)];
    }
}
