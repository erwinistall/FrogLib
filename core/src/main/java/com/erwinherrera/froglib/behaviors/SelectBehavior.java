/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package com.erwinherrera.froglib.behaviors;

import androidx.annotation.NonNull;

import java.util.Map;
import java.util.function.Supplier;

/**
 * Runs one of a selection of Behaviors, either using a selector and a key to Behavior mapping, or a
 * supplier that returns the Behavior directly at runtime.  Does not actually schedule the selected
 * Behavior - rather, the Behavior is run through this Behavior; this ensures that the Behavior will
 * behave as expected if used as part of a BehaviorGroup.  Requires the requirements of all included
 * Behaviors, again to ensure proper functioning when used in a BehaviorGroup.  If this is undesired,
 * consider using {@link ScheduleBehavior}.
 *
 * <p>As this Behavior contains multiple component Behaviors within it, it is technically a Behavior
 * group; the Behavior instances that are passed to it cannot be added to any other groups, or
 * scheduled individually.
 *
 * <p>As a rule, BehaviorGroups require the union of the requirements of their component Behaviors.
 */
public class SelectBehavior extends BehaviorBase {
    private final Map<Object, Behavior> m_Behaviors;
    private final Supplier<Object> m_selector;
    private final Supplier<Behavior> m_toRun;
    private Behavior m_selectedBehavior;

    /**
     * Creates a new selectBehavior.
     *
     * @param Behaviors the map of Behaviors to choose from
     * @param selector the selector to determine which Behavior to run
     */
    public SelectBehavior(@NonNull Map<Object, Behavior> Behaviors, @NonNull Supplier<Object> selector) {
        BehaviorGroupBase.registerGroupedBehaviors(Behaviors.values().toArray(new Behavior[]{}));

        m_Behaviors = Behaviors;
        m_selector = selector;

        m_toRun = null;

        for (Behavior Behavior : m_Behaviors.values()) {
            m_requirements.addAll(Behavior.getRequirements());
        }
    }

    /**
     * Creates a new selectBehavior.
     *
     * @param toRun a supplier providing the Behavior to run
     */
    public SelectBehavior(@NonNull Supplier<Behavior> toRun) {
        m_Behaviors = null;
        m_selector = null;

        m_toRun = toRun;
    }

    @Override
    public void onDive() {
        if (m_selector != null) {
            if (!m_Behaviors.keySet().contains(m_selector.get())) {
                m_selectedBehavior = new LogCatBehavior(
                        "SelectBehavior failure",
                        "SelectBehavior selector value does not correspond to" + " any Behavior!");
                return;
            }
            m_selectedBehavior = m_Behaviors.get(m_selector.get());
        } else {
            m_selectedBehavior = m_toRun.get();
        }
        m_selectedBehavior.onDive();
    }

    @Override
    public void onPaddle() {
        m_selectedBehavior.onPaddle();
    }

    @Override
    public void onFly(boolean interrupted) {
        m_selectedBehavior.onFly(interrupted);
    }

    @Override
    public boolean hasSurfaced() {
        return m_selectedBehavior.hasSurfaced();
    }

    @Override
    public boolean runsWhenDisabled() {
        if (m_Behaviors != null) {
            boolean runsWhenDisabled = true;
            for (Behavior Behavior : m_Behaviors.values()) {
                runsWhenDisabled &= Behavior.runsWhenDisabled();
            }
            return runsWhenDisabled;
        } else {
            return m_toRun.get().runsWhenDisabled();
        }
    }
}
