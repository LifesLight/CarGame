package edu.kit.cargame.game.logic.scheduler;

import edu.kit.cargame.game.common.Game;
import edu.kit.cargame.game.geometry.Point;
import edu.kit.cargame.game.common.GameObject;

/**
 * The type Scheduler holds all possible {@link ScheduleEvent} active in the game.
 * This is not strictly necessary, as a {@link ScheduleEvent} is a {@link GameObject} that could live within any other.
 * Still, this approach is used since it eliminates the risk of the {@link ScheduleEvent}'s {@link GameObject}
 * being deleted before it has performed its action.
 * {@link GameObject}. However, this allows all {@link ScheduleEvent}s to be organised in one place.
 */
public class Scheduler extends GameObject {
    /**
     * Instantiates a new Scheduler.
     *
     * @param game   the game in which this scheduler lives
     */
    public Scheduler(Game game) {
        super(null, game, new Point(0, 0));
    }

    /**
     * Insert a new Schedule Job to be executed.
     *
     * @param remainingGameTicks the time after which the event is to be executed
     * @param event                the event to be executed
     */
    public void insert(double remainingGameTicks, ScheduleJob event) {
        new ScheduleEvent(this, remainingGameTicks, getGame(), event);
    }
}
