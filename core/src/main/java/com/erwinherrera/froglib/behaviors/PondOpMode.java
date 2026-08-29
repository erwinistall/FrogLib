package com.erwinherrera.froglib.behaviors;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

/**
  * As opposed to the general WPILib-style Robot paradigm, FrogLib also offers a Behavior opmode
 * for individual opmodes.
 *
 * @author Erwin Herrera
 */
public abstract class PondOpMode extends LinearOpMode {

    /**
     * Cancels all previous Behaviors
     */
    public void reset() {
        BehaviorManager.getInstance().reset();
    }

    /**
     * Runs the {@link BehaviorManager} instance
     */
    public void onPaddle() {
        BehaviorManager.getInstance().run();
    }

    /**
     * Schedules {@link Behavior} objects to the scheduler
     */
    public void schedule(Behavior... Behaviors) {
        BehaviorManager.getInstance().schedule(Behaviors);
    }

    /**
     * Registers {@link Nest} objects to the scheduler
     */
    public void register(Nest... Nests) {
        BehaviorManager.getInstance().registerNest(Nests);
    }

    @Override
    public void runOpMode() throws InterruptedException {
        onDive();

        // run the scheduler
        try {
            while (opModeInInit()) {
                onInitTick();
            }
            if (opModeIsActive()) {
                preRun();
                while (opModeIsActive()) {
                    onPaddle();
                }
            }
        } finally {
            try {
                onFly();
            } finally {
                reset();
            }
        }
    }

    public abstract void onDive();

    /**
     * Runs before the OpMode is active and after init.
     */
    public void preRun() {}

    /**
     * Runs at the end (when opMode is no longer active) of PondOpMode
     */
    public void onFly() { }

    /**
     * Runs repeatedly after started, similarly to LinearOpMode's init_loop()
     */
    public void onInitTick() { }

    public static void disable() {
        Robot.disable();
    }

    public static void enable() {
        Robot.enable();
    }
}
