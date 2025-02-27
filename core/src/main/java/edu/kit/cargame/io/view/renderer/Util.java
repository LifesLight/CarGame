package edu.kit.cargame.io.view.renderer;

import com.badlogic.gdx.graphics.Texture;
import edu.kit.cargame.io.view.Scale;
import edu.kit.cargame.io.view.TextureCache;

import java.util.ArrayList;
import java.util.List;

/**
 * Util provides utility functions to assist with interacting with libGDXs graphics system.
 */
public final class Util {


    private Util() {
        // Utility class
    }

    /**
     * This function loads multiple textures at given location into the given list.
     * While libgdx has built in support for animations, this requires complex setup
     * and only provides marginal performance setup.
     * As we use undemanding pixel art, we can just load animations as a list of separate
     * textures.
     * This function is idempotent.
     *
     * @param name       The formated string used to generate the paths. E.g., for
     *                   "assets/cargame/default/car(1,2,3).png" use
     *                   "cargame/car%d.png" with a count of three.
     * @param count      The number off assets to be loaded.
     * @param startIndex The index to start at.
     * @return The list of textures.
     */
    public static List<Texture> loadNTextures(String name, int count, int startIndex) {
        List<Texture> list = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            list.add(TextureCache.getTexture(String.format(name, i + startIndex)));
        }
        return list;
    }

    /**
     * Same as {@link #loadNTextures(String, int, int)} but starts at 1.
     *
     * @param name  The formated string used to generate the paths. E.g., for
     *              "assets/cargame/default/car(1,2,3).png" use
     *              "cargame/car%d.png" with a count of three.
     * @param count The number off assets to be loaded.
     * @return The list of textures.
     */
    public static List<Texture> loadNTextures(String name, int count) {
        return loadNTextures(name, count, 1);
    }


    /**
     * Same as {@link #loadNTextures(String, int)} but allows selecting a scale.
     * Note that this means the name needs to contain an extra %s where the dir is located.
     *
     * @param name  The formated string used to generate the paths. E.g., for
     *              "assets/cargame/characters/scale/d1.png" use
     *              "cargame/characters/%s/d%d.png".
     * @param count The number off assets to be loaded.
     * @param scale The scale which should be loaded.
     * @return The list of textures.
     */
    public static List<Texture> loadNTextures(String name, int count, Scale scale) {
        List<Texture> list = new ArrayList<>();
        for (int i = 1; i <= count; i++) {
            list.add(new Texture(String.format(name, scale.getName(), i)));
        }
        return list;
    }
}
