package edu.kit.cargame.game.logic.spawning;

import edu.kit.cargame.game.geometry.BoundingBox;
import edu.kit.cargame.game.geometry.Point;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;


/**
 * The type Point sampler is a Utility Class helping the Spawner decide where to spawn obstacles.
 */
public final class PointTools {
    private PointTools() {
        // Utility
    }

    /**
     * Samples specified amount of random points inside bounding box.
     *
     * @param boundingBox    the box inside which to sample points
     * @param numberOfPoints the amount of points to sample
     * @param numLanes       the number of lanes to sample from
     * @param scatterFactor  the scatter factor to apply to the lanes. This means how far things can scatter from the middle of the lane
     * @param random         the random generator to use
     * @return collection of sampled points
     */
    public static Collection<Point> samplePoints(BoundingBox boundingBox, int numberOfPoints,
                                                 int numLanes, float scatterFactor, Random random) {
        List<Point> points = new ArrayList<>(numberOfPoints);

        final float minX = boundingBox.bottomLeft().x();
        final float minY = boundingBox.bottomLeft().y();
        final float maxX = boundingBox.topRight().x();
        final float maxY = boundingBox.topRight().y();

        float width = maxX - minX;
        float height = maxY - minY;

        float lineGap = height / numLanes;
        float offset = lineGap / 2;

        for (int i = 0; i < numberOfPoints; i++) {
            final float x = random.nextFloat() * width + minX;

            int lane = random.nextInt(numLanes);
            float laneCenter = minY + offset + lane * lineGap;

            float scatter = (random.nextFloat() - 0.5f) * lineGap * scatterFactor;
            final float y = laneCenter + scatter;

            points.add(new Point(x, y));
        }

        return points;
    }
}
