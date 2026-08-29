/*
 * ----------------------------------------------------------------------------
 *  Copyright (c) 2018-2019 FIRST. All Rights Reserved.
 *  Open Source Software - may be modified and shared by FRC teams. The code
 *  must be accompanied by the FIRST BSD license file in the root directory of
 *  the project.
 * ----------------------------------------------------------------------------
 */

package com.erwinherrera.froglib.behaviors;

import static com.erwinherrera.froglib.Behavior.BehaviorGroupBase.registerGroupedBehaviors;
import static com.erwinherrera.froglib.Behavior.BehaviorGroupBase.requireUngrouped;

import java.util.function.BooleanSupplier;

/**
 * A Behavior that runs another Behavior repeatedly, restarting it when it ends, until this Behavior is
 * interrupted. While this class does not extend {@link BehaviorGroupBase}, it is still considered a
 * BehaviorGroup, as it allows one to compose another Behavior within it; the Behavior instances that
 * are passed to it cannot be added to any other groups, or scheduled individually.
 *
 * <p>As a rule, BehaviorGroups require the union of the requirements of their component Behaviors.
 *
 * @author Erwin Herrera
 */
public class RepeatBehavior extends BehaviorBase{

    protected final Behavior m_Behavior;
    private int timesRepeated;
    private int maxRepeatTimes;
    private BooleanSupplier condition;

    /**
     * Creates a new RepeatBehavior. Will run another Behavior repeatedly, restarting it whenever it
     * ends, until this Behavior is interrupted.
     *
     * @param Behavior the Behavior to run repeatedly
     */
    public RepeatBehavior(Behavior Behavior) {
        requireUngrouped(Behavior);
        registerGroupedBehaviors(Behavior);
        m_Behavior = Behavior;
        m_requirements.addAll(Behavior.getRequirements());
    }

    /**
     * Creates a new overloaded RepeatBehavior. Will run another Behavior repeatedly, restarting it whenever it
     * ends, until this Behavior is interrupted or a condition is met. Effectively acts as a repeat until.
     *
     * @param Behavior the Behavior to run repeatedly
     * @param condition the condition to end the Behavior
     */
    public RepeatBehavior(Behavior Behavior, BooleanSupplier condition) {
        this.condition = condition;

        requireUngrouped(Behavior);
        registerGroupedBehaviors(Behavior);
        m_Behavior = Behavior;
        m_requirements.addAll(Behavior.getRequirements());
    }

    /**
     * Creates a new overloaded RepeatBehavior. Runs another Behavior maxRepeatTimes amount of times, and ends when
     * it has repeated enough times or if this Behavior is interrupted.
     *
     * @param Behavior the Behavior to run repeatedly
     * @param maxRepeatTimes the number of times to repeat the Behavior (has to be greater than 0)
     */
    public RepeatBehavior(Behavior Behavior, int maxRepeatTimes) {
        if (maxRepeatTimes <= 0) {
            throw new IllegalArgumentException("RepeatBehaviors' maxRepeatTimes cannot be negative or zero!");
        }
        this.maxRepeatTimes = maxRepeatTimes;

        requireUngrouped(Behavior);
        registerGroupedBehaviors(Behavior);
        m_Behavior = Behavior;
        m_requirements.addAll(Behavior.getRequirements());
    }

    @Override
    public void onDive() {
        timesRepeated = 0;
        m_Behavior.onDive();
    }

    @Override
    public void onPaddle() {
        m_Behavior.onPaddle();

        if (m_Behavior.hasSurfaced()) {
            m_Behavior.onFly(false);
            timesRepeated++;

            if (!this.hasSurfaced()) {
                m_Behavior.onDive();
            }
        }
    }

    @Override
    public boolean hasSurfaced() {
        return (maxRepeatTimes > 0 && timesRepeated >= maxRepeatTimes) || (condition != null && condition.getAsBoolean());
    }

    @Override
    public void onFly(boolean interrupted) {
        if (m_Behavior.isScheduled() || !m_Behavior.hasSurfaced()) {
            m_Behavior.onFly(interrupted);
        }
    }

    @Override
    public boolean runsWhenDisabled() {
        return m_Behavior.runsWhenDisabled();
    }
}
