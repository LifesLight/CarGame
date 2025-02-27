package edu.kit.cargame.game.geometry;

/**
 * The type Point describes a Point in 2D space using x and y Coordinates.
 * @param x the x coordinate
 * @param y the y coordinate
 */
public record Point(float x, float y) {

    /**
     * Creates a new Point, which is the result of adding this Vector to the given other Vector.
     *
     * @param other the Point to be added
     * @return a new Point
     */
    public Point add(Point other) {
        return new Point(x + other.x, y + other.y);
    }

    /**
     * Creates a new Point, which is the result of adding the given x value to the x value of this Point.
     *
     * @param x the x value to be added
     * @return a new Point
     */
    public Point addX(float x) {
        return new Point(this.x + x, y);
    }

    /**
     * Creates a new Point, which is the result of adding the given y value to the y value of this Point.
     *
     * @param y the y value to be added
     * @return a new Point
     */
    public Point addY(float y) {
        return new Point(x, this.y + y);
    }

    /**
     * Creates a new Point, which is the result of scaling this Point by the given value.
     *
     * @param value the value to scale by
     * @return a new Point
     */
    public Point scale(float value) {
        return new Point(this.x * value, this.y * value);
    }

    /**
     * Calculates the distance between this Point and the given other Point.
     *
     * @param other the other Point
     * @return the distance
     */
    public float distance(Point other) {
        return (float) Math.sqrt(Math.pow(this.x - other.x, 2) + Math.pow(this.y - other.y, 2));
    }

    /**
     * Consider the points as vectors and add them.
     *
     * @param a first vector
     * @param b second vector
     * @return new Point which is the sum of both
     */
    public static Point add(Point a, Point b) {
        return new Point(a.x + b.x, a.y + b.y);
    }

    /**
     * Creates a new Point with x and y set to 0.
     *
     * @return a new Point
     */
    public static Point zero() {
        return new Point(0, 0);
    }

    /**
     * Creates a new Point with x and y set to -1 * this.x and -1 * this.y.
     *
     * @return a new Point
     */
    public Point negative() {
        return new Point(-x, -y);
    }
}
