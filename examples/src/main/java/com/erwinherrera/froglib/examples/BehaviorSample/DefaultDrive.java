package com.erwinherrera.froglib.examples.BehaviorSample;

import com.erwinherrera.froglib.behaviors.BehaviorBase;

import java.util.function.DoubleSupplier;

/**
 * A Behavior to drive the robot with joystick input (passed in as {@link DoubleSupplier}s). Written
 * explicitly for pedagogical purposes.
 */
public class DefaultDrive extends BehaviorBase {

    private final DriveNest m_drive;
    private final DoubleSupplier m_forward;
    private final DoubleSupplier m_rotation;

    /**
     * Creates a new DefaultDrive.
     *
     * @param Nest The drive Nest this Behavior wil run on.
     * @param forward   The control input for driving forwards/backwards
     * @param rotation  The control input for turning
     */
    public DefaultDrive(DriveNest Nest, DoubleSupplier forward, DoubleSupplier rotation) {
        m_drive = Nest;
        m_forward = forward;
        m_rotation = rotation;
        addRequirements(m_drive);
    }

    @Override
    public void execute() {
        m_drive.drive(m_forward.getAsDouble(), m_rotation.getAsDouble());
    }

}
