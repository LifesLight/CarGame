package edu.kit.cargame.io.score;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import edu.kit.cargame.game.playercar.CarType;

/**
 * ScoreList Contains a list of scores for each car type.
 * It provides methods to add and read scores. It also provides the option to
 * limit the number of entries in a list according to the scores to avoid
 * needing to save every result.
 * As some components do not need to have write access to this list, it is
 * recommended
 * to use the read or read/ write interfaces provided by this class. This
 * guarantees that a game component
 * who should only read this list, does only read this list.
 */
public class ScoreList implements HighScoreSetterProvider {
    private static final int MAX_SCORES_PER_CAR = 5;
    private static final int MAX_SCORES = 5;
    @JsonProperty("scores")
    Map<CarType, List<Score>> scores;

    /**
     * Creates a {@link ScoreList} with given list of scores.
     * This is primarily used
     * to recreate an existing score list through a ScoreListProvider, like from
     * JSON.
     *
     * @param scores The list of scores.
     */
    public ScoreList(@JsonProperty("scores") Map<CarType, List<Score>> scores) {
        this.scores = scores;
    }

    /**
     * Create a new empty score list.
     */
    public ScoreList() {
        this.scores = new HashMap<>();
    }

    @Override
    public void addScore(CarType type, Score score) {
        if (!checkScoreApplicable(type, score.score())) {
            throw new IllegalArgumentException("The score provided needs to be greater than one of the high scores");
        }

        List<Score> scoresForType = getOrComputeListForType(type);
        if (scoresForType.size() >= MAX_SCORES_PER_CAR) {
            scoresForType.remove(scoresForType.stream()
                    .min(Comparator.comparingInt(Score::score)));
        }
        scoresForType.add(score);
    }

    @Override
    public boolean checkScoreApplicable(CarType type, int score) {
        List<Score> scoresForType = getOrComputeListForType(type);
        int scoresHigher = (int) scoresForType.stream()
                .filter(s -> s.score() >= score)
                .count();
        return scoresHigher < MAX_SCORES_PER_CAR;
    }

    /**
     * Returns the scores for a car type in ascending order.
     *
     * @param type the car type.
     * @return The List of scores.
     */
    public List<Score> getHighScores(CarType type) {
        getOrComputeListForType(type).sort(Comparator.comparingInt(Score::score));
        return Collections.unmodifiableList(getOrComputeListForType(type));
    }

    private List<Score> getOrComputeListForType(CarType type) {
        return scores.computeIfAbsent(type, k -> new ArrayList<>());
    }

    @Override
    public Optional<Score> getHighScore(CarType type) {
        List<Score> listForType = getOrComputeListForType(type);
        return listForType.stream().max(Comparator.comparingInt(Score::score));

    }

    @Override
    public List<Score> getHighestScores() {
        List<Score> completeList = new ArrayList<>();
        for (List<Score> list : scores.values()) {
            completeList.addAll(list);
        }
        completeList.sort(Comparator.comparingInt(Score::score));
        completeList = completeList.reversed();
        while (completeList.size() > MAX_SCORES) {
            completeList.removeLast();
        }
        return completeList;
    }


}
