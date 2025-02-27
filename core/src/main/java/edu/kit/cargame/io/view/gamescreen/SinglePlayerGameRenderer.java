package edu.kit.cargame.io.view.gamescreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import edu.kit.cargame.common.logging.Logger;
import edu.kit.cargame.common.logging.LoggerManagement;
import edu.kit.cargame.game.common.Game;
import edu.kit.cargame.game.geometry.Point;
import edu.kit.cargame.game.playercar.CarSettings;
import edu.kit.cargame.io.config.Config;
import edu.kit.cargame.io.config.MetaData;
import edu.kit.cargame.io.input.ActionTypes;
import edu.kit.cargame.io.input.UserInput;
import edu.kit.cargame.io.view.RenderTarget;
import edu.kit.cargame.io.view.Scale;
import edu.kit.cargame.io.view.TextureCache;
import edu.kit.cargame.io.view.gamerenderers.Renderer;
import edu.kit.cargame.io.view.postprocessing.ChromaticAbberationShader;
import edu.kit.cargame.io.view.postprocessing.VertexShader;
import edu.kit.cargame.io.view.renderer.TextRenderer;

/**
 * Class for rendering a single player game screen to a stage.
 * Renders the game to a render target and then draws the render target to the screen at the right position
 * Abstraction for displaying two games on screen
 * Also renders the countdown when the game is paused
 */
public class SinglePlayerGameRenderer {

    private RenderTarget gameRenderTarget;
    private RenderTarget renderTarget;
    private int windowWidth;
    private int windowHeight;
    private final Game game;
    private Batch batch;
    private final Config config;
    private final Logger logger = LoggerManagement.getLogger();

    private TextRenderer textRenderer;
    private int countDown = 0;
    private float countDownTimer = 0;

    //Amount of time to show each count down step for
    private static final int COUNT_DOWN_TIME = 1;

    private static final int GAME_WIDTH = 1500;
    private static final int GAME_HEIGHT = 500;
    private static final float GAME_RATIO = (float) GAME_WIDTH / GAME_HEIGHT;
    private static final int SCORE_X = 5;
    private static final int SCORE_Y = 460;
    // The texture might be bigger than the game for super sampling
    private final int gameTextureWidth;
    private final int gameTextureHeight;


    /**
     * Instantiates a new Single player game renderer.
     *
     * @param userInput   the user input for this instance
     * @param carSettings the car settings for the player
     * @param config      the config for the game
     */
    public SinglePlayerGameRenderer(UserInput<ActionTypes> userInput, CarSettings carSettings, Config config) {
        game = new Game(userInput, carSettings, config, new MetaData(windowHeight, windowWidth));
        this.config = config;
        gameTextureWidth = GAME_WIDTH * config.superSamplingAliasingMultiplier();
        gameTextureHeight = GAME_HEIGHT * config.superSamplingAliasingMultiplier();
    }

    /**
     * Call when the screen holding this renderer is shown.
     */
    public void show() {
        // Create the SpriteBatch
        batch = new SpriteBatch();
    }

    /**
     * Get the game object itself.
     *
     * @return the game object
     */
    public Game getGame() {
        return game;
    }


    private Point pointToScreenSpace(Point point) {
        return new Point(point.x() * gameTextureWidth / GAME_WIDTH, point.y() * gameTextureHeight / GAME_HEIGHT);
    }

    /**
     * Tell the game it was just paused.
     * When render is called again, it will first play the countdown animation and then tick the game.
     */
    public void pause() {
        countDown = 3;
        countDownTimer = System.nanoTime() / 1000000000.0f; // Reset timer
    }

    /**
     * Render the game to the screen.
     * Also renders the countdown if the game was paused
     * Otherwise ticks game
     *
     * @param delta the time delta between this frame and the last frame
     * @param muted whether the game is muted
     */
    public void render(float delta, boolean muted) {
        if (countDown > 0) {
            float time = System.nanoTime() / 1000000000.0f;
            if (time - countDownTimer > COUNT_DOWN_TIME) {
                countDownTimer = time;
                countDown--;
            }
        }
        if (countDown == 0) {
            //Redraw game
            game.tick();
        }
        gameRenderTarget.begin(50.0f / 255.0f, 60f / 255f, 57f / 255f, 1);

        game.setMuted(muted);
        // Start rendering onto the target
        batch.begin();
        batch.setProjectionMatrix(new Matrix4().setToOrtho2D(0, 0, GAME_WIDTH, GAME_HEIGHT));
        for (Renderer r : game.getRenderers()) {
            r.render(batch);
        }

        // Draw score and coins above the game
        String scoreText = "Score: " + game.getScore();
        if (config.showFpsCounter()) {
            scoreText = "FPS: " + Gdx.graphics.getFramesPerSecond();
        }
        textRenderer.drawText(scoreText, SCORE_X, SCORE_Y, batch);
        batch.end();

        gameRenderTarget.end();

        renderTarget.begin(50.0f / 255.0f, 60f / 255f, 57f / 255f, 1);

        // Draw the RenderTarget to the screen
        float maxSize = Math.min(windowWidth, windowHeight * GAME_RATIO);
        float height = maxSize / GAME_RATIO;
        float width = maxSize;
        // Center render target
        float x = (windowWidth - width) / 2;
        float y = (windowHeight - height) / 2;
        gameRenderTarget.setUniforms((float) game.getCurrentTime(), game.isSlowdown(), game.getPlayerCar().getDirection().toInt(),
            pointToScreenSpace(game.getPlayerCar().getGlobalBoundingBox().getMiddleRight()));
        gameRenderTarget.drawToScreen(x, y, width, height, 0, 0, gameTextureWidth, gameTextureHeight, true);


        // Draw countdown on top
        if (countDown > 0) {
            // Draw gray translucent background
            batch.begin();
            batch.setProjectionMatrix(new Matrix4().setToOrtho2D(0, 0, windowWidth, windowHeight));
            batch.setColor(0.1f, 0.1f, 0.1f, 0.5f);
            batch.draw(TextureCache.getTexture("game/box.png"), 0, 0, windowWidth, windowHeight);
            batch.end();

            batch.begin();
            batch.setProjectionMatrix(new Matrix4().setToOrtho2D(0, 0, windowWidth, windowHeight));
            float size = maxSize / 5;
            batch.draw(TextureCache.getTexture("game/countdown/" + countDown + ".png"),
                (windowWidth - size) / 2, (windowHeight - size) / 2, size, size);
            batch.end();
        }
        renderTarget.end();
    }

    /**
     * Resize the game screen.
     *
     * @param width  the new width of the screen
     * @param height the new height of the screen
     */
    public void resize(int width, int height) {
        if (width == 0 || height == 0) {
            return;
        }
        if (width == windowWidth && height == windowHeight) {
            return;
        }
        if (gameRenderTarget != null) {
            gameRenderTarget.cleanup();
        }
        if (renderTarget != null) {
            renderTarget.cleanup();
        }

        gameRenderTarget = new RenderTarget(gameTextureWidth, gameTextureHeight, VertexShader.getShader(),
            ChromaticAbberationShader.getShader());
        renderTarget = new RenderTarget(width, height, null, null);

        // Create text renderer
        textRenderer = new TextRenderer(null, Scale.NORMAL);

        windowWidth = width;
        windowHeight = height;
    }

    /**
     * Dispose of the renderer resources.
     */
    public void dispose() {
        // Dispose of the SpriteBatch and RenderTarget
        if (batch != null) {
            batch.dispose();
        }
        if (gameRenderTarget != null) {
            gameRenderTarget.cleanup();
        }
        if (renderTarget != null) {
            renderTarget.cleanup();
        }
    }

    /**
     * Get the render target the game finally renders to.
     *
     * @return the render target of the game
     */
    public RenderTarget getRenderTarget() {
        return renderTarget;
    }

}
