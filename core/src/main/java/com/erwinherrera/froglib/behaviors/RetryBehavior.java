package com.erwinherrera.froglib.behaviors;

import java.util.function.BooleanSupplier;

/**
 * A Behavior that runs a given Behavior and, if a condition is met,
 * retries it (or a different Behavior) up to a specified number of times.
 * <p>
 * This Behavior is useful for actions that may not succeed on the first attempt
 * and require re-running, such as vision alignment or precise mechanism movement.
 *
 * @author Erwin Herrera
 */
public class RetryBehavior extends BehaviorBase {
    private final Behavior Behavior;
    private final Behavior retryBehavior;
    private final BooleanSupplier successCondition;
    private final int maxRetries;

    private Behavior currentBehavior;
    private int retryCount = 0;
    private boolean isFinished = false;

    /**
     * Creates a new RetryBehavior.
     *
     * @param Behavior        Supplies the Behavior to run on the first attempt.
     * @param retryBehavior   A function that takes the retry count (starting at 1) and returns the Behavior for that attempt.
     * @param successCondition A condition that returns {@code true} if a retry should be attempted, or {@code false} if the Behavior should finish without retrying.
     * @param maxRetries     The maximum number of retries allowed.
     */
    public RetryBehavior(Behavior Behavior, Behavior retryBehavior, BooleanSupplier successCondition, int maxRetries) {
        BehaviorGroupBase.requireUngrouped(Behavior, retryBehavior);

        this.Behavior = Behavior;
        this.retryBehavior = retryBehavior;
        this.successCondition = successCondition;
        this.maxRetries = maxRetries;

        addRequirements(Behavior.getRequirements().toArray(new Nest[0]));
        addRequirements(retryBehavior.getRequirements().toArray(new Nest[0]));
    }

    /**
     * Creates a new RetryBehavior where the retry Behavior is the same as the initial one.
     *
     * @param Behavior        A supplier that creates a new instance of the Behavior to run.
     * @param successCondition A condition that returns {@code true} if a retry should be attempted, or {@code false} if the Behavior should finish without retrying.
     * @param maxRetries     The maximum number of retries allowed.
     */
    public RetryBehavior(Behavior Behavior, BooleanSupplier successCondition, int maxRetries) {
        this(Behavior, Behavior, successCondition, maxRetries);
    }

    @Override
    public void onDive() {
        isFinished = false;
        retryCount = 0;

        currentBehavior = Behavior;
        currentBehavior.onDive();
    }

    @Override
    public void onPaddle() {
        // If the sub-Behavior is not finished, execute it
        if (!currentBehavior.hasSurfaced()) {
            currentBehavior.onPaddle();
            return;
        }

        currentBehavior.onFly(false);

        // Check if we should retry
        if (retryCount < maxRetries && !successCondition.getAsBoolean()) {
            retryCount++;
            currentBehavior = retryBehavior;
            currentBehavior.onDive();
        } else {
            isFinished = true;
        }
    }

    @Override
    public void onFly(boolean interrupted) {
        // When RetryBehavior is ended (for any reason), we must also end the sub-Behavior it is currently managing
        if (currentBehavior != null) {
            currentBehavior.onFly(interrupted);
        }
    }

    @Override
    public boolean hasSurfaced() {
        return isFinished;
    }
}
