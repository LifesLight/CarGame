package edu.kit.cargame.io.view.postprocessing;

/**
 * Wrapper class used to get the shader code for the chromatic abberation shader.
 */
public final class ChromaticAbberationShader {

    /**
     * Private constructor to prevent instantiation.
     */
    private ChromaticAbberationShader() {
        // Do nothing
    }
    /**
     * Get the shader code for the chromatic abberation shader.
     * @return the shader code
     */
    public static String getShader() {
        return """
            #version 120

            uniform sampler2D u_texture;
            varying vec2 v_texCoord;

            uniform vec2 u_playerCarHeadlightPos;
            // 0 = left up, 1 = left, 2 = ahead, 3 = right, 4 = right down
            uniform int u_playerCarDirection;
            uniform float u_time;
            uniform int u_slowdown;

            void main() {
                // Use uniforms so they dont get optimized out causing the game to crash
                float x = u_time;

                if(x < 0.0) {
                discard;}
                vec4 color = texture2D(u_texture, v_texCoord);
                if (u_slowdown == 1) {
                                //Chromatic Abberation
                    float r = texture2D(u_texture, v_texCoord + vec2(0.004, 0)).r;
                    float g = texture2D(u_texture, v_texCoord + vec2(0.00, 0)).g;
                    float b = texture2D(u_texture, v_texCoord + vec2(-0.004, 0)).b;
                    color = vec4(r, g, b, 1.0);
                }
                // Draw dark mask over whole screen but cut out light cone in front of player car
                float headLightMask = 0.0;
                float carDirectionAngle;
                if (u_playerCarDirection == 0) {
                    carDirectionAngle = 1.05;
                }
                else if (u_playerCarDirection == 1) {
                    carDirectionAngle = 0.75;
                }
                else if (u_playerCarDirection == 2) {
                    carDirectionAngle = 0.0;
                }
                else if (u_playerCarDirection == 3) {
                    carDirectionAngle = -0.75;
                }
                else {
                    carDirectionAngle = -1.05;
                }
                // Mask with 0 meaning dark and 1 being bright
                // Smoothstep to make light cone have round corners
                float carPixelAngle = atan(v_texCoord.y - u_playerCarHeadlightPos.y, v_texCoord.x - u_playerCarHeadlightPos.x);
                headLightMask = 1 - smoothstep(0.1, 0.3, abs(carPixelAngle - carDirectionAngle));
                float lightConeStartMask = smoothstep(0.05, 0.1, v_texCoord.x - u_playerCarHeadlightPos.x);
                headLightMask *= lightConeStartMask;
                //Shade left of car dark
                if (v_texCoord.x - u_playerCarHeadlightPos.x < 0.0) {
                    headLightMask = 0.0;
                }
                float nightCycleFactor = sin((x + 1900) / 500);
                float nightMixFactor = 1.5 * nightCycleFactor + 0.5;
                nightMixFactor = clamp(nightMixFactor, 0.0, 1.0);
                if (nightMixFactor > 0.2) {
                    nightMixFactor *= (1 - (headLightMask));
                }
                color = mix(color, vec4(0.0, 0.0, 0.3, 1.0), (0.4 * nightMixFactor));



                gl_FragColor = color;
            }
            """;
    }
}
