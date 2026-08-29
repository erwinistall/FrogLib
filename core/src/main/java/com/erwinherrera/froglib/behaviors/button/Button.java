/*----------------------------------------------------------------------------*/
/* Copyright (c) 2008-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com.erwinherrera.froglib.behaviors.button;

import com.erwinherrera.froglib.behaviors.Behavior;

import java.util.function.BooleanSupplier;

/**
 * This class provides an easy way to link Behaviors to OI inputs.
 *
 * <p>It is very easy to link a button to a Behavior. For instance, you could link the trigger
 * button of a joystick to a "score" Behavior.
 *
 * <p>This class represents a subclass of Trigger that is specifically aimed at buttons on an
 * operator interface as a common use case of the more generalized Trigger objects. This is a simple
 * wrapper around Trigger with the method names renamed to fit the Button object use.
 *
 * @author Erwin Herrera
 */
@SuppressWarnings("PMD.TooManyMethods")
public abstract class Button extends Trigger {

    /**
     * Default constructor; creates a button that is never pressed (unless {@link Button#get()} is
     * overridden).
     */
    public Button() {
    }

    /**
     * Creates a new button with the given condition determining whether it is pressed.
     *
     * @param isPressed returns whether or not the trigger should be active
     */
    public Button(BooleanSupplier isPressed) {
        super(isPressed);
    }

    /**
     * Starts the given Behavior whenever the button is newly pressed.
     *
     * @param Behavior       the Behavior to start
     * @param interruptible whether the Behavior is interruptible
     * @return this button, so calls can be chained
     */
    public Button whenPressed(final Behavior Behavior, boolean interruptible) {
        whenActive(Behavior, interruptible);
        return this;
    }

    /**
     * Starts the given Behavior whenever the button is newly pressed. The Behavior is set to be
     * interruptible.
     *
     * @param Behavior the Behavior to start
     * @return this button, so calls can be chained
     */
    public Button whenPressed(final Behavior Behavior) {
        whenActive(Behavior);
        return this;
    }

    /**
     * Runs the given runnable whenever the button is newly pressed.
     *
     * @param toRun the runnable to run
     * @return this button, so calls can be chained
     */
    public Button whenPressed(final Runnable toRun) {
        whenActive(toRun);
        return this;
    }

    /**
     * Constantly starts the given Behavior while the button is held.
     * <p>
     * {@link Behavior#schedule(boolean)} will be called repeatedly while the button is held, and will
     * be canceled when the button is released.
     *
     * @param Behavior       the Behavior to start
     * @param interruptible whether the Behavior is interruptible
     * @return this button, so calls can be chained
     */
    public Button whileHeld(final Behavior Behavior, boolean interruptible) {
        whileActiveContinuous(Behavior, interruptible);
        return this;
    }

    /**
     * Constantly starts the given Behavior while the button is held.
     * <p>
     * {@link Behavior#schedule(boolean)} will be called repeatedly while the button is held, and will
     * be canceled when the button is released.  The Behavior is set to be interruptible.
     *
     * @param Behavior the Behavior to start
     * @return this button, so calls can be chained
     */
    public Button whileHeld(final Behavior Behavior) {
        whileActiveContinuous(Behavior);
        return this;
    }

    /**
     * Constantly runs the given runnable while the button is held.
     *
     * @param toRun the runnable to run
     * @return this button, so calls can be chained
     */
    public Button whileHeld(final Runnable toRun) {
        whileActiveContinuous(toRun);
        return this;
    }

    /**
     * Starts the given Behavior when the button is first pressed, and cancels it when it is released,
     * but does not start it again if it ends or is otherwise interrupted.
     *
     * @param Behavior       the Behavior to start
     * @param interruptible whether the Behavior is interruptible
     * @return this button, so calls can be chained
     */
    public Button whenHeld(final Behavior Behavior, boolean interruptible) {
        whileActiveOnce(Behavior, interruptible);
        return this;
    }

    /**
     * Starts the given Behavior when the button is first pressed, and cancels it when it is released,
     * but does not start it again if it ends or is otherwise interrupted.  The Behavior is set to be
     * interruptible.
     *
     * @param Behavior the Behavior to start
     * @return this button, so calls can be chained
     */
    public Button whenHeld(final Behavior Behavior) {
        whileActiveOnce(Behavior, true);
        return this;
    }


    /**
     * Starts the Behavior when the button is released.
     *
     * @param Behavior       the Behavior to start
     * @param interruptible whether the Behavior is interruptible
     * @return this button, so calls can be chained
     */
    public Button whenReleased(final Behavior Behavior, boolean interruptible) {
        whenInactive(Behavior, interruptible);
        return this;
    }

    /**
     * Starts the Behavior when the button is released.  The Behavior is set to be interruptible.
     *
     * @param Behavior the Behavior to start
     * @return this button, so calls can be chained
     */
    public Button whenReleased(final Behavior Behavior) {
        whenInactive(Behavior);
        return this;
    }

    /**
     * Runs the given runnable when the button is released.
     *
     * @param toRun the runnable to run
     * @return this button, so calls can be chained
     */
    public Button whenReleased(final Runnable toRun) {
        whenInactive(toRun);
        return this;
    }

    /**
     * Toggles the Behavior whenever the button is pressed (on then off then on).
     *
     * @param Behavior       the Behavior to start
     * @param interruptible whether the Behavior is interruptible
     */
    public Button toggleWhenPressed(final Behavior Behavior, boolean interruptible) {
        toggleWhenActive(Behavior, interruptible);
        return this;
    }

    /**
     * Toggles the Behavior whenever the button is pressed (on then off then on).  The Behavior is set
     * to be interruptible.
     *
     * @param Behavior the Behavior to start
     * @return this button, so calls can be chained
     */
    public Button toggleWhenPressed(final Behavior Behavior) {
        toggleWhenActive(Behavior);
        return this;
    }

    /**
     * Toggles the between the two Behaviors whenever the button is pressed (commadOne then
     * BehaviorTwo then BehaviorOne).
     *
     * @param BehaviorOne    the Behavior to start
     * @param BehaviorTwo    the Behavior to be activated after
     * @param interruptible whether the Behavior is interruptible
     * @return this button, so calls can be chained
     */
    public Button toggleWhenPressed(final Behavior BehaviorOne, final Behavior BehaviorTwo, boolean interruptible) {
        toggleWhenActive(BehaviorOne, BehaviorTwo, interruptible);
        return this;
    }

    /**
     * Toggles the between the two Behaviors whenever the button is pressed (commadOne then
     * BehaviorTwo then BehaviorOne).  These Behaviors are set to be interruptible.
     *
     * @param BehaviorOne the Behavior to start
     * @param BehaviorTwo the Behavior to be activated after
     * @return this button, so calls can be chained
     */
    public Button toggleWhenPressed(final Behavior BehaviorOne, final Behavior BehaviorTwo) {
        toggleWhenActive(BehaviorOne, BehaviorTwo);
        return this;
    }

    /**
     * Toggles the between the two given runnables whenever the button is pressed (runnableOne then
     * runnableTwo then runnableOne).  These runnables are set to be interruptible.
     *
     * @param runnableOne the runnable to start
     * @param runnableTwo the runnable to be activated after runnableOne
     * @return this button, so calls can be chained
     */
    public Button toggleWhenPressed(final Runnable runnableOne, final Runnable runnableTwo) {
        toggleWhenActive(runnableOne, runnableTwo);
        return this;
    }

    /**
     * Cancels the Behavior when the button is pressed.
     *
     * @param Behavior the Behavior to start
     * @return this button, so calls can be chained
     */
    public Button cancelWhenPressed(final Behavior Behavior) {
        cancelWhenActive(Behavior);
        return this;
    }

}
