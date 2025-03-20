package edu.kit.cargame.menu.car;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import edu.kit.cargame.common.settings.GameMode;
import edu.kit.cargame.common.settings.GameSettings;
import edu.kit.cargame.game.playercar.CarType;
import edu.kit.cargame.game.playercar.ColorOptions;
import edu.kit.cargame.io.input.ActionTypes;
import edu.kit.cargame.io.input.UserInput;
import edu.kit.cargame.io.input.implement.Keyboard;
import edu.kit.cargame.io.input.implement.SecondPlayerInput;
import edu.kit.cargame.io.score.HighScoreProvider;
import edu.kit.cargame.io.score.Score;
import edu.kit.cargame.io.view.Scale;
import edu.kit.cargame.io.view.renderer.TextRenderer;
import edu.kit.cargame.io.view.renderer.Util;
import edu.kit.cargame.menu.Menu;

import java.util.List;

/**
 * The car menu serves two main purposes:
 * <ul>
 * <li>Let the player select a car.</li>
 * <li>Let the player select a color for that car.</li>
 * </ul>
 * This can be done for either single-player or two-player modes:
 * <li>In single-player mode, one menu is drawn in the middle of the
 * screen.</li>
 * <li>In two-player mode, two menus are drawn side by side:
 * <ul>
 * <li>The left menu is for the keyboard player.</li>
 * <li>The right menu is for the controller player.</li>
 * </ul>
 * Each menu contains four main sections:
 * <ul>
 * <li>A title: "Select a car".</li>
 * <li>A stats group, showing the attributes of the car.</li>
 * <li>A preview of the car.</li>
 * <li>A color field, providing a preview of the selected color.</li>
 * </ul>
 * Only the car preview and the color field (sections 3 and 4) are selectable.
 * The currently selected item is highlighted by two arrows, one on either side.
 * When the screen is first displayed, the third section (car preview) is
 * selected by default.
 * <p>
 * Button functionality is described in {@code CarMenuLogic}.
 * <p>
 * If the player has chosen a car and a color, the following happens:
 *
 * <ul>
 * <li>A "Ready" banner is overlaid on their menu.</li>
 * <li>A timer starts, and if it reaches zero, the game begins.</li>
 * </ul>
 */

public class CarMenu extends Menu<CarMenuAction> {
    // as the menu would be too large otherwise, we use a smaller local scale;
    private static final float MENU_SCALE = 5f;
    /**
     * The width of a single menu (in multiplayer, two are shown). This width is
     * needed to calculate the offset for the objects drawn on the menu, like its
     * stats.
     */
    public static final float MENU_WIDTH = 73 * MENU_SCALE;
    private static final int MENU_FRAMES = 3;
    private static final int START_FRAMES = 3;
    // Car Textures
    private static final float TIME_PER_FRAME = 0.04f;
    private static final int DEFAULT_CAR_FRAMES = 14;
    private static final int BUS_FRAMES = 16;
    private static final int ROCKET_CAR_FRAMES = 18;
    private static final int RACE_CAR_FRAMES = 11;
    // High Score
    private static final float HIGH_SCORE_OFFSET_X = 25 * MENU_SCALE;
    private static final float HIGH_SCORE_OFFSET_Y = 16 * MENU_SCALE;
    // game logic
    private final CarMenuLogic logic = new CarMenuLogic();
    private final UserInput<ActionTypes> inputController = new SecondPlayerInput();
    private final Keyboard inputKeyboard = new Keyboard();
    private final HighScoreProvider highScore;
    private Stage stage;
    // Textures
    private Texture textureStar;
    private Texture textureHeart;
    private Texture textureLogo;
    private Texture textureReady;
    private List<Texture> textureMenuFrames;
    private List<Texture> textureArrayList;
    private List<Texture> textureStartFrames;
    private List<Texture> textureDefaultCarFrames;
    private List<Texture> textureBusFrames;
    private List<Texture> textureRocketCarFrames;
    private List<Texture> textureRaceCarFrames;
    private float carAnimationTimeP1 = 0;
    private float carAnimationTimeP2 = 0;
    private TextRenderer textRenderer;

    /**
     * Constructs a new CarMenu with the provided HighScoreProvider.
     *
     * @param highScore The HighScoreProvider instance used to fetch high score
     *                  data.
     */
    public CarMenu(HighScoreProvider highScore) {
        this.highScore = highScore;
    }

    @Override
    public void show() {
        loadTextures();

        generateStages();

        // setup logic and animation timers
        carAnimationTimeP1 = 0;
        carAnimationTimeP2 = 0;

        inputKeyboard.updateJustPressed();
        inputController.updateJustPressed();
    }

    private void generateStages() {
        // full hd
        if (Gdx.graphics.getWidth() <= 1920 || Gdx.graphics.getHeight() <= 1080) {
            stage = new Stage(new FitViewport(1920, 1080));
        } else {
            stage = new Stage(new ScreenViewport());
        }
        textRenderer = new TextRenderer(stage, Scale.NORMAL);

    }

    @Override
    public CarMenuAction pollAction() {
        return logic.pollWithReset();
    }

    /**
     * Returns the game settings according to the user input.
     *
     * @return The game settings according to the user input.
     */
    public GameSettings generateGameSettings() {
        return logic.generateGameSettings();
    }

    /**
     * Sets the game mode, resting the settings in the process.
     *
     * @param mode The game mode to be set.
     */
    public void setGameMode(GameMode mode) {
        logic.resetToMode(mode);
    }

    @Override
    public void resize(int width, int height) {
        generateStages();
    }

    @Override
    public void dispose() {
        if (stage != null) {
            stage.dispose();
        }
    }

    @Override
    protected Stage generateContent() {
        stage.clear();
        logic.testStartGame(Gdx.graphics.getDeltaTime());
        drawLogo();
        switch (logic.getMode()) {
            case MULTI_PLAYER:
                drawPlayer(logic.getPlayer1(), CarMenuOffset.DOUBLE_LEFT, carAnimationTimeP1);
                drawPlayer(logic.getPlayer2(), CarMenuOffset.DOUBLE_RIGHT, carAnimationTimeP2);
                break;
            case SINGLE_PLAYER:
                drawPlayer(logic.getPlayer1(), CarMenuOffset.SINGLE, carAnimationTimeP1);
                break;
        }
        drawGameStart();
        carAnimationTimeP1 += Gdx.graphics.getDeltaTime();
        carAnimationTimeP2 += Gdx.graphics.getDeltaTime();
        return stage;
    }

    @Override
    protected void processUserInput() {
        CarType typeP1 = logic.getPlayer1().getCar();
        CarType typeP2 = null;
        if (logic.getMode() == GameMode.MULTI_PLAYER) {
            typeP2 = logic.getPlayer2().getCar();
        }
        inputKeyboard.justPressedFirstExecute(logic::consumeKeyboard);
        inputController.justPressedFirstExecute(logic::consumeController);
        if (typeP1 != logic.getPlayer1().getCar()) {
            carAnimationTimeP1 = 0;
        }
        if (typeP2 != null && typeP2 != logic.getPlayer2().getCar()) {
            carAnimationTimeP2 = 0;
        }
    }

    private void loadTextures() {
        textureStar = new Texture("car_menu/star.png");
        textureHeart = new Texture("car_menu/heart.png");
        textureMenuFrames = Util.loadNTextures("car_menu/menu%d.png", 3);
        textureLogo = new Texture("logo/logo-big.png");
        textureReady = new Texture("car_menu/ready.png");

        textureDefaultCarFrames = Util.loadNTextures("car_menu/default_car/car%d.png", DEFAULT_CAR_FRAMES);
        textureBusFrames = Util.loadNTextures("car_menu/bus/car%d.png", BUS_FRAMES);
        textureRocketCarFrames = Util.loadNTextures("car_menu/rocket_car/car%d.png", ROCKET_CAR_FRAMES);
        textureRaceCarFrames = Util.loadNTextures("car_menu/race_car/car%d.png", RACE_CAR_FRAMES);

        textureMenuFrames = Util.loadNTextures("car_menu/menu%d.png", MENU_FRAMES);
        textureArrayList = Util.loadNTextures("car_menu/color_badge/badge%d.png", ColorOptions.values().length);
        textureStartFrames = Util.loadNTextures("car_menu/start/start%d.png", START_FRAMES);
    }

    private void drawHighScore(CarMenuOffset offset, int score) {
        textRenderer.drawText(score==0? "---":String.valueOf(score), stage.getWidth() / 2f + offset.offsetX() + HIGH_SCORE_OFFSET_X,
            stage.getHeight() / 2f + offset.offsetY() + HIGH_SCORE_OFFSET_Y);
    }

    private void drawMenu(CarMenuButtonTypes button, CarMenuOffset offset) {
        drawTextureRelative(textureMenuFrames.get(button.ordinal()), offset.offsetX(), offset.offsetY());
    }

    private void drawReady(CarMenuOffset offset) {
        drawTextureRelative(textureReady, offset.offsetX(), offset.offsetY(), 0, 0);
    }

    private void drawTextureRelative(Texture texture, float x, float y) {
        Image image = new Image(texture);
        image.setPosition(stage.getWidth() / 2f + x, stage.getHeight() / 2f + y);
        stage.addActor(image);
    }

    private void drawTextureRelative(Texture texture, float x, float y, float width, float height) {
        float xOffset = (-width) * MENU_SCALE / 2f;
        float yOffset = (height) * MENU_SCALE / 2f;
        drawTextureRelative(texture, x + xOffset, y + yOffset);
    }

    private void drawTextureRelative(Texture texture, float x, float y,
                                     CarMenuOffset offset) {
        drawTextureRelative(texture, x + offset.offsetX(), y + offset.offsetY());

    }

    private void drawInStats(Texture texture, int column, int row, CarMenuOffset offset) {
        drawTextureRelative(texture, (29 + 6 * row) * MENU_SCALE, (106 - 25 - 6 * column) * MENU_SCALE, offset);
    }

    private void drawStar(int column, int row, CarMenuOffset offset) {
        drawInStats(textureStar, column, row, offset);
    }

    private void drawNStars(int column, int count, CarMenuOffset offset) {
        for (int i = 0; i < count; i++) {
            drawStar(column, i, offset);
        }
    }

    private void drawHeart(int row, CarMenuOffset offset) {
        drawInStats(textureHeart, 4, row, offset);
    }

    private void drawNHearts(int count, CarMenuOffset offset) {
        for (int i = 0; i < count; i++) {
            drawHeart(i, offset);
        }
    }

    private void drawStats(CarType car, CarMenuOffset offset) {
        drawNStars(0, car.getSpeed(), offset);
        drawNStars(1, car.getAgility(), offset);
        drawNStars(2, car.getBoost(), offset);
        drawNStars(3, car.getSize(), offset);
        drawNHearts(car.getHp(), offset);

    }

    private void drawLogo() {
        float offsetY = (BANNER_HEIGHT - 3 * BIG_LOGO_HEIGHT) / 2f;
        drawTextureRelative(textureLogo, 0, offsetY, 99 * 2, 41 * 2);
    }

    private void drawCar(CarType car, CarMenuOffset offset, float animationTime) {
        int frames = 0;
        List<Texture> textures = null;
        switch (car) {
            case ROCKET:
                frames = ROCKET_CAR_FRAMES;
                textures = textureRocketCarFrames;
                break;
            case NORMAL:
                frames = DEFAULT_CAR_FRAMES;
                textures = textureDefaultCarFrames;
                break;
            case SPORT:
                frames = RACE_CAR_FRAMES;
                textures = textureRaceCarFrames;
                break;
            case BUS:
                frames = BUS_FRAMES;
                textures = textureBusFrames;
                break;
            default:
                throw new IllegalStateException();
        }
        if (textures == null) {
            throw new IllegalStateException();
        }
        int frameToDraw = Math.min((int) (animationTime / TIME_PER_FRAME), frames - 1);
        drawTextureRelative(textures.get(frameToDraw), 12f * MENU_SCALE, 17f * MENU_SCALE, offset);

    }

    private void drawColor(ColorOptions color, CarMenuOffset offset) {
        drawTextureRelative(textureArrayList.get(color.ordinal()), 38f * MENU_SCALE, 6f * MENU_SCALE, offset);
    }

    private void drawGameStart() {
        if (logic.getTimeTillStart() < 0) {
            return;
        }
        int frameToDraw = Math
            .min((int) (logic.getTimeTillStart() * START_FRAMES / CarMenuLogic.START_TIMER_DURATION), 2);
        drawTextureRelative(textureStartFrames.get(frameToDraw), 0, -87 * MENU_SCALE / 2, 73, 87);
    }

    private void drawPlayer(CarMenuPlayer player, CarMenuOffset offset, float carFrameStart) {
        drawMenu(player.getButton(), offset);
        drawStats(player.getCar(), offset);
        drawCar(player.getCar(), offset, carFrameStart);
        drawColor(player.getColor(), offset);
        int carHighScore = highScore.getHighScore(player.getCar()).orElse(new Score("", 0)).score();
        drawHighScore(offset, carHighScore);
        if (player.getButton() == CarMenuButtonTypes.NONE) {
            drawReady(offset);
        }
    }

}
