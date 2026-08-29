package com.erwinherrera.froglib.purepursuit.actions;

import com.erwinherrera.froglib.purepursuit.waypoints.InterruptWaypoint;

/**
 * This interface represents an action that InterruptWaypoint perform when
 * they reach their interrupt point.
 *
 * @author Erwin Herrera
 * @version 1.0
 * @see InterruptWaypoint
 */
public interface InterruptAction {

    /**
     * Performs the action.
     */
    public void doAction();

}
