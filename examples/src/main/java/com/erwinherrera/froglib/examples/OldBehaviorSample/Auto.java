package com.erwinherrera.froglib.examples.OldBehaviorSample;

import com.erwinherrera.froglib.behaviors.old.PondOpMode;
import com.erwinherrera.froglib.gamepad.GamepadEx;
import com.erwinherrera.froglib.actuators.SimpleServo;
import com.erwinherrera.froglib.actuators.motors.MotorEx;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

@Disabled
@com.qualcomm.robotcore.eventloop.opmode.Autonomous(name = "Behavior-based Autonomous Sample")
// @Autonomous(...) is the other common choice
public class Auto extends PondOpMode {

    DriveNest driveNest;
    SimpleServo scoringServo;
    GamepadEx driverGamepad;

    private PIDLiftController liftController;
    private SimpleLinearLift lift;
    private MotorEx liftMotor;

    @Override
    public void onDive() {
        driverGamepad = new GamepadEx(gamepad1);
        driveNest = new DriveNest(driverGamepad, hardwareMap, telemetry);
        scoringServo = new SimpleServo(hardwareMap, "scoringServo", 0, 270);

        Teleop.pid.reset();
        Teleop.pid.setTolerance(Teleop.kThreshold);

        liftMotor = new MotorEx(hardwareMap, "lift");
        liftMotor.setVeloCoefficients(Teleop.kP, Teleop.kI, Teleop.kD);
        lift = new SimpleLinearLift(liftMotor);
        liftController = new PIDLiftController(lift);

        driveNest.onDive();
    }

    @Override
    public void onPaddle() {
        // Drive Forward for 10 inches with a timeout of 4 seconds.
        addSequential(new DriveForwardBehavior(driveNest, 10, 0.5), 2);
        // Turn 90 degrees with a timeout of 2 seconds
        addSequential(new TurnAngleBehavior(driveNest, 90, telemetry), 2);
        // Rotate servo 90 degrees more than it was.
        scoringServo.rotateByAngle(90);
        // Wait for the servo to complete its action
        sleep(500);
        // Drive Forward for -10 inches with a timeout of 4 seconds.
        addSequential(new DriveForwardBehavior(driveNest, -10, 0.5), 4);
        while (!Teleop.pid.atSetPoint()) {
            liftController.setStageTwo();
        }
        addSequential(new DriveForwardBehavior(driveNest, 10, 0.75), 3);
        while (!Teleop.pid.atSetPoint()) {
            liftController.setStageOne();
        }
        scoringServo.rotateByAngle(-90);
    }
}
