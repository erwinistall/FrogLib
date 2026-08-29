package org.firstinspires.ftc.teamcode

import com.seattlesolvers.solverslib.command.CommandScheduler
import com.seattlesolvers.solverslib.gamepad.GamepadEx
import com.seattlesolvers.solverslib.gamepad.GamepadKeys
import com.seattlesolvers.solverslib.hardware.SimpleServo
import com.seattlesolvers.solverslib.gamepad.and
import com.seattlesolvers.solverslib.gamepad.not
import com.seattlesolvers.solverslib.gamepad.whenActive
import com.qualcomm.robotcore.eventloop.opmode.Disabled
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp

@TeleOp(name = "Simple GamepadExExt Example")
@Disabled
class GamepadExExtSamples : LinearOpMode() {

    /**
     * We want to use the tool op gamepad to set the servo
     * to position 1 when the 'A' button is pressed, then
     * set it to position 0 when 'B' is pressed
     */
    private val toolOp = GamepadEx(gamepad2)
    private val grip = SimpleServo(hardwareMap, "gripper", 0.0, 270.0)

    override fun runOpMode() {
        toolOp.getGamepadButton(GamepadKeys.Button.A) and !toolOp.getGamepadButton(GamepadKeys.Button.A) whenActive {
            grip.position = 1.0
        }

        toolOp.getGamepadButton(GamepadKeys.Button.B) and !toolOp.getGamepadButton(GamepadKeys.Button.A) whenActive {
            grip.position = 0.0
        }

        waitForStart()

        while (opModeIsActive()) {
            CommandScheduler.getInstance().run()
        }
    }
}