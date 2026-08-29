package com.erwinherrera.froglib.behaviors;

import java.util.List;
import java.util.Map;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

/**
 * A utility class that provides pre-defined Behaviors for the Behavior-based framework.
 * These Behaviors encapsulate common behaviors, such as running actions once, repeatedly,
 * with start and end actions, handling time delays, and printing messages.
 *
 * @author Erwin Herrera
 */
public final class Behaviors {
    /**
     * Constructs a Behavior that does nothing, finishing immediately.
     *
     * @return the Behavior
     */
    public static Behavior none() {
        return new InstantBehavior();
    }

    /**
     * Constructs a Behavior that does nothing until interrupted.
     *
     * @param requirements Nests to require
     * @return the Behavior
     */
    public static Behavior idle(Nest... requirements) {
        return run(() -> {}, requirements);
    }

    // Action Behaviors

    /**
     * Constructs a Behavior that runs an action once and finishes.
     *
     * @param action the action to run
     * @param requirements Nests the action requires
     * @return the Behavior
     * @see InstantBehavior
     */
    public static Behavior runOnce(Runnable action, Nest... requirements) {
        return new InstantBehavior(action, requirements);
    }

    /**
     * Constructs a Behavior that runs an action every iteration until interrupted.
     *
     * @param action the action to run
     * @param requirements Nests the action requires
     * @return the Behavior
     * @see RunBehavior
     */
    public static Behavior run(Runnable action, Nest... requirements) {
        return new RunBehavior(action, requirements);
    }

    /**
     * Constructs a Behavior that runs an action once and another action when the Behavior is
     * interrupted.
     *
     * @param start the action to run on start
     * @param end the action to run on interrupt
     * @param requirements Nests the action requires
     * @return the Behavior
     * @see StartEndBehavior
     */
    public static Behavior startEnd(Runnable start, Runnable end, Nest... requirements) {
        return new StartEndBehavior(start, end, requirements);
    }

    /**
     * Constructs a Behavior that runs an action every iteration until interrupted, and then runs a
     * second action.
     *
     * @param run the action to run every iteration
     * @param end the action to run on interrupt
     * @param requirements Nests the action requires
     * @return the Behavior
     */
    public static Behavior runEnd(Runnable run, Runnable end, Nest... requirements) {
        return new FunctionalBehavior(() -> {}, run, interrupted -> end.run(), () -> false, requirements);
    }

    /**
     * Constructs a Behavior that runs an action once, and then runs an action every iteration until
     * interrupted.
     *
     * @param start the action to run on start
     * @param run the action to run every iteration
     * @param requirements Nests the action requires
     * @return the Behavior
     */
    public static Behavior startRun(Runnable start, Runnable run, Nest... requirements) {
        return new FunctionalBehavior(start, run, interrupted -> {}, () -> false, requirements);
    }

    /**
     * Constructs a Behavior that prints a message and finishes.
     *
     * @param message the message to print
     * @return the Behavior
     * @see PrintBehavior
     */
    public static Behavior print(String message) {
        return new PrintBehavior(message);
    }

    // Idling Behaviors

    /**
     * Constructs a Behavior that does nothing, finishing after a specified duration.
     *
     * @param millis after how long the Behavior finishes
     * @return the Behavior
     * @see WaitBehavior
     */
    public static Behavior waitMillis(long millis) {
        return new WaitBehavior(millis);
    }

    /**
     * Constructs a Behavior that does nothing, finishing once a condition becomes true.
     *
     * @param condition the condition
     * @return the Behavior
     * @see WaitUntilBehavior
     */
    public static Behavior waitUntil(BooleanSupplier condition) {
        return new WaitUntilBehavior(condition);
    }

    // Selector Behaviors

    /**
     * Runs one of two Behaviors, based on the boolean selector function.
     *
     * @param onTrue the Behavior to run if the selector function returns true
     * @param onFalse the Behavior to run if the selector function returns false
     * @param selector the selector function
     * @return the Behavior
     * @see ConditionalBehavior
     */
    public static Behavior either(Behavior onTrue, Behavior onFalse, BooleanSupplier selector) {
        return new ConditionalBehavior(onTrue, onFalse, selector);
    }

    /**
     * Runs one of several Behaviors, based on the selector function.
     *
     * @param Behaviors the map of Behaviors to choose from
     * @param selector the selector to determine which Behavior to run
     * @return the Behavior
     * @see SelectBehavior
     */
    public static Behavior select(Map<Object, Behavior> Behaviors, Supplier<Object> selector) {
        return new SelectBehavior(Behaviors, selector);
    }

    /**
     * Runs the Behavior supplied by the supplier.
     *
     * @param supplier the Behavior supplier
     * @param requirements the list of requirements for this Behavior
     * @return the Behavior
     * @see DeferredBehavior
     */
    public static Behavior defer(Supplier<Behavior> supplier, List<Nest> requirements) {
        return new DeferredBehavior(supplier, requirements);
    }

    /**
     * Constructs a Behavior that schedules the Behavior returned from the supplier when started,
     * and ends when it is no longer scheduled. The supplier is called when the Behavior is
     * started.
     *
     * @param supplier the Behavior supplier
     * @return the Behavior
     * @see DeferredBehavior
     */
    public static Behavior deferredProxy(Supplier<Behavior> supplier) {
        return defer(() -> supplier.get().asProxy(), List.of());
    }

    // Behavior Groups

    /**
     * Runs a group of Behaviors in series, one after the other.
     *
     * @param Behaviors the Behaviors to include
     * @return the Behavior group
     * @see SequentialBehaviorGroup
     */
    public static Behavior sequence(Behavior... Behaviors) {
        return new SequentialBehaviorGroup(Behaviors);
    }

    /**
     * Runs a group of Behaviors in series, one after the other. Once the last Behavior ends, the group
     * is restarted and runs perpetually.
     *
     * @param Behaviors the Behaviors to include
     * @return the Behavior group
     * @see SequentialBehaviorGroup
     * @see Behavior#perpetually()
     */
    public static Behavior perpetuatingSequence(Behavior... Behaviors) {
        return sequence(Behaviors).perpetually();
    }

    /**
     * Runs a group of Behaviors at the same time. Ends once all Behaviors in the group finish.
     *
     * @param Behaviors the Behaviors to include
     * @return the Behavior
     * @see ParallelBehaviorGroup
     */
    public static Behavior parallel(Behavior... Behaviors) {
        return new ParallelBehaviorGroup(Behaviors);
    }

    /**
     * Runs a group of Behaviors at the same time. Ends once any Behavior in the group finishes, and
     * cancels the others.
     *
     * @param Behaviors the Behaviors to include
     * @return the Behavior group
     * @see ParallelRaceGroup
     */
    public static Behavior race(Behavior... Behaviors) {
        return new ParallelRaceGroup(Behaviors);
    }

    /**
     * Runs a group of Behaviors at the same time. Ends once a specific Behavior finishes, and cancels
     * the others.
     *
     * @param deadline the deadline Behavior
     * @param otherBehaviors the other Behaviors to include
     * @return the Behavior group
     * @see ParallelDeadlineGroup
     */
    public static Behavior deadline(Behavior deadline, Behavior... otherBehaviors) {
        return new ParallelDeadlineGroup(deadline, otherBehaviors);
    }

    private Behaviors() {
        throw new UnsupportedOperationException("This is a utility class");
    }
}
