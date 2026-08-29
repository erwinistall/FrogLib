/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com.erwinherrera.froglib.behaviors;

import java.util.function.BooleanSupplier;

/**
 * A Behavior that does nothing but ends after a specified condition. Useful for
 * BehaviorGroups.
 *
 * @author Erwin Herrera
 */
public class WaitUntilBehavior extends BehaviorBase {

    private final BooleanSupplier m_condition;

    /**
     * Creates a new WaitUntilBehavior that ends after a given condition becomes true.
     *
     * @param condition the condition to determine when to end
     */
    public WaitUntilBehavior(BooleanSupplier condition) {
        m_condition = condition;
    }

    @Override
    public boolean hasSurfaced() {
        return m_condition.getAsBoolean();
    }

    @Override
    public boolean runsWhenDisabled() {
        return true;
    }

}
