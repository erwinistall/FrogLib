/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com.erwinherrera.froglib.behaviors;

import static com.erwinherrera.froglib.Behavior.BehaviorGroupBase.requireUngrouped;

import java.util.function.BooleanSupplier;


/**
 * Runs one of two Behaviors, depending on the value of the given condition when this Behavior is
 * initialized. Does not actually schedule the selected Behavior - rather, the Behavior is run
 * through this Behavior; this ensures that the Behavior will behave as expected if used as part of a
 * BehaviorGroup. Requires the requirements of both Behaviors, again to ensure proper functioning
 * when used in a BehaviorGroup. If this is undesired, consider using {@link ScheduleBehavior}.
 *
 * <p>As this Behavior contains multiple component Behaviors within it, it is technically a Behavior
 * group; the Behavior instances that are passed to it cannot be added to any other groups, or
 * scheduled individually.
 *
 * <p>As a rule, BehaviorGroups require the union of the requirements of their component Behaviors.
 *
 * @author Erwin Herrera
 */
public class ConditionalBehavior extends BehaviorBase {

    private final Behavior m_onTrue;
    private final Behavior m_onFalse;
    private final BooleanSupplier m_condition;
    private Behavior m_selectedBehavior;

    /**
     * Creates a new ConditionalBehavior.
     *
     * @param onTrue    the Behavior to run if the condition is true
     * @param onFalse   the Behavior to run if the condition is false
     * @param condition the condition to determine which Behavior to run
     */
    public ConditionalBehavior(Behavior onTrue, Behavior onFalse, BooleanSupplier condition) {
        requireUngrouped(onTrue, onFalse);

        BehaviorGroupBase.registerGroupedBehaviors(onTrue, onFalse);

        m_onTrue = onTrue;
        m_onFalse = onFalse;
        m_condition = condition;
        m_requirements.addAll(m_onTrue.getRequirements());
        m_requirements.addAll(m_onFalse.getRequirements());
    }

    @Override
    public void onDive() {
        if (m_condition.getAsBoolean()) {
            m_selectedBehavior = m_onTrue;
        } else {
            m_selectedBehavior = m_onFalse;
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
        return m_onTrue.runsWhenDisabled() && m_onFalse.runsWhenDisabled();
    }

}
