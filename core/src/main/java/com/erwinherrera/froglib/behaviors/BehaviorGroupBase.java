/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com.erwinherrera.froglib.behaviors;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.WeakHashMap;

/**
 * A base for BehaviorGroups. Statically tracks Behaviors that have been allocated to groups to
 * ensure those Behaviors are not also used independently, which can result in inconsistent Behavior
 * state and unpredictable execution.
 *
 * @author Erwin Herrera
 */
public abstract class BehaviorGroupBase extends BehaviorBase implements Behavior {

    private static final Set<Behavior> m_groupedBehaviors =
            Collections.newSetFromMap(new WeakHashMap<>());

    static void registerGroupedBehaviors(Behavior... Behaviors) {
        m_groupedBehaviors.addAll(Arrays.asList(Behaviors));
    }

    /**
     * Clears the list of grouped Behaviors, allowing all Behaviors to be freely used again.
     *
     * <p>WARNING: Using this haphazardly can result in unexpected/undesirable behavior.  Do not
     * use this unless you fully understand what you are doing.
     */
    public static void clearGroupedBehaviors() {
        m_groupedBehaviors.clear();
    }

    /**
     * Removes a single Behavior from the list of grouped Behaviors, allowing it to be freely used
     * again.
     *
     * <p>WARNING: Using this haphazardly can result in unexpected/undesirable behavior. Do not
     * use this unless you fully understand what you are doing.
     *
     * @param Behavior the Behavior to remove from the list of grouped Behaviors
     */
    public static void clearGroupedBehavior(Behavior Behavior) {
        m_groupedBehaviors.remove(Behavior);
    }


    /**
     * Requires that the specified Behaviors not have been already allocated to a BehaviorGroup. Throws
     * an {@link IllegalArgumentException} if Behaviors have been allocated.
     *
     * @param Behaviors The Behaviors to check
     */
    public static void requireUngrouped(Behavior... Behaviors) {
        requireUngrouped(Arrays.asList(Behaviors));
    }

    /**
     * Requires that the specified Behaviors not have been already allocated to a BehaviorGroup. Throws
     * an {@link IllegalArgumentException} if Behaviors have been allocated.
     *
     * @param Behaviors The Behaviors to check
     */
    public static void requireUngrouped(Collection<Behavior> Behaviors) {
        if (!Collections.disjoint(Behaviors, getGroupedBehaviors())) {
            throw new IllegalArgumentException("Behaviors cannot be added to more than one BehaviorGroup");
        }
    }

    static Set<Behavior> getGroupedBehaviors() {
        return m_groupedBehaviors;
    }

    /**
     * Adds the given Behaviors to the Behavior group.
     *
     * @param Behaviors The Behaviors to add.
     */
    public abstract void addBehaviors(Behavior... Behaviors);

}
