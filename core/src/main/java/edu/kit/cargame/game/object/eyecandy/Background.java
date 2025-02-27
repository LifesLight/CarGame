package edu.kit.cargame.game.object.eyecandy;


import edu.kit.cargame.game.common.Game;
import edu.kit.cargame.game.common.GameObject;
import edu.kit.cargame.game.geometry.BoundingBox;
import edu.kit.cargame.game.geometry.Point;

/**
 * The type Background is a non-moving, non-collidable GameObject, which just displays a changing Sprite.
 */
public class Background extends GameObject {
    private static final int CHUNK_AMOUNT_ON_SCREEN = 2;
    private static final int BIOME_LENGTH = 10;
    private static final int TOTAL_CHUNK_AMOUNT = CHUNK_AMOUNT_ON_SCREEN + 2;

    private final int chunkSize;
    private final int height;

    private int chunksUntilBiomeChange = BIOME_LENGTH;
    private BackgroundType currentBiome = BackgroundType.GRASS;

    /**
     * Instantiates a new Background.
     *
     * @param parent   the parent Object
     * @param game     the game in which this background lives
     * @param position the position of the background
     * @param width    the width of the background
     * @param height   the height of the background
     */
    public Background(GameObject parent, Game game, Point position, int width, int height) {
        super(parent, game, position);


        setBoundingBox(new BoundingBox(new Point(width, height)));

        chunkSize = width / CHUNK_AMOUNT_ON_SCREEN;
        this.height = height;

        for (int i = 0; i < TOTAL_CHUNK_AMOUNT; i++) {
            new BackgroundChunk(this, getGame(), new Point(i * chunkSize, 0), new BoundingBox(new Point(chunkSize, height)), currentBiome);
        }

    }

    @Override
    protected void updateBounds(GameObject updatedChild) {
        //i dont think so bitch
    }

    @Override
    public void tick(double timeScale) {
        if (getChildren().size() < TOTAL_CHUNK_AMOUNT) {
            new BackgroundChunk(this, getGame(), new Point(endX(), 0), new BoundingBox(new Point(chunkSize, height)), currentBiome);
            chunksUntilBiomeChange--;
            updateBiome();
        }
    }

    private void updateBiome() {
        if (chunksUntilBiomeChange <= 0) {
            chunksUntilBiomeChange = BIOME_LENGTH;
            currentBiome = BackgroundType.getRandom(getGame().getRandom());
        }
    }

    private float endX() {
        if (getChildren().isEmpty()) {
            return 0;
        }
        return getChildren().getLast().getBoundingBox().topRight().x();
    }
}
