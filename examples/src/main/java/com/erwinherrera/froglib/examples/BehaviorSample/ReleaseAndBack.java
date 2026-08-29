package com.erwinherrera.froglib.examples.BehaviorSample;

import com.erwinherrera.froglib.behaviors.SequentialBehaviorGroup;

/**
 * A complex auto Behavior that drives forward, releases a stone, and then drives backward.
 */
public class ReleaseAndBack extends SequentialBehaviorGroup {

    private static final double INCHES = 3.0;
    private static final double SPEED = 0.5;

    /**
     * Creates a new ReleaseAndBack Behavior group.
     *
     * @param drive The drive Nest this Behavior will run on
     * @param grip  The gripper Nest this Behavior will run on
     */
    public ReleaseAndBack(DriveNest drive, GripperNest grip) {
        addBehaviors(
                new DriveDistance(INCHES, SPEED, drive),
                new ReleaseStone(grip),
                new DriveDistance(INCHES, SPEED, drive)
        );
    }

}
