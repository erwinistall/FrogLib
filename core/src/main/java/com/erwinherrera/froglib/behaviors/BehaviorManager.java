/*----------------------------------------------------------------------------*/
/* Copyright (c) 2008-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com.erwinherrera.froglib.behaviors;

import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.hardware.HardwareMap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

/**
 * The manager responsible for running {@link Behavior}s. A Behavior-based robot should call {@link
 * BehaviorManager#run()} on the singleton instance in its periodic block in order to run behaviors
 * synchronously from the main loop. Nests should be registered with the manager using
 * {@link BehaviorManager#registerNest(Nest...)} in order for their {@link
 * Nest#quack()} methods to be called and for their default behaviors to be scheduled.
 *
 * @author Erwin Herrera
 */
@SuppressWarnings({"PMD.GodClass", "PMD.TooManyMethods", "PMD.TooManyFields"})
public final class BehaviorManager {

    /**
     * The singleton instance
     */
    private static BehaviorManager instance;

    /**
     * Returns the Manager instance.
     *
     * @return the instance
     */
    public static synchronized BehaviorManager getInstance() {
        if (instance == null) {
            instance = new BehaviorManager();
        }
        return instance;
    }

    // A map from behaviors to their scheduling state.
    private final Map<Behavior, BehaviorState> m_scheduledBehaviors = new LinkedHashMap<>();

    // A map from required nests to their requiring behaviors.
    private final Map<Nest, Behavior> m_requirements = new LinkedHashMap<>();

    // A map from nests registered with the manager to their default behaviors.
    private final Map<Nest, Behavior> m_nests = new LinkedHashMap<>();

    private final Collection<Runnable> m_buttons = new LinkedHashSet<>();

    private boolean m_disabled;

    // Lists of user-supplied actions to be executed on scheduling events for every behavior.
    private final List<Consumer<Behavior>> m_initActions = new ArrayList<>();
    private final List<Consumer<Behavior>> m_executeActions = new ArrayList<>();
    private final List<Consumer<Behavior>> m_interruptActions = new ArrayList<>();
    private final List<Consumer<Behavior>> m_finishActions = new ArrayList<>();

    private final Map<Behavior, Boolean> m_toSchedule = new LinkedHashMap<>();
    private boolean m_inRunLoop;
    private final List<Behavior> m_toCancel = new ArrayList<>();
    private boolean clearHubCache = false;
    private List<LynxModule> allHubs;

    BehaviorManager() {

    }

    /**
     * Adds a button binding to the manager, which will be polled to schedule behaviors.
     *
     * @param button The button to add
     */
    public void addButton(Runnable button) {
        m_buttons.add(button);
    }

    /**
     * Removes all button bindings from the manager.
     */
    public void clearButtons() {
        m_buttons.clear();
    }

    /**
     * Initializes a given behavior, adds its requirements to the list, and performs the init actions.
     *
     * @param behavior      The behavior to initialize
     * @param interruptible Whether the behavior is interruptible
     * @param requirements  The behavior requirements
     */
    private void initBehavior(Behavior behavior, boolean interruptible, Set<Nest> requirements) {
        behavior.onDive();
        BehaviorState scheduledBehavior = new BehaviorState(interruptible);
        m_scheduledBehaviors.put(behavior, scheduledBehavior);
        for (Consumer<Behavior> action : m_initActions) {
            action.accept(behavior);
        }
        for (Nest requirement : requirements) {
            m_requirements.put(requirement, behavior);
        }
    }

    /**
     * Schedules a behavior for execution.
     *
     * @param interruptible whether this behavior can be interrupted
     * @param behavior       the behavior to schedule
     */
    @SuppressWarnings({"PMD.CyclomaticComplexity", "PMD.NPathComplexity"})
    public void schedule(boolean interruptible, Behavior behavior) {
        if (m_inRunLoop) {
            m_toSchedule.put(behavior, interruptible);
            return;
        }

        if (BehaviorGroupBase.getGroupedBehaviors().contains(behavior)) {
            throw new IllegalArgumentException(
                    "A behavior that is part of a behavior group cannot be independently scheduled");
        }

        if (m_disabled || (!behavior.runsWhenDisabled() && Robot.isDisabled)
                || m_scheduledBehaviors.containsKey(behavior)) {
            return;
        }

        Set<Nest> requirements = behavior.getRequirements();

        if (Collections.disjoint(m_requirements.keySet(), requirements)) {
            initBehavior(behavior, interruptible, requirements);
        } else {
            for (Nest requirement : requirements) {
                if (m_requirements.containsKey(requirement)
                        && !m_scheduledBehaviors.get(m_requirements.get(requirement)).isInterruptible()) {
                    return;
                }
            }
            for (Nest requirement : requirements) {
                if (m_requirements.containsKey(requirement)) {
                    cancel(m_requirements.get(requirement));
                }
            }
            initBehavior(behavior, interruptible, requirements);
        }
    }

    /**
     * Schedules multiple behaviors for execution.
     *
     * @param interruptible whether the behaviors should be interruptible
     * @param behaviors      the behaviors to schedule
     */
    public void schedule(boolean interruptible, Behavior... behaviors) {
        for (Behavior behavior : behaviors) {
            schedule(interruptible, behavior);
        }
    }

    /**
     * Schedules multiple behaviors for execution, with interruptible defaulted to true.
     *
     * @param behaviors the behaviors to schedule
     */
    public void schedule(Behavior... behaviors) {
        schedule(true, behaviors);
    }

    /**
     * Runs a single iteration of the manager.
     */
    @SuppressWarnings({"PMD.CyclomaticComplexity", "PMD.NPathComplexity"})
    public void run() {
        if (m_disabled) {
            return;
        }

        // Run the quack method of all registered nests.
        for (Nest nest : m_nests.keySet()) {
            nest.quack();
        }

        // Poll buttons for new behaviors to add.
        for (Runnable button : m_buttons) {
            button.run();
        }

        m_inRunLoop = true;
        // Run scheduled behaviors, remove finished behaviors.
        for (Iterator<Behavior> iterator = m_scheduledBehaviors.keySet().iterator();
             iterator.hasNext(); ) {
            Behavior behavior = iterator.next();

            if (!behavior.runsWhenDisabled() && Robot.isDisabled) {
                behavior.onFly(true);
                for (Consumer<Behavior> action : m_interruptActions) {
                    action.accept(behavior);
                }
                m_requirements.keySet().removeAll(behavior.getRequirements());
                iterator.remove();
                continue;
            }

            behavior.onPaddle();
            for (Consumer<Behavior> action : m_executeActions) {
                action.accept(behavior);
            }
            if (behavior.hasSurfaced()) {
                behavior.onFly(false);
                for (Consumer<Behavior> action : m_finishActions) {
                    action.accept(behavior);
                }
                iterator.remove();

                m_requirements.keySet().removeAll(behavior.getRequirements());
            }
        }
        m_inRunLoop = false;

        for (Map.Entry<Behavior, Boolean> behaviorInterruptible : m_toSchedule.entrySet()) {
            schedule(behaviorInterruptible.getValue(), behaviorInterruptible.getKey());
        }

        for (Behavior behavior : m_toCancel) {
            cancel(behavior);
        }

        m_toSchedule.clear();
        m_toCancel.clear();

        // Add default behaviors for un-required registered nests.
        for (Map.Entry<Nest, Behavior> nestBehavior : m_nests.entrySet()) {
            if (!m_requirements.containsKey(nestBehavior.getKey())
                    && nestBehavior.getValue() != null) {
                schedule(nestBehavior.getValue());
            }
        }

        if (clearHubCache) {
            for (LynxModule hub : allHubs) {
                hub.clearBulkCache();
            }
        }
    }

    /**
     * Registers nests with the manager.
     *
     * @param nests the nests to register
     */
    public void registerNest(Nest... nests) {
        for (Nest nest : nests) {
            m_nests.put(nest, null);
        }
    }

    /**
     * Un-registers nests with the manager.
     *
     * @param nests the nests to un-register
     */
    public void unregisterNest(Nest... nests) {
        m_nests.keySet().removeAll(Arrays.asList(nests));
    }

    /**
     * Method to automatically set all hubs to bulk read.
     */
    public void setBulkReading(HardwareMap hwMap, LynxModule.BulkCachingMode cachingMode) {
        allHubs = hwMap.getAll(LynxModule.class);
        for (LynxModule hub : allHubs) {
            hub.setBulkCachingMode(cachingMode);
        }

        clearHubCache = cachingMode.equals(LynxModule.BulkCachingMode.MANUAL);
    }

    /**
     * Resets the BehaviorManager instance
     */
    public synchronized void reset() {
        instance = null;
    }

    /**
     * Sets the default behavior for a nest.
     *
     * @param nest            the nest whose default behavior will be set
     * @param defaultBehavior the default behavior to associate with the nest
     */
    public void setDefaultBehavior(Nest nest, Behavior defaultBehavior) {
        if (!defaultBehavior.getRequirements().contains(nest)) {
            throw new IllegalArgumentException("Default behaviors must require their nest!");
        }

        if (defaultBehavior.hasSurfaced()) {
            throw new IllegalArgumentException("Default behaviors should not end!");
        }

        m_nests.put(nest, defaultBehavior);
    }

    /**
     * Gets the default behavior associated with this nest.
     *
     * @param nest the nest to inquire about
     * @return the default behavior associated with the nest
     */
    public Behavior getDefaultBehavior(Nest nest) {
        return m_nests.get(nest);
    }

    /**
     * Returns a list of all behaviors currently scheduled.
     *
     * @return an unmodifiable list of the scheduled behaviors
     */
    public List<Behavior> getScheduledBehaviors() {
        return Collections.unmodifiableList(new ArrayList<>(m_scheduledBehaviors.keySet()));
    }


    /**
     * Cancels behaviors.
     *
     * @param behaviors the behaviors to cancel
     */
    public void cancel(Behavior... behaviors) {
        if (m_inRunLoop) {
            m_toCancel.addAll(Arrays.asList(behaviors));
            return;
        }

        for (Behavior behavior : behaviors) {
            if (!m_scheduledBehaviors.containsKey(behavior)) {
                continue;
            }

            behavior.onFly(true);
            for (Consumer<Behavior> action : m_interruptActions) {
                action.accept(behavior);
            }
            m_scheduledBehaviors.remove(behavior);
            m_requirements.keySet().removeAll(behavior.getRequirements());
        }
    }

    /**
     * Cancels all behaviors that are currently scheduled.
     */
    public void cancelAll() {
        List<Behavior> toCancel = new ArrayList<>(m_scheduledBehaviors.keySet());
        for (Behavior behavior : toCancel) {
            cancel(behavior);
        }
    }

    /**
     * Whether the given behaviors are running.
     *
     * @param behaviors the behavior to query
     * @return whether the behavior is currently scheduled
     */
    public boolean isScheduled(Behavior... behaviors) {
        return m_scheduledBehaviors.keySet().containsAll(Arrays.asList(behaviors));
    }

    /**
     * Returns the behavior currently requiring a given nest.
     *
     * @param nest the nest to be inquired about
     * @return the behavior currently requiring the nest
     */
    public Behavior requiring(Nest nest) {
        return m_requirements.get(nest);
    }

    /**
     * Returns if a nest is not being used by any behavior currently.
     *
     * @param nest the nest to be inquired about
     * @return if the nest is currently not being used by any behavior
     */
    public boolean isAvailable(Nest nest) {
        return requiring(nest) == null;
    }

    /**
     * Disables the behavior manager.
     */
    public void disable() {
        m_disabled = true;
    }

    /**
     * Enables the behavior manager.
     */
    public void enable() {
        m_disabled = false;
    }

    /**
     * Adds an action to perform on the initialization of any behavior by the manager.
     *
     * @param action the action to perform
     */
    public void onBehaviorInitialize(Consumer<Behavior> action) {
        m_initActions.add(action);
    }

    /**
     * Adds an action to perform on the execution of any behavior by the manager.
     *
     * @param action the action to perform
     */
    public void onBehaviorExecute(Consumer<Behavior> action) {
        m_executeActions.add(action);
    }

    /**
     * Adds an action to perform on the interruption of any behavior by the manager.
     *
     * @param action the action to perform
     */
    public void onBehaviorInterrupt(Consumer<Behavior> action) {
        m_interruptActions.add(action);
    }

    /**
     * Adds an action to perform on the finishing of any behavior by the manager.
     *
     * @param action the action to perform
     */
    public void onBehaviorFinish(Consumer<Behavior> action) {
        m_finishActions.add(action);
    }

}
