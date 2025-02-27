package edu.kit.cargame.io.input;

/**
 * TextTypes is a collection of text-related characters, including letters, numbers, and special characters.
 * It provides functionality to convert each enum value into its corresponding character representation.
 */
public enum TextTypes {
    // Letters A-Z
    A, B, C, D, E, F, G, H, I, J, K, L, M,
    N, O, P, Q, R, S, T, U, V, W, X, Y, Z,

    // Numbers 0-9
    ZERO, ONE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE,

    // Dash
    DASH;

    /**
     * Returns the character associated with the input.
     * @return The character associated with the input.
     * @throws IllegalStateException if the input is not handled by the enum.
     */
    public char toChar() {
        // For letters A-Z
        if (this.ordinal() >= A.ordinal() && this.ordinal() <= Z.ordinal()) {
            return (char) ('A' + this.ordinal());
        }

        // For numbers 0-9
        if (this.ordinal() >= ZERO.ordinal() && this.ordinal() <= NINE.ordinal()) {
            return (char) ('0' + (this.ordinal() - ZERO.ordinal()));
        }

        // For DASH
        if (this == DASH) {
            return '-';
        }

        throw new IllegalStateException("Unhandled enum value: " + this);
    }
}
