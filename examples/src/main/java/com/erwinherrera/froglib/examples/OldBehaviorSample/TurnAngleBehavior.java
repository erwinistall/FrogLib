package com.erwinherrera.froglib.examples.OldBehaviorSample;

import com.erwinherrera.froglib.behaviors.old.Behavior;
import com.erwinherrera.froglib.controller.PController;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class TurnAngleBehavior implements Behavior {

    DriveNest driveNest;
    Telemetry tl;
    double angle;

    // Proportional Controller for correcting for gyro error
    PController headingController;

    public TurnAngleBehavior(DriveNest driveNest, double angle, Telemetry telemetry) {
        this.driveNest = driveNest;
        this.angle = angle;
        this.tl = telemetry;
        // At 180 degrees, we should spin almost as fast as we can to correct
        // 1 is full power. 180 * 0.05 = 0.9
        headingController = new PController(0.05, angle, driveNest.getHeading());
        headingController.setSetPoint(angle);

    }


    @Override
    public void onDive() {
        // Reset gyro and encoders
        driveNest.reset();

        // Set target to the target angle
        tl.addData("Heading Setpoint", headingController.getSetPoint());
        // If within 5 degrees of setpoint, the target is considered reached
        headingController.setTolerance(1);

    }

    @Override
    public void onPaddle() {
        // Calculate output
        double rotate = headingController.calculate(driveNest.getHeading());

        // apply output
        driveNest.driveTrain.driveRobotCentric(0, 0, rotate);
    }

    @Override
    public void onFly() {
        driveNest.driveTrain.driveRobotCentric(0, 0, 0);

    }

    @Override
    public boolean hasSurfaced() {
        tl.addData("Position Error: ", headingController.getPositionError());
        tl.addData("Heading Setpoint", headingController.getSetPoint());
        tl.addData("At Setpoint", headingController.atSetPoint());
        tl.addData("Current heading", driveNest.getHeading());
        boolean angleReached = headingController.atSetPoint();
        return angleReached;
    }
}
