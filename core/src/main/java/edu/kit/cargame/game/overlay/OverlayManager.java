package edu.kit.cargame.game.overlay;

import edu.kit.cargame.game.common.Game;
import edu.kit.cargame.game.common.GameObject;
import edu.kit.cargame.game.geometry.Point;
import edu.kit.cargame.game.overlay.objects.OverlayBackground;
import edu.kit.cargame.game.overlay.objects.OverlayCoin;
import edu.kit.cargame.game.overlay.objects.OverlayHearth;

/**
 * Manages UI elements like Hearths and Coins on the Game Screen.
 * On change of hearth or coin redraws all UI elements.
 */
public class OverlayManager extends GameObject {
    private static final int DRAW_HEIGHT = 450;
    private static final int COIN_HEIGHT_OFFSET = -3;
    private static final int BACKGROUND_START = 0;
    private static final int COIN_START = 380;
    private static final int HEARTH_START = 650;
    private static final int COIN_STEP_SIZE = 75;
    private static final int HEART_STEP_SIZE = 55;
    private static final int BACKGROUND_PADDING = 10;

    // Forces redraw on first tick
    private int currentCoins = -1;
    private int currentHearths = -1;

    /**
     * Instantiates a new Game object.
     *
     * @param parent   the parent Object
     * @param game     the game in which this object lives
     * @param position the position of the object
     */
    public OverlayManager(GameObject parent, Game game, Point position) {
        super(parent, game, position);
    }

    @Override
    public void tick(double timeScale) {
        int hearths = getGame().getPlayerCar().getLives();
        int coins = getGame().getPlayerCar().getCoins();

        if (hearths != currentHearths || coins != currentCoins) {
            currentHearths = hearths;
            currentCoins = coins;
            redraw();
        }
    }

    private void redraw() {
        for (GameObject child : getChildren()) {
            child.kill();
        }

        new OverlayBackground(this, getGame(), new Point(BACKGROUND_START - BACKGROUND_PADDING, DRAW_HEIGHT - BACKGROUND_PADDING));

        for (int i = 0; i < currentCoins; i++) {
            new OverlayCoin(this, getGame(), new Point(COIN_START + i * COIN_STEP_SIZE, DRAW_HEIGHT + COIN_HEIGHT_OFFSET));
        }

        for (int i = 0; i < currentHearths; i++) {
            new OverlayHearth(this, getGame(), new Point(HEARTH_START + i * HEART_STEP_SIZE, DRAW_HEIGHT));
        }
    }
}
