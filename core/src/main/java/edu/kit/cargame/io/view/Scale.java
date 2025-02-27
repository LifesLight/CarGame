package edu.kit.cargame.io.view;

/**
 * As LibGDX does not support pixel-perfect image scaling, we need to prescale
 * assets in the graphics program.
 * This enum contains the common Scale factors with their scales and names. This
 * allows us to automate
 * logic which uses multiple scales.
 */
public enum Scale {
    /**
     * The default scale size.
     */
    NORMAL(5, "normal"),
    /**
     * The larger scale size.
     */
    LARGE(10, "large");

    private final int scale;
    private final String name;

    Scale(int scale, String name) {
        this.name = name;
        this.scale = scale;
    }

    /**
     * Returns the scale.
     *
     * @return The scale.
     */
    public int getScale() {
        return scale;
    }

    /**
     * Returns the name of the scale. This should be used as a directory when assets
     * are exported in multiple scales.
     * E.g., characters/ should contain characters/normal and characters/large.
     *
     * @return The name of the scale.
     */
    public String getName() {
        return name;
    }
}
