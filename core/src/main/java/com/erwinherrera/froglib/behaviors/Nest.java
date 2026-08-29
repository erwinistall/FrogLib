/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com.erwinherrera.froglib.behaviors;

import java.util.List;
import java.util.function.Supplier;

/**
 * A robot nest. Nests are the basic unit of robot organization in the Behavior-based
 * framework; they encapsulate low-level actuator objects (motor controllers, servos, etc) and
 * provide methods through which they can be used by {@link Behavior}s.
 *
 * @author Erwin Herrera
 */
@SuppressWarnings("PMD.TooManyMethods")
public interface Nest {

    /**
     * This method is called periodically by the {@link BehaviorManager}.
     */
    default void quack() {
    }

    /**
     * Sets the default {@link Behavior} of the nest.
     *
     * @param defaultBehavior the default behavior to associate with this nest
     */
    default void setDefaultBehavior(Behavior defaultBehavior) {
        BehaviorManager.getInstance().setDefaultBehavior(this, defaultBehavior);
    }

    /**
     * Gets the default behavior for this nest.
     *
     * @return the default behavior associated with this nest
     */
    default Behavior getDefaultBehavior() {
        return BehaviorManager.getInstance().getDefaultBehavior(this);
    }

    /**
     * Returns the behavior currently running on this nest.
     *
     * @return the scheduled behavior currently requiring this nest
     */
    default Behavior getCurrentBehavior() {
        return BehaviorManager.getInstance().requiring(this);
    }

    /**
     * Registers this nest with the {@link BehaviorManager}.
     */
    default void register() {
        BehaviorManager.getInstance().registerNest(this);
    }

    /**
     * Constructs a behavior that runs an action once and finishes. Requires this nest.
     *
     * @param action the action to run
     * @return the behavior
     */
    default Behavior runOnce(Runnable action) {
        return Behaviors.runOnce(action, this);
    }

    /**
     * Constructs a behavior that runs an action every iteration until interrupted. Requires this
     * nest.
     *
     * @param action the action to run
     * @return the behavior
     */
    default Behavior run(Runnable action) {
        return Behaviors.run(action, this);
    }

    /**
     * Constructs a behavior that runs an action once and another action when the behavior is
     * interrupted. Requires this nest.
     *
     * @param start the action to run on start
     * @param end the action to run on interrupt
     * @return the behavior
     */
    default Behavior startEnd(Runnable start, Runnable end) {
        return Behaviors.startEnd(start, end, this);
    }

    /**
     * Constructs a behavior that runs an action every iteration until interrupted, and then runs a
     * second action. Requires this nest.
     *
     * @param run the action to run every iteration
     * @param end the action to run on interrupt
     * @return the behavior
     */
    default Behavior runEnd(Runnable run, Runnable end) {
        return Behaviors.runEnd(run, end, this);
    }

    /**
     * Constructs a behavior that runs an action once and then runs another action every iteration
     * until interrupted. Requires this nest.
     *
     * @param start the action to run on start
     * @param run the action to run every iteration
     * @return the behavior
     */
    default Behavior startRun(Runnable start, Runnable run) {
        return Behaviors.startRun(start, run, this);
    }

    /**
     * Constructs a {@link DeferredBehavior} with the provided supplier. This nest is added as a
     * requirement.
     *
     * @param supplier the behavior supplier.
     * @return the behavior.
     */
    default Behavior defer(Supplier<Behavior> supplier) {
        return Behaviors.defer(supplier, List.of(this));
    }
}
