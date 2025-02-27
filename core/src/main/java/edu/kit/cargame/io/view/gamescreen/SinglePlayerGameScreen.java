package edu.kit.cargame.io.view.gamescreen;

import edu.kit.cargame.common.settings.GameSettings;
import edu.kit.cargame.io.input.implement.Combined;
import edu.kit.cargame.io.score.GameOutcome;

/**
 * A screen creating one game and showing it to the monitor.
 */
public class SinglePlayerGameScreen extends GameScreen {

    private final SinglePlayerGameRenderer gameRenderer;
    private float windowWidth;
    private float windowHeight;

    /**
     * Instantiates a new Single player game screen.
     *
     * @param settings the settings
     */
    public SinglePlayerGameScreen(GameSettings settings) {
        super(settings);
        gameRenderer = new SinglePlayerGameRenderer(new Combined(), settings.getPlayer1(), config);
    }

    @Override
    public void show() {
        super.show();
        gameRenderer.show();
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        gameRenderer.render(delta, isMuted());
        gameRenderer.getRenderTarget().drawToScreen(0, 0, windowWidth, windowHeight, true);
    }

    @Override
    public boolean isGameOver() {
        return gameRenderer.getGame().isGameOver();
    }

    @Override
    public GameOutcome getGameOutcome() {
        return GameOutcome.createSinglePlayerResult(settings.getPlayer1().carType(), gameRenderer.getGame().getScore());
    }

    @Override
    public void resize(int width, int height) {
        gameRenderer.resize(width, height);

        windowWidth = width;
        windowHeight = height;

    }

    @Override
    public void dispose() {
        super.dispose();
        if (gameRenderer != null) {
            gameRenderer.dispose();
        }
    }

    @Override
    public void unPause() {
        gameRenderer.pause();
    }

}
