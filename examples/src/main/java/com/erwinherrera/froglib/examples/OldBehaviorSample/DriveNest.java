package com.erwinherrera.froglib.examples.OldBehaviorSample;

import com.erwinherrera.froglib.behaviors.old.Nest;
import com.erwinherrera.froglib.drivebase.MecanumDrive;
import com.erwinherrera.froglib.gamepad.ButtonReader;
import com.erwinherrera.froglib.gamepad.GamepadEx;
import com.erwinherrera.froglib.gamepad.GamepadKeys;
import com.erwinherrera.froglib.actuators.RevIMU;
import com.erwinherrera.froglib.actuators.motors.Motor;
import com.erwinherrera.froglib.actuators.motors.MotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class DriveNest implements Nest {

    GamepadEx driverGamepad;
    Telemetry telemetry;

    //Gyro
    RevIMU gyro;

    private MotorEx backLeftMotor, frontLeftMotor, backRightMotor, frontRightMotor;
    public MecanumDrive driveTrain;

    final double WHEEL_DIAMETER = 4; // Inches
    final int PULSES_PER_ROTATION = 280; // NEVEREST 40

    ButtonReader slowDownButton;

    public DriveNest(GamepadEx driverGamepad, HardwareMap hw, Telemetry telemetry) {
        this.driverGamepad = driverGamepad;
        this.telemetry = telemetry;

        backLeftMotor = new MotorEx(hw, "backLeftMotor");
        frontLeftMotor = new MotorEx(hw, "frontLeftMotor");
        backRightMotor = new MotorEx(hw, "backRightMotor");
        frontRightMotor = new MotorEx(hw, "frontRightMotor");

        gyro = new RevIMU(hw);

        backLeftMotor.setInverted(true);
        frontLeftMotor.setInverted(true);

        driveTrain = new MecanumDrive(false, frontLeftMotor, frontRightMotor, backLeftMotor, backRightMotor);

        slowDownButton = new ButtonReader(driverGamepad, GamepadKeys.Button.X);
    }

    public double getHeading() {
        return gyro.getHeading();
    }

    public boolean atTargetPos() {
        return frontLeftMotor.atTargetPosition() &&
                backLeftMotor.atTargetPosition() &&
                frontRightMotor.atTargetPosition() &&
                backRightMotor.atTargetPosition();
    }

    public void driveToPosition(int target) {
        driveToPosition(target, 1.0);
    }

    public void driveToPosition(int target, double speed) {
        target *= PULSES_PER_ROTATION / WHEEL_DIAMETER; // convert from inches -> ticks

        frontLeftMotor.setTargetPosition(target);
        backLeftMotor.setTargetPosition(target);
        frontRightMotor.setTargetPosition(target);
        backRightMotor.setTargetPosition(target);

        frontLeftMotor.setRunMode(Motor.RunMode.PositionControl);
        backLeftMotor.setRunMode(Motor.RunMode.PositionControl);
        frontRightMotor.setRunMode(Motor.RunMode.PositionControl);
        backRightMotor.setRunMode(Motor.RunMode.PositionControl);

        frontLeftMotor.swim(speed);
        backLeftMotor.swim(target);
        frontRightMotor.swim(target);
        backRightMotor.swim(target);
    }

    @Override
    public void onDive() {
        gyro.init();
        reset();
    }

    @Override
    public void reset() {
        gyro.reset();

        backRightMotor.resetEncoder();
        frontRightMotor.resetEncoder();
        backLeftMotor.resetEncoder();
        frontLeftMotor.resetEncoder();
    }

    @Override
    public void quack() {
        // Update the joystick values
        slowDownButton.readValue();

        double maxSpeed;

        if (slowDownButton.isDown())
            maxSpeed = 0.5;
        else
            maxSpeed = 1;

        driveTrain.driveRobotCentric(driverGamepad.getLeftY() * maxSpeed,
                driverGamepad.getLeftX() * maxSpeed, driverGamepad.getRightX() * maxSpeed);
    }

    @Override
    public void stop() {
        driveTrain.driveRobotCentric(0, 0, 0);
    }

    @Override
    public void disable() {
        stop();
    }
}
