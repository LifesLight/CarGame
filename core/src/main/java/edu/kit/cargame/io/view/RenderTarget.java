package edu.kit.cargame.io.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import edu.kit.cargame.game.geometry.Point;

/**
 * A render target is a frame buffer that can be drawn to and then drawn to the screen.
 * It can be used to apply post-processing effects.
 */
public class RenderTarget {
    private final FrameBuffer frameBuffer;
    private final Texture texture;
    private final SpriteBatch spriteBatch;
    private ShaderProgram shader;

    /**
     * Instantiates a new Render target.
     *
     * @param width the width of the render target
     * @param height the height of the render target
     * @param vertexShader the vertex shader
     * @param fragmentShader the fragment shader
     * @throws IllegalArgumentException if the shader compilation fails
     */
    public RenderTarget(int width, int height, String vertexShader, String fragmentShader) {
        frameBuffer = new FrameBuffer(Pixmap.Format.RGBA8888, width, height, false);
        texture = frameBuffer.getColorBufferTexture();
        spriteBatch = new SpriteBatch();

        // Load shader
        if (fragmentShader != null) {
            shader = new ShaderProgram(vertexShader, fragmentShader);
            if (!shader.isCompiled()) {
                throw new IllegalArgumentException("Shader compilation failed: " + shader.getLog());
            }
        }
    }

    /**
     * Begin drawing to the render target.
     * @param r the red component of the clear color
     * @param g the green component of the clear color
     * @param b the blue component of the clear color
     * @param a the alpha component of the clear color
     */
    public void begin(float r, float g, float b, float a) {
        //Clear frame buffer
        frameBuffer.begin();
        Gdx.gl.glClearColor(r, g, b, a); // Set the clear color to black
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    /**
     * End drawing to the render target.
     * This will allow all draw calls after to draw to the screen.
     */
    public void end() {
        frameBuffer.end();
    }

    /**
     * Draw the render target to the screen.
     * @param x the x position to draw the texture
     * @param y the y position to draw the texture
     * @param width the width to draw the texture
     * @param height the height to draw the texture
     * @param flipY whether to flip the texture on the y axis
     */
    public void drawToScreen(float x, float y, float width, float height, boolean flipY) {
        drawToScreen(x, y, width, height, 0, 0, texture.getWidth(), texture.getHeight(), flipY);
    }

    /**
     * Set the uniforms of the shader.
     * @param time the time to set
     * @param isSlowdown whether to set the slowdown uniform
     * @param playerCarDirection the direction of the player car
     * @param playerCarHeadlightPos the position of the player car headlight
     */
    public void setUniforms(float time, boolean isSlowdown, int playerCarDirection, Point playerCarHeadlightPos) {
        if (shader != null) {
            shader.bind(); // Bind the shader before setting uniforms
            shader.setUniformf("u_time", time);
            shader.setUniformi("u_slowdown", isSlowdown ? 1 : 0);
            shader.setUniformi("u_playerCarDirection", playerCarDirection);
            float x = playerCarHeadlightPos.x() / texture.getWidth();
            float y = playerCarHeadlightPos.y() / texture.getHeight();
            shader.setUniform2fv("u_playerCarHeadlightPos", new float[]{x, y}, 0, 2);
        }
    }

    /**
     * Draw the render target to the screen.
     * @param x the x position to draw the texture
     * @param y the y position to draw the texture
     * @param width the width to draw the texture
     * @param height the height to draw the texture
     * @param srcX the x position of the source texture
     * @param srcY the y position of the source texture
     * @param srcWidth the width of the source texture
     * @param srcHeight the height of the source texture
     * @param flipY whether to flip the texture on the y axis
     */
    public void drawToScreen(float x, float y, float width, float height, int srcX, int srcY, int srcWidth, int srcHeight, boolean flipY) {

        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        texture.setWrap(Texture.TextureWrap.ClampToEdge, Texture.TextureWrap.ClampToEdge);

        texture.bind();

        // Draw the texture to the screen
        if (shader != null) {
            shader.bind();
            spriteBatch.setShader(shader);
        }
        spriteBatch.disableBlending();
        spriteBatch.begin();
        spriteBatch.draw(texture, x, y, width, height, srcX, srcY, srcWidth, srcHeight, false, flipY);
        spriteBatch.end();
        spriteBatch.setShader(null);
    }

    /**
     * Clean up resources used by the render target.
     */
    public void cleanup() {
        frameBuffer.dispose();
        spriteBatch.dispose();
        if (shader != null) {
            shader.dispose();
        }
    }
}
