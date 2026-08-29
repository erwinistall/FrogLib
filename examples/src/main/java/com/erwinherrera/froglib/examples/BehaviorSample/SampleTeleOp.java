package com.erwinherrera.froglib.examples.BehaviorSample;

import com.erwinherrera.froglib.behaviors.PondOpMode;
import com.erwinherrera.froglib.behaviors.button.Button;
import com.erwinherrera.froglib.behaviors.button.GamepadButton;
import com.erwinherrera.froglib.gamepad.GamepadEx;
import com.erwinherrera.froglib.gamepad.GamepadKeys;
import com.erwinherrera.froglib.actuators.motors.MotorEx;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

/**
 * @author Erwin Herrera
 */
@TeleOp(name = "Sample TeleOp")
@Disabled
public class SampleTeleOp extends PondOpMode {

    static final double WHEEL_DIAMETER = 100.0; // millimeters

    private MotorEx m_left, m_right;
    private DriveNest m_drive;
    private GamepadEx m_driverOp;
    private DefaultDrive m_driveBehavior;
    private GripperNest m_gripper;
    private GrabStone m_grabBehavior;
    private ReleaseStone m_releaseBehavior;
    private Button m_grabButton, m_releaseButton;

    @Override
    public void onDive() {
        m_left = new MotorEx(hardwareMap, "drive_left");
        m_right = new MotorEx(hardwareMap, "drive_right");
        m_drive = new DriveNest(m_left, m_right, WHEEL_DIAMETER);

        m_driverOp = new GamepadEx(gamepad1);
        m_driveBehavior = new DefaultDrive(m_drive, () -> m_driverOp.getLeftY(), () -> m_driverOp.getLeftX());

        m_gripper = new GripperNest(hardwareMap, "gripper");
        m_grabBehavior = new GrabStone(m_gripper);
        m_releaseBehavior = new ReleaseStone(m_gripper);

        m_grabButton = (new GamepadButton(m_driverOp, GamepadKeys.Button.A))
                .whenPressed(m_grabBehavior);
        m_releaseButton = (new GamepadButton(m_driverOp, GamepadKeys.Button.B))
                .whenPressed(m_releaseBehavior);

        register(m_drive);
        m_drive.setDefaultBehavior(m_driveBehavior);
    }

}
