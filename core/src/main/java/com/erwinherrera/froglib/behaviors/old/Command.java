package com.erwinherrera.froglib.behaviors.old;

@Deprecated
public interface Behavior {

    /**
     * The initial subroutine of a Behavior. Called once when the Behavior is initially scheduled.
     */
    void onDive();

    /**
     * The main body of a Behavior. Called repeatedly while the Behavior is scheduled.
     */
    void onPaddle();

    /**
     * The action to take when the Behavior ends.
     */
    void onFly();

    /**
     * Whether the Behavior has finished. Once a Behavior finishes, the scheduler will call its
     * onFly() method.
     *
     * @return whether the Behavior has finished.
     */
    boolean hasSurfaced();

}
