/*----------------------------------------------------------------------------*/
/* Copyright (c) 2008-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com.erwinherrera.froglib.behaviors.button;

import com.erwinherrera.froglib.behaviors.Behavior;
import com.erwinherrera.froglib.behaviors.BehaviorManager;
import com.erwinherrera.froglib.behaviors.InstantBehavior;

import java.util.function.BooleanSupplier;

/**
 * This class provides an easy way to link Behaviors to inputs.
 *
 * <p>It is very easy to link a button to a Behavior. For instance, you could link the trigger
 * button of a joystick to a "score" Behavior.
 *
 * <p>It is encouraged that teams write a subclass of Trigger if they want to have something
 * unusual (for instance, if they want to react to the user holding a button while the robot is
 * reading a certain sensor input). For this, they only have to write the {@link Trigger#get()}
 * method to get the full functionality of the Trigger class.
 *
 * @author Erwin Herrera
 */
@SuppressWarnings("PMD.TooManyMethods")
public class Trigger {

    private final BooleanSupplier m_isActive;

    /**
     * Creates a new trigger with the given condition determining whether it is active.
     *
     * @param isActive returns whether or not the trigger should be active
     */
    public Trigger(BooleanSupplier isActive) {
        m_isActive = isActive;
    }

    /**
     * Creates a new trigger that is always inactive.  Useful only as a no-arg constructor for
     * subclasses that will be overriding {@link Trigger#get()} anyway.
     */
    public Trigger() {
        m_isActive = () -> false;
    }

    /**
     * Returns whether or not the trigger is active.
     *
     * <p>This method will be called repeatedly a Behavior is linked to the Trigger.
     *
     * @return whether or not the trigger condition is active.
     */
    public boolean get() {
        return m_isActive.getAsBoolean();
    }

    /**
     * Starts the given Behavior whenever the trigger just becomes active.
     *
     * @param Behavior       the Behavior to start
     * @param interruptible whether the Behavior is interruptible
     * @return this trigger, so calls can be chained
     */
    public Trigger whenActive(final Behavior Behavior, boolean interruptible) {
        BehaviorManager.getInstance().addButton(new Runnable() {
            private boolean m_pressedLast = get();

            @Override
            public void run() {
                boolean pressed = get();

                if (!m_pressedLast && pressed) {
                    Behavior.schedule(interruptible);
                }

                m_pressedLast = pressed;
            }
        });

        return this;
    }

    /**
     * Starts the given Behavior whenever the trigger just becomes active.  The Behavior is set to be
     * interruptible.
     *
     * @param Behavior the Behavior to start
     * @return this trigger, so calls can be chained
     */
    public Trigger whenActive(final Behavior Behavior) {
        return whenActive(Behavior, true);
    }

    /**
     * Runs the given runnable whenever the trigger just becomes active.
     *
     * @param toRun the runnable to run
     * @return this trigger, so calls can be chained
     */
    public Trigger whenActive(final Runnable toRun) {
        return whenActive(new InstantBehavior(toRun));
    }

    /**
     * Constantly starts the given Behavior while the button is held.
     * <p>
     * {@link Behavior#schedule(boolean)} will be called repeatedly while the trigger is active, and
     * will be canceled when the trigger becomes inactive.
     *
     * @param Behavior       the Behavior to start
     * @param interruptible whether the Behavior is interruptible
     * @return this trigger, so calls can be chained
     */
    public Trigger whileActiveContinuous(final Behavior Behavior, boolean interruptible) {
        BehaviorManager.getInstance().addButton(new Runnable() {
            private boolean m_pressedLast = get();

            @Override
            public void run() {
                boolean pressed = get();

                if (pressed) {
                    Behavior.schedule(interruptible);
                } else if (m_pressedLast) {
                    Behavior.cancel();
                }

                m_pressedLast = pressed;
            }
        });

        return this;
    }

    /**
     * Constantly starts the given Behavior while the button is held.
     * <p>
     * {@link Behavior#schedule(boolean)} will be called repeatedly while the trigger is active, and
     * will be canceled when the trigger becomes inactive.  The Behavior is set to be interruptible.
     *
     * @param Behavior the Behavior to start
     * @return this trigger, so calls can be chained
     */
    public Trigger whileActiveContinuous(final Behavior Behavior) {
        return whileActiveContinuous(Behavior, true);
    }

    /**
     * Constantly runs the given runnable while the button is held.
     *
     * @param toRun the runnable to run
     * @return this trigger, so calls can be chained
     */
    public Trigger whileActiveContinuous(final Runnable toRun) {
        return whileActiveContinuous(new InstantBehavior(toRun));
    }

    /**
     * Starts the given Behavior when the trigger initially becomes active, and ends it when it becomes
     * inactive, but does not re-start it in-between.
     *
     * @param Behavior       the Behavior to start
     * @param interruptible whether the Behavior is interruptible
     * @return this trigger, so calls can be chained
     */
    public Trigger whileActiveOnce(final Behavior Behavior, boolean interruptible) {
        BehaviorManager.getInstance().addButton(new Runnable() {
            private boolean m_pressedLast = get();

            @Override
            public void run() {
                boolean pressed = get();

                if (!m_pressedLast && pressed) {
                    Behavior.schedule(interruptible);
                } else if (m_pressedLast && !pressed) {
                    Behavior.cancel();
                }

                m_pressedLast = pressed;
            }
        });
        return this;
    }

    /**
     * Starts the given Behavior when the trigger initially becomes active, and ends it when it becomes
     * inactive, but does not re-start it in-between.  The Behavior is set to be interruptible.
     *
     * @param Behavior the Behavior to start
     * @return this trigger, so calls can be chained
     */
    public Trigger whileActiveOnce(final Behavior Behavior) {
        return whileActiveOnce(Behavior, true);
    }

    /**
     * Starts the Behavior when the trigger becomes inactive.
     *
     * @param Behavior       the Behavior to start
     * @param interruptible whether the Behavior is interruptible
     * @return this trigger, so calls can be chained
     */
    public Trigger whenInactive(final Behavior Behavior, boolean interruptible) {
        BehaviorManager.getInstance().addButton(new Runnable() {
            private boolean m_pressedLast = get();

            @Override
            public void run() {
                boolean pressed = get();

                if (m_pressedLast && !pressed) {
                    Behavior.schedule(interruptible);
                }

                m_pressedLast = pressed;
            }
        });
        return this;
    }

    /**
     * Starts the Behavior when the trigger becomes inactive.  The Behavior is set to be interruptible.
     *
     * @param Behavior the Behavior to start
     * @return this trigger, so calls can be chained
     */
    public Trigger whenInactive(final Behavior Behavior) {
        return whenInactive(Behavior, true);
    }

    /**
     * Runs the given runnable when the trigger becomes inactive.
     *
     * @param toRun the runnable to run
     * @return this trigger, so calls can be chained
     */
    public Trigger whenInactive(final Runnable toRun) {
        return whenInactive(new InstantBehavior(toRun));
    }

    /**
     * Toggles a Behavior when the trigger becomes active.
     *
     * @param Behavior       the Behavior to toggle
     * @param interruptible whether the Behavior is interruptible
     * @return this trigger, so calls can be chained
     */
    public Trigger toggleWhenActive(final Behavior Behavior, boolean interruptible) {
        BehaviorManager.getInstance().addButton(new Runnable() {
            private boolean m_pressedLast = get();

            @Override
            public void run() {
                boolean pressed = get();

                if (!m_pressedLast && pressed) {
                    if (Behavior.isScheduled()) {
                        Behavior.cancel();
                    } else {
                        Behavior.schedule(interruptible);
                    }
                }

                m_pressedLast = pressed;
            }
        });
        return this;
    }

    /**
     * Toggles a Behavior when the trigger becomes active.  The Behavior is set to be interruptible.
     *
     * @param Behavior the Behavior to toggle
     * @return this trigger, so calls can be chained
     */
    public Trigger toggleWhenActive(final Behavior Behavior) {
        return toggleWhenActive(Behavior, true);
    }

    /**
     * Toggles between two Behaviors when the trigger becomes active (BehaviorOne then BehaviorTwo
     * then BehaviorOne).
     *
     * @param BehaviorOne    the Behavior to toggle
     * @param BehaviorTwo    the Behavior to be toggled
     * @param interruptible whether the Behaviors are interruptible
     * @return this trigger, so calls can be chained
     */
    public Trigger toggleWhenActive(final Behavior BehaviorOne, final Behavior BehaviorTwo, boolean interruptible) {
        BehaviorManager.getInstance().addButton(new Runnable() {
            private boolean m_pressedLast = get();
            private boolean m_firstBehaviorActive = false;

            @Override
            public void run() {
                boolean pressed = get();

                if (!m_pressedLast && pressed) {
                    if (m_firstBehaviorActive) {
                        if (BehaviorOne.isScheduled()) {
                            BehaviorOne.cancel();
                        }
                        BehaviorTwo.schedule(interruptible);
                    } else {
                        if (BehaviorTwo.isScheduled()) {
                            BehaviorTwo.cancel();
                        }
                        BehaviorOne.schedule(interruptible);
                    }

                    m_firstBehaviorActive = !m_firstBehaviorActive;
                }

                m_pressedLast = pressed;
            }
        });
        return this;
    }

    /**
     * Toggles between two Behaviors when the trigger becomes active (BehaviorOne then BehaviorTwo
     * then BehaviorOne). These Behaviors are set to be interruptible.
     *
     * @param BehaviorOne the Behavior to start
     * @param BehaviorTwo the Behavior to be activated after
     * @return this trigger, so calls can be chained
     */
    public Trigger toggleWhenActive(final Behavior BehaviorOne, final Behavior BehaviorTwo) {
        return toggleWhenActive(BehaviorOne, BehaviorTwo, true);
    }

    /**
     * Toggles between two runnables when the trigger becomes active (runnableOne then runnableTwo
     * then runnableOne). These runnables are set to be interruptible.
     *
     * @param runnableOne the runnable to start
     * @param runnableTwo the runnable to be activated after
     * @return this trigger, so calls can be chained
     */
    public Trigger toggleWhenActive(final Runnable runnableOne, final Runnable runnableTwo) {
        return toggleWhenActive(new InstantBehavior(runnableOne), new InstantBehavior(runnableTwo));
    }

    /**
     * Cancels a Behavior when the trigger becomes active.
     *
     * @param Behavior the Behavior to cancel
     * @return this trigger, so calls can be chained
     */
    public Trigger cancelWhenActive(final Behavior Behavior) {
        BehaviorManager.getInstance().addButton(new Runnable() {
            private boolean m_pressedLast = get();

            @Override
            public void run() {
                boolean pressed = get();

                if (!m_pressedLast && pressed) {
                    Behavior.cancel();
                }

                m_pressedLast = pressed;
            }
        });
        return this;
    }

    /**
     * Composes this trigger with another trigger, returning a new trigger that is active when both
     * triggers are active.
     *
     * @param trigger the trigger to compose with
     * @return the trigger that is active when both triggers are active
     */
    public Trigger and(Trigger trigger) {
        return new Trigger(() -> get() && trigger.get());
    }

    /**
     * Composes this trigger with another trigger, returning a new trigger that is active when either
     * trigger is active.
     *
     * @param trigger the trigger to compose with
     * @return the trigger that is active when either trigger is active
     */
    public Trigger or(Trigger trigger) {
        return new Trigger(() -> get() || trigger.get());
    }

    /**
     * Creates a new trigger that is active when this trigger is inactive, i.e. that acts as the
     * negation of this trigger.
     *
     * @return the negated trigger
     */
    public Trigger negate() {
        return new Trigger(() -> !get());
    }

}
