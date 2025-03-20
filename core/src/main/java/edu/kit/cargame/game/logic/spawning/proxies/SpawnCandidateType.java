package edu.kit.cargame.game.logic.spawning.proxies;

/**
 * The difference classes of spawn candidates.
 */
public enum SpawnCandidateType {
    /**
     * Ghosts are "ghosts" of previously spawned objects in previous chunks.
     * Needed for moving cars path finding and border distance prune between chunks.
     */
    GHOST,
    /**
     * Moving obstacle candidate
     */
    MOVING,
    /**
     * Immobile obstacle candidate
     */
    IMMOBILE,
    /**
     * Collectable candidate
     */
    COLLECTABLE
}
