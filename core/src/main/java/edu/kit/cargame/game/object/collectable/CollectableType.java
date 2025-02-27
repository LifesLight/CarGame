package edu.kit.cargame.game.object.collectable;

import edu.kit.cargame.game.common.Game;
import edu.kit.cargame.game.common.GameObject;
import edu.kit.cargame.game.geometry.BoundingBox;
import edu.kit.cargame.game.geometry.Point;
import edu.kit.cargame.io.config.Config;

import java.util.NavigableMap;
import java.util.Random;
import java.util.TreeMap;

/**
 * The enum CollectableType is an enum that represents the different types of collectables that can be spawned in the game.
 */
public enum CollectableType {
    COIN(CollectableCoin::new, Config.getConfig().spawnChanceCoin()),
    HEART(CollectableHeart::new, Config.getConfig().spawnChanceHeart()),
    OIL_PUDDLE(CollectableOilPuddle::new, Config.getConfig().spawnChanceOilPuddle()),
    STAR(CollectableStar::new, Config.getConfig().spawnChanceStar()),
    STOPWATCH(CollectableStopwatch::new, Config.getConfig().spawnChanceStopwatch());

    @FunctionalInterface
    private interface CollectableConstructor {
        void constructor(GameObject parent, Game game, Point position, BoundingBox boundingBox);
    }

    private final CollectableConstructor type;
    private final float weight;
    private static final NavigableMap<Float, CollectableType> COLLETABLE_WEIGHTS = new TreeMap<>();
    private static float totalWeight;

    static {
        for (CollectableType c : values()) {
            if (c.weight > 0) {
                totalWeight += c.weight;
                COLLETABLE_WEIGHTS.put(totalWeight, c);
            }
        }
    }


    CollectableType(CollectableConstructor type, float weight) {
        this.type = type;
        this.weight = weight;
    }

    /**
     * Spawn random collectable.
     *
     * @param parent   the parent
     * @param game     the game
     * @param position the position
     * @param random   the random generator
     */
    public static void spawnRandomCollectable(GameObject parent, Game game, Point position, Random random) {
        getRandom(random).spawn(parent, game, position);
    }

    private static CollectableType getRandom(Random random) {
        return COLLETABLE_WEIGHTS.higherEntry(random.nextFloat() * totalWeight).getValue();
    }

    private void spawn(GameObject parent, Game game, Point position) {
        type.constructor(parent, game, position, new BoundingBox(new Point(50, 50)));
    }
}
