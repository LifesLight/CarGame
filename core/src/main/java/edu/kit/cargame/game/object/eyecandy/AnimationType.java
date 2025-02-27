package edu.kit.cargame.game.object.eyecandy;

import edu.kit.cargame.game.geometry.Point;

/**
 * The type AnimationType is an enum that holds all the information needed to play a specific animation.
 */
public enum AnimationType {
    COIN_PICKUP("collectables/coin", 13, 1, 1.0f, "game/sounds/coin.wav", 0.3f, 100),
    HEART_PICKUP("collectables/heart", 9, 1, 1.0f, "game/sounds/heart.wav", 0.3f, 100),
    STOP_WATCH_PICKUP("collectables/stop_watch", 20, 1, 1.0f, "game/sounds/stopwatch.wav", 0.5f, 200),
    STAR_PICKUP("collectables/star", 15, 1, 1.0f, "game/sounds/star.wav", 0.5f, 200),
    EXPLODE("explode", 13, 1, 1.0f, "game/sounds/crash.wav", 1.0f, 400),
    CLOSE_CALL("closecall", 10, 1, 1.0f, "game/sounds/close_call.wav", 0.3f, 100),
    TEST("test", 5, 2, 1.0f, null, 0.0f, 0);

    private final String name;
    private final int frameCount;
    private final int frameDuration;
    private final float scale;
    private final Point offset;
    private final String soundLocation;
    private final float rumbleIntensity;
    private final int rumbleDuration;

    AnimationType(String name, int frameCount, int frameDuration, float scale, Point offset,
                  String soundLocation, float rumbleIntensity, int rumbleDuration) {
        this.name = name;
        this.frameCount = frameCount;
        this.frameDuration = frameDuration;
        this.scale = scale;
        this.offset = offset;
        this.soundLocation = soundLocation;
        this.rumbleIntensity = rumbleIntensity;
        this.rumbleDuration = rumbleDuration;
    }

    AnimationType(String name, int frameCount, int frameDuration, float scale,
                  String soundLocation, float rumbleIntensity, int rumbleDuration) {
        this(name, frameCount, frameDuration, scale, new Point(0, 0), soundLocation, rumbleIntensity, rumbleDuration);
    }

    /**
     * Gets the name of the animation.
     *
     * @return the name of the animation
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the frame count of the animation.
     *
     * @return the frame count of the animation
     */
    public int getFrameCount() {
        return frameCount;
    }

    /**
     * Gets the frame duration of the animation.
     *
     * @return the frame duration of the animation
     */
    public int getFrameDuration() {
        return frameDuration;
    }

    /**
     * Gets the scale of the animation.
     *
     * @return the scale of the animation
     */
    public float getScale() {
        return scale;
    }

    /**
     * Gets the offset of the animation.
     *
     * @return the offset of the animation
     */
    public Point getOffset() {
        return offset;
    }

    /**
     * Gets the sound location of the animation.
     * This means the path to the sound file that should be played when the animation is played.
     *
     * @return the sound location of the animation
     */
    public String getSoundLocation() {
        return soundLocation;
    }

    /**
     * Gets the rumble intensity of the animation.
     * This means the intensity of the rumble that should be played when the animation is played.
     *
     * @return the rumble intensity of the animation
     */
    public float getRumbleIntensity() {
        return rumbleIntensity;
    }

    /**
     * Gets the rumble duration of the animation.
     * This means the duration of the rumble that should be played when the animation is played.
     *
     * @return the rumble duration of the animation
     */
    public int getRumbleDuration() {
        return rumbleDuration;
    }
}
