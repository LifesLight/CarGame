package edu.kit.cargame.io.view.gamescreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import edu.kit.cargame.common.settings.GameMode;
import edu.kit.cargame.io.input.ActionTypes;
import edu.kit.cargame.io.input.implement.Combined;
import edu.kit.cargame.io.score.GameOutcome;
import edu.kit.cargame.common.settings.GameSettings;
import edu.kit.cargame.io.config.Config;

/**
 * Abstract class for the Game Screen.
 * Manages pauses
 * Manages game over handling and switching to the game over screen
 * Will be extended by the differing single and multiplayer screens
 * Also manages mute state and extended config from file
 */
public abstract class GameScreen implements Screen {

    private final Combined combined = new Combined();
    protected final GameSettings settings;
    protected final Config config;
    private static final String AMBIENT_SOUND_PATH = "game/sounds/motor.wav";
    private final Sound ambientSound;

    private boolean isMuted = false;

    private GameScreenAction gameScreenAction = GameScreenAction.NONE;

    /**
     * Instantiates a new Game screen.
     *
     * @param settings the settings for the game
     */
    public GameScreen(GameSettings settings) {
        this.settings = settings;
        this.config = Config.getConfig();
        this.ambientSound = Gdx.audio.newSound(Gdx.files.internal(AMBIENT_SOUND_PATH));
    }

    @Override
    public void show() {
        combined.updateJustPressed();
        if (!isMuted) {
            this.ambientSound.loop();
        }
    }

    /**
     * Factory method to create a new GameScreen from the settings.
     *
     * @param settings the settings
     * @return the game screen
     */
    public static GameScreen fromSettings(GameSettings settings) {
        if (settings.getGameMode() == GameMode.SINGLE_PLAYER) {
            return new SinglePlayerGameScreen(settings);
        } else {
            return new TwoPlayerGameScreen(settings);
        }
    }


    /**
     * Checks if the game is over.
     *
     * @return true, if all the games are over and the game should switch to the game over screen
     */
    public abstract boolean isGameOver();

    @Override
    public void render(float delta) {
        gameScreenAction = GameScreenAction.NONE;
        if (combined.justPressed().contains(ActionTypes.BACK)) {
            gameScreenAction = GameScreenAction.PAUSE;
        }
        if (isGameOver()) {
            gameScreenAction = GameScreenAction.GOTO_HIGHSCORE;
        }
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        this.ambientSound.stop();
    }

    @Override
    public void dispose() {
        this.ambientSound.dispose();
    }

    /**
     * Checks if the game is muted.
     *
     * @return true, if the game is muted
     */
    public boolean isMuted() {
        return isMuted;
    }

    /**
     * Sets the game to be muted.
     *
     * @param muted the new muted state
     */
    public void setMuted(boolean muted) {
        isMuted = muted;
        this.ambientSound.stop();
        if (!muted) {
            this.ambientSound.loop();
        }
    }

    /**
     * Poll if the game screen has wants its owner to execute an action.
     *
     * @return the action it wants to be done
     */
    public GameScreenAction pollAction() {
        return gameScreenAction;
    }

    /**
     * Gets the game outcome.
     *
     * @return the game outcome
     */
    public abstract GameOutcome getGameOutcome();

    /**
     * Unpause the game.
     * And trigger the countdown
     */
    public void unPause() {
    }
}
