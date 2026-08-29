package com.erwinherrera.froglib.examples.PedroBehaviorSample;


import com.pedropathing.follower.Follower;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.erwinherrera.froglib.behaviors.PondOpMode;
import com.erwinherrera.froglib.util.TelemetryData;

@TeleOp
public class PedroTeleOpSample extends PondOpMode {
    Follower follower;
    TelemetryData telemetryData = new TelemetryData(telemetry);

    @Override
    public void onDive() {
        follower = Constants.createFollower(hardwareMap);
        super.reset();

        follower.startTeleopDrive();
    }

    @Override
    public void onPaddle() {
        super.onPaddle();

        /* Robot-Centric Drive
        follower.setTeleOpDrive(-gamepad1.left_stick_y, -gamepad1.left_stick_x, -gamepad1.right_stick_x, true);
        */

        // Field-Centric Drive
        follower.setTeleOpDrive(-gamepad1.left_stick_y, -gamepad1.left_stick_x, -gamepad1.right_stick_x, false);
        follower.update();

        telemetryData.addData("X", follower.getPose().getX());
        telemetryData.addData("Y", follower.getPose().getY());
        telemetryData.addData("Heading", follower.getPose().getHeading());
        telemetryData.update();
    }
}
