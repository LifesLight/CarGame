package edu.kit.cargame.io.score;

import edu.kit.cargame.io.view.renderer.TextRenderer;
import edu.kit.cargame.menu.highscore.HighScoreMenuLogic;

/**
 * A Record of a raw score as well as the name of the player who achieved it.
 * As the name is unknown at the time of driving (we do not currently have real
 * user
 * profiles, the name is typed after a game by the player), it is only used in
 * the high score
 * system and not before it.
 *
 * @param name  The name of the player that achieved the score.
 * @param score The score in points itself.
 */
public record Score(String name, int score) {

    /**
     * Instantiates a new Score.
     *
     * @param name  the name of the player that achieved the score
     * @param score the score
     */
    public Score(String name, int score) {
        this.score = score;
        // we remove characters which cannot be rendererd. While they cannot be entered through normal means,
        // manually edited save files might contain them. Editing the name in this case is seen as an
        // acceptable way of recovering from this fault.
        this.name = name.replaceAll(TextRenderer.UNSUPORTED_CHARACTERS, "").substring(0,
                Math.min(name.length(), HighScoreMenuLogic.MAX_NAME_LENGTH));
    }
}
