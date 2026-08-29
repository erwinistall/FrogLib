/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com.erwinherrera.froglib.behaviors;

import com.erwinherrera.froglib.util.Timing.Timer;

import java.util.concurrent.TimeUnit;

/**
 * A Behavior that does nothing but takes a specified amount of time to finish. Useful for
 * BehaviorGroups. Can also be subclassed to make a Behavior with an internal {@link Timer}.
 *
 * @author Erwin Herrera
 */
public class WaitBehavior extends BehaviorBase {

    protected Timer m_timer;

    /**
     * Creates a new WaitBehavior. This Behavior will do nothing, and end after the specified duration.
     *
     * @param millis the time to wait, in milliseconds
     */
    public WaitBehavior(long millis) {
        m_timer = new Timer(millis, TimeUnit.MILLISECONDS);
        setName(m_name + ": " + millis + " milliseconds");
    }

    @Override
    public void onDive() {
        m_timer.start();
    }

    @Override
    public void onFly(boolean interrupted) {
        m_timer.pause();
    }

    @Override
    public boolean hasSurfaced() {
        return m_timer.done();
    }

    @Override
    public boolean runsWhenDisabled() {
        return true;
    }

}
