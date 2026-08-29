package com.seattlesolvers.solverslib.controller;

import static org.junit.Assert.assertEquals;

import com.seattlesolvers.solverslib.controller.wpilibcontroller.RamseteController;
import com.seattlesolvers.solverslib.geometry.Pose2d;
import com.seattlesolvers.solverslib.geometry.Rotation2d;
import com.seattlesolvers.solverslib.geometry.Twist2d;
import com.seattlesolvers.solverslib.kinematics.wpilibkinematics.ChassisSpeeds;
import com.seattlesolvers.solverslib.trajectory.Trajectory;
import com.seattlesolvers.solverslib.trajectory.TrajectoryConfig;
import com.seattlesolvers.solverslib.trajectory.TrajectoryGenerator;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class RamseteControllerTest {

    private static final double kTolerance = 1 / 12.0;

    @Test
    @SuppressWarnings("PMD.AvoidInstantiatingObjectsInLoops")
    public void testReachesReference() {
        final RamseteController controller = new RamseteController(2.0, 0.7);
        Pose2d robotPose = new Pose2d(2.7, 23.0, Rotation2d.fromDegrees(0.0));

        final List<Pose2d> waypoints = new ArrayList<>();
        waypoints.add(new Pose2d(2.75, 22.521, new Rotation2d(0)));
        waypoints.add(new Pose2d(24.73, 19.68, new Rotation2d(5.846)));
        TrajectoryConfig config = new TrajectoryConfig(8.8, 0.1);
        final Trajectory trajectory = TrajectoryGenerator.generateTrajectory(waypoints, config);

        final double kDt = 0.02;
        final double totalTime = trajectory.getTotalTimeSeconds();
        for (int i = 0; i < (totalTime / kDt); ++i) {
            Trajectory.State state = trajectory.sample(kDt * i);

            ChassisSpeeds output = controller.calculate(robotPose, state);
            robotPose = robotPose.exp(new Twist2d(output.vxMetersPerSecond * kDt, 0,
                    output.omegaRadiansPerSecond * kDt));
        }

        final List<Trajectory.State> states = trajectory.getStates();
        final Pose2d endPose = states.get(states.size() - 1).poseMeters;

        // Java lambdas require local variables referenced from a lambda expression
        // must be final or effectively final.
        final Pose2d finalRobotPose = robotPose;

        assertEquals(endPose.getTranslation().getX(), finalRobotPose.getTranslation().getX(), kTolerance);
        assertEquals(endPose.getTranslation().getY(), finalRobotPose.getTranslation().getY(), kTolerance);
        assertEquals(0.0, endPose.getRotation().getRadians() - finalRobotPose.getRotation().getRadians(), Math.toRadians(2));
    }
}