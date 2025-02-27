package edu.kit.cargame.game.object.collectable;

import edu.kit.cargame.game.common.Game;
import edu.kit.cargame.game.geometry.BoundingBox;
import edu.kit.cargame.game.geometry.Point;
import edu.kit.cargame.game.common.GameObject;
import edu.kit.cargame.game.playercar.PlayerCar;
import edu.kit.cargame.io.view.gamerenderers.SpriteRenderer;

/**
 * The type CollectableOilPuddle is a collectable that decreases the player cars agility for a short amount of time.
 */
public class CollectableOilPuddle extends Collectable {
    /**
     * Instantiates a new Collectable.
     *
     * @param parent   the parent Object
     * @param game     the game in which this collectable lives
     * @param position the position of the collectable
     * @param boundingBox the bounding box of the collectable
     */
    public CollectableOilPuddle(GameObject parent, Game game, Point position, BoundingBox boundingBox) {
        super(parent, game, position, boundingBox);
        setRenderer(new SpriteRenderer(this, "game/obstacles/oil_puddle.png"));
    }
    @Override
    public void handleCollision(PlayerCar playerCar) {
        playerCar.worsenSteering(getGame().getConfig().oilDuration());
        negateScore(playerCar);
    }

    @Override
    protected void pickup() {
        //cant pickup an oil puddle
    }
}
