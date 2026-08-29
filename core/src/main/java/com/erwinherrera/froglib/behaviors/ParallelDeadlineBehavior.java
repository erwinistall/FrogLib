/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com.erwinherrera.froglib.behaviors;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * A BehaviorGroup that runs a set of Behaviors in parallel, ending only when a specific Behavior
 * (the "deadline") ends, interrupting all other Behaviors that are still running at that point.
 *
 * <p>As a rule, BehaviorGroups require the union of the requirements of their component Behaviors.
 *
 * @author Erwin Herrera
 */
public class ParallelDeadlineGroup extends BehaviorGroupBase {

    // maps Behaviors in this group to whether they are still running
    private final Map<Behavior, Boolean> m_Behaviors = new HashMap<>();
    private boolean m_runWhenDisabled = true;
    private Behavior m_deadline;

    /**
     * Creates a new ParallelDeadlineGroup.  The given Behaviors (including the deadline) will be
     * executed simultaneously.  The BehaviorGroup will finish when the deadline finishes,
     * interrupting all other still-running Behaviors.  If the BehaviorGroup is interrupted, only
     * the Behaviors still running will be interrupted.
     *
     * @param deadline the Behavior that determines when the group ends
     * @param Behaviors the Behaviors to be executed
     */
    public ParallelDeadlineGroup(Behavior deadline, Behavior... Behaviors) {
        m_deadline = deadline;
        addBehaviors(Behaviors);
        if (!m_Behaviors.containsKey(deadline)) {
            addBehaviors(deadline);
        }
    }

    /**
     * Sets the deadline to the given Behavior.  The deadline is added to the group if it is not
     * already contained.
     *
     * @param deadline the Behavior that determines when the group ends
     */
    public void setDeadline(Behavior deadline) {
        if (!m_Behaviors.containsKey(deadline)) {
            addBehaviors(deadline);
        }
        m_deadline = deadline;
    }

    @Override
    public void addBehaviors(Behavior... Behaviors) {
        requireUngrouped(Behaviors);

        if (m_Behaviors.containsValue(true)) {
            throw new IllegalStateException(
                    "Behaviors cannot be added to a BehaviorGroup while the group is running");
        }

        registerGroupedBehaviors(Behaviors);

        for (Behavior Behavior : Behaviors) {
            if (!Collections.disjoint(Behavior.getRequirements(), m_requirements)) {
                throw new IllegalArgumentException("Multiple Behaviors in a parallel group cannot"
                        + "require the same Nests");
            }
            m_Behaviors.put(Behavior, false);
            m_requirements.addAll(Behavior.getRequirements());
            m_runWhenDisabled &= Behavior.runsWhenDisabled();
        }
    }

    @Override
    public void onDive() {
        for (Map.Entry<Behavior, Boolean> BehaviorRunning : m_Behaviors.entrySet()) {
            BehaviorRunning.getKey().onDive();
            BehaviorRunning.setValue(true);
        }
    }

    @Override
    public void onPaddle() {
        for (Map.Entry<Behavior, Boolean> BehaviorRunning : m_Behaviors.entrySet()) {
            if (!BehaviorRunning.getValue()) {
                continue;
            }
            BehaviorRunning.getKey().onPaddle();
            if (BehaviorRunning.getKey() != m_deadline && BehaviorRunning.getKey().hasSurfaced()) {
                BehaviorRunning.getKey().onFly(false);
                BehaviorRunning.setValue(false);
            }
        }
    }

    @Override
    public void onFly(boolean interrupted) {
        for (Map.Entry<Behavior, Boolean> BehaviorRunning : m_Behaviors.entrySet()) {
            if (BehaviorRunning.getKey() == m_deadline) {
                BehaviorRunning.getKey().onFly(interrupted);
            }
            else if (BehaviorRunning.getValue()) {
                BehaviorRunning.getKey().onFly(true);
            }
        }
    }

    @Override
    public boolean hasSurfaced() {
        return m_deadline.hasSurfaced();
    }

    @Override
    public boolean runsWhenDisabled() {
        return m_runWhenDisabled;
    }

}
