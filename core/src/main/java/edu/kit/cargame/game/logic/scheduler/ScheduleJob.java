package edu.kit.cargame.game.logic.scheduler;

/**
 * The interface Schedule job is used to create Anonymous classes that can be passed to a {@link ScheduleEvent}.
 */
public interface ScheduleJob {
    /**
     * The Code that is executed by the {@link ScheduleEvent} that holds this object.
     */
    void run();
}
