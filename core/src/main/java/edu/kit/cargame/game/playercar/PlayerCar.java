package edu.kit.cargame.game.playercar;

import edu.kit.cargame.game.common.CollidingGameObject;
import edu.kit.cargame.game.common.Game;
import edu.kit.cargame.game.common.GameObject;
import edu.kit.cargame.game.geometry.BoundingBox;
import edu.kit.cargame.game.geometry.Point;
import edu.kit.cargame.io.input.ActionTypes;
import edu.kit.cargame.io.input.UserInput;
import edu.kit.cargame.io.view.gamerenderers.PlayerCarRenderer;

import java.util.List;

/**
 * This car models the car which is driven by the player.
 */
public class PlayerCar extends CollidingGameObject {
    private static final int MAX_VERTICAL_VELOCITY = 5;
    private static final float UPPER_DIRECTION_THRESHOLD = 0.5f;
    private static final float LOWER_DIRECTION_THRESHOLD = 0.1f;

    private static final int DURATION_INVULNERABLE_ON_OBSTACLE_HIT = 40;
    private static final int TIME_AFTER_DEATH = 30;
    private static final int SCORE_MULTIPLIER = 10;
    private final UserInput<ActionTypes> userInput;
    private int score = 0;
    private int badSteeringLayers = 0;
    private int invulnerableLayers = 0;
    private int coins = 0;
    private int lives;
    private final float speed;
    private final float agility;
    private final float boost;
    private boolean isBoosting = false;
    private float velocity = 0;
    private boolean dead = false;

    /**
     * Instantiates a new Car.
     *
     * @param parent      the parent Object
     * @param game        the game in which this car lives
     * @param position    the position of the car
     * @param sprite      the sprite which displays this car
     * @param userInput   the user input which handles this car
     * @param speed       the speed of the car
     * @param lives       the lives of the car
     * @param agility     the agility of the car
     * @param boundingBox the boundingBox i.e. size of the car
     * @param boost       the boost of the car
     */
    PlayerCar(GameObject parent, Game game, Point position, UninitialisedCarSprites sprite, UserInput<ActionTypes> userInput,
              float speed, int lives, float agility, float boost, BoundingBox boundingBox) {
        super(parent, game, position, boundingBox);
        //setRenderer(new TextRenderer(this, new BoundingBoxRenderer(this, )));
        setRenderer(new PlayerCarRenderer(this, sprite));
        this.userInput = userInput;
        this.lives = lives;
        this.agility = agility;
        this.boost = boost;
        this.speed = speed;
    }

    @Override
    public void handleCollision(CollidingGameObject other) {
    }

    /**
     * Checks whether the player car is boosting during this tick.
     *
     * @return boolean
     */
    public boolean boosting() {
        return isBoosting;
    }

    /**
     * Checks the current amount of coins the player has.
     *
     * @return the amount of coins
     */
    public int getCoins() {
        return coins;
    }

    /**
     * Gets the Base speed of the player car as determined by its type.
     *
     * @return the speed
     */
    public float getSpeed() {
        return speed;
    }

    /**
     * Worsens the players steering from now until (current Time + duration).
     *
     * @param duration the duration
     */
    public void worsenSteering(int duration) {
        badSteeringLayers++;
        getGame().addScheduledEvent(duration, () -> badSteeringLayers--);
    }


    /**
     * Checks whether the car has bad steering.
     *
     * @return true if the car has bad steering
     */
    public boolean hasBadSteering() {
        return badSteeringLayers > 0;
    }

    /**
     * Gives the player a coin. The max is currently 3.
     */
    public void addCoin() {
        if (coins >= 3) {
            return;
        }
        coins++;
    }

    private void removeCoins() {
        coins = 0;
    }

    /**
     * Add a life to the car.
     */
    public void addLife() {
        lives++;
    }


    /**
     * Make the player invulnerable from now until (current Time + duration).
     *
     * @param duration the duration
     */
    public void makeInvulnerable(int duration) {
        invulnerableLayers++;
        getGame().addScheduledEvent(duration, () -> invulnerableLayers--);
    }


    /**
     * Checks whether the car is currently invulnerable.
     *
     * @return boolean
     */
    public boolean isInvulnerable() {
        return invulnerableLayers > 0;
    }


    /**
     * Gets current score.
     *
     * @return the current score
     */
    public int getScore() {
        return score;
    }

    /**
     * Gives the Player score.
     *
     * @param score the amount of score the player gains
     */
    public void addScore(int score) {
        this.score += score;
    }

    @Override
    public void tick(double timeScale) {
        if (dead) {
            return;
        }

        List<ActionTypes> action = userInput.pressed();

        computeMovement(action, timeScale);

        isBoosting = action.contains(ActionTypes.ENTER);

        addScore((int) (timeScale * SCORE_MULTIPLIER * getCoinMultiplier()));
    }

    private void computeMovement(List<ActionTypes> action, double timeScale) {

        if (action.contains(ActionTypes.UP)) {
            // Move the car down
            velocity += (float) (getSteering() * timeScale);
        }
        if (action.contains(ActionTypes.DOWN)) {
            // Move the car down
            velocity -= (float) (getSteering() * timeScale);
        }
        if (!action.contains(ActionTypes.DOWN) && !action.contains(ActionTypes.UP)) {
            float temp = (float) (Math.abs(velocity) - getSteering() * timeScale);
            temp = Math.max(temp, 0);
            velocity = temp * Math.signum(velocity);
        }

        velocity = Math.min(getMaxVelocity(), Math.max(-getMaxVelocity(), velocity));
        setPosition(getLocalPosition().addY((float) (velocity * timeScale)));
    }

    private float getSteering() {
        return agility * (hasBadSteering() ? getGame().getConfig().oilSteeringFactor() : 1);
    }


    @Override
    public boolean destroysCollectables() {
        return true;
    }

    /**
     * Gets the boost amount.
     *
     * @return the boost amount
     */
    public float getBoostAmount() {
        return boost;
    }

    /**
     * Gets the vertical velocity.
     *
     * @return the vertical velocity
     */
    public float getVerticalVelocity() {
        return velocity;
    }

    /**
     * Gets the max velocity.
     *
     * @return the max velocity
     */
    public float getMaxVelocity() {
        return MAX_VERTICAL_VELOCITY;
    }

    /**
     * Removes a life from the player. If the player is at zero lives, the game is ended.
     */
    public void removeLife() {
        if (isInvulnerable() || getGame().getConfig().invincible()) {
            return;
        }

        lives--;

        if (lives <= 0) {
            getGame().addScheduledEvent(TIME_AFTER_DEATH, () -> getGame().endGame());
            dead = true;
        }
        removeCoins();
        makeInvulnerable(DURATION_INVULNERABLE_ON_OBSTACLE_HIT);
    }

    /**
     * Gets the coin multiplier which is used to calculate the score and timescales.
     *
     * @return the coin multiplier
     */
    public double getCoinMultiplier() {
        return 1 + coins * (getGame().getConfig().coinFactor());
    }

    @Override
    protected boolean needAllTimescales() {
        return true;
    }

    /**
     * Gets the lives of the player.
     *
     * @return the lives
     */
    public int getLives() {
        return lives;
    }

    /**
     * Gets the direction the car sprite should point to.
     * The direction is determined by the vertical velocity of the car.
     * The car can point in 5 directions: left, left up, ahead, right, right down.
     *
     * @return the direction
     */
    public PlayerCarDirection getDirection() {
        float relativeYVelocity = getVerticalVelocity() / getMaxVelocity();

        if (relativeYVelocity > UPPER_DIRECTION_THRESHOLD) {
            return PlayerCarDirection.LEFT_UP;
        }

        if (relativeYVelocity > LOWER_DIRECTION_THRESHOLD) {
            return PlayerCarDirection.LEFT;
        }

        if (relativeYVelocity > -LOWER_DIRECTION_THRESHOLD) {
            return PlayerCarDirection.AHEAD;
        }

        if (relativeYVelocity > -UPPER_DIRECTION_THRESHOLD) {
            return PlayerCarDirection.RIGHT;
        }

        return PlayerCarDirection.RIGHT_DOWN;
    }

    /**
     * Gets the user input controlling the player car.
     *
     * @return the user input
     */
    public UserInput<ActionTypes> getUserInput() {
        return userInput;
    }

    /**
     * Sets the velocity of the player car.
     *
     * @param velocity the velocity
     */
    public void setVelocity(float velocity) {
        this.velocity = velocity;
    }
}
