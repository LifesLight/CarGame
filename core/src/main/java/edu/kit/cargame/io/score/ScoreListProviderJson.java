package edu.kit.cargame.io.score;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * ScoreListProviderJson A ScoreListProvider loading and saving the ScoreList
 * from a file named
 * score.json.
 * It uses the standard JSON format, as well as the standard
 * encoding and decoding scheme
 * translated to java.
 */
public class ScoreListProviderJson implements ScoreListProvider {
    private static final String FILE_NAME = "score.json";

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Optional<ScoreList> loadScoreList() {
        try {
            // Read the JSON file and convert it into a ScoreList object
            File file = new File(FILE_NAME);
            if (!file.exists()) {
                System.out.println("File not found, json store failed");
                return Optional.empty();

            }

            // Deserialize JSON to ScoreList
            ScoreList scoreList = objectMapper.readValue(file, ScoreList.class);
            return Optional.of(scoreList);
        } catch (IOException e) {
            return Optional.empty();
        }
    }

    @Override
    public void saveScoreList(ScoreList list) {
        try {
            // Serialize the ScoreList object to JSON and write to file
            objectMapper.writeValue(new File(FILE_NAME), list);
        } catch (IOException ignored) {
            //TODO catch
        }
    }
}
