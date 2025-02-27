package edu.kit.cargame.game.logic.scheduler;

import edu.kit.cargame.game.common.Game;
import edu.kit.cargame.game.geometry.Point;
import edu.kit.cargame.game.common.GameObject;

/**
 * The type Schedule event holds a Function inside a {@link ScheduleJob} to be executed at a later time.
 */
public class ScheduleEvent extends GameObject {
    private double remainingGameTime;
    private final ScheduleJob event;

    /**
     * Instantiates a new Schedule event.
     *
     * @param parent               the parent Object
     * @param remainingGameTicks the time after which the Job is to be executed
     * @param game                 the game in which this scheduled event lives
     * @param event                the event to be executed after the time has elapsed
     */
    public ScheduleEvent(GameObject parent, double remainingGameTicks, Game game, ScheduleJob event) {
        super(parent, game, new Point(0, 0));
        this.remainingGameTime = remainingGameTicks;
        this.event = event;
    }

    @Override
    public void tick(double timeScale) {
        remainingGameTime -= timeScale;
        if (remainingGameTime <= 0) {
            event.run();
            kill();
        }
    }
}
