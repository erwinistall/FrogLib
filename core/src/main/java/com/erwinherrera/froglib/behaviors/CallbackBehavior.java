package com.erwinherrera.froglib.behaviors;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * Wrapper to easily add callbacks to a Behavior
 * @author Erwin Herrera
 */
public class CallbackBehavior<T extends Behavior> implements Behavior {
    private final Map<BooleanSupplier, Runnable> whenRunnables = new HashMap<>();
    private final Map<BooleanSupplier, Behavior> whenBehaviors = new HashMap<>();
    private final Map<BooleanSupplier, Consumer<T>> whenConsumers = new HashMap<>();
    private final Map<Predicate<T>, Runnable> whenSelfRunnables = new HashMap<>();
    private final Map<Predicate<T>, Behavior> whenSelfBehaviors = new HashMap<>();
    private final Map<Predicate<T>, Consumer<T>> whenSelfConsumers = new HashMap<>();
    protected Set<Nest> m_requirements = new HashSet<>();
    private final T Behavior;

    /**
     * Wrapper for adding custom callbacks to Behaviors. This expects a single Behavior,
     * so multiple Behaviors need to be put in a BehaviorGroup first:
     * @param Behavior the Behavior to be schedules as uninterruptible
     * {@link SequentialBehaviorGroup}
     * {@link ParallelBehaviorGroup}
     */
    public CallbackBehavior(T Behavior) {
        this.Behavior = Behavior;
    }

    public final void addRequirements(Nest... requirements) {
        m_requirements.addAll(Arrays.asList(requirements));
    }

    /**
     * Adds a callback with a boolean supplier
     * @param condition Runs the runnable the first time this is true
     * @param action Callback to run
     * @return Itself for chaining purposes
     */
    @Override
    public CallbackBehavior<T> when(BooleanSupplier condition, Runnable action) {
        whenRunnables.put(condition, action);
        return this;
    }

    /**
     * Adds a callback with a boolean supplier
     * @param condition Schedules the Behavior the first time this is true
     * @param action Behavior to schedule
     * @return Itself for chaining purposes
     */
    @Override
    public CallbackBehavior<T> when(BooleanSupplier condition, Behavior action) {
        whenBehaviors.put(condition, action);
        return this;
    }

    /**
     * Adds a callback with a boolean supplier
     * @param condition Schedules the Behavior the first time this is true
     * @param action Consumer for using the inner Behavior
     * @return Itself for chaining purposes
     */
    public CallbackBehavior<T> whenSelf(BooleanSupplier condition, Consumer<T> action) {
        whenConsumers.put(condition, action);
        return this;
    }

    /**
     * Adds a callback with access to the inner Behavior
     * @param condition Runs the runnable the first time this is true
     * @param action Callback to run
     * @return Itself for chaining purposes
     */
    public CallbackBehavior<T> whenSelf(Predicate<T> condition, Runnable action) {
        whenSelfRunnables.put(condition, action);
        return this;
    }

    /**
     * Adds a callback with access to the inner Behavior
     * @param condition Schedules the Behavior the first time this is true
     * @param action Consumer for using the inner Behavior
     * @return Itself for chaining purposes
     */
    public CallbackBehavior<T> whenSelf(Predicate<T> condition, Behavior action) {
        whenSelfBehaviors.put(condition, action);
        return this;
    }

    /**
     * Adds a callback with access to the inner Behavior
     * @param condition Schedules the Behavior the first time this is true
     * @param action Behavior to schedule
     * @return Itself for chaining purposes
     */
    public CallbackBehavior<T> whenSelf(Predicate<T> condition, Consumer<T> action) {
        whenSelfConsumers.put(condition, action);
        return this;
    }

    @Override
    public void onDive() {
        Behavior.schedule();
    }

    @Override
    public void onPaddle() {
        // Callbacks
        for (Iterator<Map.Entry<BooleanSupplier, Runnable>> it = whenRunnables.entrySet().iterator(); it.hasNext();) {
            Map.Entry<BooleanSupplier, Runnable> action = it.next();
            if (action.getKey().getAsBoolean()) {
                action.getValue().run();
                it.remove();
            }
        }
        for (Iterator<Map.Entry<BooleanSupplier, Behavior>> it = whenBehaviors.entrySet().iterator(); it.hasNext();) {
            Map.Entry<BooleanSupplier, Behavior> action = it.next();
            if (action.getKey().getAsBoolean()) {
                action.getValue().schedule();
                it.remove();
            }
        }
        for (Iterator<Map.Entry<BooleanSupplier, Consumer<T>>> it = whenConsumers.entrySet().iterator(); it.hasNext();) {
            Map.Entry<BooleanSupplier, Consumer<T>> action = it.next();
            if (action.getKey().getAsBoolean()) {
                action.getValue().accept(Behavior);
                it.remove();
            }
        }

        // Self callbacks
        for (Iterator<Map.Entry<Predicate<T>, Runnable>> it = whenSelfRunnables.entrySet().iterator(); it.hasNext();) {
            Map.Entry<Predicate<T>, Runnable> action = it.next();
            if (action.getKey().test(Behavior)) {
                action.getValue().run();
                it.remove();
            }
        }
        for (Iterator<Map.Entry<Predicate<T>, Behavior>> it = whenSelfBehaviors.entrySet().iterator(); it.hasNext();) {
            Map.Entry<Predicate<T>, Behavior> action = it.next();
            if (action.getKey().test(Behavior)) {
                action.getValue().schedule();
                it.remove();
            }
        }
        for (Iterator<Map.Entry<Predicate<T>, Consumer<T>>> it = whenSelfConsumers.entrySet().iterator(); it.hasNext();) {
            Map.Entry<Predicate<T>, Consumer<T>> action = it.next();
            if (action.getKey().test(Behavior)) {
                action.getValue().accept(Behavior);
                it.remove();
            }
        }
    }

    @Override
    public boolean hasSurfaced() {
        return !BehaviorManager.getInstance().isScheduled(Behavior);
    }

    @Override
    public Set<Nest> getRequirements() {
        return m_requirements;
    }
}
