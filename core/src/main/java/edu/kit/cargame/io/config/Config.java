package edu.kit.cargame.io.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.kit.cargame.common.logging.LoggerManagement;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * The Config class represents the configuration of the game.
 * It is loaded from a file and can be accessed statically.
 * If the file is missing or malformed, a default config is created and written to the file.
 * The config can be reloaded at any time.
 * The config file is located at ./config.json.
 * @param frameRate the frame rate of the game
 * @param vsync whether vsync is enabled
 * @param obstacleAmount the amount of spawn attempts for obstacles
 * @param worldHeight the height of the world
 * @param worldWidth the width of the world
 * @param baseTimeScale the base time scale of the game
 * @param movingObstacleFactor the percentage of how many obstacle attempts are moving cars
 * @param movingObstacleBlinkerDistance the distance at which moving cars start blinking
 * @param collectableChance the chance of a collectable spawning for each obstacleAmount
 * @param collectableChanceDecayRate the decay rate of the collectable chance. used for balancing the amount of items over time as the game speeds up
 * @param spawnChanceHeart the chance of a heart spawning being weighted random with other collectables
 * @param spawnChanceCoin the chance of a coin spawning being weighted random with other collectables
 * @param spawnChanceOilPuddle the chance of an oil puddle spawning being weighted random with other collectables
 * @param spawnChanceStar the chance of a star spawning being weighted random with other collectables
 * @param spawnChanceStopwatch the chance of a stopwatch spawning being weighted random with other collectables
 * @param alwaysPossibleLineFrequency the frequency of always possible line
 * @param alwaysPossibleLineWidth the distance to the always possible line that is pruned
 * @param spawningScatterFactor the factor by which the spawn position is scattered away from the lane middle
 * @param movingCarRandomLaneSwapChance the chance of a moving car swapping lanes
 * @param invincible whether the player is invincible
 * @param bestPathVisible whether the best path is visible
 * @param showFpsCounter whether the fps counter is visible
 * @param skipMenus whether the menus are skipped
 * @param mute whether the game is muted
 * @param skipMenusCarName the name of the car to skip menus with
 * @param superSamplingAliasingMultiplier multiplier for the internal resolution of the game. 1 means no super sampling
 * @param seed the seed for the random number generator, 0 means random seed
 * @param stopwatchDuration the duration of the stopwatch powerup
 * @param stopwatchSpeed the speed multiplier of the stopwatch powerup
 * @param starDuration the duration of the start countdown
 * @param coinFactor the speedup of the coin and how much additional score you get while having them
 * @param oilDuration the duration of the oil puddle
 * @param oilSteeringFactor the factor by which the steering is reduced while oil puddle effect is active
 * @param closeCallPoints the points you get for a close call
 * @param closeCallDistance the distance at which a close call is detected
 * @param rumbleEnabled whether rumble is enabled
 *
 *
 */
public record Config(
    @JsonProperty("FRAME_RATE") int frameRate,
    @JsonProperty("VSYNC") boolean vsync,
    @JsonProperty("OBSTACLE_AMOUNT") int obstacleAmount,
    @JsonProperty("WORLD_HEIGHT") int worldHeight,
    @JsonProperty("WORLD_WIDTH") int worldWidth,
    @JsonProperty("BASE_TIMESCALE") float baseTimeScale,
    @JsonProperty("MOVING_OBSTACLE_FACTOR") float movingObstacleFactor,
    @JsonProperty("MOVING_OBSTACLE_BLINKER_DISTANCE") float movingObstacleBlinkerDistance,
    @JsonProperty("COLLECTABLE_CHANCE") float collectableChance,
    @JsonProperty("COLLECTABLE_CHANCE_DECAY_RATE") float collectableChanceDecayRate,
    @JsonProperty("SPAWN_CHANCE_HEART") float spawnChanceHeart,
    @JsonProperty("SPAWN_CHANCE_COIN") float spawnChanceCoin,
    @JsonProperty("SPAWN_CHANCE_OIL_PUDDLE") float spawnChanceOilPuddle,
    @JsonProperty("SPAWN_CHANCE_STAR") float spawnChanceStar,
    @JsonProperty("SPAWN_CHANCE_STOPWATCH") float spawnChanceStopwatch,
    @JsonProperty("ALWAYS_POSSIBLE_LINE_FREQUENCY") int alwaysPossibleLineFrequency,
    @JsonProperty("ALWAYS_POSSIBLE_LINE_WIDTH") float alwaysPossibleLineWidth,
    @JsonProperty("SPAWNING_SCATTER_FACTOR") float spawningScatterFactor,
    @JsonProperty("MOVING_CAR_RANDOM_LANE_SWAP_CHANCE") float movingCarRandomLaneSwapChance,
    @JsonProperty("INVINCIBLE") boolean invincible,
    @JsonProperty("BEST_PATH_VISIBLE") boolean bestPathVisible,
    @JsonProperty("SHOW_FPS_COUNTER") boolean showFpsCounter,
    @JsonProperty("SKIP_MENUS") boolean skipMenus,
    @JsonProperty("MUTE") boolean mute,
    @JsonProperty("SKIP_MENUS_CAR_NAME") String skipMenusCarName,
    @JsonProperty("SUPER_SAMPLING_ALIASING_MULTIPLIER") int superSamplingAliasingMultiplier,
    @JsonProperty("SEED") long seed,
    @JsonProperty("STOPWATCH_DURATION") int stopwatchDuration,
    @JsonProperty("STOPWATCH_SPEED") float stopwatchSpeed,
    @JsonProperty("STAR_DURATION") int starDuration,
    @JsonProperty("COIN_FACTOR") float coinFactor,
    @JsonProperty("OIL_DURATION") int oilDuration,
    @JsonProperty("OIL_STEERING_FACTOR") float oilSteeringFactor,
    @JsonProperty("CLOSE_CALL_POINTS") int closeCallPoints,
    @JsonProperty("CLOSE_CALL_DISTANCE") float closeCallDistance,
    @JsonProperty("RUMBLE_ENABLED") boolean rumbleEnabled
) {
    private static final String CONFIG_LOCATION = "./config.json";
    private static final ObjectMapper MAPPER = new ObjectMapper();

    private static Config config = null;


    /**
     * Returns the current config. If the config has not been loaded yet, it will be loaded from the file.
     */
    public static Config getConfig() {
        if (config == null) {
            reloadConfig();
        }
        return config;
    }

    /**
     * Reloads the config from the file.
     */
    public static void reloadConfig() {
        config = loadConfigFromFile();
    }

    private static Config loadConfigFromFile() {
        File configFile = new File(CONFIG_LOCATION);
        Config defaultConfig = defaultConfig();

        if (!configFile.exists()) {
            LoggerManagement.getLogger().info("Creating default config file");
            writeConfigToFile(defaultConfig, configFile);
            return defaultConfig;
        }

        try {
            JsonNode fileNode = MAPPER.readTree(configFile);
            JsonNode defaultNode = MAPPER.valueToTree(defaultConfig);
            JsonNode mergedNode = merge(defaultNode, fileNode);
            return MAPPER.treeToValue(mergedNode, Config.class);
        } catch (IOException e) {
            LoggerManagement.getLogger().error(String.format("Malformed or unreadable config file. Rewriting with default config. %s", e));
            if (!configFile.delete()) {
                LoggerManagement.getLogger().error("Could not delete malformed config file!");
            }
            writeConfigToFile(defaultConfig, configFile);
            return defaultConfig;
        }
    }

    /**
     * Merges two JsonNodes. For every field in defaultNode that is missing in overrideNode,
     * the default value is copied over.
     */
    private static JsonNode merge(JsonNode defaultNode, JsonNode overrideNode) {
        if (defaultNode.isObject() && overrideNode.isObject()) {
            ObjectNode merged = defaultNode.deepCopy();
            overrideNode.fields().forEachRemaining(entry -> {
                merged.set(entry.getKey(), entry.getValue());
            });
            return merged;
        }
        return overrideNode;
    }

    private static void writeConfigToFile(Config config, File configFile) {
        try {
            MAPPER.writerWithDefaultPrettyPrinter().writeValue(configFile, config);
        } catch (IOException e) {
            LoggerManagement.getLogger().error(String.format("Couldn't write to config file! %s", e));
        }
    }

    private static Config defaultConfig() {
        return new Config(
            30,
            true,
            7,
            500,
            1500,
            1.5f,
            0.5f,
            300f,
            1.0f,
            0.25f,
            1.25f,
            5.0f,
            3.5f,
            0.5f,
            1.0f,
            3,
            50f,
            0.2f,
            0.005f,
            false,
            false,
            false,
            false,
            false,
            "normal car",
            5,
            0,
            150,
            0.5f,
            300,
            0.1f,
            400,
            0.35f,
            400,
            8.0f,
            true
        );
    }
}
