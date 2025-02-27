package edu.kit.cargame.io.view.gamescreen;

import edu.kit.cargame.common.settings.GameSettings;
import edu.kit.cargame.io.input.implement.Keyboard;
import edu.kit.cargame.io.input.implement.SecondPlayerInput;
import edu.kit.cargame.io.score.GameOutcome;

/**
 * A screen for a two-player game.
 * Creates two SinglePlayerGameRenderers and renders them side by side.
 */
public class TwoPlayerGameScreen extends GameScreen {

    private final SinglePlayerGameRenderer gameRendererPlayer1;
    private final SinglePlayerGameRenderer gameRendererPlayer2;

    private float windowWidth;
    private float windowHeight;
    /**
     * Instantiates a new two-player Game screen.
     *
     * @param settings the settings
     */
    public TwoPlayerGameScreen(GameSettings settings) {
        super(settings);
        gameRendererPlayer1 = new SinglePlayerGameRenderer(new Keyboard(), settings.getPlayer1(), config);
        gameRendererPlayer2 = new SinglePlayerGameRenderer(new SecondPlayerInput(), settings.getPlayer2(), config);
    }

    @Override
    public void show() {
        super.show();
        gameRendererPlayer1.show();
        gameRendererPlayer2.show();
    }

    @Override
    public boolean isGameOver() {
        return gameRendererPlayer1.getGame().isGameOver() && gameRendererPlayer2.getGame().isGameOver();
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        gameRendererPlayer1.render(delta, isMuted());
        gameRendererPlayer1.getRenderTarget().drawToScreen(0, 0, windowWidth, windowHeight, true);
        gameRendererPlayer2.render(delta, isMuted());
        gameRendererPlayer2.getRenderTarget().drawToScreen(0, windowHeight / 2, windowWidth, windowHeight, true);

    }

    @Override
    public void resize(int width, int height) {
        gameRendererPlayer1.resize(width, height / 2);
        gameRendererPlayer2.resize(width, height / 2);

        windowWidth = width;
        windowHeight = height;

    }

    @Override
    public void dispose() {
        super.dispose();
        if (gameRendererPlayer1 != null) {
            gameRendererPlayer1.dispose();
        }
        if (gameRendererPlayer2 != null) {
            gameRendererPlayer2.dispose();
        }

    }

    @Override
    public GameOutcome getGameOutcome() {
        return GameOutcome.createMultiPlayerResult(settings.getPlayer1().carType(), settings.getPlayer2().carType(),
            gameRendererPlayer1.getGame().getScore(), gameRendererPlayer2.getGame().getScore());
    }

    @Override
    public void unPause() {
        gameRendererPlayer1.pause();
        gameRendererPlayer2.pause();
    }


}
