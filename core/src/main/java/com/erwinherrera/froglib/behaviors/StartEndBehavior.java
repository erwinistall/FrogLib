package com.erwinherrera.froglib.behaviors;

import java.util.function.BooleanSupplier;


/**
 * A Behavior that runs a given runnable when it is initialized, and another runnable when it ends.
 * Useful for running and then stopping a motor, or extending and then retracting a solenoid.
 * Has no end condition as-is; either subclass it or use {@link Behavior#withTimeout(long)} or
 * {@link Behavior#interruptOn(BooleanSupplier)} to give it one.
 */
public class StartEndBehavior extends BehaviorBase {

    protected final Runnable m_onStart;
    protected final Runnable m_onEnd;

    /**
     * Creates a new StartEndBehavior.  Will run the given runnables when the Behavior starts and when
     * it ends.
     *
     * @param onStart      the Runnable to run on Behavior start
     * @param onEnd        the Runnable to run on Behavior end
     * @param requirements the Nests required by this Behavior
     */
    public StartEndBehavior(Runnable onStart, Runnable onEnd, Nest... requirements) {
        m_onStart = onStart;
        m_onEnd = onEnd;

        addRequirements(requirements);
    }

    @Override
    public void onDive() {
        m_onStart.run();
    }

    @Override
    public void onFly(boolean interrupted) {
        m_onEnd.run();
    }

}
