package edu.kit.cargame.game.geometry;


/**
 * The type Bounding box is a Rectangle which describes the position of an Object in a 2D coordinate system.
 * The rectangle is always aligned horizontally = it cannot be turned
 * @param bottomLeft the bottom left corner of the BB
 * @param topRight the top right corner of the BB
 */
public record BoundingBox(Point bottomLeft, Point topRight) {

    /**
     * Create an empty new Bounding Box.
     *
     * @return a new empty Bounding Box
     */
    public static BoundingBox empty() {
        return new BoundingBox(new Point(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY),
            new Point(Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY));

    }

    /**
     * the top right corner of this BB.
     *
     * @param topRight corner
     */
    public BoundingBox(Point topRight) {
        this(Point.zero(), topRight);
    }

    /**
     * Create a new Bounding Box where both corners are Zero.
     */
    public BoundingBox() {
        this(Point.zero());
    }

    /**
     * Check if this {@link BoundingBox} intersects with the given other {@link BoundingBox} .
     *
     * @param other the other
     * @return the boolean
     */
    public boolean intersects(BoundingBox other) {
        return this.topRight.y() > other.bottomLeft.y() &&
            this.bottomLeft.y() < other.topRight.y() &&
            this.topRight.x() > other.bottomLeft.x() &&
            this.bottomLeft.x() < other.topRight.x();
    }

    /**
     * Create a new {@link BoundingBox} which has been moved along a given Vector (Point).
     *
     * @param offset the offset
     * @return a new BoundingBox
     */
    public BoundingBox offset(Point offset) {
        return new BoundingBox(bottomLeft.add(offset), topRight.add(offset));
    }

    /**
     * Check if this BB completely surrounds the given BB
     *
     * @return boolean
     */
    public boolean contains(BoundingBox boundingBox) {
        return bottomLeft.x() <= boundingBox.bottomLeft.x() &&
            bottomLeft.y() <= boundingBox.bottomLeft.y() &&
            topRight.x() >= boundingBox.topRight.x() &&
            topRight.y() >= boundingBox.topRight.y();
    }

    /**
     * Adds the offset to both the x and y coordinate of the top right corner
     *
     * @param offset the amount by which this BB is enlarged
     * @return a new BB
     */
    public BoundingBox enlarge(float offset) {
        return new BoundingBox(bottomLeft, topRight.add(new Point(offset, offset)));
    }

    /**
     * Creates the smallest BB which contains both given BB's.
     *
     * @param boundingBox1 the first BB
     * @param boundingBox2 the second BB
     * @return a new BB which contains both
     */
    public static BoundingBox combine(BoundingBox boundingBox1, BoundingBox boundingBox2) {
        return new BoundingBox(
            new Point(
                Math.min(boundingBox1.bottomLeft.x(), boundingBox2.bottomLeft.x()),
                Math.min(boundingBox1.bottomLeft.y(), boundingBox2.bottomLeft.y())
            ),
            new Point(
                Math.max(boundingBox1.topRight.x(), boundingBox2.topRight.x()),
                Math.max(boundingBox1.topRight.y(), boundingBox2.topRight.y())
            )
        );
    }

    /**
     * Gets the width of this BB.
     *
     * @return the width
     */
    public float getWidth() {
        return topRight.x() - bottomLeft.x();
    }

    /**
     * Gets the height of this BB.
     *
     * @return the height
     */
    public float getHeight() {
        return topRight.y() - bottomLeft.y();
    }

    /**
     * Gets the middle point of the right side of this BB.
     *
     * @return the middle point
     */
    public Point getMiddleRight() {
        return new Point(topRight.x(), (topRight.y() + bottomLeft.y()) / 2);
    }

}
