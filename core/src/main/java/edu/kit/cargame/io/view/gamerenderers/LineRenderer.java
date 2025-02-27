package edu.kit.cargame.io.view.gamerenderers;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import edu.kit.cargame.game.geometry.Point;
import edu.kit.cargame.game.logic.spawning.Chunk;
import edu.kit.cargame.io.view.TextureCache;

/**
 * The type Line renderer is a renderer for drawing lines between points.
 * Used for debugging the spawning algorithm.
 */
public class LineRenderer extends Renderer {
    private final Chunk chunk;
    private final Texture box;

    /**
     * Instantiates a new Line renderer.
     *
     * @param chunk the chunk holding the points to draw lines between
     */
    public LineRenderer(Chunk chunk) {
        this.chunk = chunk;
        this.box = TextureCache.getTexture("game/box.png");
    }

    @Override
    public void render(Batch batch) {
        Point basePoint = chunk.getGlobalPosition();
        batch.setColor(1, 0, 0, 1);
        for (int i = 0; i < chunk.getLinePoints().size() - 1; i++) {
            drawLine(batch, chunk.getLinePoints().get(i).x() + basePoint.x(),
                chunk.getLinePoints().get(i).y() + basePoint.y(), chunk.getLinePoints().get(i + 1).x() + basePoint.x(),
                chunk.getLinePoints().get(i + 1).y() + basePoint.y(), 3);
        }
    }

    private void drawLine(Batch batch, float x1, float y1, float x2, float y2, float thickness) {
        float dx = x2 - x1;
        float dy = y2 - y1;
        float dist = (float) Math.sqrt(dx * dx + dy * dy);
        float rad = (float) Math.atan2(dy, dx);
        batch.draw(box, x1, y1, 0, thickness / 2, dist, thickness, 1, 1,
            rad * (180 / (float) Math.PI), 0, 0, box.getWidth(), box.getHeight(), false, false);
    }
}
