package edu.kit.cargame.game.common;


import edu.kit.cargame.game.geometry.Point;
import edu.kit.cargame.game.logic.Colliders;
import edu.kit.cargame.game.logic.scheduler.ScheduleJob;
import edu.kit.cargame.game.logic.scheduler.Scheduler;
import edu.kit.cargame.game.logic.spawning.Spawner;
import edu.kit.cargame.game.object.eyecandy.Background;
import edu.kit.cargame.game.object.eyecandy.Foreground;
import edu.kit.cargame.game.overlay.OverlayManager;
import edu.kit.cargame.game.playercar.CarSettings;
import edu.kit.cargame.game.playercar.CarType;
import edu.kit.cargame.game.playercar.PlayerCar;
import edu.kit.cargame.io.config.Config;
import edu.kit.cargame.io.config.MetaData;
import edu.kit.cargame.io.input.ActionTypes;
import edu.kit.cargame.io.input.UserInput;
import edu.kit.cargame.io.view.gamerenderers.Renderer;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;

/**
 * The type Game holds and manages all objects (some only indirectly) needed to simulate an
 * instance of the Car game.
 */
public class Game {
    private static final int DAY_LENGTH = 54;
    private final Collection<GameObject> gameObjects;

    private final Config config;

    private final DeterministicRandom random;

    // Direct references to relevant Game Objects
    private final PlayerCar playerCar;
    private final Colliders colliders;
    private final Scheduler scheduler;

    private double currentTime = 0;
    private final double baseTimeScale;

    //Cached timescales to stop timescales changing inside a single tick
    private double cachedTimeScale = 1;
    private double cachedBonusTimeScale = 1;

    private final double boostTimeScale;
    private int slowdownLayers = 0;

    private boolean gameOver = false;
    private boolean duringTime = false;

    private final Collection<GameObject> delete = new ArrayList<>();
    private final Collection<CollidingGameObject> moved = new ArrayList<>();

    private final List<Entry<GameObject, GameObject>> childCreationJobs = new ArrayList<>();
    private boolean muted;

    /**
     * Marks a GameObject for deletion at the end of the tick.
     *
     * @param gameObject the GameObject to be deleted
     */
    public void markForExecution(GameObject gameObject) {
        delete.add(gameObject);
    }

    /**
     * Instantiates a new Game.
     *
     * @param userInput   the user input
     * @param carSettings the car settings
     * @param config      the config
     * @param metaData    the metadata of the game
     */
    public Game(UserInput<ActionTypes> userInput, CarSettings carSettings, Config config, MetaData metaData) {
        gameObjects = new ArrayList<>();
        this.config = config;

        long seed = config.seed();
        if (seed == 0) {
            random = new DeterministicRandom();
        } else {
            random = new DeterministicRandom(seed);
        }

        scheduler = new Scheduler(this);
        colliders = new Colliders(this);

        // Scheduler needs to be added first so Scheduled events always get ticked first!
        gameObjects.add(scheduler);
        gameObjects.add(new Background(null, this, new Point(0, 0), config.worldWidth(), config.worldHeight()));
        gameObjects.add(colliders);

        CarType carType = carSettings.carType();

        baseTimeScale = config.baseTimeScale();


        playerCar = carType.getCar(colliders, this, userInput, carSettings.color());
        boostTimeScale = playerCar.getBoostAmount();
        new Spawner(colliders, this, new Point(0, 0));
        gameObjects.add(new Foreground(null, this, new Point(0, 0), config.worldWidth(), config.worldHeight()));

        OverlayManager overlayManager = new OverlayManager(null, this, new Point(0, 0));
        gameObjects.add(overlayManager);
    }

    /**
     * Returns a collection of all current renderers.
     *
     * @return the collection
     */
    public Collection<Renderer> getRenderers() {
        Collection<Renderer> renderers = new ArrayList<>();
        for (GameObject gameObject : gameObjects) {
            renderers.addAll(gameObject.getAllRenderers());
        }
        return Collections.unmodifiableCollection(renderers);
    }

    /**
     * Register a Collidable GO as having moved, this schedules a collision check for this GO at the end of the tick.
     *
     * @param collidingGameObject the colliding GO which has moved
     */
    public void collidableHasMoved(CollidingGameObject collidingGameObject) {
        if (!duringTime) {
            return;
        }
        moved.add(collidingGameObject);
    }

    /**
     * Register a child to be added to the parent at the end of the tick.
     * Used to avoid changing a list while looping through it
     *
     * @param parent the parent gameobject
     * @param child  the child gameobject
     */
    public void registerAddChild(GameObject parent, GameObject child) {
        childCreationJobs.add(new SimpleEntry<>(parent, child));
    }

    /**
     * Executes all child creation jobs that have been registered.
     * This method processes deferred child additions to avoid
     * modifying the game object hierarchy during the game update loop.
     * It ensures that all registered child game objects are properly
     * added to their parent game objects.
     * Also ticks the newly created children at the end of the tick instead of at the middle
     */
    private void executeChildCreationJobs() {
        for (Entry<GameObject, GameObject> entry : childCreationJobs) {
            entry.getKey().addChild(entry.getValue());
        }
        for (GameObject child : childCreationJobs.stream().map(Entry::getValue).toList()) {
            child.tick(getTimeScale() * (child.needAllTimescales() ? getBonusTimescale() : 1));
        }
        childCreationJobs.clear();
    }


    /**
     * Removes a game object from the list of game objects tracked by the game.
     * Note that most game objects aren't tracked by the game directly, but by other game objects.
     *
     * @param object the game object to remove
     */
    public void removeGameObject(GameObject object) {
        gameObjects.remove(object);
    }

    /**
     * Checks for collision and ticks all objects.
     */
    public void tick() {
        if (gameOver) {
            return;
        }

        cachedTimeScale = baseTimeScale * getSlowDownTimeMultiplier();
        cachedBonusTimeScale = getBoostTimeMultiplier() * getScoreTimeMultiplier() * getCoinTimeMultiplier() * playerCar.getSpeed();

        doTime();
        doCollision();
        executeChildCreationJobs();
        doDelete();
    }

    private void doCollision() {
        for (CollidingGameObject gameObject : moved) {
            colliders.doCollision(gameObject);
        }
        moved.clear();
    }

    /**
     * Simulate one time step in the game
     */
    private void doTime() {
        duringTime = true;
        currentTime += getTimeScale();
        for (GameObject gameObject : gameObjects) {
            gameObject.tickTree(getTimeScale());
        }
        duringTime = false;
    }

    private void doDelete() {
        List<GameObject> toRemove = new ArrayList<>(delete);
        delete.clear();
        for (GameObject gameObject : toRemove) {
            gameObject.takeOut();
        }
    }

    private double getTimeScale() {
        return cachedTimeScale;
    }

    /**
     * Gets the bonus timescale.
     * This is the timescale that includes the boost and score.
     *
     * @return the bonus timescale
     */
    public double getBonusTimescale() {
        //Timescale including boost and score
        return cachedBonusTimeScale;
    }

    private double getBoostTimeMultiplier() {
        return playerCar.boosting() ? boostTimeScale : 1;
    }

    private double getCoinTimeMultiplier() {
        return playerCar.getCoinMultiplier();
    }

    private double getSlowDownTimeMultiplier() {
        return isSlowdown() ? getConfig().stopwatchSpeed() : 1;
    }


    /**
     * Slow Down the game from now until (current Time + duration).
     *
     * @param duration the duration
     */
    public void addSlowdown(int duration) {
        slowdownLayers++;
        addScheduledEvent(duration, () -> slowdownLayers--);
    }

    /**
     * Checks whether a Slow Motion effect is currently active.
     *
     * @return boolean
     */
    public boolean isSlowdown() {
        return slowdownLayers > 0;
    }


    /**
     * The amount of in game time that has elapsed since the game was started.
     *
     * @return double the in game time
     */
    public double getCurrentTime() {
        return currentTime;
    }


    /**
     * Marks the game as ended. No further actions will be taken.
     */
    public void endGame() {
        gameOver = true;
    }


    /**
     * Checks if the game is over i.e. that endGame has been called.
     *
     * @return boolean
     */
    public boolean isGameOver() {
        return gameOver;
    }


    /**
     * Gets the current score.
     *
     * @return the score
     */
    public int getScore() {
        return playerCar.getScore();
    }

    /**
     * The current multiplier for new score earned.
     * It's affected by number of coins and the time spent in game.
     *
     * @return the multiplier as double
     */

    private double getScoreTimeMultiplier() {
        return 1 + Math.sqrt(getScore()) / 500;
    }


    /**
     * Gets the current speed.
     *
     * @return the speed
     */
    public double getSpeed() {
        return -1;
    }

    /**
     * Gets the random number generator.
     *
     * @return the random number generator
     */
    public Random getRandom() {
        return random.getRandom();
    }

    /**
     * Adds an event to the Scheduler.
     *
     * @param remainingGameTicks in how many game ticks
     * @param event              the to be scheduled event
     */
    public void addScheduledEvent(double remainingGameTicks, ScheduleJob event) {
        scheduler.insert(remainingGameTicks, event);
    }

    /**
     * Gets the config.
     *
     * @return the config
     */
    public Config getConfig() {
        return config;
    }

    /**
     * Gets the player car.
     *
     * @return the player car
     */
    public PlayerCar getPlayerCar() {
        return playerCar;
    }

    /**
     * Set the muted state of the game.
     * @param muted the muted state
     */
    public void setMuted(boolean muted) {
        this.muted = muted;
    }

    /**
     * Get the muted state of the game.
     * @return the muted state
     */
    public boolean getMuted() {
        return muted;
    }
}
