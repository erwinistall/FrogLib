package com.erwinherrera.froglib.examples.OldBehaviorSample;

import com.erwinherrera.froglib.controller.PIDController;
import com.erwinherrera.froglib.gamepad.GamepadEx;
import com.erwinherrera.froglib.actuators.motors.MotorEx;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@Disabled
@TeleOp(name = "Behavior-Based Teleop Sample", group = "Behavior")
public class Teleop extends OpMode {

    public static final double kP = 0.003;
    public static final double kI = 0.12;
    public static final double kD = 0.053;
    public static final double kThreshold = 8;

    private DriveNest driveNest;
    private GamepadEx driverGamepad;
    private PIDLiftController liftController;
    private SimpleLinearLift lift;
    private MotorEx liftMotor;

    public static PIDController pid = new PIDController(
            kP, kI, kD
    );

    @Override
    public void init() {
        driverGamepad = new GamepadEx(gamepad1);
        driveNest = new DriveNest(driverGamepad, hardwareMap, telemetry);

        driveNest.initialize();

        pid.setTolerance(kThreshold);
        pid.reset();

        liftMotor = new MotorEx(hardwareMap, "lift");
        lift = new SimpleLinearLift(liftMotor);
        liftController = new PIDLiftController(lift);
    }

    @Override
    public void loop() {
        driveNest.loop();
        liftController.power(gamepad2.left_stick_y);

        // set the lift states through the bumpers
        if (gamepad2.a) {
            liftController.setStageOne();
        } else if (gamepad2.b) {
            liftController.setStageTwo();
        } else if (gamepad2.x) {
            liftController.setStageThree();
        } else if (gamepad2.y) {
            liftController.resetStage();
        }
    }
}
