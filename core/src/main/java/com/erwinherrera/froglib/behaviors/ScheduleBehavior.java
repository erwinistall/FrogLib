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
 * Schedules the given Behaviors when this Behavior is started.  Useful for forking off from
 * BehaviorGroups.  Note that if run from a BehaviorGroup, the group will not know about the status
 * of the scheduled Behaviors, and will treat this Behavior as finishing instantly.
 *
 * @author Erwin Herrera
 */
public class ScheduleBehavior extends BehaviorBase {

    private final Set<Behavior> m_toSchedule;

    /**
     * Creates a new ScheduleBehavior that schedules the given Behaviors when started.
     *
     * @param toSchedule the Behaviors to schedule
     */
    public ScheduleBehavior(Behavior... toSchedule) {
        m_toSchedule = new HashSet<Behavior>(Arrays.asList(toSchedule));
    }

    @Override
    public void onDive() {
        for (Behavior Behavior : m_toSchedule) {
            Behavior.schedule();
        }
    }

    @Override
    public boolean hasSurfaced() {
        return true;
    }

    @Override
    public boolean runsWhenDisabled() {
        return true;
    }

}
