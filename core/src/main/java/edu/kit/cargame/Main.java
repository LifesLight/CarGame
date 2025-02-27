package edu.kit.cargame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Colors;
import edu.kit.cargame.common.logging.LogLevel;
import edu.kit.cargame.common.logging.LoggerManagement;
import edu.kit.cargame.common.settings.GameMode;
import edu.kit.cargame.common.settings.GameSettings;
import edu.kit.cargame.common.settings.ScreenType;
import edu.kit.cargame.game.playercar.CarSettings;
import edu.kit.cargame.game.playercar.CarType;
import edu.kit.cargame.game.playercar.ColorOptions;
import edu.kit.cargame.io.config.Config;
import edu.kit.cargame.io.score.ScoreList;
import edu.kit.cargame.io.score.ScoreListProvider;
import edu.kit.cargame.io.score.ScoreListProviderJson;
import edu.kit.cargame.io.view.gamescreen.GameScreen;
import edu.kit.cargame.io.view.gamescreen.SinglePlayerGameScreen;
import edu.kit.cargame.menu.car.CarMenu;
import edu.kit.cargame.menu.car.CarMenuAction;
import edu.kit.cargame.menu.highscore.HighScoreMenu;
import edu.kit.cargame.menu.mode.ModeMenu;
import edu.kit.cargame.menu.mode.ModeMenuAction;
import edu.kit.cargame.menu.pause.PauseMenu;
import edu.kit.cargame.menu.start.StartMenu;
import edu.kit.cargame.menu.start.StartMenuAction;

/**
 * {@link com.badlogic.gdx.ApplicationListener} implementation shared by all
 * platforms.
 */
public class Main extends Game {
    private ModeMenu modeMenu;
    private StartMenu startMenu;
    private CarMenu carMenu;
    private HighScoreMenu highScoreMenu;
    private GameScreen gameScreen;
    private PauseMenu pauseMenu;

    private ScreenType currentScreen;

    private boolean gameMuted;

    private GameSettings settings = null;

    private final ScoreListProvider scoreProvider = new ScoreListProviderJson();
    private ScoreList scoreList;

    @Override
    public void create() {
        Config config = Config.getConfig();
        gameMuted = config.mute();

        LoggerManagement.setDefaultMinimalLevel(LogLevel.DEBUG);


        scoreList = scoreProvider.loadScoreList().orElse(new ScoreList());
        modeMenu = new ModeMenu(gameMuted);
        startMenu = new StartMenu();
        carMenu = new CarMenu(scoreList);
        pauseMenu = new PauseMenu(gameMuted);
        highScoreMenu = new HighScoreMenu(scoreList);
        putColorTheme();
        if (config.skipMenus()) {
            String selectedCarType = config.skipMenusCarName();

            CarType type;
            try {
                type = CarType.fromString(selectedCarType);
            } catch (IllegalArgumentException e) {
                type = CarType.NORMAL;
                LoggerManagement.getLogger().warning("Unknown car type: " + selectedCarType + " falling back to " + CarType.NORMAL);
            }
            settings = GameSettings.createSinglePlayer(new CarSettings(type, ColorOptions.BLUE));

            gameScreen = new SinglePlayerGameScreen(settings);
            switchScreen(ScreenType.GAME);
        } else {
            switchScreen(ScreenType.START);
        }
    }

    @Override
    public void render() {
        switch (currentScreen) {
            case CAR:
                handleCarMenu();
                break;
            case MODE:
                handleModeMenu();
                break;
            case START:
                handleStartMenu();
                break;
            case HIGH_SCORE:
                handleHighScoreMenu();
                break;
            case GAME:
                handleGameScreen();
                break;
            case PAUSE:
                handlePauseMenu();
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + currentScreen);
        }
        super.render();
    }

    @Override
    public void dispose() {
        modeMenu.dispose();
        startMenu.dispose();
        carMenu.dispose();
    }

    private Color rgbColor(int r, int g, int b) {
        return new Color(r / 255f, g / 255f, b / 255f, 1f);
    }

    private void putColorTheme() {
        Colors.put("background", rgbColor(50, 60, 57));
    }

    private void handleStartMenu() {
        StartMenuAction action = startMenu.pollAction();
        switch (action) {
            case GO_FORWARD:
                switchScreen(ScreenType.MODE);
                break;
            case NONE:
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + action);
        }
    }

    private void handleCarMenu() {
        CarMenuAction action = carMenu.pollAction();
        switch (action) {
            case BACK:
                this.setScreen(modeMenu);
                switchScreen(ScreenType.MODE);
                break;
            case NONE:
                break;
            case START_GAME:
                settings = carMenu.generateGameSettings();
                gameScreen = GameScreen.fromSettings(settings);
                switchScreen(ScreenType.GAME);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + action);
        }
    }

    private void handleModeMenu() {
        ModeMenuAction action = modeMenu.pollAction();
        switch (action) {
            case EXIT_GAME:
                Gdx.app.exit();
                break;
            case GO_BACKWARDS:
                switchScreen(ScreenType.START);
                break;
            case GO_FORWARD_MULTI_PLAYER:
                carMenu.setGameMode(GameMode.MULTI_PLAYER);
                switchScreen(ScreenType.CAR);
                break;
            case GO_FORWARD_SINGLE_PLAYER:
                carMenu.setGameMode(GameMode.SINGLE_PLAYER);
                switchScreen(ScreenType.CAR);
                break;
            case NONE:
                break;
            case TOGGLE_MUTE:
                gameMuted = !gameMuted;
                modeMenu.setGameMuted(gameMuted);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + action);
        }
    }

    private void handleHighScoreMenu() {
        switch (highScoreMenu.pollAction()) {
            case RESET:
                scoreProvider.saveScoreList(scoreList);
                switchScreen(ScreenType.MODE);
                break;
            case NONE:
                break;
            case PLAY_AGAIN:
                scoreProvider.saveScoreList(scoreList);
                gameScreen = GameScreen.fromSettings(settings);
                switchScreen(ScreenType.GAME);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + highScoreMenu.pollAction());
        }
    }

    private void handlePauseMenu() {
        switch (pauseMenu.pollAction()) {
            case CONTINUE:
                gameScreen.unPause();
                switchScreen(ScreenType.GAME);
                break;
            case TOGGLE_MUTE:
                gameMuted = !gameMuted;
                pauseMenu.setGameMuted(gameMuted);
                break;
            case TO_MODE:
                switchScreen(ScreenType.MODE);
                break;
            case NONE:
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + pauseMenu.pollAction());

        }
    }

    private void handleGameScreen() {
        switch (gameScreen.pollAction()) {
            case GOTO_HIGHSCORE:
                highScoreMenu.setGameOutcomeAndReset(gameScreen.getGameOutcome());
                switchScreen(ScreenType.HIGH_SCORE);
                break;
            case PAUSE:
                switchScreen(ScreenType.PAUSE);
                break;
            case NONE:
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + gameScreen.pollAction());
        }
    }

    private void switchScreen(ScreenType type) {
        currentScreen = type;
        switch (type) {
            case CAR:
                this.setScreen(carMenu);
                break;
            case MODE:
                modeMenu.setGameMuted(gameMuted);
                this.setScreen(modeMenu);
                break;
            case START:
                this.setScreen(startMenu);
                break;
            case HIGH_SCORE:
                this.setScreen(highScoreMenu);
                break;
            case GAME:
                gameScreen.setMuted(gameMuted);
                this.setScreen(gameScreen);
                break;
            case PAUSE:
                pauseMenu.setGameMuted(gameMuted);
                this.setScreen(pauseMenu);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + type);
        }
    }
}
