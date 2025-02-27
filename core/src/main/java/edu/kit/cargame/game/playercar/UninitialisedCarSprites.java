package edu.kit.cargame.game.playercar;


/**
 * Record holding the paths to the sprites of a car type. The paths are relative to the assets folder.
 * The paths are for the up, down and normal sprite of the car.
 * Because sprites need to be loaded at runtime and this is used in an enum the sprites are stored as their paths.
 *
 * @param leftPath      the path to the sprite of the car when it is moving left
 * @param leftUpPath    the path to the sprite of the car when it is moving left and up
 * @param rightPath     the path to the sprite of the car when it is moving right
 * @param rightDownPath the path to the sprite of the car when it is moving down and right
 * @param aheadPath     the path to the sprite of the car when it is moving ahead
 */
public record UninitialisedCarSprites(String leftPath, String leftUpPath, String rightPath, String rightDownPath, String aheadPath) {
    /**
     * Creates a new UninitialisedCarSprites record with the paths to the sprites of the car type.
     *
     * @param carName  the name of the car type
     * @param carColor the color of the car
     * @return the UninitialisedCarSprites record
     */
    public static UninitialisedCarSprites fromStandardPath(String carName, ColorOptions carColor) {
        String color = carColor.name().toLowerCase();
        String carNamePath = carName.replace(" ", "_");
        return new UninitialisedCarSprites(
            "game/cars/" + carNamePath + "/" + color + "/1.png",
            "game/cars/" + carNamePath + "/" + color + "/0.png",
            "game/cars/" + carNamePath + "/" + color + "/3.png",
            "game/cars/" + carNamePath + "/" + color + "/4.png",
            "game/cars/" + carNamePath + "/" + color + "/2.png"
        );
    }
}
