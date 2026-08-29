/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com.erwinherrera.froglib.behaviors;

import java.util.ArrayList;
import java.util.List;

/**
 * A BehaviorGroups that runs a list of Behaviors in sequence.
 *
 * <p>As a rule, BehaviorGroups require the union of the requirements of their component Behaviors.
 *
 * @author Erwin Herrera
 */
public class SequentialBehaviorGroup extends BehaviorGroupBase {

    private final List<Behavior> m_Behaviors = new ArrayList<>();
    private int m_currentBehaviorIndex = -1;
    private boolean m_runWhenDisabled = true;

    /**
     * Creates a new SequentialBehaviorGroup.  The given Behaviors will be run sequentially, with
     * the BehaviorGroup finishing when the last Behavior finishes.
     *
     * @param Behaviors the Behaviors to include in this group.
     */
    public SequentialBehaviorGroup(Behavior... Behaviors) {
        addBehaviors(Behaviors);
    }


    @Override
    public void addBehaviors(Behavior... Behaviors) {
        requireUngrouped(Behaviors);

        if (m_currentBehaviorIndex != -1) {
            throw new IllegalStateException(
                    "Behaviors cannot be added to a BehaviorGroup while the group is running");
        }

        registerGroupedBehaviors(Behaviors);

        for (Behavior Behavior : Behaviors) {
            m_Behaviors.add(Behavior);
            m_requirements.addAll(Behavior.getRequirements());
            m_runWhenDisabled &= Behavior.runsWhenDisabled();
        }
    }

    @Override
    public void onDive() {
        m_currentBehaviorIndex = 0;

        if (!m_Behaviors.isEmpty()) {
            m_Behaviors.get(0).onDive();
        }
    }

    @Override
    public void onPaddle() {
        if (m_Behaviors.isEmpty()) {
            return;
        }
        if (m_currentBehaviorIndex == -1) {
            return;
        }

        Behavior currentBehavior = m_Behaviors.get(m_currentBehaviorIndex);

        currentBehavior.onPaddle();
        if (currentBehavior.hasSurfaced()) {
            currentBehavior.onFly(false);
            m_currentBehaviorIndex++;
            if (m_currentBehaviorIndex < m_Behaviors.size()) {
                m_Behaviors.get(m_currentBehaviorIndex).onDive();
            }
        }
    }

    @Override
    public void onFly(boolean interrupted) {
        if (m_currentBehaviorIndex == -1) {
            return;
        }
        if (interrupted && !m_Behaviors.isEmpty()) {
            m_Behaviors.get(m_currentBehaviorIndex).onFly(true);
        }
        m_currentBehaviorIndex = -1;
    }

    @Override
    public boolean hasSurfaced() {
        return m_currentBehaviorIndex == m_Behaviors.size();
    }

    @Override
    public boolean runsWhenDisabled() {
        return m_runWhenDisabled;
    }

    public String getCurrentBehaviorName(){
        return m_Behaviors.get(m_currentBehaviorIndex).getName();
    }
}
