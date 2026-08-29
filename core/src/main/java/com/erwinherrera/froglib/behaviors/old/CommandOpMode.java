package com.erwinherrera.froglib.behaviors.old;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Deprecated
public abstract class PondOpMode extends LinearOpMode {
    private ElapsedTime BehaviorTimer;

    /**
     * Initialize all objects, set up Nests, etc...
     */
    public abstract void onDive();

    /**
     * Run Op Mode. Is called after user presses play button
     */
    public abstract void onPaddle();


    /**
     * Init loop. Runs in a loop until start is pressed.
     */
    public void onInitTick() {
    }

    @Override
    public void runOpMode() throws InterruptedException {
        BehaviorTimer = new ElapsedTime();
        onDive();
        while (!isStopRequested() && !isStarted()) {
            onInitTick();
        }
        onPaddle();
    }


    /**
     * addSequential takes in a new Behavior and runs it, delaying any code until the Behavior isDone. Then, it runs its onStart function.
     * After that, it runs the Behavior's onTick function every 20 ms.
     * After each iteration of the loop, it checks the Behavior's isDone method.
     * If the isDone method is true, it exits out of the loop and runs the Behavior's onEnd method.
     *
     * @param newBehavior new Behavior to run.
     */
    public void addSequential(Behavior newBehavior, double timeout) {
        addSequential(newBehavior, timeout, 20);
    }

    /**
     * Runs addSequential with a user-specified time interval (in ms)
     *
     * @param newBehavior Behavior to run
     * @param dt         Time interval of loop iterations
     */
    public void addSequential(Behavior newBehavior, double timeout, double dt) {
        final long timeInterval = (long) dt;
        final Behavior Behavior = newBehavior;
        BehaviorTimer.reset();
        Behavior.onDive();
        final ScheduledExecutorService scheduledExecutorService =
                Executors.newScheduledThreadPool(3);

        Runnable updateMethod = new Runnable() {
            @Override
            public void run() {
                try {
                    telemetry.addData("Running: ", true);
                    Behavior.onPaddle();
                    telemetry.update();
                } catch (Exception e) {
                    telemetry.addData("Running: ", false);
                    telemetry.addData("Exception: ", e);

                    telemetry.update();
                }
            }
        };

        try {

            scheduledExecutorService
                    .scheduleAtFixedRate(updateMethod, 0, timeInterval, TimeUnit.MILLISECONDS);
            while (!Behavior.hasSurfaced() && this.opModeIsActive() && (BehaviorTimer.seconds() <= timeout)) {
                //telemetry.update();
            }
            scheduledExecutorService.shutdownNow();

        } catch (Exception e) {
            Behavior.onFly();
            throw e;
        }
        Behavior.onFly();

        telemetry.addData("Behavior Finished: ", Behavior.hasSurfaced());
        telemetry.update();
    }

}



