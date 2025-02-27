package edu.kit.cargame.io.score;

import java.util.Optional;

/**
 * A Provider to load and save a score list.
 * This scores list then implements
 * some interfaces used by the different game components to interact with the
 * high scores.
 */
public interface ScoreListProvider {
    /**
     * Returns a new ScoreList that the given provider created, e.g., from a JSON file.
     * If the loading failed, for example, because the file from which the list is loaded did not exist,
     * empty is returned.
     * @return A new ScoreList that the given provider created, e.g., from a JSON file.
     */
    Optional<ScoreList> loadScoreList();

    /**
     * Writes the given list to the media. The next load command loads the given list.
     * @param list The list to be saved.
     */
    void saveScoreList(ScoreList list);
}
