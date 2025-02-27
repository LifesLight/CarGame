package edu.kit.cargame.io.view.postprocessing;

/**
 * This class provides the vertex shader for the post processing effect.
 */
public final class VertexShader {

    private VertexShader() {
    //Do not instantiate
    }

    /**
     * Gets the shader.
     *
     * @return the vertex shader
     */
    public static String getShader() {
        return """
                #version 120
                attribute vec4 a_position;
                attribute vec2 a_texCoord0;

                uniform mat4 u_projTrans; // Projection and transformation matrix

                varying vec2 v_texCoord;

                void main() {
                    v_texCoord = a_texCoord0;
                    gl_Position = u_projTrans * a_position; // Apply projection transformation
                }
            """;
    }

}
