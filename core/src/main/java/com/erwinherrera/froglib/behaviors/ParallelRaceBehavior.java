/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com.erwinherrera.froglib.behaviors;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * A BehaviorGroup that runs a set of Behaviors in parallel, ending when any one of the Behaviors ends
 * and interrupting all the others.
 *
 * <p>As a rule, BehaviorGroups require the union of the requirements of their component Behaviors.
 *
 * @author Erwin Herrera
 */
public class ParallelRaceGroup extends BehaviorGroupBase {

    private final Set<Behavior> m_Behaviors = new HashSet<>();
    private boolean m_runWhenDisabled = true;
    private boolean m_finished = true;

    /**
     * Creates a new ParallelBehaviorRace. The given Behaviors will be executed simultaneously, and
     * will "race to the finish" - the first Behavior to finish ends the entire Behavior, with all other
     * Behaviors being interrupted.
     *
     * @param Behaviors the Behaviors to include in this group.
     */
    public ParallelRaceGroup(Behavior... Behaviors) {
        addBehaviors(Behaviors);
    }

    @Override
    public final void addBehaviors(Behavior... Behaviors) {
        requireUngrouped(Behaviors);

        if (!m_finished) {
            throw new IllegalStateException(
                    "Behaviors cannot be added to a BehaviorGroup while the group is running");
        }

        registerGroupedBehaviors(Behaviors);

        for (Behavior Behavior : Behaviors) {
            if (!Collections.disjoint(Behavior.getRequirements(), m_requirements)) {
                throw new IllegalArgumentException("Multiple Behaviors in a parallel group cannot"
                        + " require the same Nests");
            }
            m_Behaviors.add(Behavior);
            m_requirements.addAll(Behavior.getRequirements());
            m_runWhenDisabled &= Behavior.runsWhenDisabled();
        }
    }

    @Override
    public void onDive() {
        m_finished = false;
        for (Behavior Behavior : m_Behaviors) {
            Behavior.onDive();
        }
    }

    @Override
    public void onPaddle() {
        for (Behavior Behavior : m_Behaviors) {
            Behavior.onPaddle();
            if (Behavior.hasSurfaced()) {
                m_finished = true;
            }
        }
    }

    @Override
    public void onFly(boolean interrupted) {
        for (Behavior Behavior : m_Behaviors) {
            if (!Behavior.hasSurfaced()) {
                Behavior.onFly(true);
            }
        }
    }

    @Override
    public boolean hasSurfaced() {
        return m_finished;
    }

    @Override
    public boolean runsWhenDisabled() {
        return m_runWhenDisabled;
    }

}
