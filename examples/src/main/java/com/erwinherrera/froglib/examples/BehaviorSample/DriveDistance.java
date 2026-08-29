package com.erwinherrera.froglib.examples.BehaviorSample;

import com.erwinherrera.froglib.behaviors.BehaviorBase;

public class DriveDistance extends BehaviorBase {

    private final DriveNest m_drive;
    private final double m_distance;
    private final double m_speed;

    /**
     * Creates a new DriveDistance.
     *
     * @param inches The number of inches the robot will drive
     * @param speed  The speed at which the robot will drive
     * @param drive  The drive Nest on which this Behavior will run
     */
    public DriveDistance(double inches, double speed, DriveNest drive) {
        m_distance = inches;
        m_speed = speed;
        m_drive = drive;
    }

    @Override
    public void onDive() {
        m_drive.resetEncoders();
        m_drive.drive(m_speed, 0);
    }

    @Override
    public void onFly(boolean interrupted) {
        m_drive.drive(0, 0);
    }


    @Override
    public boolean hasSurfaced() {
        return Math.abs(m_drive.getAverageEncoderDistance()) >= m_distance;
    }

}
