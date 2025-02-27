package edu.kit.cargame.game.common;


import edu.kit.cargame.game.geometry.BoundingBox;
import edu.kit.cargame.game.geometry.Point;
import edu.kit.cargame.io.view.gamerenderers.Renderer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * The type Game object is a parent class for all possible things which are in the game.
 * This includes the player car and obstacles but also the invisible borders at the side of
 * the road and the animations.
 * <p>
 * The bounding boxes are updated via a greedy resizing algorithm:
 * Bounding boxes get scaled up when a child gets added that would be outside the current bounds.
 * If a child gets removed the current bounding box never gets scaled down, this could lead to some boxes
 * growing over time, this is not a massive concern tho since it will still be faster to do slightly suboptimal
 * collision checks then having to completely recalculate the entire collision hierarchy on every leaf position change.
 */
public abstract class GameObject {
    /**
     * The Parent GameObject.
     * May be null.
     */
    private GameObject parent;
    /**
     * The Position relative to the parent.
     */
    private Point position;

    /**
     * The Game in which this object lives.
     */
    private final Game game;

    /**
     * The Renderer, if this object shows up on screen.
     * May be null.
     */
    private Renderer renderer;

    private final double creationTime;

    /**
     * The bounding box of the Object.
     * If not changed malformed so nothing intersects with it.
     */
    private BoundingBox boundingBox = BoundingBox.empty();

    /**
     * The Children of this object.
     */
    private final List<GameObject> children = new ArrayList<>();


    /**
     * Instantiates a new Game object.
     *
     * @param parent   the parent Object
     * @param game     the game in which this object lives
     * @param position the position of the object
     */
    protected GameObject(GameObject parent, Game game, Point position) {
        this.creationTime = game.getCurrentTime();
        this.game = game;
        this.position = position;
        if (parent != null) {
            game.registerAddChild(parent, this);
        }
    }


    /**
     * Check if other collides with any of the children of this GameObject.
     * If so, call doCollision on the child.
     *
     * @param other the other GameObject
     */
    public void doCollision(CollidingGameObject other) {
        for (GameObject child : children) {
            if (other.collides(child)) {
                child.doCollision(other);
            }
        }
    }

    private void setParent(GameObject parent) {
        this.parent = parent;
    }

    /**
     * Gets the children of this GameObject.
     *
     * @return the children
     */
    protected List<GameObject> getChildren() {
        return Collections.unmodifiableList(children);
    }

    /**
     * Attach a renderer to this GO
     *
     * @param renderer the renderer which will handle this GO
     */
    protected void setRenderer(Renderer renderer) {
        if (this.renderer != null) {
            this.renderer.cleanup();
        }
        this.renderer = renderer;
    }

    /**
     * Get the parent of this Game Object
     *
     * @return the parent
     */
    protected final GameObject getParent() {
        return parent;
    }

    /**
     * Recursively updates parents bounding boxes if necessary.
     *
     * @param updatedChild the {@link GameObject} child which got changed
     */
    protected void updateBounds(GameObject updatedChild) {
        BoundingBox newBoundingBox = BoundingBox.combine(boundingBox, updatedChild.getBoundingBox());

        if (!boundingBox.equals(newBoundingBox)) {
            setBoundingBox(newBoundingBox);
        }
    }


    /**
     * Sets this GameObjects bounding box.
     * Updates the parents bounding boxes automatically.
     *
     * @param boundingBox the new bounding box
     */
    protected void setBoundingBox(BoundingBox boundingBox) {
        this.boundingBox = boundingBox;

        if (parent != null) {
            parent.updateBounds(this);
        }
    }

    /**
     * Gets the bounding box of this GO.
     *
     * @return the BB
     */
    public BoundingBox getBoundingBox() {
        return boundingBox.offset(position);
    }

    /**
     * Returns bounding box offset by global coordinates.
     *
     * @return the bounding box of the object in global coordinate space.
     */
    public BoundingBox getGlobalBoundingBox() {
        return boundingBox.offset(getGlobalPosition());
    }

    /**
     * Gets the current total position in the world.
     *
     * @return the position
     */
    public final Point getGlobalPosition() {
        if (parent == null) {
            return position;
        }

        return position.add(parent.getGlobalPosition());
    }

    /**
     * Gets the GameObjects position in local Coordinate space.
     *
     * @return the GameObjects local position
     */
    protected Point getLocalPosition() {
        return position;
    }

    /**
     * Move the GO to a new Position.
     *
     * @param position the new Position for this GO
     */
    public void setPosition(Point position) {
        this.position = position;
        if (parent != null) {
            parent.updateBounds(this);
        }
    }

    /**
     * Tells the GameObject to compute one step of the game.
     * This may move the object in a certain direction, or do nothing.
     *
     * @param timeScale global time scale which should be multiplied with all changes
     */
    protected void tick(double timeScale) {

    }

    /**
     * Ticks the game object and all of its children.
     *
     * @param timeScale the global timescale to apply to all ticks
     */
    public final void tickTree(double timeScale) {

        tick(timeScale * (needAllTimescales() ? getGame().getBonusTimescale() : 1));
        for (GameObject child : children) {
            child.tickTree(timeScale);
        }
    }

    /**
     * Basically, each child class can implement this. If it is true, you also get {@link Game#getBonusTimescale()} multiplied
     * to your timescale for your {@link GameObject#tick} function, instead of just the normal timescale
     *
     * @return boolean
     */
    protected boolean needAllTimescales() {
        return false;
    }


    /**
     * Adds child to the GameObject.
     * Automatically updates bounding boxes of parent tree.
     * Also assigns itself as new parent of child.
     *
     * @param child the child to add
     */
    public void addChild(GameObject child) {
        children.add(child);
        child.setParent(this);
        updateBounds(child);
    }

    /**
     * Removes this object and its renderer from the Game or parent which hold it.
     * Allows this object to be garbage collected.
     */
    public final void kill() {
        getGame().markForExecution(this);
    }


    /**
     * Removes this object and its renderer from the Game or parent which hold it.
     * Allows this object to be garbage collected.
     */
    public final void takeOut() {
        if (parent != null) {
            parent.remove(this);
        }
        parent = null;
        for (GameObject child : children) {
            game.markForExecution(child);
        }
        if (renderer != null) {
            renderer.cleanup();
        }
        children.clear();
        setRenderer(null);
    }

    /**
     * Remove a GameObject from the list of children.
     *
     * @param gameObject the Object to remove
     */
    private void remove(GameObject gameObject) {
        children.remove(gameObject);
    }

    /**
     * Gets the renderer which is responsible for this object.
     * The Optional may be empty.
     *
     * @return the renderer as an Optional
     */
    public final Optional<Renderer> getRenderer() {
        return Optional.of(renderer);
    }

    /**
     * Returns a collection of all renderers, which should currently display something to the user.
     *
     * @return a collection
     */
    public final Collection<Renderer> getAllRenderers() {
        Collection<Renderer> renderers = new ArrayList<>();
        if (renderer != null) {
            renderers.add(renderer);
        }
        for (GameObject child : children) {
            renderers.addAll(child.getAllRenderers());
        }
        return renderers;
    }

    /**
     * Gets the time when the object was created.
     *
     * @return the creation time
     */
    public final double getCreationTime() {
        return creationTime;
    }

    /**
     * Gets the game in which this Object lives.
     *
     * @return the game
     */
    public final Game getGame() {
        return game;
    }

    /**
     * This function determines whether this GO should destroy collectables.
     *
     * @return boolean
     */
    public boolean destroysCollectables() {
        return false;
    }


    /**
     * Gets the debug text of this object.
     *
     * @return the debug text
     */
    public String getDebugText() {
        return "hi";
    }
}
