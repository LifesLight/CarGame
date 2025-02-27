package edu.kit.cargame.io.view;


import com.badlogic.gdx.graphics.Texture;

import java.util.HashMap;
import java.util.Map;

/**
 * Class used to cache textures to avoid loading them new for each new game object.
 * DO NOT dispose textures manually
 */
public final class TextureCache {

    private TextureCache() {
        // Utility class
    }

    private static final Map<String, Texture> CACHE = new HashMap<>();

    /**
     * Gets the texture from the cache or loads it if it is not in the cache.
     *
     * @param path the path to the texture
     * @return the texture
     */
    public static Texture getTexture(String path) {
        if (!CACHE.containsKey(path)) {
            CACHE.put(path, new Texture(path));
        }
        return CACHE.get(path);
    }

    /**
     * Disposes all textures in the cache.
     */
    public static void dispose() {
        for (Texture texture : CACHE.values()) {
            texture.dispose();
        }
        CACHE.clear();
    }
}
