/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com.erwinherrera.froglib.behaviors;

import java.util.Set;
import java.util.function.BooleanSupplier;

/**
 * A state machine representing a complete action to be performed by the robot. Behaviors are
 * run by the {@link BehaviorManager}, and can be composed into BehaviorGroups to allow users to
 * build complicated multi-step actions without the need to roll the state machine logic themselves.
 *
 * <p>Behaviors are run synchronously from the main robot loop; no multithreading is used, unless
 * specified explicitly from the behavior implementation.
 *
 * @author Erwin Herrera
 */
@SuppressWarnings("PMD.TooManyMethods")
public interface Behavior {

    /**
     * The initial subroutine of a behavior. Called once when the behavior is initially scheduled.
     */
    default void onDive() {
    }

    /**
     * The main body of a behavior. Called repeatedly while the behavior is scheduled.
     */
    default void onPaddle() {
    }

    /**
     * The action to take when the behavior ends. Called when either the behavior finishes normally,
     * or when it interrupted/canceled.
     *
     * @param interrupted whether the behavior was interrupted/canceled
     */
    default void onFly(boolean interrupted) {
    }

    /**
     * Whether the behavior has finished. Once a behavior finishes, the manager will call its
     * onFly() method and un-schedule it.
     *
     * @return whether the behavior has finished.
     */
    default boolean hasSurfaced() {
        return false;
    }

    /**
     * Specifies the set of nests used by this behavior. Two behaviors cannot use the same
     * nest at the same time. If the behavior is scheduled as interruptible and another
     * behavior is scheduled that shares a requirement, the behavior will be interrupted. Else,
     * the behavior will not be scheduled. If no nests are required, return an empty set.
     *
     * <p>Note: it is recommended that user implementations contain the requirements as a field,
     * and return that field here, rather than allocating a new set every time this is called.
     *
     * @return the set of nests that are required
     */
    Set<Nest> getRequirements();

    /**
     * Decorates this behavior with a timeout. If the specified timeout is exceeded before the behavior
     * finishes normally, the behavior will be interrupted and un-scheduled. Note that the
     * timeout only applies to the behavior returned by this method; the calling behavior
     * is not itself changed.
     *
     * @param millis the timeout duration
     * @return the behavior with the timeout added
     */
    default Behavior withTimeout(long millis) {
        return new ParallelRaceBehavior(this, new WaitBehavior(millis));
    }

    /**
     * Decorates this behavior with an interrupt condition. If the specified condition becomes true
     * before the behavior finishes normally, the behavior will be interrupted and un-scheduled.
     * Note that this only applies to the behavior returned by this method; the calling behavior
     * is not itself changed.
     *
     * @param condition the interrupt condition
     * @return the behavior with the interrupt condition added
     */
    default Behavior interruptOn(BooleanSupplier condition) {
        return new ParallelRaceBehavior(this, new WaitUntilBehavior(condition));
    }

    /**
     * Decorates this behavior with a runnable to run after the behavior finishes.
     *
     * @param toRun the Runnable to run
     * @return the decorated behavior
     */
    default Behavior whenFinished(Runnable toRun) {
        return new SequentialBehaviorGroup(this, new InstantBehavior(toRun));
    }

    /**
     * Decorates this behavior with a runnable to run before this behavior starts.
     *
     * @param toRun the Runnable to run
     * @return the decorated behavior
     */
    default Behavior beforeStarting(Runnable toRun) {
        return new SequentialBehaviorGroup(new InstantBehavior(toRun), this);
    }

    /**
     * An overloaded decorator of {@link Behavior#beforeStarting(Runnable)} that takes a behavior
     * as a parameter instead of a Runnable.
     *
     * @param behavior the Behavior to run
     * @return the decorated behavior
     */
    default Behavior beforeStarting(Behavior behavior) {
        return new SequentialBehaviorGroup(behavior, this);
    }

    /**
     * Decorates this behavior with a set of behaviors to run after it in sequence.
     *
     * @param next the behaviors to run next
     * @return the decorated behavior
     */
    default Behavior andThen(Behavior... next) {
        SequentialBehaviorGroup group = new SequentialBehaviorGroup(this);
        group.addBehaviors(next);
        return group;
    }

    /**
     * Decorates this behavior with a set of behaviors to run parallel to it, ending when the calling
     * behavior ends and interrupting all the others.
     *
     * @param parallel the behaviors to run in parallel
     * @return the decorated behavior
     */
    default Behavior deadlineWith(Behavior... parallel) {
        return new ParallelDeadlineBehavior(this, parallel);
    }

    /**
     * Decorates this behavior with a set of behaviors to run parallel to it, ending when the last
     * behavior ends.
     *
     * @param parallel the behaviors to run in parallel
     * @return the decorated behavior
     */
    default Behavior alongWith(Behavior... parallel) {
        ParallelBehaviorGroup group = new ParallelBehaviorGroup(this);
        group.addBehaviors(parallel);
        return group;
    }

    /**
     * Decorates this behavior with a set of behaviors to run parallel to it, ending when the first
     * behavior ends.
     *
     * @param parallel the behaviors to run in parallel
     * @return the decorated behavior
     */
    default Behavior raceWith(Behavior... parallel) {
        ParallelRaceBehavior group = new ParallelRaceBehavior(this);
        group.addBehaviors(parallel);
        return group;
    }

    /**
     * Decorates this behavior to run perpetually, ignoring its ordinary end conditions.
     *
     * @return the decorated behavior
     */
    default Behavior perpetually() {
        return new PerpetualBehavior(this);
    }

    /**
     * Decorates this behavior to run "by proxy" by wrapping it in a {@link ProxyScheduleBehavior}.
     *
     * @return the decorated behavior
     */
    default Behavior asProxy() {
        return new ProxyScheduleBehavior(this);
    }

    /**
     * Whether the behavior requires a given nest.
     *
     * @param requirement the nest to inquire about
     * @return whether the nest is required
     */
    default boolean hasRequirement(Nest requirement) {
        return getRequirements().contains(requirement);
    }

    /**
     * Schedules this behavior.
     *
     * @param interruptible whether this behavior can be interrupted by another behavior that
     *                      shares one of its requirements
     */
    default void schedule(boolean interruptible) {
        BehaviorManager.getInstance().schedule(interruptible, this);
    }

    /**
     * Schedules this behavior, defaulting to interruptible.
     */
    default void schedule() {
        schedule(true);
    }

    /**
     * Cancels this behavior.
     */
    default void cancel() {
        BehaviorManager.getInstance().cancel(this);
    }

    /**
     * Whether or not the behavior is currently scheduled.
     *
     * @return Whether the behavior is scheduled.
     */
    default boolean isScheduled() {
        return BehaviorManager.getInstance().isScheduled(this);
    }

    /**
     * Whether the given behavior should run when the robot is disabled.
     *
     * @return whether the behavior should run when the robot is disabled
     */
    default boolean runsWhenDisabled() {
        return false;
    }

    /**
     * Sets this behavior as uninterruptible.
     *
     * @return the decorated behavior
     */
    default Behavior uninterruptible() {
        return new UninterruptibleBehavior(this);
    }

    /**
     * Adds a callback with a boolean supplier
     *
     * @param condition Runs the runnable the first time this is true
     * @param runnable Callback to run
     * @return the decorated behavior
     */
    default Behavior when(BooleanSupplier condition, Runnable runnable) {
        return new CallbackBehavior<>(this).when(condition, runnable);
    }

    /**
     * Adds a callback with a boolean supplier
     *
     * @param condition Schedules the behavior the first time this is true
     * @param behavior Behavior to schedule
     * @return the decorated behavior
     */
    default Behavior when(BooleanSupplier condition, Behavior behavior) {
        return new CallbackBehavior<>(this).when(condition, behavior);
    }

    default String getName() {
        return this.getClass().getSimpleName();
    }
}
