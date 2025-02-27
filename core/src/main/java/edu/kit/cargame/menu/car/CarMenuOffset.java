package edu.kit.cargame.menu.car;

import edu.kit.cargame.menu.Menu;

/**
 * CarMenuOffset The offset which needs to be applied to items to appear on
 * a menu.
 * As the menu can be at different locations, depending on which game mode was
 * selected,
 * Each element on that menu needs to be offset to compensate for this.
 * It provides easy selection of the offset, depending on which menu
 * should be written on.
 */
public enum CarMenuOffset {
    /**
     * The offset needed for single-player.
     */
    SINGLE(-182.5f),
    /**
     * The offset needed for the left menu in multi-player.
     */
    DOUBLE_LEFT(-365f),
    /**
     * The offset needed for the right menu in multi-player.
     */
    DOUBLE_RIGHT(0f);

    private final float offsetX;
    private final float offsetY = -267.5f - Menu.BIG_LOGO_HEIGHT / 2f;

    CarMenuOffset(float offsetX) {
        this.offsetX = offsetX;
    }

    /**
     * Returns the horizontal offset needed to render items on the given
     * menu.
     *
     * @return The horizontal offset needed to render items on the given
     *         menu.
     */
    public float offsetX() {
        return offsetX;
    }

    /**
     * Returns the vertical offset needed to render items on the given menu.
     *
     * @return The vertical offset needed to render items on the given menu.
     */
    public float offsetY() {
        return offsetY;
    }
}
