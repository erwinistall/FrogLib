package com.erwinherrera.froglib.examples.PedroBehaviorSample;



import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;

import com.pedropathing.paths.PathChain;
import com.erwinherrera.froglib.behaviors.PondOpMode;
import com.erwinherrera.froglib.behaviors.RunBehavior;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.erwinherrera.froglib.pedroBehavior.FollowPathBehavior;
import com.erwinherrera.froglib.pedroBehavior.HoldPointBehavior;
import com.erwinherrera.froglib.pedroBehavior.TurnBehavior;
import com.erwinherrera.froglib.pedroBehavior.TurnToBehavior;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

@Autonomous
public class PedroBehaviors extends PondOpMode {
    Follower follower;

    Pose pose = new Pose(
            72, 72, 90
    );

    PathChain pathChain;

    @Override
    public void onDive() {
        super.reset();

        pathChain = follower.pathBuilder()
                .addPath(new BezierLine(
                        new Pose(0, 0, Math.toRadians(0)),
                        new Pose(16, 28, Math.toRadians(90)))
                ).setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(90))
                .build();

        schedule(
                // Updates follower to follow path
                new RunBehavior(() -> follower.update()),

                // HoldPointBehavior
                new HoldPointBehavior(follower, new Pose(0, 4, 0), false),
                new HoldPointBehavior(follower, pose, true),

                // TurnBehavior
                new TurnBehavior(follower, Math.PI / 2, false),
                new TurnBehavior(follower, 90.0, true, AngleUnit.DEGREES),

                // TurnToBehavior
                new TurnToBehavior(follower, Math.PI / 2),
                new TurnToBehavior(follower, 90.0, AngleUnit.DEGREES),

                // FollowPathBehavior
                new FollowPathBehavior(follower, pathChain),
                new FollowPathBehavior(follower, pathChain, true),
                new FollowPathBehavior(follower, pathChain, true, 1.0),
                new FollowPathBehavior(follower, pathChain, true, 1.0).setGlobalMaxPower(1.0)
        );
    }

    @Override
    public void onPaddle() {
        super.onPaddle();
    }
}
