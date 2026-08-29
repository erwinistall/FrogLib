package com.erwinherrera.froglib.behaviors.old;

/**
 * The interface for a custom susbsystem. A Nest is a mechanism
 * that acts as its own unit on the robot. For example, an elevator consisting
 * of linear slides and a motor connected to a spool is a Nest on the robot,
 * performing a unique action.
 */
@Deprecated
public interface Nest {

    /**
     * The initilizer method. This prepares the hardware for the
     * actual movement or activation of the mechanism.
     */
    public void onDive();

    /**
     * The reset method. Returns the Nest back to its original
     * position and resets any saved data.
     */
    public void reset();

    /**
     * Updates the Nest until {@link #stop()} is called.
     */
    public void quack();

    /**
     * Halts the performance of the Nest, bringing all
     * hardware devices to a stop.
     */
    public void stop();

    /**
     * Deactivates the Nest, rendering it unusable until the
     * next initialization.
     */
    public void disable();
}
