/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com.erwinherrera.froglib.behaviors;

import java.util.function.BooleanSupplier;
import java.util.function.Consumer;

/**
 * A Behavior that allows the user to pass in functions for each of the basic Behavior methods through
 * the constructor.  Useful for inline definitions of complex Behaviors - note, however, that if a
 * Behavior is beyond a certain complexity it is usually better practice to write a proper class for
 * it than to inline it.
 *
 * @author Erwin Herrera
 */
public class FunctionalBehavior extends BehaviorBase {

    protected final Runnable m_onStart;
    protected final Runnable m_onTick;
    protected final Consumer<Boolean> m_onEnd;
    protected final BooleanSupplier m_isDone;

    /**
     * Creates a new FunctionalBehavior.
     *
     * @param onStart      the function to run on Behavior start
     * @param onTick       the function to run on Behavior tick
     * @param onEnd        the function to run on Behavior end
     * @param isDone       the function that determines whether the Behavior has finished
     * @param requirements the Nests required by this Behavior
     */
    public FunctionalBehavior(Runnable onStart, Runnable onTick, Consumer<Boolean> onEnd,
                             BooleanSupplier isDone, Nest... requirements) {
        m_onStart = onStart;
        m_onTick = onTick;
        m_onEnd = onEnd;
        m_isDone = isDone;

        addRequirements(requirements);
    }

    @Override
    public void onDive() {
        m_onStart.run();
    }

    @Override
    public void onPaddle() {
        m_onTick.run();
    }

    @Override
    public void onFly(boolean interrupted) {
        m_onEnd.accept(interrupted);
    }

    @Override
    public boolean hasSurfaced() {
        return m_isDone.getAsBoolean();
    }

}
