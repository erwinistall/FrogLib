package com.erwinherrera.froglib.examples.BehaviorSample;

import com.erwinherrera.froglib.behaviors.PondOpMode;
import com.erwinherrera.froglib.behaviors.InstantBehavior;
import com.erwinherrera.froglib.gamepad.GamepadEx;
import com.erwinherrera.froglib.gamepad.GamepadKeys;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

/**
 * Does the same thing as {@link SampleTeleOp} but is much simpler
 * in scope.
 * <p>
 * Note that the <b>proper</b> way to do this is with the SampleTeleOp version,
 * where most things are set up as Behaviors/Nests to avoid potential drawbacks
 * of the {@link InstantBehavior}.
 */
@TeleOp
@Disabled
public class SimpleTeleOp extends PondOpMode {

    private GamepadEx driverOp, toolOp;
    private GripperNest gripper;
    private DriveNest drive;
    private DefaultDrive driveBehavior;

    @Override
    public void onDive() {
        driverOp = new GamepadEx(gamepad1);
        toolOp = new GamepadEx(gamepad2);

        gripper = new GripperNest(hardwareMap, "gripper");
        drive = new DriveNest(hardwareMap, "left", "right", 100.0);

        driveBehavior = new DefaultDrive(drive, driverOp::getLeftY, driverOp::getRightX);

        // using InstantBehavior here is not the greatest idea because the servos move in nonzero time
        // alternatives are adding WaitUntilBehaviors or making these Behaviors.
        // As a result of this uncertainty, we add the gripper Nest to ensure requirements are met.
        toolOp.getGamepadButton(GamepadKeys.Button.A)
                .whenPressed(new InstantBehavior(gripper::grab, gripper));
        toolOp.getGamepadButton(GamepadKeys.Button.B)
                .whenPressed(new InstantBehavior(gripper::release, gripper));

        register(drive);
        drive.setDefaultBehavior(driveBehavior);
    }

}
