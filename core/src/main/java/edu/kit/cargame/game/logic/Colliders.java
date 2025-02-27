package edu.kit.cargame.game.logic;

import edu.kit.cargame.game.common.Game;
import edu.kit.cargame.game.geometry.BoundingBox;
import edu.kit.cargame.game.geometry.Point;
import edu.kit.cargame.game.common.GameObject;
import edu.kit.cargame.game.object.obstacle.InvisibleObstacle;

/**
 * Colliders is the "root node" encompassing all {@link GameObject}'s which need to be evaluated
 * for collision calculations.
 * <p>
 * On creation {@link Colliders} creates some {@link GameObject}'s directly, while some get added by {@link Game}
 * later.
 */
public class Colliders extends GameObject {

    /**
     * On creation Colliders created the two {@link InvisibleObstacle}'s on top of and below the
     * {@link edu.kit.cargame.game.playercar.PlayerCar} which limit its legal range of vertical Positions.
     *
     * @param game The back reference to the Game
     */
    public Colliders(Game game) {
        super(null, game, new Point(0, 0));

        new InvisibleObstacle(this, game, new Point(30, 420), new BoundingBox(new Point(20, 5)), true);
        new InvisibleObstacle(this, game, new Point(30, 10), new BoundingBox(new Point(20, 5)), false);
    }
}
