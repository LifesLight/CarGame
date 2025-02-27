package edu.kit.cargame.game.common;

import java.util.Random;

/**
 * Holds a random number generator.
 * This allows for set seed inside the game.
 */
public class DeterministicRandom {
    private final Random random;

    /**
     * Creates a new DeterministicRandom with set seed.
     * @param seed the seed for Random
     */
    public DeterministicRandom(long seed) {
        random = new Random(seed);
    }

    /**
     * Creates a DeterministicRandom with random seed.
     */
    public DeterministicRandom() {
        random = new Random();
    }

    /**
     * Returns the Random number generator.
     * @return the random number generator
     */
    public Random getRandom() {
        return random;
    }
}
