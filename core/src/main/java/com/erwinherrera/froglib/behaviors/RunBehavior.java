/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com.erwinherrera.froglib.behaviors;

import androidx.annotation.NonNull;

import java.util.function.BooleanSupplier;

/**
 * A Behavior that runs a Runnable continuously.  Has no end condition as-is;
 * either subclass it or use {@link Behavior#withTimeout(long)} or
 * {@link Behavior#interruptOn(BooleanSupplier)} to give it one.  If you only wish
 * to execute a Runnable once, use {@link InstantBehavior}.
 */
public class RunBehavior extends BehaviorBase {

    protected final Runnable m_toRun;

    /**
     * Creates a new RunBehavior.  The Runnable will be run continuously until the Behavior
     * ends.  Does not run when disabled.
     *
     * @param toRun        the Runnable to run
     * @param requirements the Nests to require
     */
    public RunBehavior(@NonNull Runnable toRun, Nest... requirements) {
        m_toRun = toRun;

        addRequirements(requirements);
    }

    @Override
    public void onPaddle() {
        m_toRun.run();
    }

}
