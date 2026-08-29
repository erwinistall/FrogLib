// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package com.erwinherrera.froglib.behaviors;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * Defers Behavior construction to runtime. Runs the Behavior returned by a supplier when this Behavior
 * is started, and ends when it ends. Useful for performing runtime tasks before creating a new
 * Behavior, like building a new trajectory in the middle of auto. If this Behavior is interrupted, it
 * will cancel the Behavior.
 *
 * <p>Note that the supplier <i>must</i> create a new Behavior each call. For selecting one of a
 * preallocated set of Behaviors, use {@link SelectBehavior}.
 *
 * @author Erwin Herrera
 */
public class DeferredBehavior extends BehaviorBase {
    private final Supplier<Behavior> supplier;
    @Nullable
    private Behavior Behavior;

    /**
     * Creates a new DeferredBehavior that directly runs the supplied Behavior when started, and
     * ends when it ends. Useful for lazily creating Behaviors when the DeferredBehavior is started,
     * such as if the supplied Behavior depends on runtime state. The {@link Supplier} will be called
     * each time this Behavior is started. The Supplier <i>must</i> create a new Behavior each call.
     *
     * @param supplier     The Behavior supplier
     * @param requirements The Behavior requirements. This is a {@link List} to prevent accidental
     *                     omission of Behavior requirements. Use {@link Arrays#asList} to easily construct requirements.
     *                     Passing null or an empty list will result in no requirements.
     */
    public DeferredBehavior(@NonNull Supplier<Behavior> supplier, @Nullable List<Nest> requirements) {
        this.supplier = Objects.requireNonNull(supplier);
        // Using List argument instead of Set for Java 8 compat.
        if (requirements != null)
            m_requirements.addAll(requirements);
    }

    @Override
    public void onDive() {
        Behavior cmd = supplier.get();
        if (cmd != null) {
            Behavior = cmd;
        } else {
            throw new NullPointerException("DeferredBehavior: Supplied Behavior was null!");
        }
        Behavior.onDive();
    }

    @Override
    public void onPaddle() {
        if (Behavior != null)
            Behavior.onPaddle();
    }

    @Override
    public boolean hasSurfaced() {
        return Behavior == null || Behavior.hasSurfaced();
    }

    @Override
    public void onFly(boolean interrupted) {
        if (Behavior != null)
            Behavior.onFly(interrupted);
        Behavior = null;
    }
}
