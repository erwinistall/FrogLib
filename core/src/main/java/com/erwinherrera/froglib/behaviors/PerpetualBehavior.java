/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com.erwinherrera.froglib.behaviors;

import static com.erwinherrera.froglib.Behavior.BehaviorGroupBase.registerGroupedBehaviors;
import static com.erwinherrera.froglib.Behavior.BehaviorGroupBase.requireUngrouped;

/**
 * A Behavior that runs another Behavior in perpetuity, ignoring that Behavior's end conditions.  While
 * this class does not extend {@link BehaviorGroupBase}, it is still considered a BehaviorGroup, as it
 * allows one to compose another Behavior within it; the Behavior instances that are passed to it
 * cannot be added to any other groups, or scheduled individually.
 *
 * <p>As a rule, BehaviorGroups require the union of the requirements of their component Behaviors.
 *
 * @author Erwin Herrera
 */
public class PerpetualBehavior extends BehaviorBase {

    protected final Behavior m_Behavior;

    /**
     * Creates a new PerpetualBehavior.  Will run another Behavior in perpetuity, ignoring that
     * Behavior's end conditions, unless this Behavior itself is interrupted.
     *
     * @param Behavior the Behavior to run perpetually
     */
    public PerpetualBehavior(Behavior Behavior) {
        requireUngrouped(Behavior);
        registerGroupedBehaviors(Behavior);
        m_Behavior = Behavior;
        m_requirements.addAll(Behavior.getRequirements());
    }

    @Override
    public void onDive() {
        m_Behavior.onDive();
    }

    @Override
    public void onPaddle() {
        m_Behavior.onPaddle();
    }

    @Override
    public void onFly(boolean interrupted) {
        m_Behavior.onFly(interrupted);
    }

    @Override
    public boolean runsWhenDisabled() {
        return m_Behavior.runsWhenDisabled();
    }

}
