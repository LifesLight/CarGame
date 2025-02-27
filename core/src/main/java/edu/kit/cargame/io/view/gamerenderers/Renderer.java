package edu.kit.cargame.io.view.gamerenderers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Batch;
import edu.kit.cargame.common.logging.LoggerManagement;

import java.util.HashMap;

/**
 * Abstract class representing a single gameObject renderer.
 * Each kind of gameObject with different rendering behaviour has its own renderer.
 * The renderer should check the objects state and render it accordingly.
 * Render function should not be used to count time for things anyway related to in game passing of time.
 * For that use the game objects creation time and the game time.
 * Each game object generally has a renderer and each renderer a game object.
 * Because the renderer, however, tends to specify its game object type further, it is not part of the abstract class.
 */
public abstract class Renderer {

    // Cache sounds to avoid loading
    private static final HashMap<String, Sound> SOUND_CACHE = new HashMap<>();

    /**
     * Render the renderers object or other things to the render batch.
     *
     * @param batch the batch
     */
    public abstract void render(Batch batch);

    /**
     * Play a sound from the given path.
     *
     * @param path the path
     */
    protected void playSound(String path) {
        Sound sound = loadSound(path);
        if (sound == null) {
            LoggerManagement.getLogger().error("Couldnt load sound from path:" + path);
            return;
        }
        sound.play();
    }

    private Sound loadSound(String path) {
        if (path == null) {
            return null;
        }
        if (!SOUND_CACHE.containsKey(path)) {
            SOUND_CACHE.put(path, Gdx.audio.newSound(Gdx.files.internal(path)));
        }
        return SOUND_CACHE.get(path);
    }


    /**
     * Clean up all resources used by the renderer.
     */
    public void cleanup() {
        // Clean up
    }


}
