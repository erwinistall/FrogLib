package com.erwinherrera.froglib.behaviors;

import com.erwinherrera.froglib.geometry.Pose2d;
import com.erwinherrera.froglib.kinematics.Odometry;

public class OdometryNest extends NestBase {

    protected Odometry m_odometry;

    /**
     * Make sure you are using the supplier version of the constructor
     *
     * @param odometry the odometry on the robot
     */
    public OdometryNest(Odometry odometry) {
        m_odometry = odometry;
    }

    public Pose2d getPose() {
        return m_odometry.getPose();
    }

    /**
     * Call this at the end of every loop
     */
    public void update() {
        m_odometry.updatePose();
    }

    /**
     * Updates the pose every cycle
     */
    @Override
    public void quack() {
        m_odometry.updatePose();
    }

}
