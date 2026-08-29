package com.erwinherrera.froglib.examples;

import com.erwinherrera.froglib.behaviors.PondOpMode;
import com.erwinherrera.froglib.behaviors.OdometryNest;
import com.erwinherrera.froglib.behaviors.PurePursuitBehavior;
import com.erwinherrera.froglib.drivebase.MecanumDrive;
import com.erwinherrera.froglib.actuators.motors.Motor;
import com.erwinherrera.froglib.actuators.motors.MotorEx;
import com.erwinherrera.froglib.kinematics.HolonomicOdometry;
import com.erwinherrera.froglib.purepursuit.waypoints.EndWaypoint;
import com.erwinherrera.froglib.purepursuit.waypoints.GeneralWaypoint;
import com.erwinherrera.froglib.purepursuit.waypoints.StartWaypoint;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

@Autonomous
@Disabled
public class PurePursuitSample extends PondOpMode {

    // define our constants
    static final double TRACKWIDTH = 13.7;
    static final double WHEEL_DIAMETER = 4.0;    // inches
    static double TICKS_TO_INCHES;
    static final double CENTER_WHEEL_OFFSET = 2.4;

    private HolonomicOdometry m_robotOdometry;
    private OdometryNest m_odometry;
    private PurePursuitBehavior ppBehavior;
    private MecanumDrive m_robotDrive;
    private Motor fL, fR, bL, bR;
    private MotorEx leftEncoder, rightEncoder, centerEncoder;

    @Override
    public void onDive() {
        fL = new Motor(hardwareMap, "frontLeft");
        fR = new Motor(hardwareMap, "frontRight");
        bL = new Motor(hardwareMap, "backLeft");
        bR = new Motor(hardwareMap, "backRight");

        // create our drive object
        m_robotDrive = new MecanumDrive(fL, fR, bL, bR);

        leftEncoder = new MotorEx(hardwareMap, "leftEncoder");
        rightEncoder = new MotorEx(hardwareMap, "rightEncoder");
        centerEncoder = new MotorEx(hardwareMap, "centerEncoder");

        // calculate multiplier
        TICKS_TO_INCHES = WHEEL_DIAMETER * Math.PI / leftEncoder.getCPR();

        // create our odometry object and Nest
        m_robotOdometry = new HolonomicOdometry(
                () -> leftEncoder.getCurrentPosition() * TICKS_TO_INCHES,
                () -> rightEncoder.getCurrentPosition() * TICKS_TO_INCHES,
                () -> centerEncoder.getCurrentPosition() * TICKS_TO_INCHES,
                TRACKWIDTH, CENTER_WHEEL_OFFSET
        );
        m_odometry = new OdometryNest(m_robotOdometry);

        // create our pure pursuit Behavior
        ppBehavior = new PurePursuitBehavior(
                m_robotDrive, m_odometry,
                new StartWaypoint(0, 0),
                new GeneralWaypoint(200, 0, 0.8, 0.8, 30),
                new EndWaypoint(
                        400, 0, 0, 0.5,
                        0.5, 30, 0.8, 1
                )
        );

        // schedule the Behavior
        schedule(ppBehavior);
    }

}
