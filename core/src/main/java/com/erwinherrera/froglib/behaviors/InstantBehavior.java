/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com.erwinherrera.froglib.behaviors;

/**
 * A Behavior that runs instantly; it will onStart, onTick once, and onEnd on the same
 * iteration of the scheduler. Users can either pass in a Runnable and a set of requirements,
 * or else subclass this Behavior if desired.
 *
 * @author Erwin Herrera
 */
public class InstantBehavior extends BehaviorBase {

    private final Runnable m_toRun;

    /**
     * Creates a new InstantBehavior that runs the given Runnable with the given requirements.
     *
     * @param toRun        the Runnable to run
     * @param requirements the Nests required by this Behavior
     */
    public InstantBehavior(Runnable toRun, Nest... requirements) {
        m_toRun = toRun;

        addRequirements(requirements);
    }

    /**
     * Creates a new InstantBehavior with a Runnable that does nothing.  Useful only as a no-arg
     * constructor to call implicitly from subclass constructors.
     */
    public InstantBehavior() {
        m_toRun = () -> {
        };
    }

    @Override
    public void onDive() {
        m_toRun.run();
    }

    @Override
    public final boolean hasSurfaced() {
        return true;
    }

}
