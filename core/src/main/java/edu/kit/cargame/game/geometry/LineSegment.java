package edu.kit.cargame.game.geometry;

/**
 * The type LineSegment represents a line between two points.
 * @param start the start point of the line
 * @param end the end point of the line
 */
public record LineSegment(Point start, Point end) {
    /**
     * Calculate the distance between the line and a point.
     *
     * @param point the point to calculate the distance to
     * @return the distance between the line and the point
     */
    public float distance(Point point) {
        float a = point.x() - start.x();
        float b = point.y() - start.y();
        float c = end.x() - start.x();
        float d = end.y() - start.y();
        float dot = a * c + b * d;
        float lenSq = c * c + d * d;
        float param = dot / lenSq;
        float xx;
        float yy;
        if (param < 0) {
            xx = start.x();
            yy = start.y();
        } else if (param > 1) {
            xx = end.x();
            yy = end.y();
        } else {
            xx = start.x() + param * c;
            yy = start.y() + param * d;
        }
        float dx = point.x() - xx;
        float dy = point.y() - yy;
        return (float) Math.sqrt(dx * dx + dy * dy);
    }
}
