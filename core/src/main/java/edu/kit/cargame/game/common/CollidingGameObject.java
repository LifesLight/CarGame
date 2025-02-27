package edu.kit.cargame.game.common;


import edu.kit.cargame.common.logging.LoggerManagement;
import edu.kit.cargame.game.geometry.BoundingBox;
import edu.kit.cargame.game.geometry.Point;

/**
 * The interface Collidable enforces an object to have a {@link BoundingBox} to check for collisions
 * and for the object to handle a collision in some way.
 */
public abstract class CollidingGameObject extends GameObject {

    /**
     * Instantiates a new CollidingGameObject.
     *
     * @param parent      the parent Object
     * @param game        the game in which this collidable lives
     * @param position    the position of the collidable
     * @param boundingBox the bounding box of the collidable
     */
    protected CollidingGameObject(GameObject parent, Game game, Point position, BoundingBox boundingBox) {
        super(new GameObject(parent, game, position) {
        }, game, Point.zero());
        super.setBoundingBox(boundingBox);
    }

    /**
     * Lets this object know that it has collided with the given object.
     * Should probably not be called directly; Use {@link CollidingGameObject#collide(CollidingGameObject, CollidingGameObject)} instead.
     *
     * @param other object to be collided with
     */
    protected abstract void handleCollision(CollidingGameObject other);


    /**
     * Checks whether this Object collides with the given Collidable.
     *
     * @param other the Collidable
     * @return boolean
     */
    public boolean collides(GameObject other) {
        return getGlobalBoundingBox().intersects(other.getGlobalBoundingBox());
    }


    /**
     * Checks whether this Object collides with the given Rectangle.
     *
     * @param rectangle the rectangle
     * @return boolean
     */
    public boolean collides(BoundingBox rectangle) {
        return getGlobalBoundingBox().intersects(rectangle);
    }

    /**
     * Collides the two objects, unless they are the same object.
     * Calls {@link CollidingGameObject#handleCollision(CollidingGameObject)} on both objects.
     *
     * @param c1 the first object
     * @param c2 the second object
     */
    private static void collide(CollidingGameObject c1, CollidingGameObject c2) {
        if (c1 == c2) {
            return;
        }
        c1.handleCollision(c2);
        c2.handleCollision(c1);
    }

    @Override
    public void setPosition(Point position) {
        getGame().collidableHasMoved(this);
        this.getParent().setPosition(position);
    }

    @Override
    public void addChild(GameObject child) {
        if (getParent() != null) {
            getParent().addChild(child);
        } else {
            LoggerManagement.getLogger().warning("Parent of colliding gameobject is null");
        }
    }

    @Override
    public final void setBoundingBox(BoundingBox boundingBox) {
        super.setBoundingBox(boundingBox);
        LoggerManagement.getLogger().warning("Yo, you just changed the size/shape of a colliding object on screen(e.g. Player Car). Are you sure you want to do this?");
    }

    @Override
    public Point getLocalPosition() {
        return getParent().getLocalPosition();
    }

    @Override
    public final void doCollision(CollidingGameObject other) {
        collide(other, this);
    }

}
