/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com.erwinherrera.froglib.behaviors;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Schedules the given Behaviors when this Behavior is started, and ends when all the Behaviors are
 * no longer scheduled.  Useful for forking off from BehaviorGroups.  If this Behavior is interrupted,
 * it will cancel all of the Behaviors.
 *
 * @author Erwin Herrera
 */
public class ProxyScheduleBehavior extends BehaviorBase {
    private final Set<Behavior> m_toSchedule;
    private boolean m_finished;

    /**
     * Creates a new ProxyScheduleBehavior that schedules the given Behaviors when started,
     * and ends when they are all no longer scheduled.
     *
     * @param toSchedule the Behaviors to schedule
     */
    public ProxyScheduleBehavior(Behavior... toSchedule) {
        m_toSchedule = new HashSet<Behavior>(Arrays.asList(toSchedule));
    }

    @Override
    public void onDive() {
        for (Behavior Behavior : m_toSchedule) {
            Behavior.schedule();
        }
    }

    @Override
    public void onFly(boolean interrupted) {
        if (interrupted) {
            for (Behavior Behavior : m_toSchedule) {
                Behavior.cancel();
            }
        }
    }

    @Override
    public void onPaddle() {
        m_finished = true;
        for (Behavior Behavior : m_toSchedule) {
            m_finished &= !Behavior.isScheduled();
        }
    }

    @Override
    public boolean hasSurfaced() {
        return m_finished;
    }
}
