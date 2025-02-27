package edu.kit.cargame.io.input.implement;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import edu.kit.cargame.io.input.TextTypes;
import edu.kit.cargame.io.input.UserInput;

import java.util.ArrayList;
import java.util.List;

/**
 * The Text class extends the UserInput class with the type parameter {@link TextTypes}.
 * It serves as a handler for processing textual input by mapping keyboard inputs
 * (letters A-Z, numbers 0-9, and the dash symbol) to their corresponding {@link TextTypes}.
 */
public class Text extends UserInput<TextTypes> {

    @Override
    public List<TextTypes> pressed() {
        return rawPressed();
    }

    /**
     * Returns a list of buttons pressed.
     *
     * @return A list of buttons pressed.
     */
    public static List<TextTypes> rawPressed() {
        List<TextTypes> pressedKeys = new ArrayList<>();

        // Map letters A-Z
        if (Gdx.input.isKeyPressed(Input.Keys.A))
            pressedKeys.add(TextTypes.A);
        if (Gdx.input.isKeyPressed(Input.Keys.B))
            pressedKeys.add(TextTypes.B);
        if (Gdx.input.isKeyPressed(Input.Keys.C))
            pressedKeys.add(TextTypes.C);
        if (Gdx.input.isKeyPressed(Input.Keys.D))
            pressedKeys.add(TextTypes.D);
        if (Gdx.input.isKeyPressed(Input.Keys.E))
            pressedKeys.add(TextTypes.E);
        if (Gdx.input.isKeyPressed(Input.Keys.F))
            pressedKeys.add(TextTypes.F);
        if (Gdx.input.isKeyPressed(Input.Keys.G))
            pressedKeys.add(TextTypes.G);
        if (Gdx.input.isKeyPressed(Input.Keys.H))
            pressedKeys.add(TextTypes.H);
        if (Gdx.input.isKeyPressed(Input.Keys.I))
            pressedKeys.add(TextTypes.I);
        if (Gdx.input.isKeyPressed(Input.Keys.J))
            pressedKeys.add(TextTypes.J);
        if (Gdx.input.isKeyPressed(Input.Keys.K))
            pressedKeys.add(TextTypes.K);
        if (Gdx.input.isKeyPressed(Input.Keys.L))
            pressedKeys.add(TextTypes.L);
        if (Gdx.input.isKeyPressed(Input.Keys.M))
            pressedKeys.add(TextTypes.M);
        if (Gdx.input.isKeyPressed(Input.Keys.N))
            pressedKeys.add(TextTypes.N);
        if (Gdx.input.isKeyPressed(Input.Keys.O))
            pressedKeys.add(TextTypes.O);
        if (Gdx.input.isKeyPressed(Input.Keys.P))
            pressedKeys.add(TextTypes.P);
        if (Gdx.input.isKeyPressed(Input.Keys.Q))
            pressedKeys.add(TextTypes.Q);
        if (Gdx.input.isKeyPressed(Input.Keys.R))
            pressedKeys.add(TextTypes.R);
        if (Gdx.input.isKeyPressed(Input.Keys.S))
            pressedKeys.add(TextTypes.S);
        if (Gdx.input.isKeyPressed(Input.Keys.T))
            pressedKeys.add(TextTypes.T);
        if (Gdx.input.isKeyPressed(Input.Keys.U))
            pressedKeys.add(TextTypes.U);
        if (Gdx.input.isKeyPressed(Input.Keys.V))
            pressedKeys.add(TextTypes.V);
        if (Gdx.input.isKeyPressed(Input.Keys.W))
            pressedKeys.add(TextTypes.W);
        if (Gdx.input.isKeyPressed(Input.Keys.X))
            pressedKeys.add(TextTypes.X);
        if (Gdx.input.isKeyPressed(Input.Keys.Y))
            pressedKeys.add(TextTypes.Y);
        if (Gdx.input.isKeyPressed(Input.Keys.Z))
            pressedKeys.add(TextTypes.Z);

        // Map numbers 0-9
        if (Gdx.input.isKeyPressed(Input.Keys.NUM_0))
            pressedKeys.add(TextTypes.ZERO);
        if (Gdx.input.isKeyPressed(Input.Keys.NUM_1))
            pressedKeys.add(TextTypes.ONE);
        if (Gdx.input.isKeyPressed(Input.Keys.NUM_2))
            pressedKeys.add(TextTypes.TWO);
        if (Gdx.input.isKeyPressed(Input.Keys.NUM_3))
            pressedKeys.add(TextTypes.THREE);
        if (Gdx.input.isKeyPressed(Input.Keys.NUM_4))
            pressedKeys.add(TextTypes.FOUR);
        if (Gdx.input.isKeyPressed(Input.Keys.NUM_5))
            pressedKeys.add(TextTypes.FIVE);
        if (Gdx.input.isKeyPressed(Input.Keys.NUM_6))
            pressedKeys.add(TextTypes.SIX);
        if (Gdx.input.isKeyPressed(Input.Keys.NUM_7))
            pressedKeys.add(TextTypes.SEVEN);
        if (Gdx.input.isKeyPressed(Input.Keys.NUM_8))
            pressedKeys.add(TextTypes.EIGHT);
        if (Gdx.input.isKeyPressed(Input.Keys.NUM_9))
            pressedKeys.add(TextTypes.NINE);

        // Map dash
        if (Gdx.input.isKeyPressed(Input.Keys.MINUS))
            pressedKeys.add(TextTypes.DASH);

        return pressedKeys;
    }

}
